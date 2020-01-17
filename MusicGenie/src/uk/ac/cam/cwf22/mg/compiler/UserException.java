package uk.ac.cam.cwf22.mg.compiler;

/** the absolute superclass of all exceptions which are caused by
 * the user doing something wrong, and which can be caught 
 * and reported in an error box, and recovered from
 * 
 * GenomeSyntaxException and BadGenomeException are the extensions.
 * */


public class UserException extends Exception
{
	public String userReport;
	
	public UserException(String s) {
		super(s);
		this.userReport = s;
	}
}
