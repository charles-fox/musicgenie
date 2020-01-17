package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;
import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.BadGenomeException;

/** a panel on the main window used to play, 
 *  edit etc. a particular genome
 */
public class GenomePanel extends Panel
{
	//=======variables====
	
	TextArea genomeNamePanel;
	
	Button
			edit = new Button("Edit"),
			play = new Button("Play");
				
	Checkbox select = new Checkbox("Breed",false);
	
	// NB - we have no handle on the actual genome that we represent
	// we must call the manager to perform actions for us
	
	Manager theManager;
	
	//position of my genome in the master genome list
	int genomePosition;
	
	//====================
	
	/** Constructor takes a handle to a genome and the manager*/
	public GenomePanel(int genomePosition, 
					   Manager theManager) 
	{
		
		this.theManager = theManager;
		this.genomePosition = genomePosition;
		
		String genomeName = ((Genome)(theManager.genomes.elementAt(genomePosition))).genomeName;
		
		genomeNamePanel = new TextArea(genomeName, 1, 15, TextArea.SCROLLBARS_NONE);
		genomeNamePanel.setBackground(
			((Genome)theManager.genomes.elementAt(genomePosition)).color
									 );
		
		//add buttons
		add(genomeNamePanel);
		add(edit); add(play); 
		add(select);

		//attach buttons to their handlers
		edit.addActionListener(new EditListen(this));
		play.addActionListener(new PlayListen());
		select.addItemListener(new SelectListen(this));
		
		this.setBackground(Color.lightGray);
	}
	
	/** sets the breeding checkbox
	 */
	public void setBreeding(boolean breeding) {
		select.setState(breeding);
	}
	
	/** set name in genome name box */
	public void setName(String name) {
		genomeNamePanel.setText(name);	
	}

	
	//======INNER CLASSES to listen for button clicks=======
	
	/** listen for Edit */
	class EditListen implements ActionListener {
		GenomePanel p;
		public EditListen(GenomePanel p) {this.p=p;}
		
		public void actionPerformed(ActionEvent e) {
			//update the master genome name, then open an editing window
			p.theManager.setGenomeName(p.genomePosition, p.genomeNamePanel.getText());
			p.theManager.showGenomeEditor(p.genomePosition);
		}
	}
	
	/** listen for Play button */
	class PlayListen implements ActionListener {
		
		public void actionPerformed(ActionEvent ev) {
			// need to compile the genome to a score
			// then play, or report any genome errors
			Genome genome = (Genome)theManager.genomes.elementAt(genomePosition);
			
			try { 
				WaitBox w = new WaitBox("Please wait", "Preparing to play...");
				w.show();
				Score score = genome.getScore();
				theManager.setVoices(genome.voices);
				w.dispose();
				theManager.playScore(score);
			}
			catch (BadGenomeException e) {
				theManager.reportException("Error in genome");
			}
		}
	}
	
	
	/** listen for checkbox changing state
	 *  and update the genome which it represents accordingly
	 */
	class SelectListen implements ItemListener {
		GenomePanel p;
		public SelectListen(GenomePanel p) {this.p=p;}
		
		//when the breed-select is changed, ask the manager to process the event
		public void itemStateChanged(ItemEvent e) {
			int newState = e.getStateChange();
			
			if (newState == e.SELECTED) p.theManager.selectGenome(p.genomePosition, true);
			else p.theManager.selectGenome(p.genomePosition, false);
		}
	}	
		
}
