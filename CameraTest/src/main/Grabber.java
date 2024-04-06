// Java Program to take a Snapshot from System Camera 
// using OpenCV 

// Importing openCV modules 
package main;

// importing swing and awt classes 
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
// Importing VideoCapture class 
// This class is responsible for taking screenshot 
import org.opencv.videoio.VideoCapture;

// Class - Swing Class 
public class Grabber extends JFrame {

	// Camera screen
	private JLabel cameraScreen;

	// Button for image capture
	private JButton btnCapture;

	// Start camera
	private VideoCapture capture;

	// Store image as 2D matrix
	private Mat image;

	private boolean clicked = false;

	public Grabber() {
		// Designing UI
		setLayout(null);

		cameraScreen = new JLabel();
		cameraScreen.setBounds(0, 0, 640, 480);
		add(cameraScreen);

		btnCapture = new JButton("capture");
		btnCapture.setBounds(300, 480, 80, 40);
		add(btnCapture);

		btnCapture.addActionListener((ActionEvent e) -> {
			clicked = true;
		});

		setPreferredSize(new Dimension(640, 560));
		pack();
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	// Creating a camera
	public void startCamera() {
		System.out.println("Starting camera...");
		capture = new VideoCapture(1);
		image = new Mat();
		byte[] imageData;

		ImageIcon icon;
		while (true) {
			// read image to matrix
			capture.read(image);

			// convert matrix to byte
			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", image, buf);

			imageData = buf.toArray();

			// Add to JLabel
			icon = new ImageIcon(imageData);
			cameraScreen.setIcon(icon);

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