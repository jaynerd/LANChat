package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Server {

    private int port;
    private ServerView serverView;
    private ServerSocket serverSocket;

    public Server(ServerView serverView, int port) {
        this.port = port;
        this.serverView = serverView;
        init();
    }

    private void init() {
        try {
            serverSocket = new ServerSocket(port);
            if (serverSocket != null) launch();
        } catch (IOException ex) {
            //unable to listen to the port.
            ex.printStackTrace();
        }
    }

    private void launch() {
        displayInfo();
        Thread serverThread = new Thread(() -> {
        });
    }

    private void displayInfo() {
        String serverPort = Integer.toString(port);
        serverView.showMessage("Initiated on port " + serverPort + ".");
        try {
            String serverIP = InetAddress.getLocalHost().toString();
            serverView.showMessage("at IP address of " + serverIP);
            serverView.showMessage("Waiting for incoming connections.");
        } catch (UnknownHostException ex) {
            // unable to get local host information.
            ex.printStackTrace();
        }
    }

}
