package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;

/** a rectangle in virtual space, with x,y position and width and height
 * (non-graphical)
 */

public class VirtualRectangle
{
	public double x,y,w,h;
	
	public VirtualRectangle(double x, double y, double w, double h) {
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
	}
	
	//translate me by dx,dy
	public void translate(double dx, double dy) {
		x+=dx;
		y+=dy;
	}
	
	public Point2D getBottomLeft() {
		Point2D result = new Point2D(x,y);
		return result;
	}
	
	public Point2D getTopRight() {
		Point2D result = new Point2D(x+w, y+h);
		return result;
	}
	
	public String toString() {
		String result = "VirtualRectangle: x:"+x+" y:"+y+" w:"+w+" h:"+h;
		return result;
	}
	
}
