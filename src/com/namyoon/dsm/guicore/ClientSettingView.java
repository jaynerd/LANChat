package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Namyoon Kim
 * <p>
 * This class instantiates a client setting view where
 * users can input desired ID, IP address, and a port number to initiating
 * a connection to the main chatting server.
 * </p>
 */

public class ClientSettingView extends SuperPanel implements InterfacePanel {

    // GUI components.
    private JButton connectButton;
    private JTextField idInputField;
    private JTextField portInputField;
    private JTextField ipInputField;

    private String errorMsg = "Please enter a valid user ID.";

    public ClientSettingView() {
        init();
        addComponents();
        addActions();
    }

    @Override
    // initialization.
    public void init() {
        super.init(this, Color.orange);
    }

    @Override
    // adds GUI components.
    public void addComponents() {
        // labels.
        JLabel idLabel = new JLabel("User ID : ");
        JLabel ipLabel = new JLabel(" Server IP :");
        JLabel portLabel = new JLabel("Port (1 - 65535) :");

        // text fields.
        idInputField = new JTextField(20);
        ipInputField = new JTextField(20);
        portInputField = new JTextField(10);
        ipInputField.setText("localhost");

        // button.
        connectButton = new JButton("Connect");

        // adding components.
        super.addComponent(this, idLabel, 0.365f, 0.35f, 200, 30);
        super.addComponent(this, idInputField, 0.45f, 0.35f, 160, 30);
        super.addComponent(this, portLabel, 0.31f, 0.42f, 200, 30);
        super.addComponent(this, portInputField, 0.45f, 0.42f, 160, 30);
        super.addComponent(this, ipLabel, 0.347f, 0.49f, 200, 30);
        super.addComponent(this, ipInputField, 0.45f, 0.49f, 160, 30);
        super.addComponent(this, connectButton, 0.45f, 0.60f, 160, 30);
    }

    @Override
    // adds corresponding actions for each GUI components for accepting inputs.
    // this method also checks whether an acceptable user ID and port number are
    // given from designate text fields.
    public void addActions() {
        connectButton.addActionListener((ActionEvent e) -> {
            String userID = idInputField.getText().trim();
            String ipAddress = ipInputField.getText().trim();
            boolean flag = false;
            int port = 0;
            try {
                port = Integer.parseInt(portInputField.getText().trim());
            } catch (NumberFormatException ex) {
                port = 0;
            }
            // checking a given port value.
            flag = mainView.validatePortValue(port);
            if (flag) {
                // checking user ID value.
                if (userID != null && !userID.isEmpty()) {
                    mainView.showClientView(userID, ipAddress, port);
                } else {
                    JOptionPane.showMessageDialog(null, errorMsg, "Server Connection Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

}
