package uk.ac.cam.cwf22.mg.core;

import java.util.*;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;
import java.io.Serializable;

//A Gene is a single production from the genome
//It contains an Id, and a vector of transformed phrases

public class Gene implements Cloneable, Serializable
{
	//The identifiers are convered to unique ints by the compiler
	public int Id;
	
	// a vector of transformedPhrases
	public Vector RHS;
	
	//(meoldy=0, harmony=1)
	public int orientation;
	
	//true if leaf note (no gene content)
	public boolean leaf;
	
	public static int ADDTP =1, REMOVETP=2, FLIP=3, MUTATETP=4, ADDT=5, REMOVET=6, MUTATET=7, MUTATEP=8;
	
	//CONSTRUCTOR - takes
	//
	// LHS Id
	// Vector of transforedPhrases
	// orientation
	//
	public Gene(int Id, 
		 Vector RHS, 
		 int ori) 
	{
		this.Id = Id;
		this.RHS = RHS;
		this.orientation = ori;
		this.leaf = false;
	}
	
	//CONSTRUCTOR for LEAF NOTES
	//
	//takes Id only
	//
	public Gene(int Id) {
		this.Id = Id;
		this.RHS = new Vector(); //empty vector
		this.orientation = 0; //not really needed, but best to initialise?
		this.leaf = true;
	}
	
	//returns number of TransformedPhrases in the RHS of this gene
	public int getLength() {
		return RHS.size();
	}
	
	//return the ith tphrase in the RHS of the gene
	public TransformedPhrase getTransformedPhrase(int i) {
		return (TransformedPhrase)(RHS.elementAt(i));
	}
	
	/** returns a *new* mutated gene
	 *  from:
	 *	- mutate a tphrase
	 *  - add a random tphrase
	 *  - remove a tphrase
	 * 
	 */
	public Gene mutate() {
		
		double r = Math.random();
		
		//if this is an empty NOTE gene, always add a tphrase
		//(if gene 0, leave well alone!)
		if (leaf & Id > 1) {
			Gene result = addRandomTPhrase();
			result.leaf = false;
		}
		
		//if there are few tPhrases, never remove!
		if (RHS.size() < 2) 
		{
			if (r<0.8) return mutateTPhrase();
			else return addRandomTPhrase();
		}
		
		//otherwise, allow possibility of removal of flipping:
				
		if (r<0.1) return flipOrientation();
		else if (r<0.8) return mutateTPhrase();
		else if (r<0.9) return addRandomTPhrase();
		else return removeTPhrase();
	}
	
	/** takes one of the static vars to specify mutation type
	 */
	public Gene mutate(int i) {
		switch (i) {
			case 1: return addRandomTPhrase();
			case 2: return removeTPhrase();
			case 3: return flipOrientation();
			default: return mutateTPhrase(i);
		}
		
	}
	
	
	public Gene flipOrientation() 
	{
		Gene r = (Gene)(this.clone());
		if (r.orientation ==0 ) r.orientation =1;
		else r.orientation = 0;
		
		if (Manager.VERBOSE) System.out.println("Flip to" +r.orientation);
		return r;
	}
	
	/** choose a tphrase and mutate it
	 */
	public Gene mutateTPhrase() {
		
		//only do this if there are any tphrases to mutate!
		if (RHS.size() >0 )  {
		
			//the new result gene to be returned
			Gene r = (Gene)(this.clone());
		
			//choose a random TP to mutate
			int index= Stats.getRandomInt(0, r.RHS.size());
			
			if (Manager.VERBOSE) System.out.println("Mutating tPhrase "+index+" in gene "+this.toString());
							   
			TransformedPhrase tp = (TransformedPhrase)(r.RHS.elementAt(index));
		
			//remove old tp and replace with new one
			r.RHS.removeElementAt(index);
			r.RHS.insertElementAt(tp.mutate(Id), index);
		
			return r;
		}
		else return this;
	}		
	
	
	/** choose a tphrase and specifically mutates it
	 */
	public Gene mutateTPhrase(int i) {
		
		//only do this if there are any tphrases to mutate!
		if (RHS.size() >0 )  {
		
			//the new result gene to be returned
			Gene r = (Gene)(this.clone());
		
			//choose a random TP to mutate
			int index= Stats.getRandomInt(0, r.RHS.size());
			
			if (Manager.VERBOSE) System.out.println("Mutating tPhrase "+index+" in gene "+this.toString());
							   
			TransformedPhrase tp = (TransformedPhrase)(r.RHS.elementAt(index));
		
			//remove old tp and replace with new one
			r.RHS.removeElementAt(index);
			r.RHS.insertElementAt(tp.mutate(Id, i), index);
		
			return r;
		}
		else return this;
	}
	
	
	
	/** adds a new totally random tphrase (with no transforms)
	 */
	public Gene addRandomTPhrase() {
		
		Gene result = (Gene)(this.clone());
		
		int newId = Stats.getRandomInt(0, Id);
		
		TransformedPhrase newTP = new TransformedPhrase(newId, new Vector());
			
		result.RHS.addElement(newTP);
		
		result.leaf = false;
		
		return result;
	}
	
	/** removes a randomly chosen tphrase
	 */
	public Gene removeTPhrase() {
		
		Gene r = (Gene)(this.clone());
		int index= Stats.getRandomInt(0,r.RHS.size());
		
		if (Manager.VERBOSE) System.out.println("Removing tPhrase "+((TransformedPhrase)r.RHS.elementAt(index)));
				
		r.RHS.removeElementAt(index);
		
		return r;
	}
	
	
	//removes all refs to gene n (ie removes tphrases using it)
	// also but reduce all id>n to id-1
	public void removeRefs(int n)  
		{
		
		if (Manager.VERBOSE) System.out.println("Removing refs "+n+" from gene "+this);
		
		Vector newRHS = new Vector();
		
		//for each tphrase in original, copy it if not ref to removed gene
		for (int i=0; i<RHS.size(); i++) 
		{
			TransformedPhrase tp = (TransformedPhrase)(RHS.elementAt(i));	
			if (tp.Id != n)	newRHS.addElement(tp);
		}
		
		//now examine each tphrase.  if id>n, replace with id-1
		for (int i=0; i<newRHS.size(); i++) 
		{
			TransformedPhrase tp = (TransformedPhrase)(newRHS.elementAt(i));
			if (tp.Id > n) tp.Id -= 1;	
		}
		
		//if my own id is > n, reduce it also
		if (this.Id > n) this.Id -= 1;
		
		
		this.RHS = newRHS;
		
		if (Manager.VERBOSE) System.out.println("result: "+this);
	}
	
	/** returns int ID version of gene
	 */ 
	public String toString() {
		String result = getLHS() + " => " + getRHS();
		return result;
	}
	
	/** this version takes the name vector to fill in the 
	 * text names rather than just IDs
	 */
	public String toString(String[] names) {
		String result = getLHS(names) + " => " + getRHS(names);
		return result;
	}
	
	/** get the int ID of the LHS
	 */
	public String getLHS() {
		return ""+Id;
	}
	
	/** version to find string name
	 */
	public String getLHS(String[] names) {
		String result = names[Id];
		return result;
	}
	
	/** to get int ID version of RHS
	 */
	public String getRHS() {
		String result;
		
		if (leaf) {
			result = "NOTE";
		}
		
		else {	
			if (orientation==0) result ="{"; else result = "[";
			for (int i=0; i<RHS.size(); i++) {
				if (i>0) result += ", ";
				result += ((TransformedPhrase)(RHS.elementAt(i))).toString();
			}
			if (orientation==0) result+="}"; else result += "]";
		}
		result += "\n";
		return result;
	}
	
	/** and version for string names
	 */
	public String getRHS(String[] names) {
		String result;
		
		if (leaf) {
			result = "NOTE";
		}
		
		else {	
			if (orientation==0) result ="{"; else result = "[";
			for (int i=0; i<RHS.size(); i++) {
				if (i>0) result += ", ";
				result += ((TransformedPhrase)(RHS.elementAt(i))).toString(names);
			}
			if (orientation==0) result+="}"; else result += "]";
		}
		result += "\n";
		return result;
	}
	
	/** SHALLOW cloning of RHS.
	 */
	public Object clone() {    
		Object result = null;    
		try {      
			result = super.clone();
		 
			Gene r = (Gene)result;
			r.RHS = new Vector();
		
		
			for (int i=0; i<this.RHS.size(); i++) {
				TransformedPhrase old = (TransformedPhrase)(RHS.elementAt(i));
				TransformedPhrase n = (TransformedPhrase)(old.clone());
				r.RHS.addElement(n);
			}
		
			return r;
			
		}
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException");
			return null;
		} 
		  
		
	}
	
}


