package com.anonify.services;

public class Services {
    /**
     * Starts the Onion server on the specified port.
     * This method makes the call to the private TorServerService method.
     *
     * @param serverPort The port on which the server should listen.
     */
    public void startOnionServer(int serverPort) {
        TorServerService.main();
    }

    /**
     * Connects to the Onion server via Tor proxy.
     * This method wraps the TorClientService method for connecting to the Onion server.
     * @param onionAddress The address of the Onion server.
     */
    public void connectToOnionServer(String onionAddress) {
        TorClientService.main(onionAddress);
    }

    /**
     Stops the Onion server by calling the appropriate method in TorServerService. */
    public void stopOnionServer() {
    }
}
