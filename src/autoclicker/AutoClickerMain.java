package autoclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;


public class AutoClickerMain {

	
	public static boolean isPaused = true;
	public static boolean allowSpace = true;
	public static int msPauseTime = -1;
	
	public static final int PAUSE = 1;
	public static final int REDOTIMER = 2;
	public static final int EXIT = 3;
	public static final int SPACEPAUSE = 4;
	
	public static Robot robot = null;
	public static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		// next check to make sure JIntellitype DLL can be found and we are on
		// a Windows operating System
		if (!JIntellitype.isJIntellitypeSupported()) {
			System.err.println("JIntellitype not supported");
			System.exit(1);
		}
		
		//add a robot
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.err.println("Problem setting up auto-program");
			System.exit(1);
		}

		System.out.println("Succesfully loaded library");
		

		//TODO: Start from here
		chooseAuto();
		
		getTimerUpdated();
		//robot.setAutoDelay(msPauseTime);
		
		
		
		
		while(true){
			if(isPaused == false){
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.delay(5);// keep this to prevent problems with clicking too fast
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				robot.delay(msPauseTime);
			}
			else{
				//Need this in otherwise it doesn't seem to be able to pause/unpause at this stage
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}//end main method
	
	public static void setupHotkeys() {
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			public void onHotKey(int aIdentifier) {
				if(aIdentifier == PAUSE){
					isPaused = invertBoolean(isPaused);
				}
				if(aIdentifier == SPACEPAUSE && allowSpace == true){
					isPaused = invertBoolean(isPaused);
				}
				if(aIdentifier == REDOTIMER){
					if(isPaused == true){
						getTimerUpdated();
						if(robot != null){
							//robot.setAutoDelay(msPauseTime);
						}
					}
				}
				if(aIdentifier == EXIT){
					JIntellitype.getInstance().unregisterHotKey(PAUSE);
					JIntellitype.getInstance().unregisterHotKey(REDOTIMER);
					JIntellitype.getInstance().unregisterHotKey(EXIT);
					JIntellitype.getInstance().cleanUp();
					try {
						File file = new File(System.getProperty("java.io.tmpdir")+"JIntellitype.dll");
						//System.out.println(file.toString());
						Files.deleteIfExists(file.toPath());
					} catch (IOException e) {
						//System.err.println("Temporary dll already removed");
					}
					System.out.println("Good-bye");
					System.exit(0);
				}
				return;
			}
		});
		
		JIntellitype.getInstance().addIntellitypeListener(new IntellitypeListener() {
			public void onIntellitype(int aCommand) {
		        switch (aCommand) {
		            case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
		            	//isPaused = invertBoolean(isPaused);
		            	if(isPaused == true){//only allow if we're paused
		            		allowSpace = invertBoolean(allowSpace);
		            		System.out.println("Adjusted if space pauses/unpauses autoclicker");
		            	}
		                break;
		            case JIntellitype.MOD_WIN: //same as APPCOMMAND_VOLUME_MUTE?a
		            	break;
		            default:
		                //System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
		                break;
		        }
			}
		});
		
		//JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, 'O');
		JIntellitype.getInstance().registerHotKey(PAUSE, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'P');	
		JIntellitype.getInstance().registerHotKey(SPACEPAUSE, 0, ' ');	
		JIntellitype.getInstance().registerHotKey(REDOTIMER, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'O');	
		JIntellitype.getInstance().registerHotKey(EXIT, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'X');	
		
		System.out.println("Hotkeys registered");
		System.out.println("CTRL+ALT+P or space to start/pause the program");
		System.out.println("CTRL+ALT+O to redo the timer");
		System.out.println("CTRL+ALT+X to exit");
	}
	
	public static boolean invertBoolean(boolean x){
		if(x==true){
			return false;
		}
		return true;
	}
	
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
	
	public static void chooseAuto() {
		String a = "";
			
		while( !(a.equalsIgnoreCase("c") || a.equalsIgnoreCase("t") || a.equalsIgnoreCase("exit")) ) {			
			System.out.println("Please enter 'c' for the auto-clicker, 't' for the auto-typer, or 'exit' to exit the program");
			a = scanner.nextLine();
		}
		
		if(a.equalsIgnoreCase("c")) {
			//Go to the auto-clicker
			setupHotkeys();
		}
		else if(a.equalsIgnoreCase("t")) {
			// Go to the auto-typer
			setupHotkeys();
		}
		else {
			System.exit(0);
		}
		
	}

}//end class
