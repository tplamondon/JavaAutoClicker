package autoclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Scanner;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;


public class Main {

	
	public static boolean isPaused = true;
	public static int msPauseTime = -1;
	
	public static final int PAUSE = 1;
	public static final int REDOTIMER = 2;
	public static final int EXIT = 3;
	
	public static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//https://www.chilkatsoft.com/java-loadlibrary-windows.asp
		/*try {
			File file = new File("dll/Jintellitype.dll");
	    	String pathLocation = file.getAbsolutePath();
	    	System.load(pathLocation);
	    	
	    } catch (UnsatisfiedLinkError e) {
	      System.err.println("Native code library failed to load.\n" + e);
	      System.exit(1);
	    }*/
		
		// next check to make sure JIntellitype DLL can be found and we are on
		// a Windows operating System
		if (!JIntellitype.isJIntellitypeSupported()) {
			System.exit(1);
		}
		
		//add a robot
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		System.out.println("Succesfully loaded library");
		

		
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			public void onHotKey(int aIdentifier) {
				if(aIdentifier == PAUSE){
					
				}	
				if(aIdentifier == REDOTIMER){
					if(isPaused == true){
						getTimerUpdated();
					}
				}
				if(aIdentifier == EXIT){
					JIntellitype.getInstance().cleanUp();
					System.out.println("Good-bye");
					System.exit(0);
				}
			}
			
		});
		
		JIntellitype.getInstance().addIntellitypeListener(new IntellitypeListener() {
			public void onIntellitype(int aCommand) {
		        switch (aCommand) {
		            case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
		                //System.out.println("Play/Pause message received " + Integer.toString(aCommand));
		                break;
		            case JIntellitype.MOD_WIN: //same as APPCOMMAND_VOLUME_MUTE?a
		            	//System.out.println("This is volume mute key for some reason on my keyboard");
		            	break;
		            default:
		                //System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
		                break;
		        }
			}
		});
		
		//JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, 'O');
		JIntellitype.getInstance().registerHotKey(PAUSE, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'P');	
		JIntellitype.getInstance().registerHotKey(REDOTIMER, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'O');	
		JIntellitype.getInstance().registerHotKey(EXIT, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'X');	
		
		System.out.println("Hotkeys registered");
		
		System.out.println("CTRL+ALT+P to start/pause the clicker");
		System.out.println("CTRL+ALT+O to redo the timer");
		System.out.println("CTRL+ALT+X to exit");
		
		getTimerUpdated();
		
		while(true){
			
		}
		
		
	}//end main method
	
	
	public static void getTimerUpdated(){
		System.out.println("Please enter the delay in ms (>0)");
		boolean shouldContinue = true;
		int a = -1;
		while(shouldContinue == true){
			if(scanner.hasNextInt() == true){
				shouldContinue = false;
				a = scanner.nextInt();
				if(a<=0){
					shouldContinue=true;
					System.out.println("Please enter the delay in ms (>0)");
				}
			}
			else{
				System.out.println("Please enter the delay in ms (>0)");
				shouldContinue = true;
				scanner.next();
			}
		}
		System.out.println("Thank you");
		msPauseTime=a;
	}

}//end class
