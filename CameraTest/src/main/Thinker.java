package main;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class Thinker {
	// Specify the paths for the 2 files
	static final String PROTO_FILE = "res/pose_deploy_linevec_faster_4_stages.prototxt";
	static final String WEIGHTS_FILE = "res/pose_iter_160000.caffemodel";
	
	private ExecutorService multiExecutor;
	private BlockingQueue<Future<Keyframe>> outputQueue;
	private Net poseModel;
	
	public Thinker(int numThreads) {
		//monoExecutor = Executors.newSingleThreadExecutor();
		multiExecutor = Executors.newFixedThreadPool(numThreads);
		outputQueue = new LinkedBlockingQueue<>();

		// Read the network into Memory
		poseModel = Dnn.readNetFromCaffe(PROTO_FILE, WEIGHTS_FILE); 
	}
	
	public void think(Photograph frame) {
		Future<Keyframe> futureFrame = 
				multiExecutor.submit(() -> {
					return keyTheFrame(frame);
				});
		outputQueue.add(futureFrame);
	}
	
	public synchronized Mat guesstimate(Mat inputBlob) {
		poseModel.setInput(inputBlob);
		Mat output = poseModel.forward();
		return output;
	}
	
	public Keyframe keyTheFrame(Photograph photo) {
		final Mat frame = photo.frame();
		final int frameWidth = frame.size(1), frameHeight = frame.size(0);
		final int inWidth = 368, inHeight = 368;
		Mat inputBlob = Dnn.blobFromImage(frame, 1.0/255, new Size(inWidth, inHeight), new Scalar(0, 0, 0), false, false);
		
		Mat output = guesstimate(inputBlob);
		
		int outHeight = output.size(2), outWidth = output.size(3);
		Point points[] = new Point[Keyframe.NUM_POINTS];
		for (int i = 0; i < Keyframe.NUM_POINTS; i++) {
			Mat probMap = new Mat(outHeight, outWidth, CvType.CV_32F);
			for (int r = 0; r < outHeight; r++) {
				for (int c = 0; c < outWidth; c++) {
					probMap.put(new int[] {r, c}, output.get(new int[] {0, i, r, c}));
				}
			}
			Point p = new Point(-1,-1);
			Core.MinMaxLocResult result = org.opencv.core.Core.minMaxLoc(probMap);
			if (result.maxVal > 0.1)
			{
				p = result.maxLoc;
				p.x *= (float) frameWidth / outWidth;
				p.y *= (float) frameHeight / outHeight;
			}
			points[i] = p;
		}
		return new Keyframe(points, photo.timeStamp());
	}
	
	public BlockingQueue<Future<Keyframe>> getQueue() {
		return outputQueue;
	}
}
