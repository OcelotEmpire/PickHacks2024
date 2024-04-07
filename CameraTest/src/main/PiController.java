package main;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class PiController {
		
	private Session session;
	private Channel channel;
	
	public PiController(String host, String user, String password)
	{
    	java.util.Properties config = new java.util.Properties(); 
    	config.put("StrictHostKeyChecking", "no");
    	
    	JSch jsch = new JSch();
    	try {
			this.session=jsch.getSession(user, host, 22);
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	this.session.setPassword(password);
    	this.session.setConfig(config);
    	
    	try {
			this.session.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("Connected");
	}
	
	public PiController()
	{
		this("127.0.0.1", "raspberry", "pi");
	}
	
	public void RunCommand(String command)
	{
		try 
		{
			this.channel=this.session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in;
			in = channel.getInputStream();
	        channel.connect();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.println(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
		}
		catch (JSchException | IOException e)
		{
			System.out.println("Pi command failed with " + e);
		}
	}
	
	public void EndSession(){
	    try{
	        this.channel.disconnect();
	        this.session.disconnect();
	        System.out.println("Session Ended");
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	}

}
