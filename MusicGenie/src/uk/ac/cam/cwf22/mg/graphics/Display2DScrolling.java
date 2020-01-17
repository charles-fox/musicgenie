package uk.ac.cam.cwf22.mg.graphics;

//import uk.ac.cam.cwf22.mg.gui.*;
import java.awt.event.*;
import java.awt.*;

/** An interactive extension of the 2D Display panel - allows user to scroll
 * and zoom around with the mouse.
 */

public class Display2DScrolling extends Display2D
{
	//used by zoom (translate needs virtual mouse position)
	Point screenMouse;
	
	public double zoomRatio = 1.05; 
	
	//============================================
	// CONSTRUCTOR
	//============================================
	public Display2DScrolling(VirtualRectangle virtual, Color background) {
		
		super(virtual, background);
		
		//add a mouseListener thingie
		this.addMouseListener(new myMouseListener(this));
		this.addMouseMotionListener(new myMouseMotionListener(this));
	}
	
	
	//============================================
	// INNER CLASSES
	//============================================

	// inner classes to respond to mouse pressing actions
	class myMouseListener extends MouseAdapter {
		Display2DScrolling owner; //so we can call methods in the owner class
		
		public myMouseListener(Display2DScrolling owner) {this.owner=owner;}
		
		//sets the owner's mouse-last-down-position
		public void mousePressed(MouseEvent e) {
			owner.screenMouse = e.getPoint();
			Point2D virtualPoint = convertToVirtual(owner.screenMouse);	
			owner.mouse = virtualPoint;
		}
	}
	
	/** INNER CLASS for mouse motion actions:
	 **/
	class myMouseMotionListener extends MouseMotionAdapter {
		
		Display2D owner; //so we can call methods in the owner class
		
		public myMouseMotionListener(Display2D owner) {this.owner=owner;}
		
		public void mouseDragged(MouseEvent e) {
			
			//left button = scroll
			if (e.getModifiers() == 16) scroll(e, owner);
			//right button = zoom
			if (e.getModifiers() == 4) zoom(e, owner);
		}
	}
	
	/** scrolls the display window by the mouse movements
	 */
	private void scroll(MouseEvent e, Display2D owner) {
		Point screenPoint = e.getPoint();
		Point2D virtualPoint = convertToVirtual(screenPoint);
			
		//find x and y displacements
		double xMoved = (virtualPoint.x - mouse.x);
		double yMoved = (virtualPoint.y - mouse.y);
			
		//and translate the view window by them
		virtual.translate(-xMoved, -yMoved);
			
		//now set the new mouse position
		// IN THE TRANSLATED frame
		//(remove translate function for inertia scrolling)
		mouse = virtualPoint.translate(-xMoved, -yMoved);
			
		//and redraw the display
		owner.paint();
	}
	
	
	/** called by right button drag handler.  makes calls to display to zoom.
	 */
	private void zoom(MouseEvent e, Display2D owner) {
		Point newScreenMouse = e.getPoint();
		
		int yMoved = newScreenMouse.y - this.screenMouse.y;
		int xMoved = newScreenMouse.x - this.screenMouse.x;
		
		double xZoomFactor = Math.pow(zoomRatio , ((double)(-xMoved)) );
		double yZoomFactor = Math.pow(zoomRatio , ((double)(-yMoved)) );
				
		owner.zoomView(xZoomFactor, yZoomFactor);
		this.screenMouse = newScreenMouse;
		
		owner.paint();
	}
	
}
