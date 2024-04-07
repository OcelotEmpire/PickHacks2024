package main;

import java.util.ArrayList;

public class VideoTimeline {
	private ArrayList<Keyframe> keyframes;
	
	public VideoTimeline()
	{
		this.keyframes = new ArrayList<>();
	}
	
	public VideoTimeline(ArrayList<Keyframe> k)
	{
		this.keyframes = k;
	}

	public ArrayList<Keyframe> getKeyframes() {
		return keyframes;
	}
	
	public void setKeyframes(ArrayList<Keyframe> k)
	{
		keyframes = k;
	}
	
}
