package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;

public class ColorWindow extends Dialog
{
	TextField[] rgbText = new TextField[3];
	Controls theControls;
	
	public ColorWindow(Controls c) {
		
		super(c.theGenomeWindow, "RGB", true);
		
		theControls = c;
		
		addWindowListener(new CloseListen(this));
		
		//setTitle("RGB");
		setLayout(new GridLayout(3,1));
		
		//make icon in corner of window
		//this.setIconImage(AboutBox.getImageFromFile("Icon.jpg"));
		
		rgbText[0] = new TextField(""+theControls.color.getRed());
		rgbText[1] = new TextField(""+theControls.color.getGreen());
		rgbText[2] = new TextField(""+theControls.color.getBlue());
		
		rgbText[0].setBackground(Color.red);
		rgbText[1].setBackground(Color.green);
		rgbText[2].setBackground(Color.blue);
		
		for (int i=0; i<3; i++) add(rgbText[i]);
		
		setSize(40,100);
		setLocation(200,200);
		setResizable(false);
		show();
	
		
	}	
	

	
	/** INNER CLASS for window closing
	 * */
	class CloseListen extends WindowAdapter {

		ColorWindow f;
		public CloseListen(ColorWindow f) {this.f=f;}
		
		public void windowClosing(WindowEvent e) 
		{		
			
			int r = new Integer(rgbText[0].getText()).intValue();
			int g = new Integer(rgbText[1].getText()).intValue();
			int b = new Integer(rgbText[2].getText()).intValue();
			
			Color c = new Color(r,g,b);
								
			theControls.color = c;
			theControls.updateColor(c);
								
			f.dispose();
		}
	}
}

