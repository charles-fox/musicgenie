package uk.ac.cam.cwf22.mg.gui;

/**
 * Contains icons for each genome, with options to play, edit them etc
 * 
 * NB this is just one possible interface to the manager - 
 * - remote applets may also do this job!
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uk.ac.cam.cwf22.mg.core.*;

public class ApplicationWindow extends Frame implements ManagerView
{
	Vector genomePanels;
	
	public Manager theManager;
	
	Color bg = Color.white;
		
	private static int myW=800, myH=250;
	
	//CONSTRUCTOR
	public ApplicationWindow(String s, Manager theManager) {
		super(s);
		
		this.theManager = theManager;
		
		setSize(myW,myH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		setLocation( (dim.width-myW)/2 , (dim.height-myH)/2);
		
		//setLocation(40,220);
		setLayout(new BorderLayout());
		this.setResizable(false);
		
		//make icon in corner of window
		Image fox = AboutBox.getImageFromFile("Icon.jpg");
		this.setIconImage(fox);
		
		//make a big panel with all the genome panels in it
		Panel main = new Panel(new GridLayout(1,2,10,10));
		add("Center", main);
		
		main.setBackground(bg);
		
		//add the control bar at the bottom
		// - for BREED and other top-level commands
		Panel masterControls = new Panel();
		add("South", masterControls);
		masterControls.setBackground(bg);
		
		Button prefs = new Button("Options");
		masterControls.add(prefs);
		prefs.addActionListener(new PrefsListen(this, prefs));
		
		Button breed = new Button("BREED GENOMES");
		masterControls.add(breed);
		breed.addActionListener(new BreedListen(this));
		
		Button stop = new Button("Stop");
		masterControls.add(stop);
		stop.addActionListener(new StopListen());
		
		this.setBackground(Color.white);
	
		//for each genome in the manager, create a genome panel for it
		genomePanels = new Vector();
		for (int i=0; i<theManager.genomes.size(); i++) 
			{
			Genome g = (Genome)(theManager.genomes.elementAt(i));
			
			GenomePanel thisPanel = new GenomePanel(i, theManager);
			main.add(thisPanel);
			//also put handle to it in the class vector so we can access it later
			genomePanels.addElement(thisPanel);
		}	
		
		
		//add a listener for the 'close' icon, to quit the application
		//(using anonymous inner class!)
		this.addWindowListener( new WindowAdapter() {
								   public void windowClosing(WindowEvent e) {
									   exiting();
									   System.exit(0);
								   }
								}
							   );
	}	
	
	
	private void exiting() { theManager.saveAllGenomes();}
	
	/** report caught exceptions to user, in an error box */
	public void reportException(String s) {
		
		ErrorBox eb = new ErrorBox("Error!", s, this);
	}
	
	/** sets the breeding option of the specified genome panel
	*/
	public void setGenomePanelBreeding(int genomeNumber, boolean value) {
		GenomePanel p = (GenomePanel)genomePanels.elementAt(genomeNumber);
		p.setBreeding(value);
	}

	/** set name on one of the genome panels
	 */
	public void setGenomePanelName(int genomeNumber, String name) {
		GenomePanel p = (GenomePanel)genomePanels.elementAt(genomeNumber);
		p.setName(name);		
	}
	
	/** refresh genome names (and eventually other data?)
	 */
	public void refresh() {
		for (int i=0; i<genomePanels.size(); i++) {
			GenomePanel gp = (GenomePanel)genomePanels.elementAt(i);
			Genome g = (Genome)theManager.genomes.elementAt(i);
			gp.genomeNamePanel.setText(g.genomeName);
			gp.genomeNamePanel.setBackground(g.color);
			
			//set selection state
			gp.select.setState(g.selected);
		}
	}
	
	//======INNER CLASSES to listen for button clicks=======
	
	/** listen for BREED */
	class BreedListen implements ActionListener {
		ApplicationWindow w;
		public BreedListen(ApplicationWindow w) {this.w=w;}
		
		//when clicked, ask the manager to breed the next generation of genomes
		public void actionPerformed(ActionEvent e) {
			w.theManager.breedNextGeneration();
			
		}
	}
	
	/** listen for STOP */
	class StopListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theManager.stopPlay();
		}
	}
	
	/** listen for Prefs */
	class PrefsListen implements ActionListener {
		ApplicationWindow w; Button b;
		public PrefsListen(ApplicationWindow w, Button b) {this.w=w; this.b=b;}
		public void actionPerformed(ActionEvent e) 
		{
			int x = w.getLocation().x + myW/2 - 100;
			int y = w.getLocation().y + myH;
			Preferences p = new Preferences(w, theManager, x, y);
		}
	}
	
}

