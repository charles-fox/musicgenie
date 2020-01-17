/* The key is a pattern of intervals
// Major scale by default, but the pattern can be changed (eg eastern scales)
*/

package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;

public class Key implements Cloneable, Serializable
{
	// array of absolute semitone intervals for each degree
	public int [] scalePattern;
	
	//an int 0-11 to represent the letter of the key
	public int baseKey;
	
	
	//========CONSTRUCTORS===============
	
	// standard constructor, defaults to major scale 
	public Key(int baseKey) {
		this.baseKey = baseKey;
		int[] defaultScalePattern = {0, 2, 4, 5, 7, 9, 11};
		this.scalePattern = (int[])defaultScalePattern.clone();
	}
	
	// this one allows specification of the scale pattern
	//  public Key(int baseKey, int[] scalePattern) {
	//	this.baseKey = baseKey;
	//	this.scalePattern = scalePattern;
	//}

	//==========METHODS=============
	
	//change key so base is the old nth degree
	public void shift(int degree, int sharp) {
		baseKey += getNumberOfSemitones(degree, sharp);
	}
	
	//raise the nth degree by no of semitones
	//this is for advanced stuff like eastern scales
	//(not yet used by anything)
	public void sharpen(int degree, int sharp) {
		scalePattern[degree] += sharp;
	}
	

	
	/** rolls the mode, preserving basekey
	 * eg roll(5) shifts from C maj to C mixolydian
	 * NB degree is the interval-1 - so roll(6) to roll by a 5th
	 */
	public void rollMode(int degree) {
		
		degree = degree%scalePattern.length;
		
		// if zero, do nothing
		if (degree == 0) return;
		
		//roll to the left...
		if (degree>0) {
			
			int[] newScale = new int[scalePattern.length];
			int modDegree = degree%scalePattern.length;
		
			//fill in first half of scale, starting on new base note
			for (int i=degree; i<scalePattern.length; i++) {
				newScale[i-degree] = scalePattern[i];
			}
			//fill in second half, adding an octave to each note
			for (int i=0; i<degree; i++) {
				newScale[scalePattern.length-degree + i] = scalePattern[i] + 12;
			}
		
			//subtract a constant from each degree so the first element is 0
			int first = newScale[0];
			for (int i=0; i<newScale.length; i++) newScale[i] -= first;
			
			//finally, set the scalepattern to the new scale
			scalePattern = newScale;
			
		}
		//roll to the right...
		else {
			//this is the same as rolling left...
			int leftDegree = scalePattern.length + degree;
			//so call myself again
			rollMode(leftDegree);
		}
	}
	
	/** return absolute pitch of a given interval 
	 */
	public int getPitch(int degree, int sharp) {
		int r = baseKey + getNumberOfSemitones(degree, sharp);
		return r;
	}
	
	//takes a sharpened interval and returns no of semitones in it
	public int getNumberOfSemitones(int degree, int sharp) {
		
		boolean negative = (degree<0); //true if transposing downwards
		
		
		if (!negative) {
			//the result is the number of semitones to be moved (no direction yet)
		
			//find no of degrees within the scale pattern (eg. 9th=2nd)
			int modDegree = degree%(scalePattern.length);
		
			//now check for complete octaves (add 12 semis for each octave)
			int octaves = degree/(scalePattern.length);
		
			//and put it all together
			int result = scalePattern[modDegree] + 12*octaves + sharp;
			
			return result;
		}
		else {
			int octave = 0;
			
			//move up octaves until positive.
			//(well transpose back down the octaves later)
			while (degree<0) {
				degree += scalePattern.length;
				octave ++;
			}
			int result = scalePattern[degree] - 12*octave + sharp;
			return result;			
		}
		
		
	}
	
	/** returns pretty representation
	 */
	public String toString() {
		String r = "Key:"+baseKey+"\t( ";
		for (int i=0; i<scalePattern.length; i++) r+= scalePattern[i]+" ";
		r+=")";
		return r;
	}
		
}
