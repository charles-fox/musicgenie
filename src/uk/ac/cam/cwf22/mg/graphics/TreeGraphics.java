package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import uk.ac.cam.cwf22.mg.core.*;

// the display window for 3D Objects
//
// call drawTree(Vector objects3D) to display those objects

public class TreeGraphics extends Display2DScrolling
{
	//A big vector of all the stuff to be drawn
	public Vector objects3D;
	
	
	private Point lastMouse;
	
	//CONSTRUCTOR
	public TreeGraphics(VirtualRectangle r, Color background) {
		super(r, background);
		objects3D = new Vector();
		this.setBackground(Color.white);
		
		this.addKeyListener(new RotateListen());
		this.addMouseMotionListener(new my3DMotionListener()); 
		this.addMouseListener(new my3DMouseListener());
	}
	
	private class RotateListen extends KeyAdapter {
		public void keyPressed(KeyEvent k) 
		{
			int key = k.getKeyCode();
			
			System.out.println("key "+key+"pressed");
			
			switch (key) 
			{
			case 37: rotateAboutY(0.05); break;
			//case 38: rotateAboutX(-0.08); break;
			case 39: rotateAboutY(-0.05); break;
			//case 40: rotateAboutX(0.08); break;
			}
		}
	}
	
	public void setObjects(Vector objects3D) {
		this.objects3D = objects3D;
		
		//clear the virtual space of any old 2D objects
		this.objects2D = new Vector();
		
		//for each object, get its 2D representation and put it in the Objects2D vector
		//then call paint (from Display2D) to have them drawn
		for (int i=0; i<objects3D.size(); i++) 
		{
			Object3D thisObject3D = (Object3D)objects3D.elementAt(i);
			//for now, use the orthogonal projection (upgrade to funky perspective later)
			Object2D thisObject2D = thisObject3D.projectOrthogonal(this);
			this.objects2D.addElement(thisObject2D);
		}
	}

	// call this with your vector of 3d bits to draw them
	public void drawTree(Vector objects3D) 
	{
		setObjects(objects3D);

		//call paint in Display2D to draw the 2D Projection
		paint();
	}
	
	//* rptate all objects by theta abotu y axis
	public void rotateAboutY(double theta) 
	{
		
		for (int i=0; i<objects3D.size(); i++) {
			Object3D o = (Object3D)(objects3D.elementAt(i));
			o.setUpdate(false);
		}
		
		for (int i=0; i<objects3D.size(); i++) 
		{
			Object3D o = (Object3D)(objects3D.elementAt(i));
			
			if (!o.getUpdate()); o.rotateAboutY(theta);
		}
		
		drawTree(objects3D);
	}
	
	
	
	//* rotate all objects by theta abotu y axis
	public void rotateAboutX(double theta) 
	{
		for (int i=0; i<objects3D.size(); i++) {
			Object3D o = (Object3D)(objects3D.elementAt(i));
			o.setUpdate(false);
		}
		
		for (int i=0; i<objects3D.size(); i++) 
		{
			Object3D o = (Object3D)(objects3D.elementAt(i));
			if (!o.getUpdate()); o.rotateAboutX(theta);
		}
		drawTree(objects3D);
	}

	/** INNER CLASS for mouse motion actions: **/
	class my3DMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {	
			
			//both buttons button = rotate
			if (e.getModifiers() == 20) rotate(e);
		}
	}
	
	// pressing actions - set lastMouse
	class my3DMouseListener extends MouseAdapter {
		//sets the owner's mouse-last-down-position
		public void mousePressed(MouseEvent e) {
			lastMouse = e.getPoint();
		}
	}
	
	
	/** scrolls the display window by the mouse movements
	 */
	private void rotate(MouseEvent e) 
	{
		Point newMouse = e.getPoint();
		
		int dx = newMouse.x - lastMouse.x;
		int dy = newMouse.y - lastMouse.y;
		
		double theta = ((double)dx)/100;
		
		rotateAboutY(theta);
					 
		lastMouse = newMouse;
	}
	
	
}
