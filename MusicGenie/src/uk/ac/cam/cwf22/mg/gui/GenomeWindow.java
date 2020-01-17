package uk.ac.cam.cwf22.mg.gui;

/** The big genome editing window.
 *	It contains several panels which do the work
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import uk.ac.cam.cwf22.mg.graphics.*;
import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.*;

public class GenomeWindow extends Frame
{
	Manager theManager;
	public Controls theControls;
	int genomePosition;
	
	//slightly odd construct so inner class can get a handle on the outer class
	Frame genomeWindow;
	
	public static String title = " - Music Genie [editor]";
	
	//CONSTRUCTOR
	//a handle to one of the master genomes, and the number of its position
	public GenomeWindow(Genome myGenome, 
						int genomePosition, 
						Manager theManager) {
		
		super(myGenome.genomeName + title);
		
		this.theManager = theManager;
		this.genomePosition = genomePosition;
		
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		setSize(dim.width,dim.height);
		
		
		setLocation(0,0);
		show();
		
		//corner icon
		Image fox = AboutBox.getImageFromFile("Icon.jpg");
		this.setIconImage(fox);
		
		genomeWindow = this;
		
		//setLayout(new GridLayout(1,2,10,10));
		Panel main = new Panel(new BorderLayout(10,10));
		
		this.setLayout(new GridLayout(1,1,10,10));
		this.add(main);
		
		
		
		TreeGraphics theTreeGraphics = new TreeGraphics(new VirtualRectangle(-100f,-100f,200f,200f), Color.lightGray);
		Display2DScrolling scoreDisplay = new Display2DScrolling(new VirtualRectangle(-10f,-10f,20f,20f), Color.white);

		theControls = new Controls(theTreeGraphics, 
								   scoreDisplay, 
								   myGenome,
								   theManager,
								   this);
		
		
		//make a panel containing the tree and score displays
		Panel p = new Panel();
		
		p.setLayout(new GridLayout(2,1,10,10));
		p.add(theTreeGraphics);
		p.add(scoreDisplay);
		
		//add a listener for the 'close' icon, to close this genome window
		//(using anonymous inner class!)
		this.addWindowListener(new CloseListen(this));
		
		main.add("Center",p);
		main.add("West", theControls);
		
		//
		//add a menu bar at the top of the window
		//
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		Menu mutateMenu = new Menu("Mutations");
		Menu genomeMenu = new Menu("Genome");
		Menu helpMenu = new Menu("Help");
		
		Menu mutateGeneMenu = new Menu("Mutate Gene");
		Menu mutateTPMenu = new Menu("Mutate TPhrase");
		
		
		MenuItem[] fileItems = {new MenuItem("Load genome"),
								new MenuItem("Save genome"),
								new MenuItem("Save as MIDI")};
		
		MenuItem[] mutateTPItems = {new MenuItem("Add transform"),
									new MenuItem("Remove transform"),
									new MenuItem("Mutate transform"),
									new MenuItem("Mutate phrase") };
									
		
		MenuItem[] mutateGeneItems = {new MenuItem("Add TPhrase"),
									  new MenuItem("Remove TPhrase"),
									  new MenuItem("Flip orientation"),
									  mutateTPMenu };
		
		MenuItem[] mutateItems = {new MenuItem("Add random gene"),
								  new MenuItem("Delete random gene"),
								  mutateGeneMenu,
								  new MenuItem("Copy-Mutate random gene")};
		
		MenuItem[] genomeItems = {new MenuItem("MIDI voices"),
								  new MenuItem("Colour")};
		
		MenuItem[] helpItems = {new MenuItem("Symbols"),
								new MenuItem("Online manual"),
								new MenuItem("About")};
		
		
		
		fileItems[0].addActionListener(new LoadGenomeListen());
		fileItems[1].addActionListener(new SaveGenomeListen());
		fileItems[2].addActionListener(new SaveMIDIListen());
		
		mutateItems[0].addActionListener(new AddGeneListen());
		mutateItems[1].addActionListener(new RemoveGeneListen());
		//no gene mutate listener
		mutateItems[3].addActionListener(new CopyMutateListen());
		
		mutateGeneItems[0].addActionListener(new AddTPListen());
		mutateGeneItems[1].addActionListener(new RemoveTPListen());
		mutateGeneItems[2].addActionListener(new FlipListen());
		//no TP Mutate listen

		mutateTPItems[0].addActionListener(new AddTListen());
		mutateTPItems[1].addActionListener(new RemoveTListen());
		mutateTPItems[2].addActionListener(new MutateTListen());
		mutateTPItems[3].addActionListener(new MutatePListen());
		
		genomeItems[0].addActionListener(new EditVoicesListen());
		genomeItems[1].addActionListener(new EditColorListen());
		
		helpItems[0].addActionListener(new SymbolHelpListen(this));
		helpItems[1].addActionListener(new HelpListen(this));
		helpItems[2].addActionListener(new AboutListen(this));
		
		for (int i=0;i<fileItems.length;i++) fileMenu.add(fileItems[i]);
		for (int i=0;i<mutateItems.length;i++) mutateMenu.add(mutateItems[i]);
		for (int i=0;i<genomeItems.length;i++) genomeMenu.add(genomeItems[i]);
		for (int i=0;i<helpItems.length;i++) helpMenu.add(helpItems[i]);
		for (int i=0;i<mutateGeneItems.length;i++) mutateGeneMenu.add(mutateGeneItems[i]);
		for (int i=0;i<mutateTPItems.length;i++) mutateTPMenu.add(mutateTPItems[i]);
		
		menuBar.add(fileMenu);
		menuBar.add(mutateMenu);
		menuBar.add(genomeMenu);
		menuBar.add(helpMenu);
		
		
		this.setMenuBar(menuBar);
		
		main.setBackground(Color.lightGray);
	}
	
	//INNER CLASS for window closing
	class CloseListen extends WindowAdapter {
		Frame f;
		public CloseListen(GenomeWindow f) {this.f=f;}
		
		public void windowClosing(WindowEvent e) {
			
			
			//set the managers version of this genome to a newly-compiled version
			//of the one we have been editing
			//TODO: flag for last modified so we know if we need to compile again
			
			//if (recently modified)
			
			theControls.compileOnClose();
			
			
			f.dispose();
		}
	}
	

	
	
	/** listen for About */
	class AboutListen implements ActionListener {
		Frame w;
		public AboutListen(Frame w) {this.w=w;}
		public void actionPerformed(ActionEvent e) {
			AboutBox ab = new AboutBox(w, theManager);
		}
	}
	
	/** listen for online help */
	class HelpListen implements ActionListener {
		Frame w;
		public HelpListen(Frame w) {this.w=w;}
		public void actionPerformed(ActionEvent e) {
			ErrorBox eb = new ErrorBox("Online Help", "The Music Genie user guide, demos, and latest updates can all be found online at www.i.am/charlesfox\n\n (If the website has moved, search for 'Music Genie' and 'Charles Fox' on any good search engine)",w);
		}
	}
	
	/** listen for online help */
	class SymbolHelpListen implements ActionListener {
		Frame w;
		public SymbolHelpListen(Frame w) {this.w=w;}
		public void actionPerformed(ActionEvent e) 
		{
			String samples = "A composition is described by a GENOME, which consists of one of more GENES.  Each gene has a name and a definition, and is written as\n  'NAME => {DEFINITION}'\n\nThe definition can be written in {curley} or [square] brackets to indictate that it should be played in {melody} or [harmony].\n\nThe elements of the definition are separated by commas.  Each element consists of a gene name, optionally preceeded by transforms.\n\neg.\n  SONG => {VERSE, (~1/2)(@3/4)VERSE, (^4##)CHORUS}\n\n The transforms are as follows:\n\nTransposition:\n  (^5) : up a fifth\n  (^-2) : down a second\n  (^4#) : up a sharpened fourth\n  (^-7bbb) : down a triple flattened-seventh\n\nTime-stretch\n  (~3) : stretch by 3 times\n  (~1/2) : stretch by half (ie. double speed)\n\nKey change\n  (&6) : move key up a fifth (eg. C Maj to A Maj)\n  (&-3b) : move key down by flattened third (eg. C Maj to Bb Maj)\n\nShift scale pattern\n  ($2) : change to mode formed by second note, eg. C Maj to C Dorian\n  ($-1) to mode formed by last note eg. C Maj to C Locrian\n\nRetrograde\n  (R) : play backwards\n\nInversion\n  (I) : play upside-down\n\nAmplitude\n  (@2) : play twice as loud\n  (@1/2) : play half as loud\n\nVoice change\n  (v2) : change up 2 voices up\n  (v-3) : down 3 voices\n\n";
			ErrorBox eb = new ErrorBox("Symbols", samples,w, true);
			
		}
	}
	
	
	
	class LoadGenomeListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileDialog eb = new FileDialog(genomeWindow, "Load genome", FileDialog.LOAD);
			eb.setLocation(200,200);
			
			eb.setFilenameFilter(new GenFilter());
			eb.setFile("*.gen");
			
			eb.show();
			String fileName = eb.getFile();
			String directory = eb.getDirectory();
			
			if (fileName != null) {
				//load new genome
				loadFile(fileName, directory);
			}
			else {
				//user pressed Cancel
			}
		}
	}
	
	class SaveGenomeListen implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			theControls.myGenome.genomeName = theControls.genomeNameBox.getText();
			
			FileDialog eb = new FileDialog(genomeWindow, "Save genome", FileDialog.SAVE);
					
			eb.setFilenameFilter(new GenFilter());
			eb.setFile(theControls.genomeNameBox.getText()+".gen");
			eb.show();
			
			
			
			String fileName = eb.getFile();
			String directory = eb.getDirectory();
			
			if (fileName != null) {
				//append extension if not already supplied
				if (!fileName.endsWith(".gen")) {
					fileName = fileName.concat(".gen");
				}
				
				
				
				//save genome
				saveFile(fileName, directory);

			}
			else {
				//user pressed Cancel
			}
		}
		
		
	}
	
	
	/** inner inner class for file filter
	 */
	public class GenFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name) {
			String f = new File(name).getName();
			return f.indexOf(".gen") != -1;
		}
	}
	
	/** class to listen for 'Save MIDI' user action
	 */
	class SaveMIDIListen implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			FileDialog eb = new FileDialog(genomeWindow, "Save as MIDI", FileDialog.SAVE);
			eb.setFile("*.mid");
			eb.show();
			
			String fileName = eb.getFile();
			String directory = eb.getDirectory();
			
			if (fileName != null) 
			{
				if (!fileName.endsWith(".mid")) 
				{
					fileName = fileName.concat(".mid");
				}
				//save genome
				theControls.saveMIDI(fileName, directory);
			}
			else {
				//user pressed Cancel
			}
		}
	}
	
	/** class to listen for AddGene
	 */
	class AddGeneListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.addRandomNewGene();
			theControls.buildScore();
			
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'RemoveGene'
	 */
	class RemoveGeneListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.removeGene();
			theControls.buildScore();
			
			theControls.updateDisplays();
		}
	}
	
	
	/*
	mutateGeneItems[0].addActionListener(new AddTPListen());
		mutateGeneItems[1].addActionListener(new RemoveTPListen());
		mutateGeneItems[2].addActionListener(new FlipListen());
		mutateGeneItems[3].addActionListener(new MutateTPListen());
	*/
	
	/** class to listen for 'MutateGene->AddTP'
	 */
	class AddTPListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.ADDTP);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene->RemoveTP'
	 */
	class RemoveTPListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.REMOVETP);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene->Flip ori'
	 */
	class FlipListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.FLIP);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene->MutateTP->AddT'
	 */
	class AddTListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.ADDT);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene->MutateTP->RemoveTListen'
	 */
	class RemoveTListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.REMOVET);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
		/** class to listen for 'MutateGene->MutateTP->MutateTListen'
	 */
	class MutateTListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.MUTATET);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene->MutateTP->MutatePhrase'
	 */
	class MutatePListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.mutateGene(Gene.MUTATEP);
			theControls.buildScore();
			theControls.updateDisplays();
		}
	}
	
	
	
	/** class to listen for 'CopyMutate'
	 */
	class CopyMutateListen implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theControls.myGenome = theControls.myGenome.copyMutate();
			
			//theManager.reportException(theControls.myGenome.toString());
			theControls.buildScore();
			
			theControls.updateDisplays();
		}
	}
	
	/** class to listen for 'MutateGene'
	 */
	class EditVoicesListen implements ActionListener {
		public void actionPerformed(ActionEvent e) 
		{
			InstrumentWindow i = new InstrumentWindow(theControls);
		}
	}
	
	/** class to listen for 'MutateGene'
	 */
	class EditColorListen implements ActionListener {
		public void actionPerformed(ActionEvent e) 
		{
			ColorWindow c = new ColorWindow(theControls);
		}
	}
	
	
	private void loadFile(String fileName, String directory) {
		
		try {
			
			ObjectInputStream in = new ObjectInputStream(
									 new FileInputStream(
										directory+fileName));
			
			theControls.myGenome = (Genome)in.readObject();
			
			theControls.compileFromFile();
			
		}
		catch (Exception e) {
			theManager.reportException("Cannot load file");
			e.printStackTrace();
		}
			
	}
	
	/** saves the genome string
	 */
	private void saveFile(String fileName, String directory) {

		Genome g = theControls.myGenome;
		
		try {
			ObjectOutputStream out = 
				new ObjectOutputStream(
					new FileOutputStream(directory+fileName));
			
			out.writeObject(g);
			out.close();
			
			//strip .gen off again for display names
			String stripped = (String)((GeneString.split(fileName,".")).elementAt(0));
				
			theControls.myGenome.genomeName = stripped;
			theControls.updateText();
			
			
			/*
			DataOutputStream out = 
				new DataOutputStream(
					new BufferedOutputStream(
						new FileOutputStream(directory+fileName)));
			
			out.writeBytes(s);
			out.close();
			
			//update the genome name
			theControls.myGenome.genomeName = fileName;
		*/
		}
		catch (Exception e) {
			theManager.reportException("Cannot save file\n\n"+e.getMessage());
			e.printStackTrace();
		}
			
	}
	
}
