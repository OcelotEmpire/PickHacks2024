package main;

import org.opencv.core.Point;
import java.util.ArrayList;

public class Keyframe {
	private ArrayList<Point> points;
	private float timestamp;
	
	public Keyframe()
	{
		this.points = new ArrayList<>();
		this.timestamp = 0f;
	}
	
	public Keyframe(ArrayList<Point> points, float timestamp)
	{
		this.points = points;
		this.timestamp = timestamp;
	}
	
	public ArrayList<Point> getPoints()
	{
		return this.points;
	}
	
	public float getTimestamp()
	{
		return this.timestamp;
	}
	
	static public double compare(Keyframe k1, Keyframe k2)
	{
		return 0d;
	}
}
