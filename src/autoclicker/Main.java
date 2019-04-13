package autoclicker;

import java.io.File;
import java.net.URISyntaxException;

public class Main {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//https://www.chilkatsoft.com/java-loadlibrary-windows.asp
		try {
			File file = new File("dll/Jintellitype.dll");
	    	String pathLocation = file.getAbsolutePath();
	    	System.load(pathLocation);
	    	
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	      System.exit(1);
	    }
		System.out.println("Succesfully loaded library");
		
		
	}//end main method

}//end class
