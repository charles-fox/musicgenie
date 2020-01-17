package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;
/** Sets the voice of the note
 */
public class VoiceSet extends Transform implements Serializable, Cloneable
{
	
	/** sd for mutation	 */
	static int VOICE_MUT = 2;
	
	int voice;
	
	//constructor - created by compiler
	public VoiceSet(int voice) {
		this.voice=voice;
	}
	
	/** sets the target note's voice channel by my shift amount
	 */
	public void apply(Note n){
		//System.out.println("Applying VoiceSet from "+n.voice+" to "+voice);
		n.voice = this.voice;
	}
	
	public Transform mutate() {
		int newVoiceSet = Stats.getGaussian(voice, VOICE_MUT);
		
		return new VoiceSet(newVoiceSet);
	}
	
	public String toString() {
		String result = "(V"+voice+")";
		return result;
	}
	
		/** SHALLOW cloning
	 */
	public Object clone() {    
		   
		Object result = super.clone();		
		
		return result;
	}

}
