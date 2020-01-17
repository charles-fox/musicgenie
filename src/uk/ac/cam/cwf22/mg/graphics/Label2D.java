package uk.ac.cam.cwf22.mg.graphics;

import java.awt.*;
//import uk.ac.cam.cwf22.mg.gui.*;

public class Label2D implements Object2D
{
	String text;
	Point2D position;
	Color c;
	int size;
	
	public Label2D(String text, Point2D q, Color c, int size) {
		this.text = text;
		this.position = q;
		this.c = c;
		this.size = size;
	}
	
	public void draw(Display2D d) {
		d.drawString(text, position, c, size);	
	}
}
