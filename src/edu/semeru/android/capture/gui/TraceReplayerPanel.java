package edu.semeru.android.capture.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.semeru.android.capture.controller.Controller;

public class TraceReplayerPanel extends JPanel {
    private GridBagConstraints c;
    private JTextField androidSdkPathField;
    private JTextField aaptPathField;
    private JTextField apkPathField;
    private JTextField avdPortField;
    private JTextField adbPortField;
    private JTextField executionNumField;
    
    private JButton startBtn;

    private JLabel statusLabel;

    private String androidSdkPath;
    private String aaptPath;
    private String apkPath;
    private String outputPath;
    private int avdPort;
    private int adbPort;
    private int executionNum;


    public TraceReplayerPanel(String output) {
        super(new GridBagLayout());
        this.outputPath = output;
        initializeGUI();
    }

    private void initializeGUI() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Long.class);
        formatter.setMinimum(0l);
        formatter.setAllowsInvalid(false);
        
        JLabel androidSdkPathLabel = new JLabel("Android SDK Path:");
        JLabel aaptPathLabel = new JLabel("Android Build Tools Path:");
        JLabel apkPathLabel = new JLabel("APK File Path:");
        JLabel avdPortLabel = new JLabel("AVD Emulator Port Number (Default is 5554):");
        JLabel adbPortLabel = new JLabel("ADB Server Port Number (Default is 5037):");
        JLabel executionNumLabel = new JLabel("(Optional) Execution Number (Default is 1):");
        statusLabel = new JLabel("");
        
        JButton androidSdkPathSelectorBtn = new JButton();
        androidSdkPathSelectorBtn.addActionListener(new androidSdkPathBtnListener());

        JButton aaptPathSelectorBtn = new JButton();
        aaptPathSelectorBtn.addActionListener(new aaptPathBtnListener());

        JButton apkPathSelectorBtn = new JButton();
        apkPathSelectorBtn.addActionListener(new apkPathBtnListener());

        startBtn = new JButton("Start Trace Replayer!");
        startBtn.setToolTipText("Click Here to start the Trace Replayer process.");
        startBtn.addActionListener(new startBtnListener());

        try {
			Image img = ImageIO.read(new File("resources/File-Open.png"));
			img = img.getScaledInstance(25, 25, Image.SCALE_DEFAULT);
			androidSdkPathSelectorBtn.setIcon(new ImageIcon(img));
			aaptPathSelectorBtn.setIcon(new ImageIcon(img));
            apkPathSelectorBtn.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}

        androidSdkPathField = new JTextField(10);
        aaptPathField = new JTextField(10);
        apkPathField = new JTextField(10);
        avdPortField = new JFormattedTextField(formatter);
        adbPortField = new JFormattedTextField(formatter);
        executionNumField = new JFormattedTextField(formatter);

        avdPortField.setColumns(10);
        adbPortField.setColumns(10);
        executionNumField.setColumns(10);

        avdPortField.setText("5554");
        adbPortField.setText("5037");
        executionNumField.setText("1");

        c = new GridBagConstraints();
        c.insets = new Insets(2, 4, 2, 4);
		c.anchor = GridBagConstraints.LINE_START;

        // Labels and Text Fields
        c.gridx = 0;
		c.gridy = 0;
        this.add(androidSdkPathLabel, c);

		c.gridy++;
        this.add(androidSdkPathField, c);

        c.gridy++;
        this.add(aaptPathLabel, c);

        c.gridy++;
        this.add(aaptPathField, c);

        c.gridy++;
        this.add(apkPathLabel, c);

        c.gridy++;
        this.add(apkPathField, c);

        c.gridy++;
        this.add(avdPortLabel, c);

        c.gridy++;
        this.add(avdPortField, c);

        c.gridy++;
        this.add(adbPortLabel, c);

        c.gridy++;
        this.add(adbPortField, c);

        c.gridy++;
        this.add(executionNumLabel, c);

        c.gridy++;
        this.add(executionNumField, c);

        // File Selector Buttons
        c.gridx = 1;
        c.gridy = 1;
        this.add(androidSdkPathSelectorBtn, c);

        c.gridy += 2;
        this.add(aaptPathSelectorBtn, c);

        c.gridy += 2;
        this.add(apkPathSelectorBtn, c);

        // Start Button, Status, and App Info
        c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 13;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		this.add(startBtn, c);

		c.gridy++;
        c.weightx = 1;
		this.add(statusLabel, c);
    }

    public class androidSdkPathBtnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fc = new JFileChooser();

			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(TraceReplayerPanel.this);
			if (fc.getSelectedFile() != null){
				androidSdkPath = fc.getSelectedFile().getAbsolutePath();
				androidSdkPathField.setText(androidSdkPath);
			}
		}
    }

    public class aaptPathBtnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fc = new JFileChooser();

			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(TraceReplayerPanel.this);
			if (fc.getSelectedFile() != null) {
				aaptPath = fc.getSelectedFile().getAbsolutePath();
				aaptPathField.setText(aaptPath);
			}
		}
    }

    public class apkPathBtnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fc = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Android Package Kit (APK) files", "apk");
            fc.setFileFilter(filter);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.showOpenDialog(TraceReplayerPanel.this);
			if (fc.getSelectedFile() != null) {
				apkPath = fc.getSelectedFile().getAbsolutePath();
				apkPathField.setText(apkPath);
			}
		}
    }

    public class startBtnListener implements ActionListener {
        @Override
		public void actionPerformed(ActionEvent e) {
            TraceReplayerPanel.this.getComponents();
            for (Component c : TraceReplayerPanel.this.getComponents()) {
                if (c instanceof JTextField) {
                    if (((JTextField)c).getText() == null || ((JTextField)c).getText().isEmpty()) {
                        statusLabel.setForeground(Color.RED);
                        statusLabel.setText("Please ensure that all fields are filled!");
                        return;
                    }
                }
            }
            
            int avdPortNumber;
            int adbPortNumber;
            int executionNumber;
            try {
                avdPortNumber = Integer.parseInt(avdPortField.getText());
                adbPortNumber = Integer.parseInt(adbPortField.getText());
                executionNumber = Integer.parseInt(executionNumField.getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Invalid input!");
                return;
            }
            try {
                Controller.createConfigFile(androidSdkPathField.getText(), aaptPathField.getText(), apkPathField.getText(), outputPath,
                                            avdPortNumber, adbPortNumber, executionNumber);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            statusLabel.setForeground(Color.GREEN);
            statusLabel.setText("Ready!");
		}
    }
}
