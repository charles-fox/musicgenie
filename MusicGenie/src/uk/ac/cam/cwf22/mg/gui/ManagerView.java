package uk.ac.cam.cwf22.mg.gui;


/** Any view on the manager must implement this interface, to guarentee
 *  display services that the manager reuqires
 */
public interface ManagerView
	
{
	public void setGenomePanelBreeding(int genomeNumber, boolean value);
	
	public void setGenomePanelName(int genomeNumber, String name);
	
	public void refresh();
	
	public void reportException(String s);
}
