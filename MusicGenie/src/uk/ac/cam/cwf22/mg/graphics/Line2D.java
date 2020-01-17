package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
//import uk.ac.cam.cwf22.mg.gui.*;

/** a line in 2D space, for use with Display2D
 */
public class Line2D implements Object2D
{
	Point2D start;
	Point2D finish;
	Color c;
	double width;
	
	public Line2D(Point2D s,Point2D e,Color c, int width){
		this.start =s;
		this.finish = e;
		this.c = c;
		this.width = width;
	}
	
	public void draw(Display2D d) {
		d.drawLine(start, finish, c, width);
	}
}
