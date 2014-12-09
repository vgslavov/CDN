import java.io.*;


public class run_exe {
	public static void main(String ek[]){
		 
		try {
			String command ="sudo rm /home/tk27f/del.now";
			final Process process = Runtime.getRuntime().exec(command);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
