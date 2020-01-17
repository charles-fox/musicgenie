package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
//import uk.ac.cam.cwf22.mg.gui.*;

public class Label3D extends Object3D
{
	Point3D p;
	
	String label;
	
	Color c;
	int size;
	
	public Label3D(Point3D p, String label) {
		this.p = p;
		this.label = label;
		size = 10;
		c = Color.white;
	}
	
	/** projects into 2D virtual space
	 */
	public Object2D projectOrthogonal(Display2D d) {
		
		Point2D q = (Point2D)p.projectOrthogonal(d);
		return new Label2D(label, q, c, size);	
	}
		
	public void rotateAboutY(double theta) {
		if (!p.getUpdate()) p.rotateAboutY(theta); p.setUpdate(true);
	}
	public void rotateAboutX(double theta) {
		if (!p.getUpdate()) p.rotateAboutX(theta); p.setUpdate(true);
	}
	
	
	public String toString() {
		String result = "Label3D "+label+" at "+p.toString();
		return result;
	}
	
	public void setUpdate(boolean b) {updated=b; p.setUpdate(b);}
	public boolean getUpdate() {return updated;}
	
}
