package uk.ac.cam.cwf22.mg.core;
import java.io.Serializable;

public class TimeStretch extends Transform implements Serializable, Cloneable
{
	
	/** choices for numerator and denominator of mutation ratio	 */
	public static int [] NUM_MUTATE_RATS = {1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,3,5};
	public static int [] DEN_MUTATE_RATS = {1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,3,3,5};
	
	
	public Rational stretchFactor;

	public TimeStretch(Rational r) {
		this.stretchFactor = r;
	}
	
	public TimeStretch(int numerator, int denominator) {
		stretchFactor = new Rational(numerator, denominator);
	}	
	
	
	public static void main(String[] args) {
		TimeStretch t = new TimeStretch(2,3);
		for  (int i=0; i<50; i++) {
			System.out.println(t.mutate());
		}
	}
		
	
	//multiples the notes duration by my stretch factor
	public void apply(Note n) {
		n.duration.multiplyBy(stretchFactor);
		return;
	}
	
	/** return a *new* TimeStrech which is a mutation of me */
	public Transform mutate() {
		
		Rational newFactor = (Rational)(stretchFactor.clone());
		
		//select a random numerator and denominator from the weghted lists.
		int numChoice = Stats.getRandomInt(0, NUM_MUTATE_RATS.length);
		int denChoice = Stats.getRandomInt(0, DEN_MUTATE_RATS.length);
		
		/*
		System.out.println(NUM_MUTATE_RATS[numChoice] + "\t\t" +
											  DEN_MUTATE_RATS[denChoice]);
		
		*/
		
		
		//use the new rational as a factor to multiply the old value
		Rational mutateFactor = new Rational( NUM_MUTATE_RATS[numChoice],
											  DEN_MUTATE_RATS[denChoice]);
		newFactor.multiplyBy(mutateFactor);
				
		return new TimeStretch(newFactor);
	}
	
	public String toString() {
		String result = "(~"+stretchFactor+")";
		return result;
	}

		/** SHALLOW cloning
	 */
	public Object clone() {    
		
		Object result = super.clone();		
		
		return (TimeStretch)result;
	}

	
}
