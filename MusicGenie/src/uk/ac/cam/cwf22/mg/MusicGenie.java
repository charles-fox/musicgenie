/**
 * Starting point for whole application.
 * Creates a genome window and place it on screen
 */

package uk.ac.cam.cwf22.mg;

import java.awt.*;


import uk.ac.cam.cwf22.mg.core.Genome;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;

public class MusicGenie
{
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args)
	{
		//create the manager
		Manager theManager = new Manager();
		
		//create the master application window
		ApplicationWindow theAppWindow = new ApplicationWindow("Music Genie", theManager);
		
		//give the manager a handle on the view
		theManager.setView(theAppWindow);
		
		theAppWindow.show();
		
		AboutBox ab = new AboutBox(theAppWindow, theManager);
		
	}
}
