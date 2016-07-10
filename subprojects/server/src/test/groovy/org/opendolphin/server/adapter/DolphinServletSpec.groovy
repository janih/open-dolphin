package org.opendolphin.server.adapter

import org.opendolphin.core.comm.Codec
import org.opendolphin.core.server.DefaultServerDolphin
import org.opendolphin.core.server.ServerConnector
import org.opendolphin.core.server.ServerModelStore
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import java.util.logging.Level

class DolphinServletSpec extends Specification {

    private Codec codec = Mock()
    private ServerModelStore serverModelStore = Mock()
    private HttpServletRequest request = Mock()
    private HttpServletResponse response = Mock()
    private HttpSession session = Mock(HttpSession)

    def setup() {
        BufferedReader reader = new BufferedReader(new StringReader(' '))
        Writer writer = new PrintWriter(new StringWriter())

        codec.encode(_) >> ''
        codec.decode(_) >> []

        request.getSession() >> { session }
        request.getReader() >> reader

        response.getWriter() >> writer
    }

    void "calling doPost in new session must reach registration of custom actions - no logging"() {
        given:
        DolphinServlet.log.level = Level.OFF // for full branch coverage
        def servlet = mockServlet(null)

        when:
        servlet.doPost(request, response)

        then:
        servlet.registerReached
    }

    void "calling doPost in new session must reach registration of custom actions - all logging for branch coverage"() {
        given:
        DolphinServlet.log.level = Level.ALL // for full branch coverage
        def servlet = mockServlet(null)

        when:
        servlet.doPost(request, response)

        then:
        servlet.registerReached
    }

    void "calling doPost in existing session must not reach registration of custom actions"() {
        given:
        DolphinServlet.log.level = Level.ALL // for full branch coverage
        def connector = new ServerConnector(serverModelStore: serverModelStore, codec: codec)
        def servlet = mockServlet(new DefaultServerDolphin(serverModelStore, connector))

        when:
        servlet.doPost(request, response)

        then:
        !servlet.registerReached
    }

    TestDolphinServlet mockServlet(DefaultServerDolphin serverDolphin) {
        def servlet = new TestDolphinServlet()
        session.getAttribute(_) >> serverDolphin
        return servlet
    }

    class TestDolphinServlet extends DolphinServlet {
        boolean registerReached

        @Override
        protected Codec createCodec() {
            DolphinServletSpec.this.codec
        }

        @Override
        protected ServerModelStore createServerModelStore() {
            DolphinServletSpec.this.serverModelStore
        }

        @Override
        protected void registerApplicationActions(DefaultServerDolphin serverDolphin) {
            registerReached = true
        }
    }
}

