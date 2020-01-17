package uk.ac.cam.cwf22.mg.core;

/** A transform which rolls the mode of a key around,
 *  keeping the same base pitch put altering the scale pattern
 */
import java.io.Serializable;

public class KeyRoll extends Transform implements Serializable, Cloneable
{
	/** s.d.'s for mutations */
	static int DEGREE_MUTATE = 2;
	
	int degree;
	
	//constructor - created by compiler
	public KeyRoll(int degree) {
		this.degree = degree;
	}
	
	
	/** sets the target note's voice channel by my shift amount */
	public void apply(Note n){
		n.key.rollMode(degree);
	}
	
	/** return a new mutated form */
	public Transform mutate() {
		
		return new KeyRoll( Stats.getGaussian(degree, DEGREE_MUTATE));
	}
	
	public String toString() {
		int screenDegree = (degree>=0)? degree+1 : degree-1;
		String result = "($" + screenDegree + ")";
		return result;
	}
	
	/** SHALLOW cloning
	 */
	public Object clone() {    
		      
		Object result = super.clone();	
			
		
		return result;
	}

}
