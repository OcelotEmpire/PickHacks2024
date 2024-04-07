package main;

import java.util.Dictionary;
import java.util.Map;

import org.opencv.core.Point;

public class Keyframe {
	public static final int NUM_POINTS = 14;
	public static final int[][] ADJACENCY_MAP = {
				{1, 0}, // head
				{1, 2}, {2, 3}, {3, 4}, // right arm
				{1, 5}, {5, 6}, {6, 7}, // left arm
				{1, 8}, {8, 9}, {9, 10}, // right leg
				{1, 11}, {11, 12}, {12, 13}, // left leg
			};
	public static final String[] KEYFRAME_NAMES	= {
			"head", "neck",
			"r_shoulder", "r_elbow", "r_hand",
			"l_shoulder", "l_elbow", "l_hand",
			"r_hip", "r_knee", "r_foot",
			"l_hip", "l_knee", "l_foot",
	};
	
	public static final float[] KEYPOINT_CONSTS = {
			0.025f, 0.025f, 0.079f, 0.072f, 0.062f, 0.079f, 0.072f, 0.062f, 0.107f, 0.087f, 0.089f, 0.107f, 0.087f, 0.089f 
	};
	
	
	private Point[] points;
	private float timestamp;
	
	public Keyframe() {
		this.points = new Point[NUM_POINTS];
		this.timestamp = 0f;
	}
	
	public Keyframe(Point[] points, float timestamp) {
		this.points = points;
		this.timestamp = timestamp;
	}
	
	public Point[] getPoints() {
		return this.points;
	}
	
	public float getTimestamp() {
		return this.timestamp;
	}
	
	public static double compare(Keyframe k1, Keyframe k2, int type) {
		double sum = 0d;
		for (int i = 0; i < NUM_POINTS; i++) {
			sum += Keyframe.comparePoint(k1.getPoints()[i], k2.getPoints()[i], i);
		}
		
		return sum / NUM_POINTS;
	}
	
	private static double comparePoint(Point p1, Point p2, int type) {
		double d = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
		double k = KEYPOINT_CONSTS[type];
		double s = 1d;
		double exp = Math.exp(-(Math.pow(d, 2) / (2 * Math.pow(s, 2) * Math.pow(d, 2))));
		return 0d;
		
	}
}
