package com.namyoon.dsm.guicore;

import com.namyoon.dsm.appcore.LANChat;
import com.sun.security.ntlm.Server;

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

    // server networking specifications.
    private int port = 0;
    private int minValue = 1;
    private int maxValue = 65535;
    private String errorMsg = "Please enter values between 1 to 65535.";

    // main frame size & visual specifications.
    public static int frameWidth;
    public static int frameHeight;
    private float frameWidthRatio = 1.5f;
    private float frameHeightRatio = 1.2f;

    // panel titles.
    private String modeViewTitle = "LANChat: Mode Selection";
    private String serverSetViewTitle = "LANChat: Server Settings";
    private String clientSetViewTitle = "LANChat: Client Settings";
    private String serverViewTitle = "LANChat: Server";

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
    // can be assumed as a general login view.
    public void showClientSettingView() {
        ClientSettingView clientSetView = new ClientSettingView();
        addPanel(clientSetViewTitle, clientSetView);
    }

    // displays the server side view to control over the chat
    // application.
    // Also, by showing the server view, the server socket gets
    // activated from the application core library.
    public void showServerView(int port) {
        ServerView serverView = new ServerView();
        addPanel(serverViewTitle, serverView);
        lanChat.activateServer(serverView, port);
    }

    // displays the client side of the main chatting window.
    // users can send and receive messages including the server log.
    // They also can send private messages to a specific user.
    public void showClientView(String userID, String ipAddress, int port) {
    }

    // validates given port number before initiating the server socket.
    // returns true when a legit port number is provided.
    // a server will be running upon receiving correct port values.
    public boolean validatePortValue(int port) {
        boolean flag = false;
        this.port = port;
        if (port < minValue || maxValue < port) {
            JOptionPane.showMessageDialog(null, errorMsg, "Server Initialization Failed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            flag = true;
        }
        return flag;
    }

}
