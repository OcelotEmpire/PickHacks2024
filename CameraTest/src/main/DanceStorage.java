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

public class DanceStorage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static ArrayList<Keyframe> keyframes = new ArrayList<>();

	public static ArrayList<Keyframe> getKeyframes() {
		try {
			FileInputStream fileInputStream = new FileInputStream("DanceStorage.txt");
		    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		    keyframes = new ArrayList<>();
		    try { 
		    	  for (;;) { 
		    	    keyframes.add((Keyframe) objectInputStream.readObject());
		    	    // System.out.println(keyframes.get(keyframes.size() - 1));
		    	  }
		    	} catch (EOFException e) {
		    	  // End of stream
		    	} 
		    objectInputStream.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	    
		return keyframes;
	}

	public static void setKeyframes(ArrayList<Keyframe> k) {
		keyframes = k;
		
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream("DanceStorage.txt");
			  ObjectOutputStream objectOutputStream;
				try {
					objectOutputStream = new ObjectOutputStream(fileOutputStream);
					for (Keyframe frame : k)
					{
						objectOutputStream.writeObject(frame);
					}
					objectOutputStream.flush();
					objectOutputStream.close();
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

}
