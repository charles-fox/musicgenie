package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;
//Transpose transform.  Exists in Genome, and is copied into trees

public class Transpose extends Transform implements Serializable, Cloneable
{
	/** s.d.'s for mutations */
	static int DEGREE_MUTATE = 2;
	static int SHARP_MUTATE = 1;
	
	Degree degree;
	
	//constructor - created by compiler
	public Transpose(Degree degree) {
		this.degree = degree;
	}
	
	public Transpose(int degree, int sharp) {
		this.degree = new Degree(degree, sharp);
	}
	
	/**raise a note by my values, in the note's key */
	public void apply(Note n){
		//OLD CODE - for absolute transposition
		//no of semitones to raise the note's pitch by
		//int semitones = n.key.getNumberOfSemitones(degree, sharp);
		//n.pitch += semitones;
		//System.out.println("Applying transpose "+degree);
		
		n.degree += this.degree.degree;
		
		if (this.degree.degree > 0 ) 
			n.sharp += this.degree.sharp;
		else 
			n.sharp -= this.degree.sharp;
	}
	
	
	/** most likely returns a mutation on the degree - small chance of sharpening instead
	 */
	public Transform mutate() {
		
		if (Math.random() > 0.3)
			return new Transpose( Stats.getGaussian(degree.degree, DEGREE_MUTATE),
								  degree.sharp );
	
		else return new Transpose( degree.degree,
								   Stats.getGaussian(degree.sharp, SHARP_MUTATE) );
	}
	
	public String toString() {
		String r = "(^"+degree+")";
		return r;
	}
	
		/** SHALLOW cloning
	 */
	public Object clone() {    
		Object result = null;    
		
		result = super.clone();		
	
		
		return result;
	}

}
