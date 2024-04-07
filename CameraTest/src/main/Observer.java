package main;

import java.util.Queue;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Observer {

	// Start camera
	private VideoCapture capture;
	
	// Store image as 2D matrix
	private Mat image = new Mat();
	
	private int cameraId;
	private long beginTime;
	
	private Thread cameraThread;
	
	public Observer(int cameraId) {
		this.cameraId = cameraId;
		System.out.println("Waiting for camera...");
		capture = new VideoCapture(cameraId);
		while (!capture.isOpened());
		System.out.println("Camera connected");
		cameraThread = new Thread(() -> startCamera());
		//cameraThread.start();
	}
	
	// BLOCKING
	private void startCamera() {
		begin();
		while(true) {
//			synchronized (image) {
//				capture.read(image);
//			}
		}
	}
	
	Photograph snapshot() {
		Mat copy = new Mat();
		long now;
//		synchronized (image) {
//			capture.read(image);
//			now = System.currentTimeMillis();
//			
//			copy = image.clone();
//		}
		capture.read(copy);
		now = System.currentTimeMillis();
		float timeStamp = (now - beginTime) / 1000.0f;
		
		return new Photograph(copy, timeStamp);
	}
	
	public void begin() {
		beginTime = System.currentTimeMillis();
	}
	public void shutdown() {
		try {
			cameraThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
