/**
 * Created by hendrikebbers on 20.01.15.
 */
package org.opendolphin.core.server;

/**
 * A factory class to create a ServerDolphin object.
 */
public class ServerDolphinFactory {

    private ServerDolphinFactory() {}

    /**
     * Creates a default ServerDolphin object containing a default ServerModelStore and ServerConnector.
     */
    public static ServerDolphin create() {
        return new DefaultServerDolphin();
    }

    /**
     * Creates a default ServerDolphin object using the supplied model store and server connector.
     * @param serverModelStore
     * @param serverConnector
     * @return
     */
    public static ServerDolphin create(ServerModelStore serverModelStore, ServerConnector serverConnector) {
        return new DefaultServerDolphin(serverModelStore, serverConnector);
    }
}
