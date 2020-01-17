package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;

//a point in 3D space

public class Point3D extends Object3D implements Cloneable
{
	public double x,y,z;
	
	//scale factors
	static int sx = 26, sy=26, sxz=16, syz=16;
	
	public Point3D(double x,
				   double y,
				   double z) 
	{	this.x=x; 
		this.y=y; 
		this.z=z;
		updated = true;
	}
	
	public void rotateAboutY(double theta) 
	{		
		double r = Math.sqrt(  Math.pow(x,2) + Math.pow(z,2)  );
		double phi = Math.atan2(z,x);
		
		this.x = r*Math.cos(phi+theta);
		this.z = r*Math.sin(phi+theta);		
	}
	
	public void rotateAboutX(double theta) 
	{		
		double r = Math.sqrt(  Math.pow(y,2) + Math.pow(z,2)  );
		double phi = Math.atan2(z,y);
		
		this.y = r*Math.cos(phi+theta);
		this.z = r*Math.sin(phi+theta);		
	}
	
	// simple 3D for now :
	// x' = sx.s + syx.z
	// y' = sy.s - syz.z
	// where the s's are scale factors
	public Object2D projectOrthogonal(Display2D d) {
		double newX = (x*sx + z*sxz);
		double newY = (y*sy - z*syz);
		return new Point2D(newX, newY);
		//return new Point((int)z*sx,(int)y*sy); //to test z branches
	}
	
	public Object clone() {
		Point3D result = new Point3D(x,y,z);
		return result;
	}
	
	public String toString() {
		String result = "(x:"+x+" y:"+y+" z:"+z+")\n";
		return result;
	}
	
	public void setUpdate(boolean b) {updated=b;}
	public boolean getUpdate() {return updated;}
}
