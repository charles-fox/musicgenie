package uk.ac.cam.cwf22.mg.compiler;

//this represents a SEMANTIC preoblem with the genome
// eg. referring to a gene name which isnt defined
//
// as opposed to a SYNTACTIC problem - these are all covered by GenomeSyntaxException

public class BadGenomeException extends UserException
	
{
	public BadGenomeException(String s) {
		super(s);
	}
}
