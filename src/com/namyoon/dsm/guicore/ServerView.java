package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Namyoon Kim
 * <p>
 * This class instantiates the server view. Mainly handles
 * displaying process of the server related GUI components.
 * When the server gets an updated notification, it passes to
 * this server view class to show the message to the server admin.
 * </p>
 */

public class ServerView extends SuperPanel implements InterfacePanel {

    // GUI component.
    private JTextArea logTextArea;
    private JList clientList;

    public ServerView() {
        init();
        addComponents();
    }

    @Override
    // initialization.
    public void init() {
        super.init(this, Color.green);
    }

    @Override
    // adds GUI components.
    public void addComponents() {
        logTextArea = new JTextArea();
        logTextArea.setRows(5);
        logTextArea.setColumns(50);
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(logTextArea);
        clientList = new JList();
        super.addComponent(this, scrollPane, 0.0125f, 0.02f, 600, 450);
        super.addComponent(this, clientList, 0.727f, 0.03f, 200, 430);
    }

    @Override
    public void addActions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // shows server log messages.
    public void showMessage(String message) {
        logTextArea.append("Server: " + message + "\n");
    }

    // updates the client list with the most current server
    // status (number of clients).
    public void updateClientList(String[] clientIDs) {
        DefaultListModel<String> contents = new DefaultListModel<>();
        for (int i = 0; i < clientIDs.length; i++) {
            contents.addElement(clientIDs[i]);
        }
        clientList.setModel(contents);
    }

}
