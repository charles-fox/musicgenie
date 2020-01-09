package uk.ac.cam.cwf22.mg.web;

import uk.ac.cam.cwf22.mg.gui.Manager;
import uk.ac.cam.cwf22.mg.core.Score;


public class WebManagerImpl
    extends java.rmi.server.UnicastRemoteObject
    implements WebManager

{
	Manager manager;

	private int hits;

    // Implementations must have an 
    // explicit constructor
    // in order to declare the 
    // RemoteException exception

    	public WebManagerImpl()
       		throws java.rmi.RemoteException 
    	{
        	super();
		this.hits = 0;
    	}

   	 public WebManagerImpl(Manager m)
     	   throws java.rmi.RemoteException 
   	 {
      	  super();
		this.hits = 0;
		this.manager = m;
    	}

   	/** select for breeding 
  	*/
    	public void selectGenome(int position, boolean select)
		throws java.rmi.RemoteException 
	{
		manager.selectGenome(position, select);
	}


	/** get score of nth genome 
	*/
	public Score getScore(int genomeIndex) 
		throws java.rmi.RemoteException
	{
		return manager.getScore(genomeIndex);
	}


	/** breed the next generation
	 */
	public void breedNextGeneration()
        throws java.rmi.RemoteException 
	{
		manager.breedNextGeneration();
	}

	/** gives no of server hits
	*/
	public int hits()
	throws java.rmi.RemoteException 
	{
		hits ++;
		return hits;
	}
}
