package com.namyoon.dsm.guitemplate;

import com.namyoon.dsm.guicore.MainView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author Namyoon Kim
 * <p>
 * This class contains fundamental methods for a panel creation.
 * Adding components, borders, and other essential placements of
 * GUI objects.
 * </p>
 */

public class SuperPanel extends JPanel {

    public MainView mainView = MainView.Instance;

    private int borderSize = 5;
    private int frameWidth = MainView.frameWidth;
    private int frameHeight = MainView.frameHeight;

    // initializes sub panels.
    public void init(JPanel panel, Color color) {
        panel.setLayout(null);
        panel.setBorder(new LineBorder(color, borderSize));
    }

    // adds a resized GUI component to the designated panel.
    public void addComponent(JPanel panel, JComponent component, float xPos, float yPos, int width, int height) {
        component.setBounds(((int) (frameWidth * xPos)), ((int) (frameHeight * yPos)), width, height);
        panel.add(component);
    }

}
