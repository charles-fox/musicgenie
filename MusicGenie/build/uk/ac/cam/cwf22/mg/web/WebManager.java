package uk.ac.cam.cwf22.mg.web;

import uk.ac.cam.cwf22.mg.core.Score;
import uk.ac.cam.cwf22.mg.compiler.BadGenomeException;


public interface WebManager extends java.rmi.Remote 
{
	/** select for breeding 
	*/
    	public void selectGenome(int position, boolean select)
	throws java.rmi.RemoteException;

	/** get score of nth genome 
	*/
	public Score getScore(int genomeIndex) 
	throws java.rmi.RemoteException;

	/** breed the next generation
	 */
	public void breedNextGeneration()
        throws java.rmi.RemoteException;

	/** gives no of server hits
	*/
	public int hits()
	throws java.rmi.RemoteException;
}
