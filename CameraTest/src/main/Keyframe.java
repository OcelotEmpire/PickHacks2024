package main;

import org.opencv.core.Point;

public class Keyframe {
	private Point[] points;
	private float timestamp;
	
	public static final int NUM_POINTS = 14;
	
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
