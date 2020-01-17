package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;

public class Inversion extends Transform implements Serializable, Cloneable
{

	public Inversion() {;}

	


	public void apply(Note n) {;}
	
	public Transform mutate() {
		return this;
	}
	
	public String toString() {
		String result = "(I)";
		return result;
	}
		/** SHALLOW cloning
	 */
	public Object clone() {    
		    
		Object result = super.clone();		
		
		return (Inversion)result;
	}

}