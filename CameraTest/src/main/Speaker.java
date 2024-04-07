package main;


//Java program to play an Audio 
//file using Clip Object 
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException; 

public class Speaker {
//	private Clip clip;
//	public Speaker() throws LineUnavailableException {
//		clip = AudioSystem.getClip();
//	}

	public static synchronized void playAudio(String path) {
		new Thread(() -> {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
				clip.open(inputStream);
				clip.start();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}).start();
	}

}
