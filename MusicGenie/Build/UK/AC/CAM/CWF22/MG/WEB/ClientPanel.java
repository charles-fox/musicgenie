package uk.ac.cam.cwf22.mg.web;

import java.awt.*;
import java.awt.event.*;
import uk.ac.cam.cwf22.mg.core.Score;
import uk.ac.cam.cwf22.mg.midi.ScorePlayer;

public class ClientPanel extends Panel
{
	ScorePlayer theScorePlayer;

	public ClientPanel(WebManager m) {
		super();
		
		//make MIDI ScorePlayer
		try {theScorePlayer = new ScorePlayer();}
		catch (Exception e) {
			System.out.print("MIDI error");
			System.exit(0);
		}


		setLayout(new GridLayout(2,1));

		Panel p = new Panel();
		add(p);

		for (int i=0; i<6; i++) {
			p.add(new GenomePanel(i,m));
		}
		
		Panel p2 = new Panel();
		add(p2);

		p2.add(new Label("Charles Fox 2000"));

		Button breedGenomes = new Button("Breed genomes");
		p2.add(breedGenomes);
		breedGenomes.addActionListener(new BreedGenomesListen(m));

		p2.add(new Label("www.i.am/charlesfox"));

	}

	/** listen for BREED GENOMES */
	class BreedGenomesListen implements ActionListener {
		WebManager m;
		
		public BreedGenomesListen (WebManager m) {
			this.m=m;
		}
	
		//when clicked, ask the manager to breed the next generation of genomes
		public void actionPerformed(ActionEvent e) {
		   try{
			m.breedNextGeneration();
		   }
		   catch (java.rmi.RemoteException er) {
			System.err.println("RMI error");
		   }
		}
	}


	class GenomePanel extends Panel {
	        WebManager m;
		int genome;

		public GenomePanel(int genome, WebManager m) {
			this.m=m;
			this.genome = genome;
			setBackground(Color.lightGray);

			setLayout(new GridLayout(2,1));

			Button play = new Button("play "+(genome+1));
			add(play);
			play.addActionListener(new PlayListen(genome,m));

			Checkbox select = new Checkbox("Breed",false);
			add(select);
			select.addItemListener(new SelectListen(genome,m));
		}


		/** listen for PLAY */
		class PlayListen implements ActionListener {
			WebManager m;
			int genome;
			
			public PlayListen(int genome, WebManager m) {
				this.m=m;
				this.genome = genome;
			}
		
			//when clicked, ask the manager to play the genomes
			public void actionPerformed(ActionEvent e) {
			   try{
				Score s = m.getScore(genome);
				try{theScorePlayer.playScore(s);}
				catch(Exception er) {System.err.println("MIDI play error");}
			   }
			   catch (java.rmi.RemoteException er) {
				System.err.println("RMI error");
			   }
			}
		}

		/** listen for checkbox changing state
		 *  and update the genome which it represents accordingly
		 */
		class SelectListen implements ItemListener {
			WebManager m;
			int genome;

			public SelectListen(int genome, WebManager m) {
				this.m=m;
				this.genome = genome;
			}
		
			//when the breed-select is changed, ask the manager to process the event
			public void itemStateChanged(ItemEvent e) 
			{
				try{
					int newState = e.getStateChange();
			
					if (newState == e.SELECTED) m.selectGenome(genome, true);
					else m.selectGenome(genome, false);
					
				   }
				   catch (java.rmi.RemoteException er) {
					System.err.println("RMI error");
				   }
			}
		}	


	}


}