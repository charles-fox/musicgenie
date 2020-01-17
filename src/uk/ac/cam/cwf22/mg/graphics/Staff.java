package uk.ac.cam.cwf22.mg.graphics;

//import uk.ac.cam.cwf22.mg.gui.*;
import java.awt.*;

public class Staff implements Object2D
{
	double length;
	
	//distances for 1 semitone of pitch space, space beneath staff, thickness of lines
	public static double SEMITONE=1f, STARTLINE=0f, LINETHICKNESS=0.1f;
	
	public Staff(double length) {
		this.length=length;
	}

	//draw the 7 lines for an equal-semitinal-distance staff
	// make the C lines fatter
	public void draw(Display2D d) {
		for(int i=-7; i<8; i++) {
			
			boolean CLine = (i%7==0);
			
			double thickness = (CLine)? LINETHICKNESS*2 : LINETHICKNESS;
			
			Point2D bottomLeft = new Point2D(0, STARTLINE+i*SEMITONE*2);
			//Point2D topRight = new Point2D(length, STARTLINE+i*SEMITONE*2 + thickness); //for rectangles
			Point2D topRight = new Point2D(length, STARTLINE+i*SEMITONE*2);
			
			
			//make middle C line different color
			Color c = (i==0)? Color.green : Color.black;
			
			//d.drawRect(bottomLeft, topRight, c, true);
			d.drawLine(bottomLeft, topRight, c, 1f);
		}
	}
}
