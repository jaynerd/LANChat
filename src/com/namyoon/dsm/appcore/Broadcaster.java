package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class Broadcaster extends Thread {

    // client settings.
    private String clientID;
    private Socket clientSocket;
    private HashMap clientInfo;

    private ServerView serverView;

    public Broadcaster(ServerView serverView, Socket clientSocket, HashMap clientInfo) {
        this.serverView = serverView;
        this.clientSocket = clientSocket;
        this.clientInfo = clientInfo;
        connect();
    }

    private void connect() {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
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
