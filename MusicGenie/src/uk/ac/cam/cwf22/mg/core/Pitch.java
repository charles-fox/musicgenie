package uk.ac.cam.cwf22.mg.core;

/** Stores SCALE pitches
 * 
 * (ie. scale-degree / octave pairs
 * 
 * operates with both degrees and letter/octave system
 */

//no longer used?

public class Pitch implements Cloneable
{
	// all these are public - no need for get/set functions yet
	// (maybe later - to tidy up wraparound degrees?)
	public int degree;
	public int octave;
	public int sharp;
	
	//Standard constructor
	//For a scale degree, octave and no of sharps
	Pitch(int degree, int octave, int sharp) {
		this.degree = degree;
		this.octave = octave;
		this.sharp = sharp;
	}
	
}
	