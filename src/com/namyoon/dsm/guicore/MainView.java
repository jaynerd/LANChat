package com.namyoon.dsm.guicore;

import com.namyoon.dsm.appcore.LANChat;

import javax.swing.*;
import java.awt.*;

/**
 * @author Namyoon Kim
 * <p>
 * This class controls specifications of the main frame for the application.
 * Shows assigned panels based on user's input.
 * </p>
 */

public class MainView extends JFrame {

    private LANChat lanChat;
    public static MainView Instance;

    // main frame size & visual specifications.
    public static int frameWidth;
    public static int frameHeight;

    private float frameWidthRatio = 1.5f;
    private float frameHeightRatio = 1.2f;

    // panel titles.
    private String modeViewTitle = "LANChat: Mode Selection";
    private String serverSetViewTitle = "LANChat: Server Settings";

    public MainView(LANChat lanChat) {
        this.lanChat = lanChat;
        Instance = this;
        init();
        showModeSelectionView();
    }

    // initializes main view components.
    private void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth = (int) (screenSize.getWidth() / frameWidthRatio);
        frameHeight = (int) (screenSize.getHeight() / frameHeightRatio);
        this.setSize(frameWidth, frameHeight);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }

    // refreshes the content of the main view frame.
    private void refresh() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
    }

    // refreshes then adds an assigned panel to the main view.
    private void addPanel(String title, JPanel panel) {
        refresh();
        this.setTitle(title);
        this.add(panel);
        this.setVisible(true);
    }

    // displays the first panel, which is the mode selection view.
    private void showModeSelectionView() {
        ModeSelectionView modeView = new ModeSelectionView();
        addPanel(modeViewTitle, modeView);
    }

    // displays the server setting panel where users can modify
    // server specifications.
    public void showServerSettingView() {
        ServerSettingView serverSetView = new ServerSettingView();
        addPanel(serverSetViewTitle, serverSetView);
    }

    // displays the client setting panel where users can modify
    // values to connect to the desired server.
    public void showClientSettingView() {
    }

    // displays the server side view to control over the chat
    // application.
    public void showServerView(int port) {
        System.out.print("Server Initiated");
    }

}
