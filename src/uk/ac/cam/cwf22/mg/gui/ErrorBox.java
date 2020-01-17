package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;

public class ErrorBox extends Dialog
{
	public ErrorBox(String title, 
					String message, 
					Frame owner
					) 
	
	{
		
		super(owner, title, true);
		
		//emit beep!
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.beep();
		
		
		setLayout(new BorderLayout());
		
		setSize(300,150);
		setLocation(250,320);
		
		TextArea t = new TextArea(message, 10, 50, TextArea.SCROLLBARS_NONE);
		t.setEditable(false);
			
		add("Center", t);
			
		Button OK = new Button("OK");
		OK.addActionListener(new OKListen());
		add("South", OK);
		
		//to make the testbox same color as button
		//(so not horrible white in j2v1.3 !
		t.setBackground(SystemColor.menu);
		
		show();
	}
	
	
	//this is a special one for the help window
	public ErrorBox(String title, 
					String message, 
					Frame owner,
					boolean big
					) 
	
	{
		
		super(owner, title, true);
		
		//emit beep!
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		this.addWindowListener( new WindowAdapter() {
								   public void windowClosing(WindowEvent e) {
									dispose();
								   }
								}
							   );
		
		
		setLayout(new BorderLayout());
		
		setSize(400,550);
		setLocation(250,120);
		
		TextArea t = new TextArea(message, 10, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
		t.setEditable(false);
			
		add("Center", t);
			
		Button OK = new Button("OK");
		OK.addActionListener(new OKListen());
		add("South", OK);
		
		//to make the testbox same color as button
		//(so not horrible white in j2v1.3 !
		t.setBackground(SystemColor.menu);
		
		show();
	}
	
	
	
	
	/** class to listen for OK */
	class OKListen implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
}
