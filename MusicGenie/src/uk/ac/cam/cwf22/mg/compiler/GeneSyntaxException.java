package uk.ac.cam.cwf22.mg.compiler;

//thrown when a particular gene has bad syntax

public class GeneSyntaxException extends GenomeSyntaxException
{
	public GeneSyntaxException(String s) {
		super(s);
	}
}
