package base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class MultiSCKUtil {
	private static Robot robot = null;

	public static void pressKeys(String keys) {
		if (keys == null || "".equals(keys)) {
			return;
		}
		boolean ctrlFlg = keys.toLowerCase().contains("ctrl");
		boolean shiftFlg = keys.toLowerCase().contains("shift");
		boolean altFlg = keys.toLowerCase().contains("alt");
		
		try {
			robot = new Robot();
			// Ctrl + i
			if (ctrlFlg && !shiftFlg && !altFlg){
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_CONTROL);
			// shift + i
			} else if (shiftFlg && !ctrlFlg && !altFlg){
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_SHIFT);
			// alt + i
			} else if (altFlg && !ctrlFlg && !shiftFlg){
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_ALT);
			// Ctrl + Shift + i
			}else if (ctrlFlg && shiftFlg && !altFlg){
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			// Ctrl + Alt + i
			} else if (ctrlFlg && altFlg && !shiftFlg){
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			// Shift + Alt + i
			} else if (shiftFlg && altFlg && !ctrlFlg){
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_SHIFT);
			// ctrl + shift + alt + i
			} else if (shiftFlg && ctrlFlg && altFlg){
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_ALT);
				robot.keyPress((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease((int)keys.split("[+]")[keys.split("[+]").length - 1].toUpperCase().charAt(0));
				robot.keyRelease(KeyEvent.VK_ALT);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			} else {
				return;
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}		
	}

	public static void longPressCtrl(String key){
	    
	    if ("".equals(key) || key == null)
	        return;
	    
	    boolean ctrlFlg = key.toLowerCase().contains("ctrl");
        boolean shiftFlg = key.toLowerCase().contains("shift");
        boolean altFlg = key.toLowerCase().contains("alt");

        try {
	        Robot t = new Robot();
	        t.delay(2000);
	        if (ctrlFlg){	            
	            t.keyPress(KeyEvent.VK_CONTROL);
	        } else if (shiftFlg) {
	            t.keyPress(KeyEvent.VK_SHIFT);
	        } else if (altFlg) {
	            t.keyPress(KeyEvent.VK_ALT);
	        }
	    } catch (AWTException e) {
	        e.printStackTrace();
	    }
	}

	public static void releaseCtrl(String key){

	    if ("".equals(key) || key == null)
            return;
        
        boolean ctrlFlg = key.toLowerCase().contains("ctrl");
        boolean shiftFlg = key.toLowerCase().contains("shift");
        boolean altFlg = key.toLowerCase().contains("alt");

	    try {
	        Robot t = new Robot();
	        t.delay(1000);
	        if (ctrlFlg) {
	            t.keyRelease(KeyEvent.VK_CONTROL);	            
	        } else if (shiftFlg) {
	            t.keyRelease(KeyEvent.VK_SHIFT);
	        } else if (altFlg) {
	            t.keyRelease(KeyEvent.VK_ALT);
	        }
	    } catch (AWTException e) {
	        e.printStackTrace();
	    }
	}
}
