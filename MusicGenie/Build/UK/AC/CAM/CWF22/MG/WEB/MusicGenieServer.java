/**
 * Starting point for whole server-version application.
 * Creates a genome window and place it on screen
 * 
 * registers services with rmiregistry for public use
 */

package uk.ac.cam.cwf22.mg.web;

import java.awt.*;
import java.net.*;

import uk.ac.cam.cwf22.mg.core.Genome;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;
import uk.ac.cam.cwf22.mg.web.*;

public class MusicGenieServer
{
	/**
	 * The main entry point for the application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args) throws UnknownHostException
	{
		//create the manager
		Manager theManager = new Manager();
		
		//Find host IP and create string URL of service name
		InetAddress a = InetAddress.getLocalHost();
		String IP = a.getHostAddress();
		String serviceName = "rmi://"+IP+":1099/uk-ac-cam-cwf22-MusicGenie";
	
		System.out.println("Starting service "+serviceName);

		//create a musicgenie server
		WebManagerServer w = new WebManagerServer(theManager, serviceName);
		
		//create the master application window
		ApplicationWindow theAppWindow = new ApplicationWindow("Music Genie SERVER", theManager);
		
		//give the manager a handle on the view
		theManager.setView(theAppWindow);
			
		theAppWindow.show();
	}
}
