package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;
/** Shifts the voice of the note
 */
public class VoiceShift extends Transform implements Serializable, Cloneable
{
	static int VOICESHIFT_MUT = 2;
	static int NO_OF_VOICES = 16;
	
	int shift;
	
	//constructor - created by compiler
	public VoiceShift(int shift) {
		this.shift=shift;
	}
	
	/** shifts the target note's voice channel by my shift amount
	 */
	public void apply(Note n){
		//System.out.println("Applying VoiceShift from "+n.voice+" by "+shift);
		n.voice += shift;
		if (n.voice<0) n.voice = NO_OF_VOICES + n.voice;
	}
	
	public Transform mutate() {
		int newShift = Stats.getGaussian(shift, VOICESHIFT_MUT);
		return new VoiceShift(newShift);
	}
	
	public String toString() {
		String result = "(v"+shift+")";
		return result;
	}
	
	/** SHALLOW cloning
	 */
	public Object clone() {    
	 
		Object result = super.clone();		
		
		return result;
	}

}
