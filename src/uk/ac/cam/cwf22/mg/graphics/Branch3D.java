package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;


public class Branch3D extends Object3D
{
	Point3D start, end;
	
	Color c;  
	int width; //not yet used 
	
	public Branch3D(Point3D start, Point3D end, Color c) {
		this.start=start;
		this.end=end;
		this.c = c;
	}
	
	//projects to 2D Display
	public Object2D projectOrthogonal(Display2D d) {
		
		Point2D s = (Point2D)(start.projectOrthogonal(d));
		Point2D e = (Point2D)(end.projectOrthogonal(d));
		
		return new Line2D(s,e,c,1);
	}	
	
	public void rotateAboutY(double theta) {
		if (!start.getUpdate()) start.rotateAboutY(theta); start.setUpdate(true);
		if (!end.getUpdate())   end.rotateAboutY(theta); end.setUpdate(true);
		updated = true;
	}
	public void rotateAboutX(double theta) {
		if (!start.getUpdate()) start.rotateAboutX(theta); start.setUpdate(true);
		if (!end.getUpdate())   end.rotateAboutX(theta); end.setUpdate(true);
		updated = true;
	}
	
	
	public String toString() {
		String result = "Branch3D\n"+start.toString() + end.toString();
		return result;
	}
	
	public void setUpdate(boolean b) {
		updated = b;
		start.setUpdate(b);
		end.setUpdate(b);
	}
	
	public boolean getUpdate() {return updated;}
}
