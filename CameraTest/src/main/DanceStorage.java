package main;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.opencv.core.Point;

public class DanceStorage implements Serializable{
	
	public static boolean setToWrite = false;

	private static final long serialVersionUID = 1L;
	
	private static ArrayList<Keyframe> keyframes = new ArrayList<>();

	public static ArrayList<Keyframe> getKeyframes() {
		System.out.println("Loading keyframes");
		try {
			FileInputStream fileInputStream = new FileInputStream("DanceStorage.txt");
		    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		    FileInputStream fileInputStream2 = new FileInputStream("Points.txt");
		    ObjectInputStream objectInputStream2 = new ObjectInputStream(fileInputStream2);
		    keyframes = new ArrayList<>();
		    try { 
		    	  for (;;) { 
		    	    keyframes.add((Keyframe) objectInputStream.readObject());
		    	    Point[] newPoints = new Point[14];
		    	    for (int i = 0; i < 14; i++)
		    	    {
		    	    	newPoints[i] = new Point((double) objectInputStream2.readObject(), (double) objectInputStream2.readObject());
		    	    }
		    	    keyframes.get(keyframes.size() - 1).setPoints(newPoints);
		    	    System.out.println(keyframes.get(keyframes.size() - 1).getPoints()[0].x);
		    	  }
		    	} catch (EOFException e) {
		    	  // End of stream
		    	} 
		    objectInputStream.close(); 
		    objectInputStream2.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	    
		System.out.println("Loaded keyframes.");
		return keyframes;
	}

	public static void setKeyframes(ArrayList<Keyframe> k) {
		if (!setToWrite) return;
		keyframes = k;
		
		FileOutputStream fileOutputStream;
		FileOutputStream fileOutputStream2;
		try {
			fileOutputStream = new FileOutputStream("DanceStorage.txt");
			fileOutputStream2 = new FileOutputStream("Points.txt");
			  ObjectOutputStream objectOutputStream;
			  ObjectOutputStream objectOutputStream2;
				try {
					objectOutputStream = new ObjectOutputStream(fileOutputStream);
					objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
					for (Keyframe frame : k)
					{
						objectOutputStream.writeObject(frame);
						for (Point p : frame.getPoints())
						{
							objectOutputStream2.writeObject(p.x);
							objectOutputStream2.writeObject(p.y);
						}
					}
					objectOutputStream.flush();
					objectOutputStream.close();
					objectOutputStream2.flush();
					objectOutputStream2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void add(Keyframe k)
	{
		keyframes.add(k);
		DanceStorage.setKeyframes(keyframes);
	}
	
	public static void clear()
	{
		keyframes = new ArrayList<>();
		setKeyframes(keyframes);
	}
	
	public static Keyframe getClosestKeyToTime(float t)
	{
		Keyframe bestMatch = keyframes.get(0);
		for (Keyframe k : keyframes)
		{
			if (Math.abs(k.getTimestamp() - t) > Math.abs(bestMatch.getTimestamp() - t)) bestMatch = k;
		}
		
		return bestMatch;
	}

}
