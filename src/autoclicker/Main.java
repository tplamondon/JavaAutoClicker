package autoclicker;

import java.awt.AWTException;
import java.awt.Robot;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;


public class Main {

	
	
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
	    
		System.out.println("Succesfully loaded library");
		

		
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			public void onHotKey(int aIdentifier) {
				if(aIdentifier == 1){
					System.out.println("WINDOWS+A hotkey pressed");
				}	
				if(aIdentifier == 2){
					System.out.println("CTRL+ALT+P hotkey pressed");
				}	
				System.out.println("WM_HOTKEY message received " + Integer.toString(aIdentifier));
			}
			
		});
		
		/*JIntellitype.getInstance().addIntellitypeListener(new IntellitypeListener() {
			public void onIntellitype(int aCommand) {
		        switch (aCommand) {
		            case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
		                System.out.println("Play/Pause message received " + Integer.toString(aCommand));
		                break;
		            case JIntellitype.MOD_WIN: //same as APPCOMMAND_VOLUME_MUTE?a
		            	System.out.println("This is volume mute key for some reason on my keyboard");
		            	break;
		            default:
		                System.out.println("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
		                break;
		        }
			}
		});*/
		
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN, 'A');
		JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'P');	
		
		System.out.println("Hotkeys registered");
		
		//add a robot
		try {
			Robot robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}//end main method

}//end class
