package uk.ac.cam.cwf22.mg.core;

import uk.ac.cam.cwf22.mg.gui.Manager;
import java.util.*;
import java.io.Serializable;

//as used in the RHS of a gene
//consists of the phrase name and a vector of transforms

public class TransformedPhrase implements Cloneable, Serializable
{
	//the int version of the phrase name
	public int Id;
	
	//vector of transforms
	public Vector transforms;
	
	//CONSTRUCTOR
	public TransformedPhrase(int Id, Vector transforms) {
		this.Id = Id;
		this.transforms = transforms;
	}
	
	/** returns a new mutated version of this tPhrase
	 *  muatations may be:
	 *		- mutate a transform
	 *		- add a new transform
	 *		- delete an existing transform
	 * 
	 *  if there are no transforms, then add a new one always
	 * 
	 */
	
	public TransformedPhrase mutate(int parentGeneID) {
		
		if (transforms.size()>0) {
			double r = Math.random();
		
			if (r<0.1) return mutatePhrase(parentGeneID);
			else if (r<0.8) return mutateTransform();
			else if (r<0.9) return addRandomTransform();
			else return removeTransform();
		}
		else return addRandomTransform();
	}
	
	/** version for specifying which mutation
	 */
	public TransformedPhrase mutate(int parentGeneID, int mutType) {
		
		switch (mutType) {
		case 5: return addRandomTransform();
		case 6: return removeTransform();
		case 7: return mutateTransform();
		case 8: return mutatePhrase(parentGeneID);
			
		default: return this;
		}
		
	}
	
	/** mutate by mutating a random transform */
	private TransformedPhrase mutateTransform() {
		
		TransformedPhrase result = (TransformedPhrase)(this.clone());
		
		int indexOfMutation = Stats.getRandomInt(0,result.transforms.size());
		
		//get handle on old transform
		Transform old = (Transform)(result.transforms.elementAt(indexOfMutation));
		
		//set the transform at the index to a new mutated form
		result.transforms.setElementAt(old.mutate(), indexOfMutation);
		
		return result;
		
	}
	
	/** mutate by mutating the phrase number */
	private TransformedPhrase mutatePhrase(int parentGeneID) {
		
		TransformedPhrase result = (TransformedPhrase)(this.clone());
		
		int newID = Stats.getRandomInt(0, parentGeneID);
		
		result.Id = newID;
		return result;
		
	}
	
	
	/** mutate by removing a random transform */
	private TransformedPhrase removeTransform() {
		
		TransformedPhrase result = (TransformedPhrase)(this.clone());
		int indexOfMutation = Stats.getRandomInt(0,result.transforms.size());
		result.transforms.removeElementAt(indexOfMutation);
		
		return result;			
	}
	
	/** mutate by adding a random transform */
	private TransformedPhrase addRandomTransform() 
	{
		TransformedPhrase result = (TransformedPhrase)(this.clone());
		
		Transform t = makeRandomTransform();
		
		int index= Stats.getRandomInt(0,result.transforms.size());
		
		result.transforms.insertElementAt(t, index);
		
		if (Manager.VERBOSE) System.out.println("Added new transform "+t);
		
		return result;
	}
	
	/** creates a new totally random transform
	 */
	public Transform makeRandomTransform() {
		
		int c = Stats.getRandomInt(0,10);
		
		switch(c) {
		case 0:
			return new KeyRoll( Stats.getRandomInt(0, 8) );
			
		case 1:
			return new KeyShift( Stats.getRandomInt(0, 8),
								 0 );
			
		case 2:
			return new RelativeAmplitude( 
							Stats.getRandomRational(
								new Rational(1,2),
								new Rational(2,1)  )
									    );
		
		case 3:
			return new Retrograde();
			
		case 4:
			return new TimeStretch(Stats.getRandomRational(
								new Rational(1,2),
								new Rational(2,1)  )
									    );
			
		case 5:
			return new Transpose(Stats.getRandomInt(-8, 8),
								 0);
		
		case 6:
			return new Transpose(Stats.getRandomInt(-5, 5),
								 0);
			
		case 7:
			return new VoiceShift(Stats.getRandomInt(-8, 8) );
			
		case 9:
			return new Inversion();
			
		}
		//should never happen!
		return new VoiceSet(99);
	
	}
	
	/** uses Id numbers (easier to debug) */
	public String toString() {
		String result = "";
		
		for (int i=0; i<transforms.size(); i++) {
			result += ((Transform)(transforms.elementAt(i))).toString();
		}
		result += Id;
		return result;
	}
	
	/** uses string names */
	public String toString(String[] names) {
		String result = "";
		
		for (int i=0; i<transforms.size(); i++) {
			result += ((Transform)(transforms.elementAt(i))).toString();
		}
		result += names[Id];
		return result;
	}
	
	/** DEEP cloning.
	 */
	public Object clone() 
	{    
		Object result = null;    
		try {      
			result = super.clone();
			TransformedPhrase r = (TransformedPhrase)result;
			
			r.Id = this.Id;
			r.transforms = new Vector();
			
			for (int i=0; i<this.transforms.size(); i++) {
				Transform old = (Transform)(transforms.elementAt(i));
				Transform n = (Transform)(old.clone());
				r.transforms.addElement(n);
			}
		} 
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException");    
		}   
		return result;
	}
}
