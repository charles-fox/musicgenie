package uk.ac.cam.cwf22.mg.core;

//the genome is the big list of genes which defines the piece
//basically a vector of genes, with clever methods for accessing data in them

import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;
import java.util.*;
import java.awt.Color;
import java.io.Serializable;

public class Genome implements Cloneable, Serializable
{
	/** the name of this genome (like a filename) */
	public String genomeName;
	
	//the actual genes, which use int IDs instead of the original string names
	public Vector genes;
	
	//a vector of the names, such that their index corresponds to their ID number
	public String[] names;
	
	//used in sexual reproduction - if true, then this genome is used
	public boolean selected;
	
	public Color color;
	
	public int[] voices;
	

	/** original constructor, takes pre-built genes and names */
	public Genome(Vector genes, 
				  String[] names) {
		this.genes = (Vector)genes.clone();
		this.names = (String[])names.clone();
		this.genomeName = "Unnamed Genome";
		this.selected = false;
		this.color = Color.red;
		this.voices = new int[16];
	}
	

	
	/** RETURNS a *new* mutated genome
	 *  from:
	 *	- mutate a gene
	 *  - add a new gene (a copy-mutate?)
	 *  - remove a gene
	 * 
	 */
	public Genome mutate() 
	{	
		//if there are no genes other than 0=>NOTE, always create a new one
		// (in fact if the genome is 'small' (2 or less) always create new gene
		if (genes.size() < 3) {
			return copyMutate();
		}		
		
		double r = Math.random();
		//NB - slight weihting to construct rather than destruct
		if (r<0.8) return mutateGene();
		else if (r<0.95) return copyMutate();
		else return removeGene();
	}
	
	/** choose random gene and mutate it
	 * 
	 *  NB - never change gene 0 as it is always NOTE!
	 * 
	 */
	public Genome mutateGene() {
		
		//make a new genome
		Genome result = (Genome)(this.clone());

		//choose a random gene at position i
		int i = Stats.getRandomInt(1,result.genes.size());
		
		Gene g = (Gene)(result.genes.elementAt(i));
		
		//System.out.println("\n**************\nMutating gene "+g);
		
		//replce with mutated version of the gene
		result.genes.removeElementAt(i);
		result.genes.insertElementAt(g.mutate(), i);
		
		//System.out.println("***Gene has mutated to "+g+"\n****************\n");
		
		//return the new genome
		return result;
	}
	
	
	/** choose random gene and mutate it with specified mutation
	 */
	public Genome mutateGene(int mutType) {
		
		//make a new genome
		Genome result = (Genome)(this.clone());

		//choose a random gene at position i
		int i = Stats.getRandomInt(1,result.genes.size());
		
		Gene g = (Gene)(result.genes.elementAt(i));
		
		//System.out.println("\n**************\nSpecifically Mutating gene "+g);
		
		//replce with mutated version of the gene
		result.genes.removeElementAt(i);
		result.genes.insertElementAt(g.mutate(mutType), i);
		
		//System.out.println("***Gene has mutated to "+g+"\n****************\n");
		
		//return the new genome
		return result;
	}
	
	
	/** for the next two methods, we have to be fiendishly clever to avoid
	 * spending hours of coding time on rejigging all the gene numbering.
	 *  the cheat way is to convert the genome back into text, make the changes to the text,
	 *  and then recompile to do the renumbering automatically :)
	 */
	
	
	
	public Genome copyMutate() {
		
		if (Manager.VERBOSE) System.out.println("Copymutate\n");
		
		//choose gene n
		int n = Stats.getRandomInt(1, noOfGenes());
					  
		//get genomeString version of genomes
		GenomeString gs = this.getGenomeString();
		
		//insert GeneString copy n2 of gene n, in a new line following line n...
		
			GeneString parent = (GeneString)(gs.geneStrings.elementAt(n));
		
			// find a new unique name for the new gene
			String newName = findNewUniqueName(parent.LHS, names);
		
		
			GeneString child = new GeneString( newName,
											   parent.RHS );
		
			gs.geneStrings.insertElementAt(child, n+1);
			gs.insertNewNameAt(newName, n+1);
		
		//recompile new genome
		try {
			Genome newG = gs.makeGenome();
		
			newG.color = this.color;
			newG.genomeName = this.genomeName;
			newG.voices = this.voices;
			
			//mutate new copy of n (at position)
			Gene newGene = (Gene)(newG.genes.elementAt(n+1));
			newGene = newGene.mutate();
		
			//System.out.println("Made new genome\n "+newG.toString(true)+"\n***\n");
			
			//the following code makes some of the references to the old 
			//gene n point to the child gene instead.
			
			//for each gene in the NEW genome above the child in the hiarachy...
			for (int i=0; i<newG.genes.size(); i++) 
			{
				Gene g = (Gene)(newG.genes.elementAt(i));
				//System.out.println("Gene "+g);
				
				//for each tphrase in that gene...
				for (int j=0; j<g.RHS.size(); j++) 
				{
					TransformedPhrase tp = (TransformedPhrase)(g.RHS.elementAt(j));
					
					//it ifs LHS is gene n (the parent of the new gene)...
					//System.out.println("Testing tp "+tp+"(Id="+tp.Id+" to match "+n);
					
					if (tp.Id == n) {
						//then 50% chance of changing to n2
						if (Math.random() > 0.5) {
							//System.out.println("Changing "+g);
							tp.Id = n+1; //change to Id of child (one after parent)
							//System.out.println("to "+g);
						}
					}
				}
			}
					
			//System.out.println(newG.toString() + "fdfdfd");
			
			return newG;
		}
		
		catch (GenomeSyntaxException e) {
			//System.out.println("GenomeSyntaxException in CopyMutate - this should never occur");
			//return the old genome in case of impossible error
			return this;
		}
	}
	
	/** make an entirely new random gene 
	 *  (only used when genome is nearly empty)
	 */
	public Genome addRandomNewGene() {
		if (Manager.VERBOSE) System.out.println("AddRandomNewGene - not yet implemented");
		return this;
	}
	
	/** remove a random gene - and update rest of genome to take account of this
	 * 
	 *  plan is do do all ops on genomestring then recompile
	 */
	public Genome removeGene() {
		
		if (Manager.VERBOSE) System.out.println("Remove gene\n");
		
		//no effect for small genomes
		if (this.genes.size() < 3 ) return this;
		
		Genome ng = (Genome)(this.clone());

		//gene no to be removed
		int n = Stats.getRandomInt(2, ng.genes.size());
		
		//System.out.println("Remove refs to gene "+(Gene)(ng.genes.elementAt(n)));
		//System.out.println("From:\n"+ng.toString(true));
		
		//remove all references in genome to gene n
		ng.removeRefs(n);
		
		//remove gene n
		ng.genes.removeElementAt(n);
		
		//remove name from name list
		String [] oldNames = ng.names;
		String [] newNames = new String[ng.names.length- 1];
		
		
		for (int i=0; i<n; i++)					newNames[i] = oldNames[i];
		for (int i=n; i<newNames.length; i++)	newNames[i] = oldNames[i+1];
		
		
		ng.names = newNames;
		
		//System.out.println("\nNEWGNOME\n\n"+ng.toString(true));
		
		return ng;
	}
	
	
	//removes all refs to gene n (ie removed tphrases using it)
	public void removeRefs(int n) 
	{
		for (int i=0; i<genes.size(); i++) 
		{	
			((Gene)(genes.elementAt(i))).removeRefs(n);
		}
		
	}
	
	
	/** returns a GenomeString representation fo this genome
	 */
	public GenomeString getGenomeString() 
	{
		String s = this.toString();
		GenomeString gs = null; //initialise in case of error
		//(error will never occur, as we know this is a health string representation!)
		try { gs = new GenomeString(s); }
		catch (GenomeSyntaxException e) {System.out.println("this should never occur");}
		return gs;
		
	}
	
	/** returns tree representation of this genome */
	public Tree getTree() throws BadGenomeException {
		
		Tree theTree = new Tree(noOfGenes()-1,
								new Vector(), 
								new Vector(), 
								this, 
								new Rational(0,1));
		return theTree;
	}
	

	
	/** returns score representation of this genome	 */
	public Score getScore() throws BadGenomeException {
		
		//make a tree first
		Tree theTree = this.getTree();
		
		//then get score from tree
		Score theScore = theTree.getScore();
		return theScore;
	}

	/** Search the genome and return the gene
	 *   whose LHS is this Id
	**/
	public Gene getGene(int searchId) throws BadGenomeException
	{
		//nasty search - just go through each one at a time for now
		for (int i=0; i<genes.size(); i++) 
		{
			if ( ((Gene)(genes.elementAt(i))).Id == searchId ) 
				return (Gene)(genes.elementAt(i));	
		}
		throw new BadGenomeException("Gene no "+searchId+" not found\n");
	}
	
	/** Methods for accessing the nth gene from the bottom of the list.
	 *  Used in breeding to greatly simpify things!
	 */ 
	public Gene getGeneAtNPositionsFromTheBottom(int n) {
		int length = noOfGenes();
		int required = (length-1) - n;
		return (Gene)(genes.elementAt(required));	
	}
	public String getNameAtNPositionsFromTheBottom(int n) {
		int length = noOfGenes();
		int required = (length-1) - n;
		if (required>=genes.size()) return null; else return names[required];
	}
	
	/** return name of nth gene, or null otherwise */
	public String getName(int geneNumber) {
		if (geneNumber>=genes.size()) return null; else return names[geneNumber];
	}
	
	/** return no of genes in this genome */
	public int noOfGenes() {
		return genes.size();
	}
	
	/** NB - we strip off gene 0, which is always "NOTE=>NOTE"
	 */
	public String toString() {
		String result = "";
		for (int i=1; i<genes.size(); i++) {
			//result += ((Gene)(genes.elementAt(i))).toString();
			result += ((Gene)(genes.elementAt(i))).toString(names);
		}
		return result;
	}
	
	/** prints in Ids if given true, otherwise prints as normal
	 */
	public String toString(boolean numbers) {
		String result = "";
		for (int i=0; i<genes.size(); i++) {
			if (numbers) result += ((Gene)(genes.elementAt(i))).toString();
			else result += ((Gene)(genes.elementAt(i))).toString(names);
		}
		return result;
	}
	
	/** Tests baseName to see if it's not already in the list.  
	 *  Returns it if so.
	 *  Otherwise, appends numbers to it to make it unique.
	 */
	public static String findNewUniqueName(String baseName, String [] names)
	{
		String result = new String(baseName);
		
		//if already unique, stop
		if (!GenomeString.isStringArrayMember(result, names)) return result;
		else {
			
			//add a new int to the end to make it unique
			
			boolean unique = false;
			int i = 0;
			
			while (!unique) 
			{
				result = baseName.concat(""+i);
				if (!GenomeString.isStringArrayMember(result, names)) unique = true;
				i++;
			}
			return result;
			
		}
			
	}
	
	/** SHALLOW cloning.
	 */
	public Object clone() {    
		Object r = null;    
		try {      
			r = super.clone();
			
			Genome result = (Genome)r;
			
			//make new gene vector - but with refs to same genes
			result.genes = new Vector();
			for (int i=0; i<this.genes.size(); i++) {
				result.genes.addElement(this.genes.elementAt(i));
			}
			
			return result;
		} 
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException");    
			return null;
		}   
		
	}
}
