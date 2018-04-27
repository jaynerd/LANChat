package com.namyoon.dsm.guicore;

import com.namyoon.dsm.guitemplate.InterfacePanel;
import com.namyoon.dsm.guitemplate.SuperPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Namyoon Kim
 * <p>
 * This class instantiates the mode selection view,
 * where users can choose to start the application as a client or server.
 * Primarily handles GUI prompt features.
 * </p>
 */

public class ModeSelectionView extends SuperPanel implements InterfacePanel {

    // buttons.
    private JButton serverButton;
    private JButton clientButton;

    // button visual specifications.
    private int buttonWidth = 80;
    private int buttonHeight = 30;
    private float serverXPos = 0.3f;
    private float serverYPos = 0.45f;
    private float clientXPos = 0.55f;
    private float clientYPos = 0.45f;

    public ModeSelectionView() {
        init();
        addComponents();
        addActions();
    }

    @Override
    // initialization.
    public void init() {
        super.init(this, Color.lightGray);
    }

    @Override
    // adds GUI components.
    public void addComponents() {
        serverButton = new JButton("Server");
        clientButton = new JButton("Client");
        super.addComponent(this, serverButton, serverXPos, serverYPos, buttonWidth, buttonHeight);
        super.addComponent(this, clientButton, clientXPos, clientYPos, buttonWidth, buttonHeight);
    }

    @Override
    // adds corresponding events to each buttons.
    public void addActions() {
        serverButton.addActionListener((ActionEvent e) -> {
            super.mainView.showServerSettingView();
        });
        clientButton.addActionListener((ActionEvent e) -> {
            super.mainView.showClientSettingView();
        });
    }

}
