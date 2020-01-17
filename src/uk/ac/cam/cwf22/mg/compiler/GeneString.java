package uk.ac.cam.cwf22.mg.compiler;

import uk.ac.cam.cwf22.mg.core.*;
import java.util.*;

public class GeneString
{
	public String LHS;
	public String RHS;
	
	//----------------------------------------------------------
	//CONSTRUCTOR
	//----------------------------------------------------------
	public GeneString(String LHS, String RHS) {
		this.LHS=LHS;
		this.RHS=RHS;
	}
	
	
	
	//----------------------------------------------------------
	// Makes a Gene based on the String information
	//----------------------------------------------------------
	public Gene makeGene(String[] names) throws GeneSyntaxException {
		
		try {
		
			//these are the arguements to make the gene
			int Id = 0;
			Vector tPhrases = new Vector();
			int ori = 0;
		
			//find the int Id corresponding to the LHS string
			Id = indexOf(LHS, names);
		
			//check for leaf note
			//
			//if leaf, return a new leaf gene
			if (RHS.compareTo("NOTE")==0) {
				Gene leafGene = new Gene(Id);
				return leafGene;
			}
		
			//the following only runs if its not a leaf gene...
		
			//make new copy of RHS to work on
			String workRHS = RHS;
		
			//find orientation
			ori = findOrientation(workRHS);
		
			//strip brackets from ends of RHS
			//and split into comma-separated chunks
			workRHS = workRHS.substring(1,workRHS.length()-1);
			Vector tPhraseStrings = split(workRHS, ",");  
		
			//for each chunk, make a tPhrase
			for (int i=0; i<tPhraseStrings.size(); i++) {
				
				String currentString = (String)tPhraseStrings.elementAt(i);
				
				TransformedPhrase currentTPhrase = makeTPhrase(currentString, names);
				tPhrases.addElement(currentTPhrase);
			}
		
			//Check for recursive definitions
			//and throw exception if the gene uses itself or lower-numbered genes!
		
			//for each tPhrase
			for (int i=0; i<tPhrases.size(); i++) {
				//is its Id <= my own Id?
				//if so, throw the exception
				TransformedPhrase currentTPhrase = (TransformedPhrase)tPhrases.elementAt(i);
				if (currentTPhrase.Id >= Id) {
					throw new GeneSyntaxException("Recursive definition in gene "+LHS);
				}
			}
		
			return new Gene(Id, tPhrases, ori);
		}
		
		catch (GeneSyntaxException e) 
		{
			String old = e.userReport;
			throw new GeneSyntaxException("Error in gene "+LHS+":\n\t"+old);							  
		}
		
	}
	
	//----------------------------------------------------------
	//returns the index of the string in the array
	//or exception if its not there
	//----------------------------------------------------------
	private static int indexOf(String s, String[] a) throws ArrayIndexOutOfBoundsException
	{
		int i=0;
		
		for (i=0; i<a.length; i++) {
			boolean found = (s.compareTo(a[i]) == 0);
			if (found) {
				return i;
			}
		}
		System.err.println("looking for "+s);
		throw new ArrayIndexOutOfBoundsException("Search string "+s+" not found in array "+a.toString());
	}
	
	//----------------------------------------------------------
	//returns orientation
	//----------------------------------------------------------
	private int findOrientation(String RHS) throws BadOrientationException
	{		
		// if [harmony]
		if (RHS.startsWith("[") && RHS.endsWith("]")) {
			return 1;
		}
		//if {melody}
		else if (RHS.startsWith("{") && RHS.endsWith("}")) {
			return 0;
		}
		else throw new BadOrientationException("Definition of "+LHS+" must be in {melody} or [harmony] brackets");
	}
	
	//----------------------------------------------------------
	//takes a TPhrase String and makes the TPhrase
	// eg. input:  "(^3)(~1/2)VERSE"
	//----------------------------------------------------------
	private TransformedPhrase makeTPhrase(String tPhraseString, String[] names) throws BadTPhraseStringException {
		
		String s = tPhraseString; //local copy to play with
		String name;
		int tpId = 0;
		Vector transforms = new Vector();
		
		if (s.length() < 1) throw new BadTPhraseStringException("Name must be at least 1 char long");
		
		//find the name at the right, and strip it:
		
		//position is length-1 (since start at zero)
		int position = s.length()-1;
		
		//start at the right, move left until a close bracket is found
		//or we run out of chars
		
		while( s.charAt(position) != ')' && position >0 ) {
			position--;
		}
		
		//position is now the position of the rightmost ')' if there are brackets
		//or is 0 if no brackets
		
		//if there are brackets...
		if (position>0) {
			//now we know the split position, do the split
			name = s.substring(position+1, s.length()); //name
			s = s.substring(0, position+1);				//chop name off original
		}
			
		//if no brackets...
		else {
			name = s.toString();
			s = "";
		}
			
		// name now holds the name
		// s now holds the transform list.
			
		//find int id for this name using the genome namelist
		//throw exception if a name is used which has not been defined in the genome
		try {
			tpId = indexOf(name, names);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		throw new BadTPhraseStringException("Gene "+name+" is used but not defined");
		}
			
		//======================
		// No transforms case
		//======================
		
		//check for zero transforms case
		if (s.length() == 0) {
			//return a tphrase with no transforms
			return new TransformedPhrase(tpId, transforms);
		}
		
		//======================
		// Transforms exist case
		//======================
		
		//check brackets at ends
		if (!(s.startsWith("(") && s.endsWith(")"))) {
			throw new BadTPhraseStringException("Bad brackets in transform list: "+s);
		}
		
		//strip end brackets
		s = s.substring(1,s.length()-1);
		
		//now split on ')(' to give transform list
		Vector transformStrings = split(s, ")(");
	
		//make the transforms from the TransformString
		for (int i=0; i<transformStrings.size(); i++) {
			Transform currentTransform = makeTransform((String)transformStrings.elementAt(i));
			transforms.addElement(currentTransform);
		}
		
		return new TransformedPhrase(tpId, transforms);	
	}
	
	

	
	//----------------------------------------------------------
	//Takes the string for a transform (not inclucing brackets)
	//and returns its transform object
	//----------------------------------------------------------
	private Transform makeTransform(String transformString) throws BadTransformStringException {
		
		Transform transform;
		
		String s = transformString; //local copy
		
		//----------------------
		//used to look up cases 
		//ADD NEW TRANSFORM SYMBOLS AND CASES HERE!
		//----------------------
		String[] symbols = {"~", "^", "@", "v", "V", "R", "&", "$", "I"}; 
		
		//remove first char from string and test it for transform type
		char firstChar = s.charAt(0);
		char[] a = {firstChar}; //to create string from char
		
		String type = new String(a);
		s = s.substring(1, s.length());
		
		//lookup case no in array
		int typeNo;
		
		try {typeNo = indexOf(type, symbols);}
		catch (Exception e) {throw new BadTransformStringException("type symbol not listed");}
		
		switch (typeNo) {
		case 0:// ~
			transform = makeTimeStretch(s);
			break;
		case 1:// ^
			transform = makeTranspose(s);
			break;
		case 2: // @
			transform = makeRelativeAmplitude(s);
			break;
		case 3: // v
			transform = makeVoiceShift(s);
			break;
		case 4: // V
			transform = makeVoiceSet(s);
			break;
		case 5: // R
			transform = makeRetrograde(s);
			break;
		case 6: // &
			transform = makeKeyShift(s);
			break;
		case 7: // $
			transform = makeKeyRoll(s);
			break;
		case 8: // I
			transform = makeInversion(s);
			break;

		default: 
			throw new BadTransformStringException("Transform "+type+" not recognised");
		}
		
		return transform;
	}
	
	
	//----------------------------------------------------------------------
	//    MAKE TRANSFORM METHODS FOLLOW -- ADD NEW CODE HERE
	//
	//    These methods are called with a string
	//	  - the brackets, whitespace AND transform symbol have already been stripped
	//
	//----------------------------------------------------------------------
	
	
	//makes Transpose from String
	//two cases - either 'n' degree or 'n#m' degrees and m sharps
	public Transpose makeTranspose(String s) throws BadTransformStringException {	
		int degree=0, sharp=0;
		
		//split on '#'
		//Vector values = split(s, "#");
		
		try {
			Degree d = new Degree(s);
			return new Transpose(d.degree, d.sharp);
		}
		catch (Exception e) {
			throw new BadTransformStringException("Bad Transpose string:"+s);
		}

	}
	
	//makes Timestretch from String
	//two cases - either 'n' or 'n/n'
	//for int and rational timeStretchs respectively
	public TimeStretch makeTimeStretch(String s) throws BadTransformStringException					
	{		
		
		int numerator, denominator;
		
		//split on '/'
		Vector ratValues = split(s, "/");
		
		try {
			Integer i,j;
		
			switch(ratValues.size()) {
			case 1:
				//integer timestretch
				i = new Integer((String)ratValues.elementAt(0));
				numerator = i.intValue();
				denominator = 1;	
				break;
			case 2:
				// rational timeStretch
				i = new Integer((String)ratValues.elementAt(0));
				j = new Integer((String)ratValues.elementAt(1));
				numerator = i.intValue();
				denominator = j.intValue();	
				break;
			default:
				throw new BadTransformStringException("Bad TimeStretch string");
			}
		
			Rational r = new Rational(numerator, denominator);
		
			return new TimeStretch(r);
		}
		catch (NumberFormatException e) {throw new BadTransformStringException("Bad TimeStretch string");}
	}
	
	//makes Timestretch from String
	//two cases - either 'n' or 'n/n'
	//for int and rational timeStretchs respectively
	public RelativeAmplitude makeRelativeAmplitude(String s) throws BadTransformStringException					
	{		
		
		int numerator, denominator;
		
		//split on '/'
		Vector ratValues = split(s, "/");
		
		try {
			Integer i,j;
		
			switch(ratValues.size()) {
			case 1:
				//integer timestretch
				i = new Integer((String)ratValues.elementAt(0));
				numerator = i.intValue();
				denominator = 1;	
				break;
			case 2:
				// rational timeStretch
				i = new Integer((String)ratValues.elementAt(0));
				j = new Integer((String)ratValues.elementAt(1));
				numerator = i.intValue();
				denominator = j.intValue();	
				break;
			default:
				throw new BadTransformStringException("Bad RelativeAmplitude string");
			}
		
			Rational r = new Rational(numerator, denominator);
		
			return new RelativeAmplitude(r);
		}
		catch (NumberFormatException e) {throw new BadTransformStringException("Bad RelativeAmplitude string");}
	}
		
	
	/** makes voiceShift transform
	 */
	public VoiceShift makeVoiceShift(String s) throws BadTransformStringException					
	{		
		try {	
			Integer voice = new Integer(s);
			return new VoiceShift(voice.intValue());
		}
		catch (NumberFormatException e) {throw new BadTransformStringException("Bad voiceShift string");}
	}
	
	
	/** makes voiceSet transform
	 */
	public VoiceSet makeVoiceSet(String s) throws BadTransformStringException					
	{		
		try {	
			Integer voice = new Integer(s);
			return new VoiceSet(voice.intValue());
		}
		catch (NumberFormatException e) {throw new BadTransformStringException("Bad voiceSet string");}
	}
	
	/** makes retrograde transform
	 */
	public Retrograde makeRetrograde(String s) throws BadTransformStringException					
	{		
		// retrograde takes no arguemtents - its just an R in brackets
		// so throw an error if there's anything in the string once the R was stripped
		if (s.length()==0) return new Retrograde();
		
		else throw new BadTransformStringException("Bad Retrograde string - Syntax: (R)");
	}
	
	/** makes inversion transform*/
	public Inversion makeInversion(String s) throws BadTransformStringException					
	{		
		// retrograde takes no arguemtents - its just an R in brackets
		// so throw an error if there's anything in the string once the R was stripped
		if (s.length()==0) return new Inversion();
		
		else throw new BadTransformStringException("Bad Inversion string - Syntax: (I)");
	}
	
	
	//makes KeyShift from String
	public KeyShift makeKeyShift(String s) throws BadTransformStringException 
	{	
		try {
			Degree d = new Degree(s);
			return new KeyShift(d.degree, d.sharp);
		}
		catch (Exception e) {
			throw new BadTransformStringException("Bad Keyshift string:"+s);	
		}
	}
	
	public KeyRoll makeKeyRoll(String s) throws BadTransformStringException
	{
		try {
			int d = (new Integer(s)).intValue();
			if (d>0) return new KeyRoll(d-1);
			else if (d<0) return new KeyRoll(d+1);
			else throw new BadTransformStringException(" $0: There is no such interval as a zeroth.\n(Do you mean a first, $1 ?)");
		}
		catch (BadTransformStringException e) {
			throw new BadTransformStringException("Bad mode-shift string:"+s+"\n"+e.userReport);	
		}
		
	}
	
	
	
	
	//----------------------------------------------------------
	//Perl-like function
	//returns a vector of strings formed by splitting s on char c
	//----------------------------------------------------------
	
	//TODO: there seems to be a bug here when splitting on '->' ?
	
	public static Vector split(String s, String c)
	{
		Vector result = new Vector();
		StringTokenizer st = new StringTokenizer(s, c);
		int noOfTokens = st.countTokens();
		
		for (int i=0; i<noOfTokens; i++) 
		{
			String currentToken = st.nextToken();
			result.addElement(currentToken);
		}
		return result;
	}
	
	
}
