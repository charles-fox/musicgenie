package uk.ac.cam.cwf22.mg.gui;

/** Simple handler to be attatched to buttons in the Control panel
 */

import java.awt.*;
import java.awt.event.*;

public class HandleButton implements ActionListener
{
	private Controls myOwner;
	private String buttonName;
	
	//CONSTRUCTOR
	HandleButton(Controls myOwner, String buttonName) {
		this.myOwner = myOwner;
		this.buttonName = buttonName;
	}
	
	public void actionPerformed(ActionEvent e) {
		// (c is not used - we just want the side effect)
		myOwner.compile();
	}
}
