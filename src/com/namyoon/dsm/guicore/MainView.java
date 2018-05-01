package com.namyoon.dsm.guicore;

import com.namyoon.dsm.appcore.LANChat;

import javax.swing.*;
import java.awt.*;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * This class controls essential characteristics of the main
 * frame. Shows assigned panels according to user's input.
 */
public class MainView extends JFrame {

    private LANChat lanChat;
    public static MainView Instance;

    // network attributes.
    private int tcpPort = 0;
    private int udpPort = 0;
    private int minPortValue = 1;
    private int maxPortValue = 65535;
    private String errorMsg = "Please enter values between " + minPortValue + " to " + maxPortValue;

    // GUI attributes.
    public static int mainFrameWidth;
    public static int mainFrameHeight;
    private float frameWidthRatio = 1.5f;
    private float frameHeightRatio = 1.2f;

    // sub-panel titles.
    private String modeSelectViewTitle = "LANChat: Mode Selection";
    private String serverSetViewTitle = "LANChat: Server Settings";
    private String clientSetViewTitle = "LANChat: Client Settings";
    private String serverViewTitle = "LANChat: Server Terminal";
    private String clientViewTitle = "LANChat: Client Terminal";

    public MainView(LANChat lanChat) {
        Instance = this;
        this.lanChat = lanChat;
        init();
        showModeSelectionView();
    }

    // initialization.
    private void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrameWidth = (int) (screenSize.getWidth() / frameWidthRatio);
        mainFrameHeight = (int) (screenSize.getHeight() / frameHeightRatio);
        this.setSize(mainFrameWidth, mainFrameHeight);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }

    // adds an assigned sub panel to the main frame.
    private void addSubPanel(String title, JPanel subPanel) {
        refreshFrame();
        this.setTitle(title);
        this.add(subPanel);
        this.setVisible(true);
    }

    // removes every content of the main frame.
    private void refreshFrame() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
    }

    /**
     * region: Sub-panel instantiations.
     */

    // displays the mode selection view where users can choose
    // to instantiate either the main chat server or a client.
    private void showModeSelectionView() {
        ModeSelectionView modeSelectView = new ModeSelectionView();
        addSubPanel(modeSelectViewTitle, modeSelectView);
    }

    // displays the server settings view where users can set
    // important specifications to initiate a server.
    public void showServerSettingView() {
        ServerSettingView serverSetView = new ServerSettingView();
        addSubPanel(serverSetViewTitle, serverSetView);
    }

    // displays the client settings view where users can modify
    // client networking attributes to connect to a desired server.
    public void showClientSettingView() {
        ClientSettingView clientSetView = new ClientSettingView();
        addSubPanel(clientSetViewTitle, clientSetView);
    }

    // displays the server side view to manage overall chatting
    // activities. this function also activates a server with
    // given port numbers for TCP and UDP connections.
    public void showServerView(int tcpPort) {
        setPortValue(tcpPort);
        ServerView serverView = new ServerView();
        addSubPanel(serverViewTitle, serverView);
        lanChat.activateServer(serverView, tcpPort, udpPort);
    }

    // displays the client side view of the main chatting application
    // window. users can send and receive messages including server logs.
    // In addition, users are able to send private messages to another
    // user.
    public void showClientView(int tcpPort, String ipAddress, String clientID) {
        setPortValue(tcpPort);
        ClientView clientView = new ClientView();
        addSubPanel(clientViewTitle, clientView);
        lanChat.createClient(clientView, tcpPort, udpPort, ipAddress, clientID);
    }

    /**
     * endRegion: Sub-panel instantiations.
     */

    // sets a port value for UDP connection based on the given TCP
    // port value.
    private void setPortValue(int tcpPort) {
        this.tcpPort = tcpPort;
        if (tcpPort <= minPortValue) {
            udpPort = tcpPort + 1;
        } else {
            udpPort = tcpPort - 1;
        }
    }

    // validates given port values before instantiating a server socket.
    // returns true if a legit port number has been provided. a server
    // will be running after receiving an acceptable port value.
    public boolean checkPortValue(int tcpPort) {
        this.tcpPort = tcpPort;
        boolean isAcceptable = false;
        if (tcpPort < minPortValue || maxPortValue < tcpPort) {
            JOptionPane.showMessageDialog(null, errorMsg, "Server Initialization Failed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            isAcceptable = true;
        }
        return isAcceptable;
    }
}