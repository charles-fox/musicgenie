package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;

public class Note2D implements Object2D
{
	Point2D p;
	
	Color c;
	double length; //not yet used
	int size;
	
	
	public Note2D(Point2D p, Color c, double length, int size) {
		this.p=p;
		this.c=c;
		this.length=length;
		this.size=size;
	}
	
	public void draw(Display2D d) {
		d.drawCircle(p,c, size, true);
	}
}
