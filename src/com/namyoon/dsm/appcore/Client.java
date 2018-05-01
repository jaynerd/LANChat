package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ClientView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * This class manages received requests and sent responses between
 * the server and clients. Received messages will be shown in the
 * client view, and sent messages will be passed to the server to
 * be broadcasted further. Initial connection to the server will be
 * made after a client gets instantiated.
 */
public class Client {

    // network settings.
    private int tcpPort;
    private int udpPort;
    private String tcpIPAddress;
    private String udpIPAddress;
    private String clientID;
    private ClientView clientView;
    private DataOutputStream dos;

    public Client(ClientView clientView, int tcpPort, int udpPort, String tcpIPAddress, String udpIPAddress, String clientID) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.tcpIPAddress = tcpIPAddress;
        this.udpIPAddress = udpIPAddress;
        this.clientID = clientID;
        this.clientView = clientView;
        init();
    }

    // initialization. connects to the server with given settings.
    private void init() {
        clientView.setClient(this);
        try {
            Socket clientSocket = new Socket(tcpIPAddress, tcpPort);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(clientID);
            /**System.out.print(clientSocket.hashCode());*/
            dos.flush();
            receiveMessage(dis);
            receiveStatus();
        } catch (IOException ex) {
            // unable to connect to the server.
            ex.printStackTrace();
        }
    }

    // sends a message typed in the input text field of the client view.
    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
            if (message.contains("/quit")) {
                System.exit(1);
            }
            if (message.indexOf("/to") == 0) {
                int start = message.indexOf(" ") + 1;
                int end = message.indexOf(" ", start);

                if (end != -1) {
                    String targetClient = message.substring(start, end);
                    String contents = message.substring(end + 1);
                    clientView.showMessage("You have sent a private message to " + targetClient + ": " + contents);
                }
            }
        } catch (IOException ex) {
            // unable to send messages.
            ex.printStackTrace();
        }
    }

    // receive messages from the server. passes to the client view
    // for displaying purposes only. processed by an individual thread
    // that was started for receiving chatting messages.
    private void receiveMessage(DataInputStream dis) {
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    String message = dis.readUTF();
                    clientView.showMessage(message);
                } catch (IOException ex) {
                    // unable to read messages.
                    ex.printStackTrace();
                    break;
                }
            }
        });
        inputThread.start();
    }

    // receives the server status.
    private void receiveStatus() {
        try {
            InetAddress udpInetAdd = InetAddress.getByName(udpIPAddress);
            /** buffer size limitation */
            try {
                MulticastSocket multiSocket = new MulticastSocket(udpPort);
                multiSocket.joinGroup(udpInetAdd);
                startUDP(multiSocket);
            } catch (IOException ex) {
                // unable to instantiate a multicast socket.
                ex.printStackTrace();
            }
        } catch (UnknownHostException ex) {
            // unable to detect the host.
            ex.printStackTrace();
        }
    }

    // starts a UDP connection to receive server status. a multicast
    // socket will be used to avoid port overload.
    private void startUDP(MulticastSocket multiSocket) {
        Thread udpClientThread = new Thread(() -> {
            try {
                while (true) {
                    // receive information.
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    multiSocket.receive(packet);
                    String message = new String(buffer, 0, buffer.length);
                    updateStatus(message);
                }
            } catch (IOException ex) {
                // unable to receive packets.
                ex.printStackTrace();
            }
        });
        udpClientThread.start();
    }

    // updates the client list of the client side application (client
    // view) with the most recent server status.
    private void updateStatus(String clientListStr) {
        String[] clientList = clientListStr.split(",");
        clientView.updateClientList(clientList);
    }
}