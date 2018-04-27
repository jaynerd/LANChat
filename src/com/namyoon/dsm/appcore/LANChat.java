package com.namyoon.dsm.appcore;

import com.namyoon.dsm.guicore.MainView;

/**
 * @author Namyoon Kim
 * <p>
 * Main framework of the LANChat application.
 * Handles essential requests from GUI and passes to the core models.
 * </p>
 */

public class LANChat {

    public static void main(String[] args) {
        LANChat lanChat = new LANChat();
        MainView mainView = new MainView(lanChat);
        mainView.setVisible(true);
    }

}