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

    public Client(ClientView clientView, int port, String ipAddress, String clientID) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.clientID = clientID;
        this.clientView = clientView;
        init();
    }

    private void init() {
        try {
            Socket clientSocket = new Socket(ipAddress, port);
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(clientID);
            dos.flush();
            connect(dis);
        } catch (IOException ex) {
            // unable to connect to the server.
            ex.printStackTrace();
        }
    }

    private void connect(DataInputStream dis) {
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

}
