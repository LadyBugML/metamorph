/**
 * Created by Patrick ijieh on Mar 24, 2025
 */
package edu.semeru.android.capture.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;

import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import edu.semeru.android.capture.controller.Controller;

public class VideoCaptureScreen extends Screen {
	private GridBagConstraints c;

	private JTextField outputFolderTextField;
	private JTextField adbTextField;

    private JTextArea exceptionLabel;

	private String outputFolderPath;
	private String adbPath;
	
	private String startTimeStamp;

	private Process videoProcess;
	private Process geteventProcess;
	private File getevent;

	private JLabel statusLabel;
	private JLabel timerLabel;
	private JLabel timer;
	private JButton startBtn;
	private JButton stopBtn;

	private JLabel previewPicLabel;
	
	private int count = 180000;
	private Timer cdTimer;

	private JDialog loading;

    public VideoCaptureScreen() throws IOException {
        super(new GridBagLayout());
        initializeGUI();
    }

    private void initializeGUI() throws IOException {
        outputFolderTextField = new JTextField(10);
		adbTextField = new JTextField(10);
		exceptionLabel = new JTextArea(50,50);

		//Set Up Labels for the Text Fields and Buttons

		JLabel outputPathLabel = new JLabel("Output Path:");
		JLabel adbPathLabel = new JLabel("ADB Path:");
		timer = new JLabel("3:00");
		timerLabel = new JLabel("Video Time Remaining:");

		BufferedImage previewPic = ImageIO.read(new File("lib" + File.separator + "img" + File.separator + "preview.png"));
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
		
		try {
			Image fileOpenImage = ImageIO.read(new File("resources/File-Open.png"))
                                         .getScaledInstance(25, 25, Image.SCALE_DEFAULT);
			outputFolderSelectorBtn.setIcon(new ImageIcon(fileOpenImage));
			adbSelectorBtn.setIcon(new ImageIcon(fileOpenImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		this.add(outputPathLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 1;
		this.add(outputFolderTextField, c);

		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		this.add(outputFolderSelectorBtn, c);
		
	    c.gridx = 0;
	    c.gridy = 2;
	    c.gridwidth = 2;
	    this.add(adbPathLabel,c);
		
	    c.gridx = 0;
	    c.gridy = 3;
	    c.weightx = 1;
	    c.gridwidth = 1;
	    this.add(adbTextField, c);
	    
	    c.gridx = 1;
	    c.gridy = 3;
	    c.weightx = 1;
	    this.add(adbSelectorBtn, c);

		//----------------------------------------------------



		//----------------------------------------------------
		//Analysis Button and Status Label

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 13;
		c.weightx = 1;
		c.fill = GridBagConstraints.NONE;
		this.add(startBtn, c);
		
		c.gridx = 1;
        c.gridy = 13;
        c.weightx = 1;
        this.add(stopBtn, c);

        statusLabel = new JLabel("Current Status: Awaiting Capture");
		c.gridx = 0;
		c.gridy = 14;
        c.gridwidth = 3;
		this.add(statusLabel, c);
		
		c.gridx = 0;
        c.gridy = 15;
        c.gridwidth = 1;
        this.add(timerLabel, c);
        
        c.gridx = 1;
        c.gridy = 15;
        this.add(timer, c);

		c.gridx = 0;
		c.gridy = 42;
		this.add(exceptionLabel, c);
        exceptionLabel.setVisible(false);

        loading = new JDialog();
		JPanel p1 = new JPanel();
		p1.add(new JLabel("Processing, Please Wait..."));
		loading.setUndecorated(true);
		loading.getContentPane().add(p1);
		loading.pack();
		loading.setLocationRelativeTo(this);
		loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		loading.setModal(true);

        String commonADBPath = Controller.getADBPath();
        if (commonADBPath.length() > 0) {
            adbPath = commonADBPath;
            adbTextField.setText(commonADBPath);
        }
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
            fc.showOpenDialog(VideoCaptureScreen.this);
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
            fc.showOpenDialog(VideoCaptureScreen.this);
            if (fc.getSelectedFile() != null){
                adbPath = fc.getSelectedFile().getAbsolutePath();
                adbTextField.setText(adbPath);
            }
        }
    }

    public class stopBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

                @Override
                protected String doInBackground() throws InterruptedException {
                    exceptionLabel.setVisible(false);
                    if (outputFolderTextField.getText() != null && !outputFolderTextField.getText().isEmpty()) {
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

                            if (video.exists() && getevent.exists()) {
                                statusLabel.setForeground(Color.GREEN);
                                statusLabel.setText("Capture Complete!");
                                VideoCaptureScreen.this.getevent = getevent;
                                nextBtn.setEnabled(true);

                            } else {
                                statusLabel.setForeground(Color.RED);
                                statusLabel.setText("Unable to Connect to Device!");

                                BufferedImage previewPic = ImageIO.read(new File("lib" + File.separator + "img" + File.separator + "preview.png"));
                                previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));
                            }

                        } catch (Exception e) {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            e.printStackTrace(pw);
                            // stack trace as a string
                            statusLabel.setText("Error Running Analysis! Please check your Settings.");
                            exceptionLabel.setText(sw.toString());
                            exceptionLabel.setVisible(true);
                            System.out.println(sw.toString());
                            done();
                        }

                    } else {
                        System.out.println("Parameters not correct!");
                        statusLabel.setForeground(Color.RED);
                        statusLabel.setText("Please Ensure all Fields are Filled!!");

                        BufferedImage previewPic;
                        try {
                            previewPic = ImageIO.read(new File("lib" + File.separator + "img" + File.separator + "preview.png"));
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

            worker.execute();
            statusLabel.setText("Capturing Information...");
            loading.setLocation(getFrameXCoord(), getFrameYCoord());
            loading.setVisible(true);
        }
    }

    public class startBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws InterruptedException {
                    exceptionLabel.setVisible(false);
                    try {
                        File outputFile = new File(outputFolderTextField.getText());
                        if(!outputFile.exists()) {
                            if(!outputFile.mkdirs())
                                throw new Exception();
                        }
                        DateTimeFormatter tf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH.mm.ss");
                        LocalDateTime time = LocalDateTime.now();
                        startTimeStamp = "[" + time.format(tf) + "]";
                        startBtn.setEnabled(false);
                        stopBtn.setEnabled(true);
                        videoProcess = Controller.startVideoCapture(adbTextField.getText());
                        geteventProcess = Controller.startGetEventCapture(outputFolderTextField.getText() + File.separator+ startTimeStamp +"getevent.log", adbTextField.getText());
                        loading.setVisible(false);
                        cdTimer.start();

                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        // stack trace as a string
                        statusLabel.setText("Error Running Analysis! Please check your Settings.");
                        exceptionLabel.setText(sw.toString());
                        exceptionLabel.setVisible(true);
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

            if (outputFolderTextField.getText() != null && !outputFolderTextField.getText().isEmpty()) {
                worker.execute();
                statusLabel.setText("Capturing Information...");
                loading.setLocation(getFrameXCoord(), getFrameYCoord());
                loading.setVisible(true);
            } else {
                System.out.println("Parameters not correct!");
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Please Ensure all Fields are Filled!!");
                
                BufferedImage previewPic;
                try {
                    previewPic = ImageIO.read(new File("lib" + File.separator + "img" + File.separator + "preview.png"));
                    previewPicLabel.setIcon(new ImageIcon(previewPic.getScaledInstance(120, 214, Image.SCALE_SMOOTH)));
                } catch (IOException ioException) {
                    // TODO Auto-generated catch block
                    ioException.printStackTrace();
                }
            }
        }
    }

    int getFrameXCoord() {
		int x =0;  
		x = (this.getWidth() - loading.getWidth()) / 2;
		x += this.getLocationOnScreen().x;
		return x;
	}

	int getFrameYCoord() {
		int y =0;  
		y = (this.getHeight() - loading.getHeight()) / 2;
		y += this.getLocationOnScreen().y;
		return y;
	}

    public String getOutputPath() {
        return outputFolderPath;
    }

    public File getEventLog() {
        return getevent;
    }
}
