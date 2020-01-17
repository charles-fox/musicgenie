package uk.ac.cam.cwf22.mg.graphics;

//import uk.ac.cam.cwf22.mg.gui.*;
import uk.ac.cam.cwf22.mg.core.*;

import java.awt.*;

/** The graphical representation of a note which is drawn onto the staff
 */

public class StaffNote2D implements Object2D
{
	//no of voices supported in MusicGenie
	public static int VOICES = 16;

	//NB all these values are acted on by external transforms, so they are public
	
	private Rational time; //absolute time when the note starts
	
	private int pitch; // absolute pitch, from -infinty to +infinity.  0 is middle C
	
	private int voice;
	private Rational amp;  //amplitude, from 0 to 1
	private Rational duration;
	private Color c;
	
	
	//to speed up drawing
	double startX;
	double endX;
	double startY;
	double endY;
		
	//(NB no key info)
	
	
	/** Constructor which sets default values
	* for everything except start time */
	public StaffNote2D(Note n) 
	{
		time = (Rational)n.time.clone();
		pitch = n.getPitch();
		voice = n.voice;
		amp = (Rational)n.amp.clone();
		duration = (Rational)n.duration.clone();
		c = getMyColor(voice);
		
		//adjust saturation to show amplitude
		float[] hsb = new float[3];
		Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(), hsb);
		hsb[1] = (float)(n.amp.getDouble());						//saturation=amplitude
		c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);	//update to new color
		
		//find double positions in virtual space
		startX = time.getDouble();
		endX = startX + duration.getDouble();
		
		//(lift down by 1/2 semitone space to draw on lines and spaces)
		startY = ((double)pitch) * Staff.SEMITONE + Staff.STARTLINE - 0.5f*Staff.SEMITONE;
		endY = startY + Staff.SEMITONE;
	}
	
	
	/** choose a color for my voice from the ordered array
	 * (Note - also used as class method by 3D Trees
	 */
	public static Color getMyColor(int voiceNumber) {

		int n = (voiceNumber>0)? voiceNumber%VOICES : -(voiceNumber%VOICES);
		
		float hue = ((float) n)/((float)VOICES);
		Color r = Color.getHSBColor(hue, 1f, 1f);
		return r;
	}
	
	/** draw the SCORE Note onto a 2D virtual coordinate display */
	public void draw(Display2D d) {
		d.drawRoundRect(new Point2D(startX, startY), new Point2D(endX, endY), c, true);
	}
}
