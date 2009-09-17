package edu.stanford.hci.flowmap.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.WindowConstants;



/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class WindowUtilities {

	
	// constants for locations on the screen
	public static final int INVALID_MIN = -1;
	public static final int DESKTOP_CENTER    = 0;
	public static final int DESKTOP_WEST      = 1;
	public static final int DESKTOP_EAST      = 2;
	public static final int DESKTOP_NORTH     = 3;
	public static final int DESKTOP_SOUTH     = 4;
	public static final int DESKTOP_NORTHWEST = 5;
	public static final int DESKTOP_NORTHEAST = 6;
	public static final int DESKTOP_SOUTHWEST = 7;
	public static final int DESKTOP_SOUTHEAST = 8;
	public static final int SCREEN_CENTER    = 9;
	public static final int SCREEN_WEST      = 10;
	public static final int SCREEN_EAST      = 11;
	public static final int SCREEN_NORTH     = 12;
	public static final int SCREEN_SOUTH     = 13;
	public static final int SCREEN_NORTHWEST = 14;
	public static final int SCREEN_NORTHEAST = 15;
	public static final int SCREEN_SOUTHWEST = 16;
	public static final int SCREEN_SOUTHEAST = 17;
	public static final int INVALID_MAX = 18;

	
	// defaults for screen width and height are very small
	public static int screenWidth = 640;
	public static int screenHeight = 480;
	public static Rectangle desktopBounds = new Rectangle(0,0,640,480);
	
	
	/**
	 * Call this before calling getCachedWindowOrigin(...) to get the current
	 * state of the user's screen and desktop size. The user may have changed it
	 * since the last call.
	 */
	public static void initCachedWindowState() {
		// get the current Screen Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
	
		// get the current Desktop Size
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		desktopBounds = env.getMaximumWindowBounds();
	}
	/**
	 * Determines where to put windows based on the cached state 
	 * 
	 * @param windowWidth
	 * @param windowHeight
	 * @param where
	 * @see utils.WindowUtilities.initCachedWindowState()
	 */
	public static Point getCachedWindowOrigin(int windowWidth, int windowHeight, int where) {
		Point p = new Point(0, 0);
		if ((where >= INVALID_MAX) || (where <= INVALID_MIN)) {
			// invalid locations
			return p;
		}
		else if ((windowWidth < 0) || (windowHeight < 0)) {
			// invalid window sizes
			return p;
		}

		int x = 0;
		int y = 0;

		switch (where) {

		case DESKTOP_CENTER:
			x = desktopBounds.x + desktopBounds.width/2 - windowWidth/2;
			y = desktopBounds.y + desktopBounds.height/2 - windowHeight/2;
			break;
		case DESKTOP_NORTH:
			x = desktopBounds.x + desktopBounds.width/2 - windowWidth/2;
			y = desktopBounds.y;
			break;
		case DESKTOP_EAST:
			x = desktopBounds.x + desktopBounds.width - windowWidth;
			y = desktopBounds.y + desktopBounds.height/2 - windowHeight/2;
			break;
		case DESKTOP_WEST:
			x = desktopBounds.x;
			y = desktopBounds.y + desktopBounds.height/2 - windowHeight/2;
			break;
		case DESKTOP_SOUTH:
			x = desktopBounds.x + desktopBounds.width/2 - windowWidth/2;
			y = desktopBounds.y + desktopBounds.height - windowHeight;
			break;
		case DESKTOP_NORTHWEST:
			x = desktopBounds.x;
			y = desktopBounds.y;
			break;
		case DESKTOP_NORTHEAST:
			x = desktopBounds.x + desktopBounds.width - windowWidth;
			y = desktopBounds.y;
			break;
		case DESKTOP_SOUTHWEST:
			x = desktopBounds.x;
			y = desktopBounds.y + desktopBounds.height - windowHeight;
			break;
		case DESKTOP_SOUTHEAST:
			x = desktopBounds.x + desktopBounds.width - windowWidth;
			y = desktopBounds.y + desktopBounds.height - windowHeight;
			break;
		////////////////////////////////////////////////
		case SCREEN_CENTER:
			x = screenWidth/2 - windowWidth/2;
			y = screenHeight/2 - windowHeight/2;
			break;
		case SCREEN_NORTH:
			x = screenWidth/2 - windowWidth/2;
			y = 0;
			break;
		case SCREEN_EAST:
			x = screenWidth - windowWidth;
			y = screenHeight/2 - windowHeight/2;
			break;
		case SCREEN_WEST:
			x = 0;
			y = screenHeight/2 - windowHeight/2;
			break;
		case SCREEN_SOUTH:
			x = screenWidth/2 - windowWidth/2;
			y = screenHeight - windowHeight;
			break;
		case SCREEN_NORTHWEST:
			x = 0;
			y = 0;
			break;
		case SCREEN_NORTHEAST:
			x = screenWidth - windowWidth;
			y = 0;
			break;
		case SCREEN_SOUTHWEST:
			x = 0;
			y = screenHeight - windowHeight;
			break;
		case SCREEN_SOUTHEAST:
			x = screenWidth - windowWidth;
			y = screenHeight - windowHeight;
			break;
		default:
			x = 0;
			y = 0;
			break;
		}
		
		p.setLocation(x,y);
		return p;
	}
	
	// given a size (width, height) of a window, and an intended location on the
	// desktop (see above), it returns the x, y location of where the origin
	// should reside. This method will be pretty resilient to the user changing
	// his/her interface (taskbar locations, etc). If you want a faster method,
	// but would sacrifice a (little) bit of assurances... then use
	// getCachedWindowOrigin(...) directly (after calling
	// initCachedWindowState() once)
	public static Point getWindowOrigin(int windowWidth, int windowHeight, int where) {
		initCachedWindowState();
		return getCachedWindowOrigin(windowWidth, windowHeight, where);
	}
	
    public static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting native LAF: " + e);
		}
	}

    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager
                    .getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Java LAF: " + e);
        }
    }

    public static void setMotifLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
            System.out.println("Error setting Motif LAF: " + e);
        }
    }

    
    // A Set of methods to open a container in a Swing JFrame
    public static JFrame openInJFrame(Container content, int width, int height,
            String title, Color bgColor, boolean exitOnClose, JMenuBar menu) {
        JFrame frame = new JFrame(title);
        frame.setBackground(bgColor);
        content.setBackground(bgColor);
        frame.setSize(width, height);
        frame.setContentPane(content);
        if (menu != null)
        	frame.setJMenuBar(menu);
        if (exitOnClose) {
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        frame.setVisible(true);
        return (frame);
    }

    public static JFrame openInJFrame(Container content, int width, int height,
            String title, Color bgColor, JMenuBar menu) {
    	return (openInJFrame(content, width, height, title, bgColor, true, menu));
    }
    public static JFrame openInJFrame(Container content, int width, int height,
            String title) {
        return (openInJFrame(content, width, height, title, Color.white, null));
    }

    public static JFrame openInJFrame(Container content, int width, int height) {
        return openInJFrame(content, width, height, content.getClass()
                .getName(), Color.white, null);
    }
    
    public static JFrame openInSeparateJFrame(Container content, int width, int height) {
    	JFrame frame = new JFrame(content.getClass().getName());
        frame.setBackground(Color.white);
        content.setBackground(Color.white);
        frame.setSize(width, height);
        frame.setContentPane(content);
       
   
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
     
        frame.setVisible(true);
        return (frame);
    }
    
    public static JFrame openInJFrame(Container content, int width, int height, JMenuBar menu, String title) {
    	JFrame myFrame = openInJFrame(content, width, height, title, Color.white, menu);
    	return myFrame;
    }
   
	
    
    // Test Methods
    public static void main(String[] args) {
    	System.out.println("Where? " + WindowUtilities.getWindowOrigin(0,0,WindowUtilities.DESKTOP_NORTHWEST));
    }
}
