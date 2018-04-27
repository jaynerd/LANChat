package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Namyoon Kim
 * <p>
 * This class instantiates a server setting view where
 * users can input desired port numbers for initiating the
 * main chatting server.
 * </p>
 */

public class ServerSettingView extends SuperPanel implements InterfacePanel {

    // GUI components.
    private JButton activateButton;
    private JTextField portInputField;

    public ServerSettingView() {
        init();
        addComponents();
        addActions();
    }

    @Override
    // initialization.
    public void init() {
        super.init(this, Color.cyan);
    }

    @Override
    // adds GUI components.
    public void addComponents() {
        JLabel portLabel = new JLabel("Port  (1 - 65535)  :");
        portInputField = new JTextField(10);
        activateButton = new JButton("Activate");
        super.addComponent(this, portLabel, 0.29f, 0.43f, 200, 30);
        super.addComponent(this, portInputField, 0.44f, 0.43f, 80, 30);
        super.addComponent(this, activateButton, 0.58f, 0.43f, 80, 30);
    }

    @Override
    // adds corresponding actions to each buttons.
    public void addActions() {
        activateButton.addActionListener((ActionEvent e) -> {
            boolean flag = false;
            int port = 0;
            try {
                port = Integer.parseInt(portInputField.getText().trim());
            } catch (NumberFormatException ex) {
                port = 0;
            }
            flag = mainView.validatePortValue(port);
            if (flag) {
                mainView.showServerView(port);
            }
        });
    }

}
