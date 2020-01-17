package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;

public class Retrograde extends Transform implements Serializable, Cloneable
{

	public Retrograde() {;}

	


	public void apply(Note n) {;}
	
	public Transform mutate() {
		return this;
	}
	
	public String toString() {
		String result = "(R)";
		return result;
	}
		/** SHALLOW cloning
	 */
	public Object clone() {    
		    
		Object result = super.clone();		
		
		return (Retrograde)result;
	}

}