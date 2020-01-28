/* The key is a pattern of intervals
// Major scale by default, but the pattern can be changed (eg eastern scales)
*/

package uk.ac.cam.cwf22.mg.core;

public class Key implements Cloneable
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
	//(not yet used byu anything)
	public void sharpen(int degree, int sharp) {
		scalePattern[degree] += sharp;
	}

	//just returns baseKey )not scale pattern yet
	public String toString() {
		String result = ""+baseKey;
		return result;
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

		//if downwards, flip right way up for now
		//(we'll flip back after the work is done)
		if (negative) degree = 0-degree;

		//the result is the number of semitones to be moved (no direction yet)

		//find no of degrees within the scale pattern (eg. 9th=2nd)
		int modDegree = degree%(scalePattern.length);

		//now check for complete octaves (add 12 semis for each octave)
		int octaves = degree/(scalePattern.length);

		//and put it all together
		int result = scalePattern[modDegree] + 12*octaves + sharp;

		//now add direction (ie. negative if downward transpose)
		if (negative) result = 0-result;

		return result;
	}

}
