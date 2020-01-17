package uk.ac.cam.cwf22.mg.core;

import uk.ac.cam.cwf22.mg.compiler.*;
import java.util.*;
import java.io.Serializable;

public class Degree implements Serializable
{
	public int degree;
	public int sharp;
	
	public Degree(int degree, int sharp) {
		this.degree=degree;
		this.sharp=sharp;
	}
	
	
	/** create a degree from a string, eg. 5#2
	 *  eventually allow for pretty syntax here
	 *  NOTE: the syntax is for intervals, the internals are for degrees
	 *        so the test version of '4#1' is "5#1'
	 */
	public Degree(String s) throws BadDegreeStringException 
	{
		this.sharp = 0;
		
		Integer i,j;
		
		//split string on '#'
		//Vector values = GeneString.split(s, "#");	
		//i = new Integer((String)values.elementAt(0));
		
		//remove each # and b from right, and keep track of sharpness
		int p = s.length();
		
		try 
		{
			while (p-- >0 && (s.charAt(p)=='#' || s.charAt(p)=='b') ) {
				if (s.charAt(p)=='#') sharp++;
				if (s.charAt(p)=='b') sharp--;
			}	
			//the substring with the degree number
			String dString = s.substring(0,p+1);
			//NB: the number of degrees moved is one minus the musical interval
			// eg. a major 2nd = 'up one interval'
			i = new Integer(dString);
		}
			
		catch (Exception e) {
			throw new BadDegreeStringException("Bad degree: "+s);
		}	
			
			
	
		if (i.intValue() > 0) degree = i.intValue() - 1;
		if (i.intValue() < 0) degree = i.intValue() + 1;
		if (i.intValue() ==0) throw 
					new BadDegreeStringException("Bad degree: "+s+"\n\n (no such thing as a zeroth degree - do you mean a first?)");
	
	
	}
	
	/** return string eg. "5#2"
	 *  NB - the interval number is the degree plus one
	 */
	public String toString() 
	{
		//convert from 'degrees shifted' to 'interval added'
		int interval = (degree>0)? degree+1 : degree-1;
		
		String result = "" +interval;
		
		//if (sharp!=0) result += "#"+sharp;
		if (sharp>0) for (int i=0;i<sharp;i++) result= result+"#";
		if (sharp<0) for (int i=0;i<-sharp;i++) result= result+"b";
		
		
		return result;
	}
}
