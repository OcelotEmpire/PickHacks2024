package main;

import java.io.Serializable;
import org.opencv.core.Point;

public class Keyframe implements Serializable {
	private static final long serialVersionUID = 1472321397216841824L;
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
			0.25f, 0.25f, 0.79f, 0.72f, 0.62f, 0.79f, 0.72f, 0.62f, 1.07f, 0.87f, 0.89f, 1.07f, 0.87f, 0.89f 
	};
	
	
	private transient Point[] points;
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
	
	public static double compare(Keyframe k1, Keyframe k2) {
		double sum = 0d;
		
		for (int i = 0; i < NUM_POINTS; i++) {
			Point diff = new Point(k1.points[0].x - k2.points[0].x, k1.points[0].y - k2.points[0].y);
			Point compPoint = new Point (k2.getPoints()[i].x - diff.x, k2.getPoints()[i].y - diff.y);
			sum += Keyframe.comparePoint(k1.getPoints()[i], compPoint, i);
		}
		
//		System.out.println("SUM: " + sum);
		return sum / NUM_POINTS;
	}
	
	private static double comparePoint(Point p1, Point p2, int type) {
		double d = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
		if (d == 0) return 1.0;
		double k = 2d * KEYPOINT_CONSTS[type];
		double s = 1d;
		
		double den = (2d * Math.pow(s, 2d) * Math.pow(k, 2d));
		
		double exp = Math.exp(-(Math.pow(d, 2) / den));
//		System.out.println("DENOM: " + den);
//		System.out.println("EXP: " + exp);
		return exp;
		
	}
	
	public void setPoints(Point[] p)
	{
		this.points = p;
	}
}
