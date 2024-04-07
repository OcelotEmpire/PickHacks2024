// Java Program to take a Snapshot from System Camera 
// using OpenCV 

// Importing openCV modules 
package main;

import java.awt.Color;
// importing swing and awt classes 
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
// Importing VideoCapture class 
// This class is responsible for taking screenshot 
import org.opencv.videoio.VideoCapture;

// Class - Swing Class 
public class Grabber {
	// Window
	private JFrame frame;
	
	// Camera screen
	private JPanel cameraScreen;

	// Button for image capture
	private JButton btnCapture;
	private JButton btnPose;

	// Start camera
	private VideoCapture capture;

	// Store image as 2D matrix
	private Mat image;
	private Keyframe keyFrame = null;

	private boolean clicked = false;
	
	GrabberPoseEstimation gpe;

	public Grabber() {
		frame = new JFrame("Mystical Camera");
		// Designing UI
		frame.setLayout(null);

		cameraScreen = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// convert matrix to byte
				MatOfByte buf = new MatOfByte();
				byte[] imageData;
				ImageIcon icon;
				
				Imgcodecs.imencode(".jpg", image, buf);

				imageData = buf.toArray();

				// Add to JLabel
				icon = new ImageIcon(imageData);
				
				g.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(), null);
				if (keyFrame != null) {
					final Point[] points = keyFrame.getPoints();
					g.setColor(Color.RED);
					for (int[] pair : Keyframe.ADJACENCY_MAP) {
						Point p1 = points[pair[0]];
						Point p2 = points[pair[1]];
						g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
					}
					g.setColor(Color.YELLOW);
					int i=0;
					for (Point p : points) {
						g.fillOval((int)p.x-5, (int)p.y-5, 10, 10);
						g.drawString(Keyframe.KEYFRAME_NAMES[i], (int)p.x + 5, (int)p.y + 2);
						i++;
					}
				}
			}
		};
		cameraScreen.setBounds(0, 0, 640, 480);
		frame.add(cameraScreen);

		btnCapture = new JButton("capture");
		btnCapture.setBounds(260, 480, 80, 40);
		frame.add(btnCapture);

		btnCapture.addActionListener((ActionEvent e) -> {
			clicked = true;
		});
		
		btnPose = new JButton("estimate pose");
		btnPose.setBounds(340, 480, 80, 40);
		frame.add(btnPose);
		btnPose.addActionListener((ActionEvent e) -> {
			estimatePose();
		});

		frame.setPreferredSize(new Dimension(640, 560));
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		capture = new VideoCapture(1);
		image = new Mat();
		gpe = new GrabberPoseEstimation();
	}

	// Creating a camera
	public void startCamera() {
		System.out.println("Starting camera...");
		
		while (true) {
			getImage();
			
			cameraScreen.repaint();

			// Capture and save to file
			if (clicked) {
				// prompt for enter image name
				System.out.println("Grabbing image...");
				String name = JOptionPane.showInputDialog(this, "Enter image name");
				if (name == null) {
					name = "bungus";
				}

				// Write to file
				boolean success = Imgcodecs.imwrite("res/images/" + name + ".jpg", image);
				System.out.println(success ? "successfully saved" : "failed to save :(");

				clicked = false;
			}
		}
	}
	synchronized Mat getImage() {
		// read image to matrix
		capture.read(image);
		
		return image.clone();
	}
	synchronized void estimatePose() {
		keyFrame = gpe.estimatePose(getImage());
	}

	// Main driver method
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(() -> {
			final Grabber grabber = new Grabber();

			// Start camera in thread
			new Thread(() -> {
				grabber.startCamera();
			}).start();
		});
	}
}