package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Namyoon Kim
 * <p>
 * This class manages to transfer messages between the
 * server and client. Incomming, or server status update
 * messages will be broadcasted to target clients, by initiating
 * each individual broadcast threads based on the client
 * information. Some of the functions will decript user
 * inputs to process required actions.
 * </p>
 */

public class Broadcaster extends Thread {

    // client settings.
    private String clientID;
    private Socket clientSocket;
    private HashMap clientInfo;

    // network attributes.
    private ServerView serverView;
    private DataInputStream dis;

    public Broadcaster(ServerView serverView, Socket clientSocket, HashMap clientInfo) {
        this.serverView = serverView;
        this.clientSocket = clientSocket;
        this.clientInfo = clientInfo;
        connect();
    }

    // initializes connections to clients.
    private void connect() {
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            clientID = dis.readUTF();
            synchronized (clientInfo) {
                clientInfo.put(clientID, dos);
            }
            broadcast("User ID: " + clientID + " has been connected.");

        } catch (IOException ex) {
            // unable to receive streams from the client.
            ex.printStackTrace();
        }
    }

    // the main tread of receiving and sending messages.
    public void run() {
        try {
            String message;
            while ((message = dis.readUTF()) != null) {
                if (message.equals("/quit")) {
                    // quitting the application by exiting the current
                    // thread.
                    break;
                }
                if (message.equals("/to")) {
                    // sending private messages.
                } else {
                    broadcast(message);
                }
            }
        } catch (IOException ex) {
            // unable to receive messages from the client.
            ex.printStackTrace();
        } finally {
            synchronized (clientInfo) {
                clientInfo.remove(clientID);
            }
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException ex) {
                // unable to update client connection status.
                ex.printStackTrace();
            }
        }
    }

    // broadcasts incoming messages to connected objects.
    private void broadcast(String message) {
        serverView.showMessage(message);
        synchronized (clientInfo) {
            Iterator client = clientInfo.values().iterator();
            while (client.hasNext()) {
                try {
                    DataOutputStream dos = (DataOutputStream) client.next();
                    dos.writeUTF(message);
                    dos.flush();
                } catch (IOException ex) {
                    // unable to send streams to the client.
                    ex.printStackTrace();
                }
            }
        }
    }

}
