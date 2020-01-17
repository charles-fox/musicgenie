package uk.ac.cam.cwf22.mg.compiler;

import uk.ac.cam.cwf22.mg.core.*;

import java.util.*;

public class GenomeString
{
	//public, so copyMutate can access it
	public Vector geneStrings;
	
	public String[] names;
	
	//CONSTRUCTOR
	//takes a vector of gene strings
	public GenomeString(Vector v) {
		this.geneStrings = (Vector)v.clone();
		
		//make the table of names
		makeNameList();	
		
	}
	
	//CONSTRUCTOR
	//added 21/12/1999 
	//to allow building from single entire genome string
	//(design decision: single textbox for genome)
	public GenomeString(String s) throws GenomeSyntaxException {

		s = removeSpaces(s, ' ');
		
		geneStrings = new Vector();
		
		//add gene zero, which is always "NOTE=>NOTE" (invisible on screen)
		geneStrings.addElement(new GeneString("NOTE","NOTE"));
		
		// we must extract each line into a string vector
		// (using class method already def'd in GeneString)
		Vector textLines = GeneString.split(s, "\n");
		
		for (int i=0; i<textLines.size(); i++) {
				
			//split on '->'
			Vector split = GeneString.split((String)textLines.elementAt(i), "=>");
				
			//check for LHS and RHS
			if (split.size() != 2) throw new GenomeSyntaxException("Line "+(i+1)+" is badly formed\n(Syntax: LHS=>RHS)\n");
					
			String LHS = (String)split.elementAt(0);
			String RHS = (String)split.elementAt(1);
			
			GeneString g = new GeneString(LHS,RHS);
			geneStrings.addElement(g);
		}	
		//make the table of names
		makeNameList();	
		
	}
	
	//-----------------
	// purpose: creates a real genome object using the string information
	//-----------------
	public Genome makeGenome() throws GenomeSyntaxException {
		
		Vector genes = new Vector();
		
		// for each gene
		// make it and add it to a gene vector
		
		GeneString currentGeneString;
		Gene currentGene;
		
		for(int i=0; i<geneStrings.size(); i++) 
		{
			currentGeneString = (GeneString)geneStrings.elementAt(i);
			currentGene = currentGeneString.makeGene(names);
			genes.addElement(currentGene);
		}
		
		// then create the genome
		return new Genome(genes, names);
	}
	
	
	//-----------------
	//the ith entry gives the ith name
	//-----------------
	private void makeNameList() {
		
	names = new String[geneStrings.size()];
	
		for (int i=0; i<names.length; i++) {
			names[i] = ((GeneString)geneStrings.elementAt(i)).LHS;
		}
	}
	
	/** insert a new name into the array
	 */
	public void insertNewNameAt(String newName, int position) {
		//plan is to convert to vector, do insert, then convert back to array
		Vector v = new Vector();
		for(int i=0; i<names.length; i++) v.addElement(names[i]);
		v.insertElementAt(newName, position);
		names = new String[v.size()];
		for (int i=0; i<v.size(); i++) names[i]=(String)(v.elementAt(i));	
	}
	
	/** true if the given name is in the list of gene names
	 */
	public boolean isAName(String name) {
		if (names.length == 0) return false;
		boolean r = false;
		for (int i=0; i<names.length; i++) if (names[i].compareTo(name)==0) r=true;
		return r;
	}
	
	//static version for outisde use
	public static boolean isStringArrayMember(String test, String[] array) 
	{
		if (array.length == 0) return false;
		boolean r = false;
		
		for (int i=0; i<array.length; i++) 
		{
			if (array[i]!=null) {
				if (array[i].compareTo(test)==0) r=true;
			}				
		}
		return r;
	}
	
	/** strips out all the space chars
	 */
	private String removeSpaces(String s, char stripChar) {
		
		String result = "";
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i) != stripChar) {
				char[] charArray = {s.charAt(i)};
				String currentChar = new String(charArray);
				result = result.concat(currentChar);
			}
		}
		return result;
	}
		
}


