package main;

import java.awt.EventQueue;
import java.nio.ByteBuffer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

public class GrabberPoseEstimation {

	// Specify the paths for the 2 files
	static final String PROTO_FILE = "res/pose_deploy_linevec_faster_4_stages.prototxt";
	static final String WEIGHTS_FILE = "res/pose_iter_160000.caffemodel";
	
	private Net net;
	
	public GrabberPoseEstimation() {
		// Read the network into Memory
		net = Dnn.readNetFromCaffe(PROTO_FILE, WEIGHTS_FILE);
	}
	
	public Keyframe estimatePose(Mat frame) {
		System.out.print("Input frame: ");
		System.out.println(frame);
		final int frameWidth = frame.size(1), frameHeight = frame.size(0);
		final int inWidth = 368, inHeight = 368;
		Mat inputBlob = Dnn.blobFromImage(frame, 1.0/255, new Size(inWidth, inHeight), new Scalar(0, 0, 0), false, false);
		System.out.print("Input blob: ");
		System.out.println(inputBlob);
		net.setInput(inputBlob);
		System.out.print("Net:");
		System.out.println(net);
		Mat output = net.forward();
		
		// remove later
		int outHeight = output.size(2), outWidth = output.size(3);
		Point points[] = new Point[Keyframe.NUM_POINTS];
		
//		output.convertTo(output, CvType.CV_8U);
		byte[] bytes = new byte[(output.rows() * output.cols() * output.channels())];
		output.get(0,0, bytes);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		
		for (int i = 0; i < Keyframe.NUM_POINTS; i++) {
			Mat probMap = new Mat(outHeight, outWidth, CvType.CV_32F);
			for (int r = 0; r < outHeight; r++) {
				for (int c = 0; c < outWidth; c++) {
					probMap.put(new int[] {r, c}, output.get(new int[] {0, i, r, c}));
				}
			}
			probMap = new Mat(outHeight, outWidth, CvType.CV_32F, buf);
			
			Point p = new Point(-1,-1);
			Core.MinMaxLocResult result = org.opencv.core.Core.minMaxLoc(probMap);
			if (result.maxVal > 0.9)
			{
				p = result.maxLoc;
				p.x *= (float) frameWidth / outWidth;
				p.y *= (float) frameHeight / outHeight;
				
				System.out.println("PX: " + p.x + ", PY: " + p.y);
				
			}
			
			points[i] = p;
			
		}
		System.out.print("Output blob: ");
		System.out.println(output);
		
		return new Keyframe(points, 0);
	}
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		final Grabber grabber = new Grabber();
		final GrabberPoseEstimation gpe = new GrabberPoseEstimation();

		// Start camera in thread
		new Thread(() -> {
			grabber.startCamera();
		}).start();
		
		Mat buf = grabber.getImage();
		gpe.estimatePose(buf);
		
		
	}
}
