package main;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.sound.sampled.LineUnavailableException;

import org.opencv.core.Core;

public class Mystical {
	private Thinker thinker;
	private Observer observer;
	private Speaker speaker;
	private Viewer viewer;
	
	private boolean inProgress = false;
	
	private double progress = 0;
	
	private static double dur = 10d;
	private static double sTime = 1d;
	
	private static double scoreSum = 0d;
	
	public Mystical() {
		thinker = new Thinker(4);
		observer = new Observer(1);
		try {
			speaker = new Speaker();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewer = new Viewer(observer);
	}
	
	public void beginDance(double duration, double sampleTime) {
		if (inProgress) return;
		System.out.println("Beginning Dance!");
		inProgress = true;
		
		File file  = new File(String.valueOf("DanceStorage.txt"));
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		var allFrames = DanceStorage.getKeyframes();
		
		observer.begin();
		speaker.playAudio("res/audio/mother3.wav");
		
		long then = System.nanoTime();
		long now;
		long totalElapsedNano = 0;
		long sampleTimeNano = (long)(sampleTime * 1e9);
		long acc = 0;
		progress = 0;
		float lastFrameStamp = 0;
		int lastFrameId = 0;
		while (progress < duration) {
			now = System.nanoTime();
			long delta = (now - then);
			totalElapsedNano += delta;
			acc += delta;
			progress = totalElapsedNano * 1e-9d;
			if (lastFrameId+1 < allFrames.size() && progress > lastFrameStamp) {
				Keyframe currentFrame = allFrames.get(lastFrameId);
				viewer.showKeyframe(currentFrame);
				lastFrameId++;
				lastFrameStamp = currentFrame.getTimestamp();
			}
			if (acc > sampleTimeNano) {
				acc -= sampleTimeNano;
				Photograph photo = observer.snapshot();
				thinker.think(photo);
			}
			then = now;
		}
		System.out.println("Dance over, finishing up some calculations...");
		
		System.out.println(thinker.getQueue().size() + " tasks queued.");
		long startTime = System.nanoTime();
		while (!thinker.getQueue().isEmpty()) {
			Keyframe keyframe;
			try {
				keyframe = thinker.getQueue().poll().get();
				System.out.println(keyframe.getPoints()[0] + ", " + keyframe.getTimestamp());
				if (!DanceStorage.setToWrite)
				{
					System.out.println("FRAME SCORE: " + Keyframe.compare(keyframe, DanceStorage.getClosestKeyToTime(keyframe.getTimestamp())));
					scoreSum +=  Keyframe.compare(keyframe, DanceStorage.getClosestKeyToTime(keyframe.getTimestamp()));
				}
				DanceStorage.add(keyframe);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done! Lasted: " + (System.nanoTime() - startTime) * 1e-9d);
		
		inProgress = false;
	}
	
	
	
	public Thinker getThinker() {
		return thinker;
	}

	public Observer getObserver() {
		return observer;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public double getProgress() {
		return progress;
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mystical mystical = new Mystical();
		DanceStorage.getKeyframes();
		mystical.beginDance(41, 1);
	}
}
