package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.ClientView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    private void init() {
        clientView.setClient(this);
        try {
            Socket clientSocket = new Socket(ipAddress, port);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(clientID);
            dos.flush();
            receiveMessage(dis);
        } catch (IOException ex) {
            // unable to connect to the server.
            ex.printStackTrace();
        }
    }

    private void receiveMessage(DataInputStream dis) {
        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = dis.readUTF();
                        clientView.showMessage(message);
                    } catch (IOException ex) {
                        // unable to read messages.
                        ex.printStackTrace();
                    }
                }
            }
        });
        inputThread.start();
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF(clientID + ": " + message);
        } catch (IOException ex) {
            // unable to send messages.
            ex.printStackTrace();
        }
    }

}
