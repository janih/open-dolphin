package org.opendolphin.core.client.comm

import org.opendolphin.core.client.ClientDolphin
import org.opendolphin.core.comm.Command
import org.opendolphin.core.server.ServerConnector

class InMemoryClientConnectorTests extends GroovyTestCase {

    void testCallConnector_NoServerConnectorWired() {
        InMemoryClientConnector connector = new InMemoryClientConnector(new ClientDolphin(), null)
        assert [] == connector.transmit([new Command()])
    }

    void testCallConnector_ServerWired() {
        boolean serverCalled = false
        def serverConnector = [receive: { cmd ->
            serverCalled = true
            return []
        }] as ServerConnector
        InMemoryClientConnector connector = new InMemoryClientConnector(new ClientDolphin(), serverConnector)
        def command = new Command()
        connector.transmit([command])
        assert serverCalled
    }

    void testCallConnector_ServerWiredWithSleep() {
        boolean serverCalled = false
        def serverConnector = [receive: { cmd ->
            serverCalled = true
            return []
        }] as ServerConnector
        InMemoryClientConnector connector = new InMemoryClientConnector(new ClientDolphin(), serverConnector)
        connector.sleepMillis = 10
        def command = new Command()
        connector.transmit([command])
        assert serverCalled
    }

}
