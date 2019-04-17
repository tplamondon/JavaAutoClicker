package autoclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
	
	public static boolean programPressed = false;
	
	public static final int PAUSE = 1;
	public static final int REDOTIMER = 2;
	public static final int EXIT = 3;
	public static final int SPACEPAUSE = 4;
	
	public static boolean isTyper = false;
	
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
		
		
		//robot.setAutoDelay(msPauseTime);
		
		

		
	}//end main method
	
	public static void setupHotkeys() {
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener(){
			public void onHotKey(int aIdentifier) {
				if(aIdentifier == PAUSE && programPressed==false){
					isPaused = invertBoolean(isPaused);
					System.out.println("Paused/Unpaused");
				}
				if(isTyper == false) {
					if(aIdentifier == SPACEPAUSE && allowSpace == true){
						isPaused = invertBoolean(isPaused);
					}
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
					if(isTyper==false) {
						JIntellitype.getInstance().unregisterHotKey(SPACEPAUSE);
					}
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
		if(isTyper == false) {
			JIntellitype.getInstance().registerHotKey(SPACEPAUSE, 0, ' ');	
		}
		JIntellitype.getInstance().registerHotKey(REDOTIMER, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'O');	
		JIntellitype.getInstance().registerHotKey(EXIT, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'X');	
		
		System.out.println("Hotkeys registered");
		System.out.println("CTRL+ALT+P or space to start/pause the program");
		System.out.println("CTRL+ALT+O to redo the timer");
		System.out.println("CTRL+ALT+X to exit");
	}
	
	public static void clicker() {
		getTimerUpdated();
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
	}
	
	public static void typer() {
		getTimerUpdated();
		setupHotkeys();
		
		System.out.println();
		System.out.println("Please enter the sentence to be auto-typed");
		String sentence = "";
		sentence = scanner.nextLine();
		System.out.println();
		System.out.println("Thank you");


		while(true){
			if(isPaused == false){
				
				//Code edit from: https://stackoverflow.com/questions/18498676/java-auto-typer-with-robot-class
				// and here: https://coderanch.com/t/544719/java/Robot-symbols
				for(char c : sentence.toCharArray()) {
					type(c);
				}
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				
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
	}
	
	//https://coderanch.com/t/544719/java/Robot-symbols
	//Method to get the character and convert it to the appropriate VK
	public static void type(char c) {
		switch (c) {
        case 'a': type(KeyEvent.VK_A); break;
        case 'b': type(KeyEvent.VK_B); break;
        case 'c': type(KeyEvent.VK_C); break;
        case 'd': type(KeyEvent.VK_D); break;
        case 'e': type(KeyEvent.VK_E); break;
        case 'f': type(KeyEvent.VK_F); break;
        case 'g': type(KeyEvent.VK_G); break;
        case 'h': type(KeyEvent.VK_H); break;
        case 'i': type(KeyEvent.VK_I); break;
        case 'j': type(KeyEvent.VK_J); break;
        case 'k': type(KeyEvent.VK_K); break;
        case 'l': type(KeyEvent.VK_L); break;
        case 'm': type(KeyEvent.VK_M); break;
        case 'n': type(KeyEvent.VK_N); break;
        case 'o': type(KeyEvent.VK_O); break;
        case 'p': programPressed=true; type(KeyEvent.VK_P); programPressed=false; break;
        case 'q': type(KeyEvent.VK_Q); break;
        case 'r': type(KeyEvent.VK_R); break;
        case 's': type(KeyEvent.VK_S); break;
        case 't': type(KeyEvent.VK_T); break;
        case 'u': type(KeyEvent.VK_U); break;
        case 'v': type(KeyEvent.VK_V); break;
        case 'w': type(KeyEvent.VK_W); break;
        case 'x': type(KeyEvent.VK_X); break;
        case 'y': type(KeyEvent.VK_Y); break;
        case 'z': type(KeyEvent.VK_Z); break;
        case 'A': type(KeyEvent.VK_SHIFT, KeyEvent.VK_A); break;
        case 'B': type(KeyEvent.VK_SHIFT, KeyEvent.VK_B); break;
        case 'C': type(KeyEvent.VK_SHIFT, KeyEvent.VK_C); break;
        case 'D': type(KeyEvent.VK_SHIFT, KeyEvent.VK_D); break;
        case 'E': type(KeyEvent.VK_SHIFT, KeyEvent.VK_E); break;
        case 'F': type(KeyEvent.VK_SHIFT, KeyEvent.VK_F); break;
        case 'G': type(KeyEvent.VK_SHIFT, KeyEvent.VK_G); break;
        case 'H': type(KeyEvent.VK_SHIFT, KeyEvent.VK_H); break;
        case 'I': type(KeyEvent.VK_SHIFT, KeyEvent.VK_I); break;
        case 'J': type(KeyEvent.VK_SHIFT, KeyEvent.VK_J); break;
        case 'K': type(KeyEvent.VK_SHIFT, KeyEvent.VK_K); break;
        case 'L': type(KeyEvent.VK_SHIFT, KeyEvent.VK_L); break;
        case 'M': type(KeyEvent.VK_SHIFT, KeyEvent.VK_M); break;
        case 'N': type(KeyEvent.VK_SHIFT, KeyEvent.VK_N); break;
        case 'O': type(KeyEvent.VK_SHIFT, KeyEvent.VK_O); break;
        case 'P': type(KeyEvent.VK_SHIFT, KeyEvent.VK_P); break;
        case 'Q': type(KeyEvent.VK_SHIFT, KeyEvent.VK_Q); break;
        case 'R': type(KeyEvent.VK_SHIFT, KeyEvent.VK_R); break;
        case 'S': type(KeyEvent.VK_SHIFT, KeyEvent.VK_S); break;
        case 'T': type(KeyEvent.VK_SHIFT, KeyEvent.VK_T); break;
        case 'U': type(KeyEvent.VK_SHIFT, KeyEvent.VK_U); break;
        case 'V': type(KeyEvent.VK_SHIFT, KeyEvent.VK_V); break;
        case 'W': type(KeyEvent.VK_SHIFT, KeyEvent.VK_W); break;
        case 'X': type(KeyEvent.VK_SHIFT, KeyEvent.VK_X); break;
        case 'Y': type(KeyEvent.VK_SHIFT, KeyEvent.VK_Y); break;
        case 'Z': type(KeyEvent.VK_SHIFT, KeyEvent.VK_Z); break;
        case '`': type(KeyEvent.VK_BACK_QUOTE); break;
        case '0': type(KeyEvent.VK_0); break;
        case '1': type(KeyEvent.VK_1); break;
        case '2': type(KeyEvent.VK_2); break;
        case '3': type(KeyEvent.VK_3); break;
        case '4': type(KeyEvent.VK_4); break;
        case '5': type(KeyEvent.VK_5); break;
        case '6': type(KeyEvent.VK_6); break;
        case '7': type(KeyEvent.VK_7); break;
        case '8': type(KeyEvent.VK_8); break;
        case '9': type(KeyEvent.VK_9); break;
        case '-': type(KeyEvent.VK_MINUS); break;
        case '=': type(KeyEvent.VK_EQUALS); break;
        //Shift + ` to =
        case '~': type(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE); break;
        case '!': type(KeyEvent.VK_SHIFT, KeyEvent.VK_1); break;
        case '@': type(KeyEvent.VK_SHIFT, KeyEvent.VK_2); break;
        case '#': type(KeyEvent.VK_SHIFT, KeyEvent.VK_3); break;
        case '$': type(KeyEvent.VK_SHIFT, KeyEvent.VK_4); break;
        case '%': type(KeyEvent.VK_SHIFT, KeyEvent.VK_5); break;
        case '^': type(KeyEvent.VK_SHIFT, KeyEvent.VK_6); break;
        case '&': type(KeyEvent.VK_SHIFT, KeyEvent.VK_7); break;
        case '*': type(KeyEvent.VK_SHIFT, KeyEvent.VK_8); break;
        case '(': type(KeyEvent.VK_SHIFT, KeyEvent.VK_9); break;
        case ')': type(KeyEvent.VK_SHIFT, KeyEvent.VK_0); break;
        case '_': type(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS); break;
        case '+': type(KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS); break;
        //Tabs + Enters
        case '\t': type(KeyEvent.VK_TAB); break;
        case '\n': type(KeyEvent.VK_ENTER); break;
        //Brackets, quotes, symbols, and periods/commas/others
        case '[': type(KeyEvent.VK_OPEN_BRACKET); break;
        case ']': type(KeyEvent.VK_CLOSE_BRACKET); break;
        case '\\': type(KeyEvent.VK_BACK_SLASH); break;
        case '{': type(KeyEvent.VK_SHIFT, KeyEvent. VK_OPEN_BRACKET); break;
        case '}': type(KeyEvent.VK_SHIFT, KeyEvent. VK_CLOSE_BRACKET); break;
        case '|': type(KeyEvent.VK_SHIFT, KeyEvent. VK_BACK_SLASH); break;
        case ';': type(KeyEvent.VK_SEMICOLON); break;
        case ':': type(KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON); break;
        case '\'': type(KeyEvent.VK_QUOTE); break;
        case '"': type(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE); break;
        case ',': type(KeyEvent.VK_COMMA); break;
        case '<': type(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA); break;
        case '.': type(KeyEvent.VK_PERIOD); break;
        case '>': type(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD); break;
        case '/': type(KeyEvent.VK_SLASH); break;
        case '?': type(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH); break;
        case ' ': type(KeyEvent.VK_SPACE); break;
        default:
                throw new IllegalArgumentException("Cannot type character " + c);
        }
	}
	//https://coderanch.com/t/544719/java/Robot-symbols
	//Typing characters that have their own key
	public static void type(int keyCode) {
		try{
	        robot.keyPress(keyCode);
	        robot.delay(1);
	        robot.keyRelease(keyCode);
	    }
	    catch(Exception e){
	        System.out.println("Invalid key code(s) for above character");
	    }
	}
	//https://coderanch.com/t/544719/java/Robot-symbols
	//Typing characters that require a shift as well
	private static void type(int keyCode1, int keyCode2){
	    try{
	        robot.keyPress(keyCode1);
	        robot.keyPress(keyCode2);
	        robot.keyRelease(keyCode2);
	        robot.keyRelease(keyCode1);
	    }
	    catch(Exception e){
	        System.out.println("Invalid key code(s) for above character");
	    }
	}
	
	public static boolean invertBoolean(boolean x){
		if(x==true){
			return false;
		}
		return true;
	}
	
	public static void getTimerUpdated(){
		int a = -1;
		
		while(a <= 0) {
			System.out.println("Please enter the delay in ms (>0)");
			if(!scanner.hasNextInt()) {
				scanner.nextLine();
				continue;
			}
			String temp = scanner.nextLine();
			try {
				a = Integer.parseInt(temp);
			}catch(NumberFormatException e) { 
				a=-1; 
			} catch(NullPointerException e) {
				a=-1;
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
			isTyper = false;
			//Go to the auto-clicker
			setupHotkeys();
			clicker();
		}
		else if(a.equalsIgnoreCase("t")) {
			// Go to the auto-typer
			isTyper = true;
			typer();
		}
		else {
			System.exit(0);
		}
		
	}

}//end class
