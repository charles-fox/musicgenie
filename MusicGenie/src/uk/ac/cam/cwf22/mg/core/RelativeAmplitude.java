package uk.ac.cam.cwf22.mg.core;

import java.io.Serializable;

public class RelativeAmplitude extends Transform implements Serializable, Cloneable
{
	public Rational amplitudeFactor;

	public RelativeAmplitude(Rational r) {
		this.amplitudeFactor = r;
	}
	
	public RelativeAmplitude(int numerator, int denominator) {
		amplitudeFactor = new Rational(numerator, denominator);
	}	
	
	
	//multiples the notes duration by my stretch factor
	public void apply(Note n){
		n.amp.multiplyBy(amplitudeFactor);
		return;
	}
	
	public Transform mutate() {
		return this;
	}
	
	public String toString() {
		String result = "(@"+amplitudeFactor+")";
		return result;
	}
	
	/** SHALLOW cloning
	 */
	public Object clone() {    
		 
		Object result = super.clone();		
		
		return (RelativeAmplitude)result;
	}

}