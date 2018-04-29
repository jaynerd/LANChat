package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ClientView;
import com.namyoon.dsm.guicore.MainView;
import com.namyoon.dsm.guicore.ServerView;

/**
 * @author Namyoon Kim
 * <p>
 * Main framework of the LANChat application.
 * Handles essential requests from GUI and passes
 * to the core models.
 * </p>
 */

public class LANChat {

    // activates a server upon receiving required server settings
    // from server view class.
    public void activateServer(ServerView serverView, int port) {
        Server server = new Server(serverView, port);
    }

    // instantiates a client when a user logs in with valid information.
    public void createClient(ClientView clientView, int port, String ipAddress, String userID) {
    // Client client = new Client(clientView,port,ipAddress,userID);
    }

    // starts the app from the main view.
    public static void main(String[] args) {
        LANChat lanChat = new LANChat();
        MainView mainView = new MainView(lanChat);
        mainView.setVisible(true);
    }

}
