package org.opendolphin.binding


import org.opendolphin.core.client.ClientDolphin
import org.opendolphin.core.client.ClientModelStore
import org.opendolphin.core.client.ClientPresentationModel
import org.opendolphin.core.client.comm.InMemoryClientConnector
import org.opendolphin.core.server.ServerConnector
import spock.lang.Specification

import javax.swing.*


class BindingSpec extends Specification {

    // exposes http://www.canoo.com/jira/browse/DOL-26
    def 'binding the text property of a Swing component to an Attribute should not throw Exceptions'() {
        given:
        def dolphin = new ClientDolphin()
        dolphin.clientModelStore = new ClientModelStore(dolphin)
        dolphin.clientConnector = new InMemoryClientConnector(dolphin, [:] as ServerConnector)
        ClientPresentationModel loginPM = dolphin.presentationModel("loginPM", [name: "abc"])

        JTextField txtName = new JTextField()

        expect:
        Binder.bind("name").of(loginPM).to("text").of(txtName)
        Binder.bind("text").of(txtName).to("name").of(loginPM)
    }
}