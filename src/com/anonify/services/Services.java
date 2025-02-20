package com.anonify.services;

import com.anonify.ui.components.ChatPanel;

public class Services {
    /**
     * Starts the Onion server on the specified port.
     * This method makes the call to the private TorServerService method.
     *
     * @param serverPort The port on which the server should listen.
     */
    public void startOnionServer(String hostFilepath, String port, ChatPanel chatPanel) {
        try {
            int serverPort = Integer.parseInt(port);
            TorServerService.main(hostFilepath, serverPort, chatPanel);
        } catch (NumberFormatException e) {
            chatPanel.addMessage("Invalid port number: " + e.getMessage(), "BOT");
        }
    }

    /**
     * Connects to the Onion server via Tor proxy.
     * This method wraps the TorClientService method for connecting to the Onion server.
     * @param onionAddress The address of the Onion server.
     */
    public void connectToOnionServer(String onionAddress, String proxyHost, String proxyPort, String serverPort, ChatPanel chatPanel) {
        try {
            int proxyPortInt = Integer.parseInt(proxyPort);
            int serverPortInt = Integer.parseInt(serverPort);
    
            TorClientService.main(onionAddress, proxyHost, proxyPortInt, serverPortInt, chatPanel);
        } catch (NumberFormatException e) {
            chatPanel.addMessage("Invalid port number: " + e.getMessage(), "BOT");
        }
    }
    
    public String getOnionAddress(){
        return TorServerService.getAddress();
    }

    public void sendMessageToServer(String message){
        TorClientService.sendMessage(message, "Anon");
    }


    /**
     Stops the Onion server by calling the appropriate method in TorServerService. */
    public void stopOnionServer() {
    }
}
