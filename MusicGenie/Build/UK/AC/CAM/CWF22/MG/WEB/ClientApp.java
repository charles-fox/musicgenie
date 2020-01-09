package uk.ac.cam.cwf22.mg.web;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.awt.*;

import uk.ac.cam.cwf22.mg.core.Score;


public class ClientApp
{
  public static void main(String[] args)
  {
	//Default IP
	String IP = "cwf22.clare.cam.ac.uk";

	if (args.length > 0) IP = args[0];

	String serviceName = "rmi://"+IP+":1099/uk-ac-cam-cwf22-MusicGenie";


        try {
		WebManager m = (WebManager)Naming.lookup(serviceName);

            	Score s = m.getScore(0);
	 	System.out.println("Connection to "+serviceName+" established");

		ClientFrame f = new ClientFrame(m);


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