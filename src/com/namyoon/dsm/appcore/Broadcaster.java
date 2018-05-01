package com.namyoon.dsm.appcore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * This class manages to transfer messages betwee the server
 * and clients. Incoming and outgoing messages incliding server
 * status updates will be broadcasted to destinations. This thread
 * will be instantiated for each client indiciduals. Some of the
 * functions will interpret user inputs to process corresponding
 * actions.
 */
public class Broadcaster extends Thread {

    // network settings.
    private Server server;
    private String clientID;
    private DataInputStream dis;
    private DataOutputStream dos;

    // initialization.
    public Broadcaster(Server server, DataInputStream dis, DataOutputStream dos, String clientID) {
        this.dis = dis;
        this.dos = dos;
        this.server = server;
        this.clientID = clientID;
        broadcast("User ID '" + clientID + "' has been connected.");
    }

    // the main chatting thread of processing incoming and
    // outgoing messages.
    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();
                //FIX: index of? or other than contains.
                if (message.equals("/quit")) {
                    // exits the current loop & thread.
                    broadcast(clientID + " has been disconnected.");
                    break;
                }
                if (message.indexOf("/to ") == 0) {
                    // sending private messages.
                    sendPrivateMessage(message);
                } else {
                    broadcast(message);
                }
            }
        } catch (IOException ex) {
            // unable to receive messages from the client.
            ex.printStackTrace();
        } finally {
            server.removeClient(clientID);
            try {
                dis.close();
                dos.close();
            } catch (IOException ex) {
                // unable to modify the client's connection status.
                ex.printStackTrace();
            }
        }
    }

    // broadcasts incoming messages to connected clients and server.
    private void broadcast(String message) {
        server.serverView.showMessage(clientID + ": " + message);
        synchronized (server.clientInfo) {
            Iterator client = server.clientInfo.values().iterator();
            while (client.hasNext()) {
                try {
                    DataOutputStream clientDos = (DataOutputStream) client.next();
                    clientDos.writeUTF(clientID + ": " + message);
                    clientDos.flush();
                } catch (IOException ex) {
                    // unable to send streams to the client.
                    ex.printStackTrace();
                }
            }
        }
    }

    // sends a private message directly to tha target client.
    private void sendPrivateMessage(String message) {
        int start = message.indexOf(" ") + 1;
        int end = message.indexOf(" ", start);

        if (end != -1) {
            String targetClient = message.substring(start, end);
            String contents = message.substring(end + 1);
            Object targetStream = server.clientInfo.get(targetClient);

            if (targetStream != null) {
                try {
                    DataOutputStream targetDos = (DataOutputStream) targetStream;
                    targetDos.writeUTF(clientID + " sent you a private message: " + contents);
                    targetDos.flush();
                } catch (IOException ex) {
                    // unable to send private message to the target client.
                    ex.printStackTrace();
                }
            }
        }
    }
}