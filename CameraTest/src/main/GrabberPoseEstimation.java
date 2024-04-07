package main;

import java.awt.EventQueue;

import org.opencv.core.MatOfByte;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

public class GrabberPoseEstimation {

	// Specify the paths for the 2 files
	final String PROTO_FILE = "pose/mpi/pose_deploy_linevec_faster_4_stages.prototxt";
	final String WEIGHTS_FILE = "pose/mpi/pose_iter_160000.caffemodel";
	
	private Net net;
	
	public GrabberPoseEstimation() {
		// Read the network into Memory
		net = Dnn.readNetFromCaffe(PROTO_FILE, WEIGHTS_FILE);
	}
	
	public static void main(String[] args) {
		final Grabber grabber = new Grabber();
		final GrabberPoseEstimation gpe = new GrabberPoseEstimation();

		// Start camera in thread
		new Thread(() -> {
			grabber.startCamera();
		}).start();
		
		MatOfByte buf = grabber.takeCapture();
		
		
	}
}
