package main;

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
	
	static public double compare(Keyframe k1, Keyframe k2) {
		return 0d;
	}
}
