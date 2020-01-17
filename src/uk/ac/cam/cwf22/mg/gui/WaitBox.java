package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;


public class WaitBox extends Frame
{
	TextArea t;
	
	public WaitBox(String title, String message) 
	{
		super(title);
		t = new TextArea(message,5,20,TextArea.SCROLLBARS_NONE);
		t.setBackground(SystemColor.menu);
		this.add(t);
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {dispose();}
								});
		
		
		Image fox = AboutBox.getImageFromFile("Icon.jpg");
		this.setIconImage(fox);
		
		this.setLocation(200,200);
		this.setSize(200,100);
	}
	
	public void setMessage(String message) {
		t.setText(message);
	}
	
	
		
}
