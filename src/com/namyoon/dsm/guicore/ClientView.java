package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;

public class ClientView extends SuperPanel implements InterfacePanel {

    public ClientView() {
        init();
        addComponents();
    }

    @Override
    // initialization.
    public void init() {
        super.init(this, Color.magenta);
    }

    @Override
    // adds GUI components.
    public void addComponents() {
        JButton sendButton = new JButton("Send");
        JTextField inputTextField = new JTextField();
        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setRows(5);
        chatTextArea.setColumns(50);
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(chatTextArea);
        super.addComponent(this, scrollPane, 0.0125f, 0.02f, 600, 450);
        super.addComponent(this, inputTextField, 0.0125f, 0.77f, 600, 90);
        super.addComponent(this, sendButton, 0.733f, 0.777f, 190, 80);
    }

    @Override
    // adds corresponding actions to each GUI components.
    public void addActions() {

    }

    // shows client messages.
    public void showMessage(String message) {
        //chatTextArea.append(message);
    }

}
