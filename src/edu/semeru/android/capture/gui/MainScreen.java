/**
 * Created by Kevin Moran on Mar 8, 2017
 */
package edu.semeru.android.capture.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
import java.text.SimpleDateFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
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
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.semeru.android.capture.controller.Controller;
import edu.semeru.android.capture.gui.TraceReplayerPanel;

/**
 * @author KevinMoran
 *
 */
public class MainScreen extends JFrame {
	private JPanel mainPanel;
	private JPanel toolPane;
	private JPanel extraPanel;
	JLabel implementationSrcLabel;
	private GridBagConstraints c;

	private JTextField outputFolderTextField;
	private JTextField adbTextField;

	private String outputFolderPath;
	private String adbPath;
	
	private String startTimeStamp;

	String url;
	Process videoProcess;
	Process geteventProcess;

	private JLabel statusLabel;
	private JLabel timerLabel;
	private JLabel timer;
	JButton startBtn;
	JButton stopBtn;
	JButton nextBtn; // test
	JButton backBtn;

	JLabel previewPicLabel;
	private JTextArea excpetionLabel;
	private int count = 180000;
	JButton implementationSrcSelectorBtn;
	Timer cdTimer;

	private JDialog loading;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1964235073593292125L;

	public MainScreen() throws IOException {

		initializeGUI();
	}

	private void initializeGUI() throws IOException {
		//Set up Text Fields for the Paths to Information needed by the GVT

		outputFolderTextField = new JTextField(10);
		adbTextField = new JTextField(10);
		excpetionLabel = new JTextArea(50,50);


		//Pre-defined paths For quick testing purposes

//		mockupFolderTextField.setText("/Users/KevinMoran/Dropbox/Documents/My_Graduate_School_Work/SEMERU/git_src_code/gitlab-code/Android-GUI-Checker/Subjects/Testing/HuaweiModification/HiApp-CategoryTest2");
//		mockupImageTextField.setText("/Users/KevinMoran/Dropbox/Documents/My_Graduate_School_Work/SEMERU/git_src_code/gitlab-code/Android-GUI-Checker/Subjects/Testing/HuaweiModification/HiApp-CategoryTest2/InputMockupImage.png");
//		implementationXmlTextField.setText("/Users/KevinMoran/Dropbox/Documents/My_Graduate_School_Work/SEMERU/git_src_code/gitlab-code/Android-GUI-Checker/Subjects/Full-Examples/Current-Files/Implementation/Huawei-App-Store/December-16/Nexus-6P/HiApp-Category.xml");
//		implementationImageTextField.setText("/Users/KevinMoran/Dropbox/Documents/My_Graduate_School_Work/SEMERU/git_src_code/gitlab-code/Android-GUI-Checker/Subjects/Full-Examples/Current-Files/Implementation/Huawei-App-Store/December-16/Nexus-6P/HiApp-Category.png");
//		implementationSrcTextField.setText("/Users/KevinMoran/Dropbox/Documents/My_Graduate_School_Work/SEMERU/git_src_code/gitlab-code/Android-GUI-Checker/Subjects/Full-Examples/Current-Files/Implementation/Huawei-App-Store/December-16/Nexus-6P/layout");
//		outputFolderTextField.setText("/Users/KevinMoran/Desktop/output");

		//Set Up Labels for the Text Fields and Buttons

		JLabel outputPathLabel = new JLabel("Output Path:");
		JLabel adbPathLabel = new JLabel("ADB Path:");
		JLabel versionNumberLabel = new JLabel("v0.1");
		timer = new JLabel("3:00");
		timerLabel = new JLabel("Video Time Remaining:");

		BufferedImage previewPic = ImageIO.read(new File("libs" + File.separator + "img" + File.separator + "preview.png"));
		previewPicLabel = new JLabel(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));


		//Set up Buttons and set corresponding ActionListeners

		JButton outputFolderSelectorBtn = new JButton();
		outputFolderSelectorBtn.addActionListener(new OutputFolderSelectorBtnListener());
		
	    JButton adbSelectorBtn = new JButton();
	    adbSelectorBtn.addActionListener(new adbBtnListener());
		
		startBtn = new JButton("Start Capture");
		startBtn.setToolTipText("Click Here to capture a video recording and replayable script of actions on your android device.");
		startBtn.addActionListener(new startBtnListener());
		
		stopBtn = new JButton("End Capture");
        stopBtn.setToolTipText("Click Here to to stop the capture process.");
        stopBtn.addActionListener(new stopBtnListener());
        stopBtn.setEnabled(false);

		nextBtn = new JButton("Next");
		nextBtn.setToolTipText("Click here to start the Trace Replayer process.");
        nextBtn.setEnabled(true);
		nextBtn.addActionListener(new nextBtnListener());

		backBtn = new JButton("←");
        backBtn.setEnabled(false);
		backBtn.addActionListener(new backBtnListener());

		
		// TEST END

		ImageIcon gvtIcon = new ImageIcon("resources/GVT-Logo.png");
		
		try {
			Image img = ImageIO.read(new File("resources/File-Open.png"));
			img = img.getScaledInstance(25, 25, Image.SCALE_DEFAULT);
			outputFolderSelectorBtn.setIcon(new ImageIcon(img));
			adbSelectorBtn.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		///JTabbedPane tabbedPane = new JTabbedPane();
		//setContentPane(tabbedPane);

		toolPane = new JPanel(new GridBagLayout());
		extraPanel = new JPanel(new GridBagLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrPane = new JScrollPane(toolPane);
		Border padding = BorderFactory.createEmptyBorder(2, 4, 2, 4);
		scrPane.setBorder(padding);

		mainPanel.add(extraPanel);
		mainPanel.add(toolPane);

		add(mainPanel);
		
	    cdTimer = new Timer(1000, new TimerListener());
	    cdTimer.setInitialDelay(0);

		c = new GridBagConstraints(); //since there aren't too  many components, we just use one constraints object
		c.insets = new Insets(2, 0, 2, 0);

		c.anchor = GridBagConstraints.LINE_START;

		//----------------------------------------------------
		//Interface for Selecting the Path to the MockUp Folder

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		toolPane.add(outputPathLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 1;
		toolPane.add(outputFolderTextField, c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		toolPane.add(outputFolderSelectorBtn, c);
		
	    c.gridx = 0;
	    c.gridy = 2;
	    c.gridwidth = 2;
	    toolPane.add(adbPathLabel,c);
		
	    c.gridx = 0;
	    c.gridy = 3;
	    c.weightx = 1;
	    c.gridwidth = 1;
	    toolPane.add(adbTextField, c);
	    
	    c.gridx = 1;
	    c.gridy = 3;
	    c.weightx = 1;
	    toolPane.add(adbSelectorBtn, c);

		//----------------------------------------------------



		//----------------------------------------------------
		//Analysis Button and Status Label

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 13;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		toolPane.add(startBtn, c);
		
		c.gridx = 1;
        c.gridy = 13;
		c.gridwidth = 1;
        c.weightx = 1;
        toolPane.add(stopBtn, c);

		c.gridx = 2; // Adjust the x-coordinate for positioning
		c.gridy = 13; // Place the button on a new row below the others
        c.gridwidth = 1;
        c.weightx = 1;
		toolPane.add(nextBtn, c);

		statusLabel = new JLabel("Current Status: Awaiting Capture");
		c.gridx = 0;
		c.gridy = 14;
		toolPane.add(statusLabel, c);
		
		c.gridx = 0;
        c.gridy = 15;
        toolPane.add(timerLabel, c);
        
        c.gridx = 1;
        c.gridy = 15;
        toolPane.add(timer, c);

		excpetionLabel.setVisible(false);
		c.gridx = 0;
		c.gridy = 42;
		toolPane.add(excpetionLabel, c);

		c.gridx = 2;
		c.gridy = 42;
		c.weightx = 1;
		toolPane.add(versionNumberLabel, c);

		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = -1;
		c.gridy = 0;
		c.weightx = 2;
		extraPanel.add(backBtn, c);

		setTitle("Android Video Capture Tool");
		setIconImage(gvtIcon.getImage());
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		loading = new JDialog(this);
		JPanel p1 = new JPanel();
		p1.add(new JLabel("Processing, Please Wait..."));
		loading.setUndecorated(true);
		loading.getContentPane().add(p1);
		loading.pack();
		loading.setLocationRelativeTo(this);
		loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		loading.setModal(true);
	}

    private void loadTraceReplayerPanel() {
        TraceReplayerPanel t = new TraceReplayerPanel(outputFolderTextField.getText());
		JScrollPane scrPane = new JScrollPane(t);
		Border padding = BorderFactory.createEmptyBorder(2, 4, 2, 4);
		scrPane.setBorder(padding);

		mainPanel.removeAll();
		mainPanel.add(extraPanel);
		mainPanel.add(scrPane);

		repaint();
		revalidate();
        pack();
    }

	 public class TimerListener implements ActionListener {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	                SimpleDateFormat df=new SimpleDateFormat("mm:ss");
	                timer.setText(df.format(count));
	                repaint();
	                count = count-1000;

	        }
	 }
	 

	public class OutputFolderSelectorBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			JFileChooser fc = new JFileChooser();

			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.showOpenDialog(MainScreen.this);
			if (fc.getSelectedFile() != null){
				outputFolderPath = fc.getSelectedFile().getAbsolutePath();
				outputFolderTextField.setText(outputFolderPath);
			}
		}
	}
	
	   public class adbBtnListener implements ActionListener {
	        @Override
	        public void actionPerformed(ActionEvent e) {

	            JFileChooser fc = new JFileChooser();

	            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	            fc.showOpenDialog(MainScreen.this);
	            if (fc.getSelectedFile() != null){
	                adbPath = fc.getSelectedFile().getAbsolutePath();
	                adbTextField.setText(adbPath);
	            }

	        }
	    }

	   public class stopBtnListener implements ActionListener {
	        @Override
	        public void actionPerformed(ActionEvent e) {

	            SwingWorker<String, Void> worker2 = new SwingWorker<String, Void>() {

	                @Override
	                protected String doInBackground() throws InterruptedException {
	                    excpetionLabel.setVisible(false);
	                    if(outputFolderTextField.getText() != null && !outputFolderTextField.getText().isEmpty()){
	                        try {

	                            Thread.sleep(4000);
	                            videoProcess.destroy();
	                            geteventProcess.destroy();
	                            startBtn.setEnabled(true);
	                            stopBtn.setEnabled(false);
	                            cdTimer.stop();
	                            count=180000; 
	                            Thread.sleep(3000);
	                            SimpleDateFormat df=new SimpleDateFormat("mm:ss");
	                            timer.setText(df.format(count));
	                            Controller.pullVideo(outputFolderTextField.getText() + File.separator + startTimeStamp + "video.mp4", adbTextField.getText());
	                            
	                            
	                            File video = new File(outputFolderTextField.getText() + File.separator + startTimeStamp + "video.mp4");
	                            File getevent = new File(outputFolderTextField.getText() + File.separator + startTimeStamp + "getevent.log");

	                            if(video.exists() && getevent.exists()){

	                                statusLabel.setForeground(Color.GREEN);
	                                statusLabel.setText("Capture Complete!");
                                    nextBtn.setEnabled(true);

	                            }else{
	                                statusLabel.setForeground(Color.RED);
	                                statusLabel.setText("Unable to Connect to Device!");
	
	                                BufferedImage previewPic = ImageIO.read(new File("libs" + File.separator + "img" + File.separator + "preview.png"));
	                                previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));

	                            }

	                        } catch (Exception e2) {
	                            StringWriter sw = new StringWriter();
	                            PrintWriter pw = new PrintWriter(sw);
	                            e2.printStackTrace(pw);
	                            // stack trace as a string
	                            statusLabel.setText("Error Running Analysis! Please check your Settings.");
	                            excpetionLabel.setText(sw.toString());
	                            excpetionLabel.setVisible(true);
	                            System.out.println(sw.toString());
	                            done();
	                        }

	                    }else{
	                        System.out.println("Parameters not correct!");
	                        statusLabel.setForeground(Color.RED);
	                        statusLabel.setText("Please Ensure all Fields are Filled!!");

	                        BufferedImage previewPic;
	                        try {
	                            previewPic = ImageIO.read(new File("libs" + File.separator + "img" + File.separator + "preview.png"));
	                            previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));
	                        } catch (IOException e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }


	                    }

	                    return "done";

	                }
	                @Override
	                protected void done() {
	                    loading.dispose();
	                }
	            };

	            worker2.execute();
	            statusLabel.setText("Capturing Information...");
	            loading.setLocation(getFrameXCoord(), getFrameYCoord());
	            loading.setVisible(true);

	        }
	    }

	public class startBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		    SwingWorker<String, Void> worker2 = new SwingWorker<String, Void>() {
		    	
                @Override
                protected String doInBackground() throws InterruptedException {
                	//boolean validDir = true;
                    excpetionLabel.setVisible(false);
                    try {
                        File outputFile = new File(outputFolderTextField.getText());
                        if(!outputFile.exists()) {
                        	if(!outputFile.mkdirs())
                        		throw new Exception();
                        }
                        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH.mm.ss");
                        LocalTime time = LocalTime.now();
                        startTimeStamp = "[" + time.format(tf) + "]";
                        startBtn.setEnabled(false);
                        stopBtn.setEnabled(true);
                        videoProcess = Controller.startVideoCapture(adbTextField.getText());
                        geteventProcess = Controller.startGetEventCapture(outputFolderTextField.getText() + File.separator+ startTimeStamp +"getevent.log", adbTextField.getText());
                        loading.setVisible(false);
                        cdTimer.start();
//                            File screenshot = new File(outputFolderTextField.getText() + File.separator + "screen.png");
//                            File uiDump = new File(outputFolderTextField.getText() + File.separator + "ui-dump.xml");

//                            if(screenshot.exists() && uiDump.exists()){

//                                BufferedImage ssPreview = ImageIO.read(screenshot);

//                                statusLabel.setForeground(Color.GREEN);
//                                statusLabel.setText("Capture Complete!");
//                                previewPicLabel.setIcon(new ImageIcon(ssPreview.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));

//                            }else{
//                                statusLabel.setForeground(Color.RED);
//                                statusLabel.setText("Unable to Connect to Device!");
//
//                                BufferedImage previewPic = ImageIO.read(new File("libs" + File.separator + "img" + File.separator + "preview.png"));
//                                previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));

//                            }

                    } catch (Exception e2) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e2.printStackTrace(pw);
                        // stack trace as a string
                        statusLabel.setText("Error Running Analysis! Please check your Settings.");
                        excpetionLabel.setText(sw.toString());
                        excpetionLabel.setVisible(true);
                        System.out.println(sw.toString());
                        done();
                    }

                    return "done";

                }
                @Override
                protected void done() {
                    loading.dispose();
                }
            };

            if(outputFolderTextField.getText() != null && !outputFolderTextField.getText().isEmpty()) {
                worker2.execute();
                statusLabel.setText("Capturing Information...");
                loading.setLocation(getFrameXCoord(), getFrameYCoord());
                loading.setVisible(true);
            } else {
                System.out.println("Parameters not correct!");
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Please Ensure all Fields are Filled!!");
                
                BufferedImage previewPic;
                try {
                    previewPic = ImageIO.read(new File("libs" + File.separator + "img" + File.separator + "preview.png"));
                    previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
            
        }
	}

    // Swaps over to a screen that collects all the necessary trace replayer information
    public class nextBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadTraceReplayerPanel();
            revalidate();
            repaint();
			backBtn.setEnabled(true);
        }
    }

	public class backBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
			mainPanel.removeAll();
			mainPanel.add(extraPanel);
			mainPanel.add(toolPane);

			nextBtn.setEnabled(false);
			backBtn.setEnabled(false);
            revalidate();
            repaint();
			pack();
        }
    }


	public int getFrameXCoord(){
		int x =0;  
		x = (this.getWidth() - loading.getWidth()) / 2;
		x += this.getLocationOnScreen().x;
		return x;
	}

	public int getFrameYCoord(){
		int y =0;  
		y = (this.getHeight() - loading.getHeight()) / 2;
		y += this.getLocationOnScreen().y;
		return y;
	}

	
	
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			MainScreen ex;
			try {
				ex = new MainScreen();
				ex.setVisible(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		//getignoredDimensions(defaultIgnored);
	}
}
