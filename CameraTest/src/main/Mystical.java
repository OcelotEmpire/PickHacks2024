package main;

import java.util.concurrent.ExecutionException;

import org.opencv.core.Core;

public class Mystical {
	private Thinker thinker;
	private Observer observer;
	
	private boolean inProgress = false;
	
	private float progress = 0;
	
	public Mystical() {
		thinker = new Thinker(12);
		observer = new Observer(0);
	}
	
	public void beginDance(double duration, double sampleTime) {
		if (inProgress) return;
		System.out.println("Beginning Dance!");
		inProgress = true;
		
		observer.begin();
		
		long then = System.nanoTime();
		long now;
		double acc = 0;
		while (progress < duration) {
			now = System.nanoTime();
			double delta = (now - then) / 1e9d;
			acc += delta;
			progress += delta;
			if (acc > sampleTime) {
				acc -= sampleTime;
				Photograph photo = observer.snapshot();
				thinker.think(photo);
			}
			then = now;
		}
		
		System.out.println(thinker.getQueue().size() + " tasks queued.");
		while (!thinker.getQueue().isEmpty()) {
			Keyframe keyframe;
			try {
				keyframe = thinker.getQueue().poll().get();
				System.out.println(keyframe.getPoints()[0] + ", " + keyframe.getTimestamp());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
		
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

	public float getProgress() {
		return progress;
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mystical mystical = new Mystical();
		Viewer viewer = new Viewer(mystical.getObserver());
		mystical.beginDance(10, 0.5);
	}
}
