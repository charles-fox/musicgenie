package uk.ac.cam.cwf22.mg.compiler;

// parent class for all exceptions caused by user syntax errors in the genome string

public class GenomeSyntaxException extends UserException
{
	public GenomeSyntaxException(String s) {
		super(s);
	}
}
