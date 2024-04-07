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
public class Viewer {
	public static final int CAMERA_NUM = 0;
	
	// Window
	private JFrame frame;
	
	// Camera screen
	private JPanel cameraScreen;

	// Store image as 2D matrix
	private Mat image;
	private Keyframe keyFrame = null;
	
	private Observer observer;

	public Viewer(Observer observer) {
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
				
				if (image.empty()) return;
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

		frame.setPreferredSize(new Dimension(640, 560));
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		image = new Mat();
		this.observer = observer;
		
		new Thread(() -> {
			this.startCamera();
		}).start();
	}

	// Creating a camera
	public void startCamera() {
		
		while (true) {
			image = observer.snapshot().frame();
			cameraScreen.repaint();
		}
	}
}