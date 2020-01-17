package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
import java.util.*;

//import uk.ac.cam.cwf22.mg.graphics.*;

// a canvas with its own methods for drawing rectangles, lines etc
// using virtual coordinates
//
// x' = ax+b
// y' = cy+d
//
// where ' = screen coordinate

public class Display2D extends Canvas
{
	//most recent virtual position in which the mouse button was down
	public Point2D mouse;

	//Vector of Object2Ds (must have draw() methods)
	// protected so that different flavours of this display can use it
	protected Vector objects2D;

	//the section of virtual space we are looking at
	public VirtualRectangle virtual;
	
	//double-buffering stuff
	private Dimension DraftSize;      
	private Image Draft;      
	private Graphics DraftGraphics;
	
	private Color background;
	
	//============================================
	// CONSTRUCTOR
	//============================================
	public Display2D(VirtualRectangle virtual, Color background) {
		this.virtual = virtual;
		this.background = background;
		
		this.setBackground(background);
		objects2D = new Vector();
	}
	
	//to change the view
	public void setView(VirtualRectangle virtual) {
		this.virtual = virtual;
	}
	
	// to translate view
	public void translateView(double x, double y) {
		this.virtual.translate(x,y);
	}
	
	// tp zoom in and out
	public void zoomView(double xZoomFactor, double yZoomFactor) {
		
		double sx = (double)xZoomFactor;
		double sy = (double)yZoomFactor;
		
		
		Point2D bl = virtual.getBottomLeft();
		Point2D tr = virtual.getTopRight();
		
		//center of view window in virtual coordinates
		Point2D c = new Point2D( 0.5f*(tr.x+bl.x) , 0.5f*(tr.y+bl.y) );
		
		//set virtual view to new display, using zoom equations
		bl.x = c.x - sx*(c.x - bl.x);
		tr.x = c.x - sx*(c.x - tr.x);
		bl.y = c.y - sy*(c.y - bl.y);
		tr.y = c.y - sy*(c.y - tr.y);
		
		virtual.x = bl.x;
		virtual.y = bl.y;
		virtual.w = tr.x - bl.x;
		virtual.h = tr.y - bl.y;
	}
	
	
	
	//============================================
	// PAINT
	//============================================
	public void paint(Graphics g) {
		
		long startTime = System.currentTimeMillis();
				
		Dimension d = this.getSize();        
		
        // initialize the Draft image if first time or changes happened
        if((DraftGraphics == null) || 
		   (d.width != DraftSize.width) ||
		   (d.height != DraftSize.height) ) 
		{
			DraftSize = d;		
			Draft = createImage(d.width,d.height);
			DraftGraphics = Draft.getGraphics();	
		}	
		// erase back buffer
		DraftGraphics.setColor(background);
		DraftGraphics.fillRect(0,0,d.width,d.height);
		
		// draw each object - on the hidden buffer
		// (each object calls Display2D back, with methods like drawRect
		// which draw into the Draft graphics)
		for(int i=0; i<objects2D.size(); i++) {
			((Object2D)objects2D.elementAt(i)).draw(this);
		}
		
		//now display the draft graphics on the visible graphics display
		g.drawImage(Draft, 0, 0, this);
		
		System.out.println("PNT: "+(System.currentTimeMillis()-startTime));
	}
	
	/** version of paint that gets its own graphics
	 */
	public void paint() {
		Graphics g = this.getGraphics();
		this.paint(g);
	}
	
	//call this with your objects to have them drawn
	public void drawObjects(Vector objects2D) {
		this.objects2D = objects2D;
		Graphics g = this.getGraphics();
		paint(g);
	}
	
	//call this with your objects to have them drawn
	public void setObjects(Vector objects2D) {
		this.objects2D = objects2D;
	}
	
	
	/** drawRect
	// draws the rectangle defined by two corner points **/
	public void drawRect(Point2D bottomLeft, Point2D topRight, Color c, boolean fill) {
		/*
		//dont draw if out of window range
		if ( bottomLeft.x>virtual.x+virtual.w |
			 bottomLeft.y>virtual.y+virtual.h |
			 topRight.x<virtual.x |
			 topRight.y<virtual.y ) return;
		*/
		//convert Point2Ds to screen Points
		Point screenBL = convertToScreen(bottomLeft);
		Point screenTR = convertToScreen(topRight);
		
		int screenX = screenBL.x;
		int screenY = screenTR.y;
		int screenW = screenTR.x - screenBL.x;
		int screenH = screenBL.y - screenTR.y; //remember that the yaxis is upside down!
		
		Graphics d = Draft.getGraphics();	
		
		d.setColor(c);
			
		if (fill) d.fillRect(screenX, screenY, screenW, screenH);
		else d.drawRect(screenX, screenY, screenW, screenH);
	}
	
	
	/** drawRect
	 *  draws the rectangle defined by two corner points **/
	public void drawRoundRect(Point2D bottomLeft, Point2D topRight, Color c, boolean fill) {
		
		//dont draw if out of window range
		if ( bottomLeft.x>virtual.x+virtual.w |
			 bottomLeft.y>virtual.y+virtual.h |
			 topRight.x<virtual.x |
			 topRight.y<virtual.y ) return;
		
		
		//convert Point2Ds to screen Points
		Point screenBL = convertToScreen(bottomLeft);
		Point screenTR = convertToScreen(topRight);
		
		int arcW = 50, arcH = 50;
		
		int screenX = screenBL.x;
		int screenY = screenTR.y;
		int screenW = screenTR.x - screenBL.x;
		int screenH = screenBL.y - screenTR.y; //remember that the yaxis is upside down!
		
		Graphics d = Draft.getGraphics();	
		
		d.setColor(c);
			
		if (fill) d.fillRoundRect(screenX, screenY, screenW, screenH, arcW, arcH);
		else d.drawRoundRect(screenX, screenY, screenW, screenH, arcW, arcH);
	}
	
	/** draws a line on the display
	 */
	public void drawLine(Point2D start, Point2D finish, Color c, double width) {
		// ditto for drawLine()		
		Point screenS = convertToScreen(start);
		Point screenF = convertToScreen(finish);
		Graphics d = Draft.getGraphics();	
		d.setColor(c);
		
		//TODO: eventually use a polygon to represent width
		d.drawLine(screenS.x, screenS.y, screenF.x, screenF.y);
		
	}
	
	/** draws text on display
	 */
	public void drawString(String text, Point2D q, Color c, int size) {
		
		Point screen = convertToScreen(q);
		Graphics d = Draft.getGraphics();	
		d.setColor(c);
		//ignore size for now
		d.drawString(text, screen.x, screen.y);
		
	}
	
	/** draws circle on display
	 */
	public void drawCircle(Point2D p, Color c, int size, boolean fill) {
		
		Point screen = convertToScreen(p);
		Graphics d = Draft.getGraphics();	
		d.setColor(c);
		if (fill) d.fillOval(screen.x, screen.y, size, size);
		else d.drawOval(screen.x, screen.y, size, size);
	}
	
	/** converts distances - rather than points
	 */
	private int convertVirtualLengthToScreen(double f) {
		//TODO: virtual length conversion
		return 0;
	}
	
	/** returns a screen point for the virtual point
	 *  gets called by Object2Ds inside me as they draw themselves
	 */
	public Point convertToScreen(Point2D v) {
		
		//work out x comonent first
		int myW = this.getSize().width;
		double vXMin = virtual.x;
		double vW = virtual.w;
		int x = convertFrames(v.x, myW, vXMin, vW);
							  
		//ditto for y
		int myH = this.getSize().height;
		double vYMin = virtual.y;
		double vH = virtual.h;
		int y = convertFrames(v.y, myH, vYMin, vH);
		
		// Java coordinates are upside down, so reflect y:
		int reflectedY = myH-y;
		return new Point(x, reflectedY);
	}
	
	// returns a virtual coordinate 
	// to the given screen coordinate
	public Point2D convertToVirtual(Point screen) {
		// x component
		int myW = this.getSize().width;
		double vXMin = virtual.x;
		double vW = virtual.w;
		double virtualX = convertFramesToVirtual(screen.x, myW, vXMin, vW);
		//ditto for y - but reflect first
		int myH = this.getSize().height;
		double vYMin = virtual.y;
		double vH = virtual.h;
		int reflectedY = myH - screen.y;
		
		double virtualY = convertFramesToVirtual(reflectedY, myH, vYMin, vH);
		
		return new Point2D(virtualX, virtualY);
	}
		
	//using a given number of pixels to represent a 1D frame between the two virtual bounds,
	//returns the number of pixels along to represent the given virtual value in that frame
	private int convertFrames(double frameX, int pixelW, double frameMin, double frameW) {
		double p = (double)pixelW / frameW; //no of pixels per unit virtual space
		int pp = (int)(p*(frameX-frameMin));
		return pp;
	}
	
	//inverse of above
	private double convertFramesToVirtual(int pixelX, int pixelW, double frameMin, double frameW) {
		double p = frameMin  +  (double)pixelX * (frameW/((double)pixelW));
		return p;
	}


	
}
