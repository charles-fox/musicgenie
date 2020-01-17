package uk.ac.cam.cwf22.mg.core;

import java.io.*;

//an actual, sounding, Note

//the note is constructed when we walk over the tree.
//notes will be stored in a list ('score') and consulted for
// creating MIDI files and sounds, and piano-roll display

public class Note implements Serializable
{
	//NB all these values are acted on by external transforms, so they are public

	public Rational time; //absolute time when the note starts

	//public int pitch; // absolute pitch, from -infinty to +infinity.  0 is middle C

	public int degree,
				sharp;  //positions within my Key
						//actual pitch is only found at play-time

	public int voice;
	public Rational amp;  //amplitude, from 0 to 1
	public Rational duration;

	public Key key;

	/** returns the real pitch number of this note
	 */
	public int getPitch() {
		return key.getPitch(degree, sharp);
	}


	/** Constructor which sets default values
	* for everything except start time */
	public Note(Rational startTime) {
		time = (Rational)startTime.clone();

		degree = 0;
		sharp = 0;

		voice = 0;
		amp = new Rational(1,2);
		duration = new Rational(1,1);
		key = new Key(0); // C major key
	}

	/** Default constructor, as above but sets starttime to zero */
	public Note() {
		time = new Rational(0,1);

		degree=0;
		sharp =0;

		voice = 0;
		amp = new Rational(1,2);
		duration = new Rational(1,1);
		key = new Key(0); // C major key
	}



	public String toString() {
		String result = "d:"+degree+"s: "+sharp+"\t t:"+time.toPrettyString()+"\t d:"+duration.toPrettyString()+"\t a:"+amp.toPrettyString()+"\t k:"+key+"\n";
		return result;
	}



}
