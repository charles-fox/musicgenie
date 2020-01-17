package uk.ac.cam.cwf22.mg.core;

import java.io.Serializable;

public class KeyShift extends Transform implements Serializable, Cloneable
{
	
	/** s.d.'s for mutations */
	static int DEGREE_MUTATE = 2;
	static int SHARP_MUTATE = 1;
	
	Degree degree;
	
	//constructor - created by compiler
	public KeyShift(Degree degree) {
		this.degree = degree;
	}
	
	public KeyShift(int degree, int sharp) {
		this.degree = new Degree(degree, sharp);
	}
	
	/** sets the target note's voice channel by my shift amount */
	public void apply(Note n){
		n.key.shift(degree.degree, degree.sharp);
	}
	
	/** return a new mutated form */
	public Transform mutate() {
		if (Math.random() > 0.3)
				return new KeyShift( Stats.getGaussian(degree.degree, 
													   DEGREE_MUTATE),
									 degree.sharp );
	
			else return new KeyShift( degree.degree,
									  Stats.getGaussian(degree.sharp, 
									   				    SHARP_MUTATE) );
	}
	
	public String toString() {
		String result = "(&" + degree + ")";
		return result;
	}
/** SHALLOW cloning
	 */
	public Object clone() {    
		
		Object result = super.clone();		
	
		return (KeyShift)result;
	}
}
