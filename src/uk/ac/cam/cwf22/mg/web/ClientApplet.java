
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.awt.*;
import java.applet.*;

import uk.ac.cam.cwf22.mg.core.Score;
import uk.ac.cam.cwf22.mg.web.*;


public class ClientApplet extends Applet
{    
  public void init ()
  {
	String serviceName = getParameter("serverURL");
	
        try {            
		WebManager m = (WebManager)Naming.lookup(serviceName);

            	Score s = m.getScore(0);
	 	System.out.println("Connection to "+serviceName+" established");
		
		this.add(new ClientPanel(m));		
    
	}
        catch (MalformedURLException murle) {            System.out.println();
            System.out.println(              "MalformedURLException");
            System.out.println(murle);        }
        catch (RemoteException re) {            System.out.println();
            System.out.println(                        "RemoteException");
            System.out.println(re);        }
        catch (NotBoundException nbe) {            System.out.println();
            System.out.println(                       "NotBoundException");
            System.out.println(nbe);        }        
}}