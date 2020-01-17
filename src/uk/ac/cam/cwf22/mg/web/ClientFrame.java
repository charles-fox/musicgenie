package uk.ac.cam.cwf22.mg.web;


import java.awt.*;
import java.awt.event.*;

public class ClientFrame extends Frame 
{
	public ClientFrame(WebManager m) {
		super("Music Genie client");
		setSize(450,130);
		setLocation(40,220);
		setLayout(new BorderLayout());
		setResizable(false);
	
		this.add(new ClientPanel(m));		

		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				}
			     }
			);

		show();
	}


}