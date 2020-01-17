package uk.ac.cam.cwf22.mg.graphics;

// A point in VIRTUAL 2D space
//
// (opposed to a Point in screen space, which uses ints)

public class Point2D implements Object2D
{
	public double x, y;
	
	public Point2D(double x, double y) {
		this.x=x;
		this.y=y;
	}
	
	public String toString() {
		String result = "Point2D x:"+x+" y: "+y;
		return result;
	}
	
	public void draw(Display2D d) {
		//nothing yet
	}
	
	//returns translated point (this one stays intact)
	public Point2D translate(double dx, double dy) {
		return new Point2D(x+dx, y+dy);
	}
}
