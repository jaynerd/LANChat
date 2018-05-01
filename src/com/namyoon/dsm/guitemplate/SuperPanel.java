package com.namyoon.dsm.guitemplate;

import com.namyoon.dsm.guicore.MainView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author Namyoon j4yn3rd@gmail.com
 * This class contains fundamental functions for a sub-panel
 * placement. Adding GUI components, borders, and adjusting
 * each components' sizes including the sub-panel itself
 * will be processed here.
 */
public class SuperPanel extends JPanel {

    public MainView mainView = MainView.Instance;

    // general GUI attributes.
    private int borderSize = 5;
    private int frameWidth = mainView.mainFrameWidth;
    private int frameHeight = mainView.mainFrameHeight;

    // initializes sub panels.
    public void init(JPanel subPanel, Color borderColor) {
        subPanel.setLayout(null);
        subPanel.setBorder(new LineBorder(borderColor, borderSize));
    }

    // adds GUI components relatively resized to the main frame.
    public void addComponent(JPanel subPanel, JComponent component, float xPos, float yPos, int width, int height) {
        component.setBounds(((int) (frameWidth * xPos)), ((int) (frameHeight * yPos)), width, height);
        subPanel.add(component);
    }
}