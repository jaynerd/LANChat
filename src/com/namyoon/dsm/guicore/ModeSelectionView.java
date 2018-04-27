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
        super.addComponent(this, serverButton, 0.30f, 0.45f, 80, 30);
        super.addComponent(this, clientButton, 0.55f, 0.45f, 80, 30);
    }

    @Override
    // adds corresponding actions to each buttons.
    public void addActions() {
        serverButton.addActionListener((ActionEvent e) -> {
            super.mainView.showServerSettingView();
        });
        clientButton.addActionListener((ActionEvent e) -> {
            super.mainView.showClientSettingView();
        });
    }

}
