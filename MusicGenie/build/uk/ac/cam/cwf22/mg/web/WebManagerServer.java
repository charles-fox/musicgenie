package uk.ac.cam.cwf22.mg.web;

import java.rmi.Naming;
import uk.ac.cam.cwf22.mg.gui.*;

public class WebManagerServer {

  //CONSTRUCTOR
  public WebManagerServer(Manager m, String serviceName)
  {
    try 
    {
      	WebManager w = new WebManagerImpl(m);
      	Naming.rebind(serviceName, w);
	System.out.println("WebManager server now running...");
    } 
	catch (Exception e) {
      	System.out.println("Trouble: " + e);
    }
  }

}
