package uk.ac.cam.cwf22.mg;

/**
 * TEST HARNESS
 */

import uk.ac.cam.cwf22.mg.core.*;

import uk.ac.cam.cwf22.mg.gui.*;
import uk.ac.cam.cwf22.mg.graphics.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;


public class Test_MusicGenie
{
	/**
	 * The main entry point for the TEST application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args) throws Exception
	{
		//create test wrapper
		Test t = new Test();
		
		Degree d = new Degree("5bb#b");
		p(""+d);
		
		//Properties p = System.getProperties();
		
		//p(""+p);
	
	}
		
		
	
	
	private static void testNaming() 
	{
		
		Color testc = new Color(300,400,500);
		
		String[] names = {"a", "a0" , "a1", "a2","a3"};
		String test = "a";
		String r = Genome.findNewUniqueName(test, names);
		p(r);
	
	}
	
	
	private static void test3D() {
		Frame f = new Frame();
		f.show();
		
		TreeGraphics tg = new TreeGraphics(new VirtualRectangle(0,0,10,10), Color.white);
		
		f.add(tg);
		
		Vector ob3D = new Vector();
		
		for (double x = 0; x< 10; x++) {
			for (double y=0; y<10; y++) {
				for (double z=0; z<10; z++) 
				{	
					ob3D.addElement(new Leaf3D(new Point3D(x,y,x), Color.red));
				}
			}
		}
		tg.drawTree(ob3D);
	}
	
	private static void testProperties() {
		String p = System.getProperty("user.dir");
		p(p);
	}
		
		
	//=======================
	
	/** wrapper to provide OK button at the end of th etest
	 */
	public Test() {
		Frame f = new Frame("Test complete");
		Button OK = new Button("Exit");	
		f.add(OK);
		OK.addActionListener(new OKListen(f));
		f.setSize(200,150);
		f.setLocation(20,220);
		f.show();
	}
	
	/** shorthand for system.out.println
	 */
	private static void p(String s) {
		System.out.println(s);
	}
	
	/** class to listen for OK */
	class OKListen implements ActionListener {
		Frame f;
		public OKListen(Frame f) {this.f=f;}
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			System.exit(7);
		}
	}
	
	
	private static void testKeyRoll() {
		Key k = new Key(5);
		p(k.toString());	
		k.rollMode(-26);
		p(k.toString());
	}
	
}
