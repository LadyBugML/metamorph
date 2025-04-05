/**
 * Created by Patrick ijieh on Mar 24, 2025
 */
package edu.semeru.android.capture.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class WelcomeScreen extends Screen {
    private GridBagConstraints c;

    private JLabel welcomeLabel;
    private JLabel instructionLabel;
    private JLabel bannerLabel;

    public WelcomeScreen() {
        super(new GridBagLayout());
        initializeGUI();
    }

    private void initializeGUI() {
        welcomeLabel = new JLabel("Welcome to the Android Video Capture Tool!");
        instructionLabel = new JLabel("Click Next to get started.");

        welcomeLabel.setFont(new Font(null, Font.PLAIN, 16));
        instructionLabel.setFont(new Font(null, Font.PLAIN, 16));

        try {
            Image sageLabImage = ImageIO.read(new File("resources/SAGE-Logo-dark.png"))
                                        .getScaledInstance(285, 65, Image.SCALE_SMOOTH);
            bannerLabel = new JLabel(new ImageIcon(sageLabImage));
        } catch (IOException e) {
            e.printStackTrace();
            bannerLabel = new JLabel();
        }

        c = new GridBagConstraints();

        c.insets = new Insets(2, 2, 2, 2);

        c.anchor = GridBagConstraints.LINE_START;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        this.add(bannerLabel);

        c.gridy++;
        c.gridwidth = 2;
        this.add(welcomeLabel, c);

        c.gridy++;
        this.add(instructionLabel, c);
    }
}
