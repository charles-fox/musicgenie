package uk.ac.cam.cwf22.mg;

/**
 * TEST HARNESS
 */

import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.*;
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
		testProperties();
		testDegree();
		testNaming();
		testKeyRoll();
		//testAwt();
		//test3D();
		Test_MusicGenie t = new Test_MusicGenie();
	}
	
	private static void testProperties() {
		Properties p = System.getProperties();
		p(""+p);
		String s = System.getProperty("user.dir");
		p(s);
	}
		
	private static void testDegree() {
		try {
			Degree d = new Degree("5bb#b");
			p(""+d);
		}
		catch (BadDegreeStringException e) {
			p("degree failed");
		}
	}

	private static void testNaming() 
	{
		Color testc = new Color(100,100,100);
		String[] names = {"a", "a0" , "a1", "a2","a3"};
		String test = "a";
		String r = Genome.findNewUniqueName(test, names);
		p(r);
	}
		
	private static void testKeyRoll() {
		Key k = new Key(5);
		p(k.toString());	
		k.rollMode(-26);
		p(k.toString());
	}

	public static void testAwt() {
		Frame f = new Frame("Test awt function");
		Button OK = new Button("Exit");	
		f.add(OK);  //TODO this is failing on 2020 openjdk9, why?
		f.setSize(200,150);
		f.setLocation(20,220);
		f.show();
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
	
	

	/** wrapper to provide OK button at the end of the test
	 */
	public Test_MusicGenie() {
		Frame f = new Frame("Test awt class");
		Button OK = new Button("Exit");	
		f.add(OK);  //TODO this is failing on 2020 openjdk9, why?
		OK.addActionListener(new OKListen(f));
		f.setSize(200,150);
		f.setLocation(20,220);
		f.show();
	}

	class OKListen implements ActionListener {
		Frame f;
		public OKListen(Frame f) {this.f=f;}
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			System.exit(7);
		}
	}




	//=======================
	
	/** shorthand for system.out.println
	 */
	private static void p(String s) {
		System.out.println(s);
	}
}
