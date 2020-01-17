package uk.ac.cam.cwf22.mg.core;

import java.io.Serializable;

//The real transforms extend this class

public abstract class Transform implements Serializable
{
	//takes a (pointer to a) note and applies this transform to that note 
	public abstract void apply(Note n);
	
	//must return a String representation 
	//which is the same as that used in the genome language
	public abstract String toString();
	
	//returns a NEW transform which is a mutated version of me
	public abstract Transform mutate();
	
	/** SHALLOW cloning
	*/
	public Object clone() {    
		Object result = null;    
		try {      
			result = super.clone();		
		}
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException");    
		} 
		return result;
	}
}
