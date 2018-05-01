package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ServerView;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * This class contains fundamental functions for initiating
 * a server. Accepts incoming client connetions. Instantiates
 * both broadcaster and UDP stream threads for clients to communicate
 * through transferred messages including client list status updates.
 */
public class Server {

    // network settings.
    private int tcpPort;
    private int udpPort;
    private String udpIPAddress;
    private ServerView serverView;
    private ServerSocket tcpServerSocket;
    private HashMap clientInfo;

    // error messages.
    private String portErrorMsg = "Unable to listen to the port";
    private String socketErrorMsg = "Unable to accept the socket";
    private String hostErrorMsg = "Cannot specify the host information";

    public Server(ServerView serverView, int tcpPort, int udpPort, String udpIPAddress) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.udpIPAddress = udpIPAddress;
        this.serverView = serverView;
        init();
    }

    // initialization.
    private void init() {
        try {
            tcpServerSocket = new ServerSocket(tcpPort);
            if (tcpServerSocket != null) {
                launch();
            }
        } catch (IOException ex) {
            // unable tp listen to the port. the port might be
            // in use by other functions.
            JOptionPane.showMessageDialog(null, portErrorMsg, "Port Invalid", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }
    }

    // initiates two main servers (TCP & UDP).
    private void launch() {
        displayInfo();
        startTCP();
        try {
            startUDP();
        } catch (UnknownHostException e) {
            // unable to get UDP IP address by name.
            e.printStackTrace();
        }
    }

    // displays the general information of the server right after
    // the first instantiation.
    private void displayInfo() {
        String tcpServerPort = Integer.toString(tcpPort);
        String udpServerPort = Integer.toString(udpPort);
        serverView.showMessage("TCP server initiated on port " + tcpServerPort + ".");
        serverView.showMessage("UDP server initiated on port " + udpServerPort + ".");
        try {
            String tcpServerIP = InetAddress.getLocalHost().toString();
            String udpServerIP = InetAddress.getByName(udpIPAddress).toString();
            serverView.showMessage("at TCP IP address of " + tcpServerIP);
            serverView.showMessage("at UDP IP address of " + udpServerIP);
            serverView.showMessage("Waiting for incoming connections");
        } catch (UnknownHostException ex) {
            // unable to specify the local host information.
            JOptionPane.showMessageDialog(null, hostErrorMsg, "Localhost Not Found", JOptionPane.INFORMATION_MESSAGE);
            ex.printStackTrace();
        }
    }

    // starts a TCP server thread when incoming connection request
    // from the client socket has been received. This thread controls
    // in and out of the messages by instantiating a broadcaster.
    private void startTCP() {
        clientInfo = new HashMap();
        Thread tcpServerThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = tcpServerSocket.accept();
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    String clientID = dis.readUTF();
                    synchronized (clientInfo) {
                        clientInfo.put(clientID, dos);
                    }
                    Broadcaster broadcaster = new Broadcaster(tcpPort, serverView, clientSocket, clientInfo);
                    broadcaster.start();
                } catch (IOException ex) {
                    // unable to accept incoming connection.
                    JOptionPane.showMessageDialog(null, socketErrorMsg, "Socket Connection Error", JOptionPane.INFORMATION_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        tcpServerThread.start();
    }

    // starts a UPD server thread for sending a client list information
    // to all of the connected clients. the information should be received
    // by a multicast socket.
    private void startUDP() throws UnknownHostException {
        InetAddress udpInetAdd = InetAddress.getByName(udpIPAddress);
        Thread udpServerThread = new Thread(() -> {
            try (DatagramSocket udpServerSocket = new DatagramSocket()) {
                while (true) {
                    updateStatus(udpInetAdd, udpServerSocket);
                    Thread.sleep(200);
                }
            } catch (IOException ex) {
                // unable to instantiate a socket.
                ex.printStackTrace();
            } catch (InterruptedException e) {
                // unable to interrupt the current thread.
                e.printStackTrace();
            }
        });
        udpServerThread.start();
    }

    // updates the client list. the list will be sent to the server view and other
    // connected clients through UDP network. The most up to date client list information
    // will be maintained for every 2 seconds.
    private void updateStatus(InetAddress udpInetAdd, DatagramSocket udpServerSocket) {
        Set<String> keys = clientInfo.keySet();
        String[] clientList = keys.toArray(new String[keys.size()]);
        serverView.updateClientList(clientList);
        try {
            for (String id : clientList) {
                id += ",";
            }
            String clientListStr = Stream.of(clientList).collect(Collectors.joining(","));
            byte[] message = clientListStr.getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length, udpInetAdd, udpPort);
            udpServerSocket.send(packet);
        } catch (IOException ex) {
            // unable to listen to the port.
            ex.printStackTrace();
        }
    }
}