package main;

import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.io.OutputStream;

public class Door {
	private boolean open = false;
	
	SerialPort port;
	
	public Door() {
		SerialPort ports[] = SerialPort.getCommPorts();
		port = ports[0];
	}
	
	
	public void open() {
		if (open) return;
		
		port.openPort();
		port.setBaudRate(9600);
		
		OutputStream os = port.getOutputStream();
		
		try {
			os.write(255);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		open = true;
	}
	public static void main(String[] args) {
		Door door = new Door();
		door.open();
	}
}
