package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private int port;
    private ServerView serverView;
    private DataInputStream dis;

    public Broadcaster(int port, ServerView serverView, Socket clientSocket, HashMap clientInfo) {
        this.port = port;
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
            broadcast("User ID '" + clientID + "' has been connected.");
            updateStatus();
        } catch (IOException ex) {
            // unable to receive streams from the client.
            ex.printStackTrace();
        }
    }

    // the main tread of receiving and sending messages.
    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();
                System.out.println(message);
                // change to index of?
                if (message.contains("/quit")) {
                    // quitting the application by exiting the current
                    // thread.
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
            synchronized (clientInfo) {
                clientInfo.remove(clientID);
                updateStatus();
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

    // updates the client list of both the server and clients with
    // the most up to date client list status.
    private void updateStatus() {
        Set<String> keys = clientInfo.keySet();
        String[] clientList = keys.toArray(new String[keys.size()]);
        serverView.updateClientList(clientList);
        try {
            for (String id : clientList) {
                id += ",";
                System.out.println(id);
            }
            String clientListMsg = Stream.of(clientList).collect(Collectors.joining(","));
            byte[] message = clientListMsg.getBytes();
            InetAddress ipAddress = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(message, message.length, ipAddress, port);
            DatagramSocket dataSocket = new DatagramSocket(port);
            dataSocket.send(packet);
            dataSocket.close();
        } catch (Exception ex) {
            // unable to listen to the port.
            // unable to locate the local host.
            ex.printStackTrace();
        }
    }

}
