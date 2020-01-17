package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class AboutBox extends Dialog
{
	Image fox;
	
	Manager theManager;
	
	String infoText; 
	
	static int ABOUTH=190, ABOUTW = 300;
	
	public AboutBox(Frame owner, Manager theManager) {
		
		super(owner, "Music Genie", true);
		
		this.theManager = theManager;
		
		this.setBackground(SystemColor.menu);
		
		infoText = "  Music Genie\n (v."+theManager.versionInfo+")\n\n  Charles Fox, 1999-2000\n\n  A Computer Science Project\n  for Cambridge University\n\n  www.i.am/charlesfox";
		
		//add a listener for the 'close' icon, to quit the application
		//(using anonymous inner class!)
		this.addWindowListener( new WindowAdapter() {
								   public void windowClosing(WindowEvent e) {
									dispose();
								   }
								}
							   );
		
		
		Panel p = new Panel(new BorderLayout());
		this.add(p);
		
		//set size
		this.setSize(ABOUTW,ABOUTH);
		
		//set location...
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		setLocation( (dim.width-ABOUTW)/2 , (dim.height-ABOUTH)/2);
		
		
		
		String imageName = "Charles.jpg";
		fox = getImageFromFile(imageName);
		FoxCanvas c = new FoxCanvas();
		p.add("Center", c);
		
		Panel textOK = new Panel(new BorderLayout());
		
		TextArea info = new TextArea(infoText, 20,22,TextArea.SCROLLBARS_NONE);
		info.setBackground(SystemColor.menu);
		info.setEditable(false);
		textOK.add("Center", info);
		p.add("East", textOK);
		this.setResizable(false);
		
		Button OK = new Button("OK");
		OK.addActionListener(new OKListen());
		textOK.add("South", OK);
		
		this.show();
	
		
	}	
	
	public static Image getImageFromFile(String imageName) 
	{	
		Toolkit tk = Toolkit.getDefaultToolkit();
		String dir = System.getProperty("user.dir");
		String sep = System.getProperty("file.separator");
		
		String fileName = dir+"\\uk\\ac\\cam\\cwf22\\mg\\gui\\"+imageName;
		
		Image image = tk.getImage(fileName);
		
		return image;
	}
	
	
	
	class FoxCanvas extends Canvas {
		public void paint(Graphics g) {		
			g.setColor(Color.red);
			g.drawImage(fox,0,0,this);
		}
	}
	
	/** INNER CLASS for window closing
	 * */
	class CloseListen extends WindowAdapter {
		Frame f;
		public CloseListen(Frame f) {this.f=f;}
		
		public void windowClosing(WindowEvent e) {		
			f.dispose();
		}
	}
	
	/** class to listen for OK */
	class OKListen implements ActionListener {
		
		public void actionPerformed(ActionEvent e) 
		{
			dispose();
		}
	}
	
	
}
