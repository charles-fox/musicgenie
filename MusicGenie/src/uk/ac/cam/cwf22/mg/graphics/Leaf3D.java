package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
//import uk.ac.cam.cwf22.mg.gui.*;

public class Leaf3D extends Object3D
{
	Point3D p;
	
	Color c;
	int size;
	
	public Leaf3D(Point3D p, Color c) {
		this.p = p;
		size = 10;
		this.c = c;
	}
	
	public Object2D projectOrthogonal(Display2D d) {
		
		Point2D q = (Point2D)p.projectOrthogonal(d);
		return new Note2D(q, c, 0, size);
	}
	
	public void rotateAboutY(double theta) {
		if (!p.getUpdate()) p.rotateAboutY(theta); p.setUpdate(true);
	}
	public void rotateAboutX(double theta) {
		if (!p.getUpdate()) p.rotateAboutX(theta); p.setUpdate(true);
	}
	
	public String toString() {
		String result = "Leaf3D at "+p.toString();
		return result;
	}
	
	public void setUpdate(boolean b) {updated=b; p.setUpdate(b);}
	public boolean getUpdate() {return updated;}
}
