package main;

public class FfmpegHandler {
	 public static String ffmpegPath = "ffmpeg";
	 public static void runCommand(String[] command) {
	        try {
	            System.out.println("running ffmpeg command: " + command);
	            ProcessBuilder builder = new ProcessBuilder(command);
	            builder.redirectErrorStream(true); // This merges the error stream with the standard output stream

	            // Start the process
	            Process process = builder.start();
	            // Wait for the process to complete
	            int exitCode = process.waitFor();
	            if (exitCode == 0) {
	            	System.out.println("FFMpeg exited successfully");
	            } else {
	                process.getErrorStream().transferTo(System.out);
	                System.out.println("FFMpeg exited with error code " + exitCode);
	            }
	        } catch (Exception e) {
	        	System.out.println("Error running ffmpeg command " + e);
	        }
	 }
}
