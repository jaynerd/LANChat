package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;

/**
 * @author Namyoon Kim
 * <p>
 * This class controls over the core server models.
 * Accepts incoming client connections, then instantiates
 * broadcaster threads for each clients for receiving
 * messages and broadcast them to assigned clien views.
 * </p>
 */

public class Server {

    // network settings.
    private int port;
    private ServerView serverView;
    private ServerSocket serverSocket;

    // error messages.
    private String portErrorMsg = "Unable to listen to the port.";
    private String socketErrorMsg = "Unable to accept the socket.";
    private String hostErrorMsg = "Cannot get host information.";

    public Server(ServerView serverView, int port) {
        this.port = port;
        this.serverView = serverView;
        init();
    }

    // initialization.
    private void init() {
        try {
            serverSocket = new ServerSocket(port);
            if (serverSocket != null) launch();
        } catch (IOException ex) {
            // unable to listen to the port. the port might be
            // in use by other functions.
            JOptionPane.showMessageDialog(null, portErrorMsg, "Port Not Valid", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }
    }

    // starts the server thread, and accepts any incoming connections when requested.
    private void launch() {
        displayInfo();
        HashMap clientInfo = new HashMap();
        Thread serverThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    Broadcaster broadcaster = new Broadcaster(port, serverView, clientSocket, clientInfo);
                    broadcaster.start();
                } catch (IOException ex) {
                    // unable to accept incoming connection.
                    JOptionPane.showMessageDialog(null, socketErrorMsg, "Socket Connection Error", JOptionPane.INFORMATION_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    // displays general information of the server upon instantiation.
    private void displayInfo() {
        String serverPort = Integer.toString(port);
        serverView.showMessage("Initiated on port " + serverPort + ".");
        try {
            String serverIP = InetAddress.getLocalHost().toString();
            serverView.showMessage("at IP address of " + serverIP);
            serverView.showMessage("Waiting for incoming connections.");
        } catch (UnknownHostException ex) {
            // unable to get local host information.
            JOptionPane.showMessageDialog(null, hostErrorMsg, "Localhost Not Found", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }
    }

}
