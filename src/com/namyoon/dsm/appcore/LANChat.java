package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ClientView;
import com.namyoon.dsm.guicore.MainView;
import com.namyoon.dsm.guicore.ServerView;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * Main framework of the LANChat application. Handles essential
 * requests from GUI and passes to the core models.
 */
public class LANChat {

    // ip address for the udp connection only (multicast socket).
    private String udpIPAddress = "224.0.0.3";

    // activates a server upon receiving required server settings
    // from the server settings class.
    public void activateServer(ServerView serverView, int tcpPort, int udpPort) {
        Server server = new Server(serverView, tcpPort, udpPort, udpIPAddress);
    }

    // instantiates a client upon receiving a valid information.
    public void createClient(ClientView clientView, int tcpPort, int udpPort, String tcpIPAddress, String clientID) {
        Client client = new Client(clientView, tcpPort, udpPort, tcpIPAddress, udpIPAddress, clientID);
    }

    // starts the chatting app with the main view active.
    public static void main(String[] args) {
        LANChat lanChat = new LANChat();
        MainView mainView = new MainView(lanChat);
        mainView.setVisible(true);
    }
}