/**
 * Created by Kevin Moran on Mar 8, 2017
 */
package edu.semeru.android.capture.gui;

import java.awt.Toolkit;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatDarkLaf;


/**
 * @author KevinMoran
 *
 */
public class MainScreen extends JFrame {
    private static int WINDOW_WIDTH = 640;
    private static int WINDOW_HEIGHT = 480;
    private static int NUM_SCREENS = 3;

    private int panelId = 0;

	private JPanel mainPanel;
	private JPanel currentPanel;
	private JPanel navigationPanel;
    private GridBagConstraints panelConstraints;
	private GridBagConstraints c;

	private JButton nextBtn;
	private JButton backBtn;

    private WelcomeScreen welcomeScreen;
    private VideoCaptureScreen videoCaptureScreen;
    private TraceReplayerScreen traceReplayerScreen;

	public MainScreen() throws IOException {
		initializeGUI();
	}

	private void initializeGUI() throws IOException {
        JLabel versionNumberLabel = new JLabel("v0.2");
        ImageIcon gvtIcon = new ImageIcon("resources/GVT-Logo.png");

		nextBtn = new JButton("Next >");
		nextBtn.setToolTipText("Click here to go to the next screen.");
        nextBtn.setEnabled(true);
		nextBtn.addActionListener(new nextBtnListener());

        backBtn = new JButton("< Back");
        nextBtn.setToolTipText("Click here to go to the previous screen.");
        backBtn.setEnabled(false);
        backBtn.setVisible(false);
		backBtn.addActionListener(new backBtnListener());

        welcomeScreen = new WelcomeScreen();
        videoCaptureScreen = new VideoCaptureScreen();
        traceReplayerScreen = new TraceReplayerScreen();
        
        welcomeScreen.setNextBtn(nextBtn);
        welcomeScreen.setBackBtn(backBtn);

        videoCaptureScreen.setNextBtn(nextBtn);
        videoCaptureScreen.setBackBtn(backBtn);

        traceReplayerScreen.setNextBtn(nextBtn);
        traceReplayerScreen.setBackBtn(backBtn);

		currentPanel = welcomeScreen;
        
		navigationPanel = new JPanel(new GridBagLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		JScrollPane scrPane = new JScrollPane(currentPanel);
		Border padding = BorderFactory.createEmptyBorder(2, 4, 2, 4);
		scrPane.setBorder(padding);
        navigationPanel.setBorder(padding);

        panelConstraints = new GridBagConstraints();
        panelConstraints.gridheight = panelConstraints.gridwidth = 1;

        panelConstraints.weighty = panelConstraints.weightx = 0.7;
        panelConstraints.anchor = GridBagConstraints.CENTER;
        panelConstraints.gridx = panelConstraints.gridy = 0;
		mainPanel.add(scrPane, panelConstraints);

        panelConstraints.weighty = panelConstraints.weightx = 0.3;
        panelConstraints.anchor = GridBagConstraints.LAST_LINE_END;
        panelConstraints.gridx = panelConstraints.gridy = 1;
		mainPanel.add(navigationPanel, panelConstraints);

		add(mainPanel);

        c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(2, 3, 2, 3);
		c.gridx = 0;
		c.gridy = 0;
        c.ipady = 2;
        c.gridwidth = 3;
		navigationPanel.add(backBtn, c);

        c.gridx += 3;
		navigationPanel.add(nextBtn, c);

        c.gridx += 3;
        c.gridy = 1;
        c.ipady = 0;
		navigationPanel.add(versionNumberLabel, c);

		setTitle("Android Video Capture Tool");
		setIconImage(gvtIcon.getImage());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

    private void loadNextScreen() {
        if(panelId >= NUM_SCREENS - 1) {
            return;
        }
        panelId += 1;
        switchCurrentScreen();
    }

    private void loadPreviousScreen() {
        if(panelId <= 0) {
            return;
        }
        panelId -= 1;
        switchCurrentScreen();
    }

    private void switchCurrentScreen() {
        backBtn.setEnabled(true);
		nextBtn.setEnabled(false);

        if (panelId > 0) {
            backBtn.setVisible(true);
        } else {
            backBtn.setVisible(false);
        }

        Screen newScreen;

        switch(panelId) {
            case 0:
                newScreen = welcomeScreen;
                nextBtn.setEnabled(true);
                break;
            case 1:
                newScreen = videoCaptureScreen;
                break;
            case 2:
                newScreen = traceReplayerScreen;
                traceReplayerScreen.setOutputPath(videoCaptureScreen.getOutputPath());
                traceReplayerScreen.setGetEventLog(videoCaptureScreen.getEventLog());
                break;

            default:
                newScreen = welcomeScreen;
                nextBtn.setEnabled(true);
                break;
        }

        currentPanel = newScreen;

        JScrollPane scrPane = new JScrollPane(currentPanel);
		Border padding = BorderFactory.createEmptyBorder(2, 4, 2, 4);
		scrPane.setBorder(padding);
        navigationPanel.setBorder(padding);

        mainPanel.removeAll();

        panelConstraints = new GridBagConstraints();
        panelConstraints.gridheight = panelConstraints.gridwidth = 1;

        panelConstraints.weighty = panelConstraints.weightx = 0.7;
        panelConstraints.anchor = GridBagConstraints.CENTER;
        panelConstraints.gridx = panelConstraints.gridy = 0;
		mainPanel.add(scrPane, panelConstraints);

        panelConstraints.weighty = panelConstraints.weightx = 0.3;
        panelConstraints.anchor = GridBagConstraints.LAST_LINE_END;
        panelConstraints.gridx = panelConstraints.gridy = 1;
		mainPanel.add(navigationPanel, panelConstraints);

		repaint();
		revalidate();
    }

    public class nextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadNextScreen();
        }
    }

	public class backBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
			loadPreviousScreen();
        }
    }
	
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			MainScreen ex;
			try {
                FlatDarkLaf.setup();
                UIManager.setLookAndFeel(new FlatDarkLaf());
				ex = new MainScreen();
				ex.setVisible(true);
                ex.setResizable(false);
                
                double x = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2.0 - WINDOW_WIDTH/2;
                double y = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2.0 - WINDOW_HEIGHT/2;

                ex.setBounds(new Rectangle((int)x, (int)y, WINDOW_WIDTH, WINDOW_HEIGHT));
			} catch (IOException | UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
