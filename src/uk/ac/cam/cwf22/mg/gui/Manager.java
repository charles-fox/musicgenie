package uk.ac.cam.cwf22.mg.gui;

/** This will be a singleton.
 *  It is the interface between the gui commands and the real code
 *  (to allow for different GUIs, eg. remote applets)
 */

import java.util.Vector;
import java.io.*;
import java.awt.*;


import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.midi.*;


public class Manager  
				

{
	//whether to mutate as well as crossbreed midi voices
	public boolean MUTATE_VOICES = true;
	
	//max sequence of genes from each praent round
	public int maxSpliceLength = 5;
	
	//no of mutations doen on each new child
	public int generationMutations = 8;
	
	//no of children to show on trees
	public int treeDetail = 5;
	
	public static boolean VERBOSE = false;
	
	/** All the genomes currently in play 
	 *  this is the big important master list!
	 */
	public Vector genomes = new Vector();
	
	/* the view, eg. applicationWindow, or appletView, that is using this manager */
	private ManagerView myView;
	
	/* the singleton score player - takes scores to the OS MIDI system*/
	public ScorePlayer theScorePlayer;
	
	//text for help menu
	public static String 
		versionInfo = "2000-04-19",
		aboutText = "Music Genie "+versionInfo+"\nA Cambridge Computer Science Tripos project\nCharles Fox, Clare College, 1999-2000\nwww.i.am/charlesfox";
		
	public static int
		PIANO = 1,
		HARPSICHORD = 7,
		VIBES = 12,
		GLOCKEN = 10,
		FLUTE = 74,
		CLARINET = 72,
		SAX = 67,
		STRINGS = 52,
		BASS = 33,
		DRUMS = 10,
		TUBA = 59,
		TRUMPET = 57,
		SAW = 82,
		SQUARE = 81,
		ORGAN = 17,
		GUITAR = 25;
		
		
	
	public static int[] DEFAULT_VOICES = {PIANO,
										  HARPSICHORD,
										  VIBES,
										  GLOCKEN,
										  FLUTE,
										  CLARINET,
										  SAX,
										  STRINGS,
										  BASS,
										  DRUMS,
										  TUBA,
										  TRUMPET,
										  SAW,
										  SQUARE,
										  ORGAN,
										  GUITAR};
	
	
	/** CONSTRUCTOR*/
	public Manager() {
		
		/*
		try {
			
			
			//create initial genome and put it in the vector
			String initialText1 = "A=>NOTE \n B=> {A,A}";
			String initialText2 = "A=>NOTE \n B=> {A,A} \n C=>{B,B}";
			
			GenomeString initialGenomeString1 = new GenomeString(initialText1);
			GenomeString initialGenomeString2 = new GenomeString(initialText2);
			
		
			//put six individual genomes in the vector
			for (int i=0; i<3; i++) {
				genomes.addElement(initialGenomeString1.makeGenome());
			}
			for (int i=3; i<6; i++) {
				genomes.addElement(initialGenomeString2.makeGenome());
			}
			
			//choose random color and default voices for each
			for (int i=0; i<6; i++) 
			{
				int r = Stats.getRandomInt(0,255);
				int g = Stats.getRandomInt(0,255);
				int b = Stats.getRandomInt(0,255);
				
				((Genome)genomes.elementAt(i)).color = new Color(r,g,b);
				
				//and set voices to default
				((Genome)genomes.elementAt(i)).voices = (int[])DEFAULT_VOICES.clone();
			}
			
		}
		catch(GenomeSyntaxException e) {
			reportException("Error in initial genome code");
			System.exit(1);
		}
		
		for (int i=0; i<genomes.size(); i++) 
		{
			Genome g = (Genome)(genomes.elementAt(i));
			g.genomeName = "Genome"+(i+1);
		}
		*/
		
		//load the six default genomes
		try {
			for (int i=0; i<6; i++) {
				genomes.addElement(this.loadDefaultGenome("Genome"+(i+1)));
			}
		}
		catch (Exception e) {
			reportException("Error loading default genome");
			e.printStackTrace();
		}
		
		//create a score player
		try 
		{
			theScorePlayer = new ScorePlayer();
		}
		catch (Exception e) {
			reportException("MIDI system not available");
			System.exit(2);
		}
	}
	
	//refresh manager display
	public void refresh() 
	{
		myView.refresh();
	}
	
	/** call to request playing a score 
	 *  this will handle any MIDI system exceptions
	 *  (eg. print error on screen if MIDI system in use)
	 * 
	 *  NB - no-one else has access to the score player
	 *  the only way is to call it through this manager.
	 * */
	public void playScore(Score s) {
		try {
			theScorePlayer.playScore(s);
		}
		catch (Exception e) {
			this.reportException("MIDI system error\n");
		
		}
	}
	
	/** stops the score from playing
	 */
	public void stopPlay() {
		try {
			theScorePlayer.stopPlay();
		}
		catch (Exception e) {
			this.reportException("MIDI system error");
			e.printStackTrace();
		}
	}
	
	/** sets the MIDI voices used by the scoreplayer
	 */
	public void setVoices(int[] voices) {
		theScorePlayer.setVoices(voices);
	}
	
	/** gets the score of the specified genome
	 *  (returns empty score if the genome is bad)
	 */
	public Score getScore(int genomeIndex) throws BadGenomeException
	{
			return ((Genome)genomes.elementAt(genomeIndex)).getScore();
		
	}
	
	/** save a given score as a MIDI file
	 */
	public void saveMIDI(Score s, String fileName, String directory) {
		try {
			theScorePlayer.saveMIDI(s, fileName, directory);
		}
		catch (Exception e) {
			this.reportException("MIDI system error");
			e.printStackTrace();
		}
	}
			
	/** Creates a genome editing window for the specified genome */
	public void showGenomeEditor(int genomePosition) {

		Genome toEdit = (Genome)(genomes.elementAt(genomePosition));
		
		//make the window.  it needs to be aware of it's genome's position
		//so that it can write back a result if it changes its genome
		GenomeWindow win = new GenomeWindow(toEdit, genomePosition, this);
		
		//compile and display the genome
		win.theControls.compile();
	}
	
	/** report caught exceptions to user, in an error box */
	public void reportException(String s) {
		try {
			myView.reportException(s);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	/** sets the genome at position to the genome g*/
	public void setGenome(int position, Genome g) {
		genomes.setElementAt(g, position);
	}
	
	/** select/deselects a genome for breeding
	 */
	public void selectGenome(int position, boolean select) {
		((Genome)genomes.elementAt(position)).selected = select;
		myView.setGenomePanelBreeding(position,select);
	}
	
	/** sets the name genome at given position*/
	public void setGenomeName(int position, String newName) {
		((Genome)genomes.elementAt(position)).genomeName = newName;
	}
	
	/** set the view that is using this manager,
	 *  so we can communicate with it
	 */
	public void setView(ManagerView v) {
		myView = v;
	}
	
	/** Breeds the next generation of genomes.
	 *  Only those which are selected are used as parents.
	 */
	public void breedNextGeneration() {
		
		Vector parentGenomes = new Vector();
		
		//make vector of selected genomes
		for (int i=0; i<genomes.size(); i++)
		{
			Genome g = (Genome)genomes.elementAt(i);
			if (g.selected) parentGenomes.addElement(g);
		}
		
		if (parentGenomes.size() ==0 ) {
			reportException("You must select one or more genomes for breeding");
			return;
		}
		
		genomes = breedManyGenomes(parentGenomes, genomes.size());
		
		//now set all the new genomes to non-breed in the display
		//and display their new names
		for (int i=0; i<genomes.size(); i++) 
		{
			Genome g = (Genome)(genomes.elementAt(i));
			g.genomeName = "Genome "+(i+1);
			myView.setGenomePanelBreeding(i, false);
			myView.setGenomePanelName( i, ((Genome)genomes.elementAt(i)).genomeName );
		}
		//refresh colors
		myView.refresh();
		
	}
	
	
	/** returns a whole vector full of ickul baby genomes */
	private Vector breedManyGenomes(Vector parentGenomes, int childrenRequired) 
	{
		Vector childGenomes = new Vector(childrenRequired);
		
		for (int i=0; i<childrenRequired; i++)
		{
			childGenomes.addElement(breedGenome(parentGenomes));
		}
		return childGenomes;
	}
	
	/** algorithm for breeding genomes 
	 *  by splicing genes at random
	 */
	private Genome breedGenome(Vector parentGenomes) {
		
		//height of new genome.
		//choose gaussian random between min and max lengths
		int babyHeight = Stats.getRandomInt(minGenomeHeight(parentGenomes),
											1+maxGenomeHeight(parentGenomes));
		
		Vector babyGenes = new Vector(babyHeight);
		String[] babyNames = new String[babyHeight];
		
		if (Manager.VERBOSE)  System.out.println("Building baby...");
		
		try {
		
			int height = 0;
			
			//beginning at NOTE and working up...
			while (height < babyHeight) 
			{
				//choose random genome
				Genome parent = (Genome)chooseRandomElement(parentGenomes);
				
				//choose random potential no of genes to take from it
				int potential = height + Stats.getRandomInt(0, maxSpliceLength);
				
				//while next of these exists
				while (height < parent.genes.size() &
					   height < potential & 
					   height < babyHeight)
				{
					//add it to genome
					babyGenes.addElement(parent.getGene(height).clone());
					
					//get potential name, and check for clashes (a-conversion)
					String testName = parent.names[height];
					babyNames[height] = Genome.findNewUniqueName(testName, babyNames);
					
					height++;						
				}
			}
		}
		catch (Exception e) 
		{
			reportException("Bug during cloning\n(please report on the Music Genie website)");
			e.printStackTrace();
			return null;
		}
		
		Genome baby = new Genome(babyGenes, babyNames);
	
		//mutate the colors
		baby.color = crossBreedColor(parentGenomes);
		baby.voices = crossBreedVoices(parentGenomes);
		//mutate the baby genome
		for (int i=0; i<generationMutations; i++) {baby = baby.mutate();}
		
		return baby;
		
	}
	
	
	/*
		// OLD CODE FOR	GENE BY GENE CROSSOVER
	
			//work down the genome from the top
			//for each required gene...
			for (int height = 0; height < heightOfNewGenome; height++) 
			{
				
				Vector eligableGenomes = makeVectorOfEligableGenomes(parentGenomes,
																	 height);
				
				//pick a genome from this vector at random
				Genome chosenGenome = (Genome)chooseRandomElement(eligableGenomes);
				
				//add the gene from this genome to the baby genome's gene array
				//TO DO this should be a CLONE not a handle copy!
				
				genes.addElement(chosenGenome.getGene(height));
				
				//get potential name, and check for clashes (a-conversion)
				String testName = chosenGenome.getName(height);	
				 
				names[height] = Genome.findNewUniqueName(testName, names);
			}
			*/
	
	
	
	
	/** returns a random elelent of a vector
	 */
	private Object chooseRandomElement(Vector v) {
		int max = v.size();
		
		int r = (int) (100000 * Math.random() );
		
		int index = r%max;
		
		return v.elementAt(index);
	}
	
	/** returns no of genes in longest genome */
	private int maxGenomeHeight(Vector genomes) {
		int max = 0;
		for (int i=0; i<genomes.size(); i++) 
		{
			Genome g = (Genome)genomes.elementAt(i);
			if (g.noOfGenes() > max) max = g.noOfGenes();
		}
		return max;
	}
	
	/** returns no of genes in shortest genome */
	private int minGenomeHeight(Vector genomes) 
	{
		int min = ((Genome)genomes.elementAt(0)).noOfGenes();
		for (int i=0; i<genomes.size(); i++) 
		{
			Genome g = (Genome)genomes.elementAt(i);
			if (g.noOfGenes() < min) min = g.noOfGenes();
		}
		return min;
	}
	
	/** reverse a vector */
	private Vector reverseVector(Vector v) {
		Vector r = new Vector();
		for (int i=v.size()-1; i>=0; i--) r.addElement(v.elementAt(i));
		return r;
	}
	
	
	/** go through the genomes in play.  each one that has a gene at the required height
	 *  gets added to the eligable list, which is returned.
	 * 
	 *  NB - height is now measured downwards!, eg:
	 *		0
	 *		1
	 *		2
	 *		etc
	 */
	private Vector makeVectorOfEligableGenomes(Vector parentGenomes, int height) {
	
		Vector eligableGenomes = new Vector();
		
		//for each potential parent, test it and add it if its at least this tall
		for (int j=0; j<parentGenomes.size(); j++) 
		{	
			Genome g = (Genome)parentGenomes.elementAt(j);
								
			if ( g.noOfGenes() >= height+1 ) eligableGenomes.addElement(parentGenomes.elementAt(j));
		}	
		
		return eligableGenomes;
	}
	
	/** returns a color based on the parents'
	 */
	private Color crossBreedColor(Vector parentGenomes) 
	{	
	int [][] parentVals = new int[3][parentGenomes.size()];
		for (int j=0; j<parentGenomes.size(); j++) 
		{
				parentVals[0][j] = ((Genome)parentGenomes.elementAt(j)).color.getRed();
				parentVals[1][j] = ((Genome)parentGenomes.elementAt(j)).color.getGreen();
				parentVals[2][j] = ((Genome)parentGenomes.elementAt(j)).color.getBlue();
		}
		int[] rgb = new int[3];
		for (int i=0; i<3; i++) {
			rgb[i] = Stats.mutatedColorMean(parentVals[i]);
		}
		Color c = new Color(rgb[0], rgb[1], rgb[2]);
		return c;
	}
	
	
	/** returns a voice set based on the parents' 
	 * 
	 *  choose a single parent for each voice
	 *  and possibly mutate by +- 1 ish
	 */
	private int[] crossBreedVoices(Vector parentGenomes) 
	{	
	int [][] parentVals = new int[16][parentGenomes.size()];
	
		//make vector of vect of parent voices
		for (int v=0; v<16; v++) 
			for (int p=0; p<parentGenomes.size(); p++) 
				parentVals[v][p] = ((Genome)parentGenomes.elementAt(p)).voices[v];
	
		int[] newVoices = new int[16];
		
		//for each voice, find new value
		for (int v=0; v<16; v++) 
		{
			int luckyParent = Stats.getRandomInt(0,parentVals[v].length);
				
			newVoices[v] = parentVals[v][luckyParent];
			
			//if option selected, mutate the voice as well
			if (MUTATE_VOICES) {
				int deviation = Stats.getRandomInt(-2,3);
				newVoices[v] += deviation;
			}
		}
		return newVoices;
	}
	
	/** loads from default directory
	 */
	private Genome loadDefaultGenome(String fileName) throws Exception
	{
	
		Toolkit tk = Toolkit.getDefaultToolkit();
		String dir = System.getProperty("user.dir");
		String sep = System.getProperty("file.separator");
		
		String path = dir+"\\uk\\ac\\cam\\cwf22\\mg\\default\\";
		
		Genome result = loadGenome(fileName, path);
		
		return result;
	}
	
	public void saveAllGenomes() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		String dir = System.getProperty("user.dir");
		String sep = System.getProperty("file.separator");
		String path = dir+"\\uk\\ac\\cam\\cwf22\\mg\\default\\";
		
		for (int i=0; i<this.genomes.size(); i++) 
		{
			String fileName = "Genome"+(i+1)+".gen";
			saveGenome(fileName, path, (Genome)(genomes.elementAt(i)));
		}
		
	}
	
	
	/** loads from any directory
	 */
	private Genome loadGenome(String fileName, String directory) throws Exception
	{	
		ObjectInputStream in = new ObjectInputStream(
									 new FileInputStream(
										directory+fileName+".gen"));
			
		Genome result = (Genome)in.readObject();
		return result;			
	}
	
	
	/** save a genome
	 */
	private void saveGenome (String fileName, String directory, Genome g) 
	{
		try {
			
			String stripped = (String)((GeneString.split(fileName,".")).elementAt(0));
			g.genomeName = stripped;	
			
			ObjectOutputStream out = 
				new ObjectOutputStream(
					new FileOutputStream(directory+fileName));
			
			out.writeObject(g);
			out.close();
			
		}
		catch (Exception e) {
			reportException("Cannot save file\n\n"+e.getMessage());
			e.printStackTrace();
		}
			
	}
	
}
