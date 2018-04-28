package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Namyoon Kim
 * <p>
 * This class instantiates the server view.
 * Mainly handles display of the server related GUI components.
 * When the server gets an updated notification, it passes to
 * this server view class to show the message to the server admin.
 * </p>
 */

public class ServerView extends SuperPanel implements InterfacePanel {

    // GUI component.
    private JTextArea logTextArea;

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
        super.addComponent(this, scrollPane, 0.0125f, 0.02f, 600, 450);
    }

    @Override
    public void addActions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // shows server log messages.
    public void showMessage(String message) {
        logTextArea.append("Server: " + message + "\n");
    }

}
