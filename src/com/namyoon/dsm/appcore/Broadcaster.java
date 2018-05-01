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
 * actions.s
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
                if (message.contains("quit")) {
                    // exits the current loop & thread.
                    break;
                }
                if (message.contains("/to")) {
                    // sending private messages.
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
        server.serverView.showMessage(message);
        synchronized (server.clientInfo) {
            Iterator client = server.clientInfo.values().iterator();
            while (client.hasNext()) {
                try {
                    DataOutputStream clientDos = (DataOutputStream) client.next();
                    clientDos.writeUTF(message);
                    clientDos.flush();
                } catch (IOException ex) {
                    // unable to send streams to the client.
                    ex.printStackTrace();
                }
            }
        }
    }
}