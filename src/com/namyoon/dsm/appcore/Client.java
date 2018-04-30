package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ClientView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * @author Namyoon Kim
 * <p>
 * This class controls requests and responses from the server
 * and client, vice versa. Received messages will be shown
 * to the client view, and sent messages will be passed to
 * the server to be broadcasted. Initial connection to the
 * server will be made after gets instantiated.
 * </p>
 */

public class Client {

    // client settings.
    private int port;
    private String ipAddress;
    private String clientID;

    private ClientView clientView;
    private DataOutputStream dos;

    public Client(ClientView clientView, int port, String ipAddress, String clientID) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.clientID = clientID;
        this.clientView = clientView;
        init();
    }

    // initialization. connecting to the server with given
    // specifications.
    private void init() {
        clientView.setClient(this);
        try {
            Socket clientSocket = new Socket(ipAddress, port);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(clientID);
            /**System.out.print(clientSocket.hashCode());*/
            dos.flush();
            receiveMessage(dis);
        } catch (IOException ex) {
            // unable to connect to the server.
            ex.printStackTrace();
        }
    }

    // receives messages from the server. passes to the client
    // view for displaying purpose. processed by an individual
    // thread that was made for receiving messages only.
    private void receiveMessage(DataInputStream dis) {
        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket dataSocket = new DatagramSocket(port);
                    /** buffer size limitation*/
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (true) {
                        try {
                            String message = dis.readUTF();
                            clientView.showMessage(message);
                            dataSocket.receive(packet);
                            String clientListMsg = new String(buffer, 0, packet.getLength());
                            updateStatus(clientListMsg);
                            packet.setLength(buffer.length);
                        } catch (IOException ex) {
                            // unable to read messages.
                            ex.printStackTrace();
                            break;
                        }
                    }
                } catch (Exception ex) {
                    // unable to listen to the port.
                    // unable to receive messages through a UDP connection.
                    ex.printStackTrace();
                }
            }
        });
        inputThread.start();
    }

    // sends a message typed in the input text field.
    public void sendMessage(String message) {
        try {
            dos.writeUTF(clientID + ": " + message);
        } catch (IOException ex) {
            // unable to send messages.
            ex.printStackTrace();
        }
    }

    // updates the client list of the client side application with
    // the most up to date client list status from the broadcaster.
    private void updateStatus(String clientListMsg) {
        String[] clientList = clientListMsg.split(",");
        clientView.updateClientList(clientList);
    }

}
