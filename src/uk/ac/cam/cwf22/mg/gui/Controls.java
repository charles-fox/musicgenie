package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import uk.ac.cam.cwf22.mg.graphics.*;
import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.midi.*;


public class Controls extends Panel implements TextWriter {
	
	TextArea genomeInput;
	TextArea out;
	
	//to choose which level of tree to work on
	Choice level;
	
	Display2D scoreDisplay;
	TreeGraphics theTreeGraphics;
	
	HandleButton theHandler;
	Manager theManager;
	GenomeWindow theGenomeWindow;
	
	//Handle on the genome we are editing.
	public Genome myGenome;
	public Tree theTree;
	public Score theScore;
	
	public int[] voices;
	public Color color;
	
	public TextField genomeNameBox;
	
	
	//CONSTRUCTOR
	public Controls(TreeGraphics theTreeGraphics, 
					Display2D scoreDisplay, 
					Genome myGenome,
					Manager theManager,
					GenomeWindow theGenomeWindow) {
		
		this.theTreeGraphics = theTreeGraphics;
		this.scoreDisplay = scoreDisplay;
		this.myGenome = myGenome;
		this.theManager = theManager;
		this.theGenomeWindow = theGenomeWindow;
		
		this.voices = myGenome.voices;
		this.color = myGenome.color;
		
		setLayout(new BorderLayout(10,10));
		
		//create the input box,
		//with a sampel genome in it
		// (maybe randomise this later?)
		String genomeText = myGenome.toString();
		genomeInput = new TextArea(genomeText, 30, 5, TextArea.SCROLLBARS_BOTH);
		genomeInput.setEditable(true);
		genomeInput.setBackground(Color.white);
		add("North", genomeInput);
		
		//now add the buttons
		Panel buttonPanel = new Panel();
		add("West", buttonPanel);
		
		buttonPanel.setLayout(new GridLayout(0, 2,10,10));
		
		Button compileButton = new Button("Compile");
		buttonPanel.add(compileButton);
		//handled by an external class
		
		Button mutateButton = new Button("Mutate");
		buttonPanel.add(mutateButton);
		mutateButton.addActionListener(new MutateListen(this));
		
		Button playButton = new Button("Play");
		buttonPanel.add(playButton);
		playButton.addActionListener(new PlayListen(this));

		Button stopButton = new Button("Stop play");
		buttonPanel.add(stopButton);
		stopButton.addActionListener(new StopListen(this));

			
		level = new Choice();
		buttonPanel.add(level);
		level.addItemListener(new levelListen(this));

		
		genomeNameBox = new TextField(myGenome.genomeName, 15);
		genomeNameBox.setSize(30,30);
		buttonPanel.add(genomeNameBox);
		genomeNameBox.setBackground(myGenome.color);
		
		//initialise handler - to respond to me, and to the compile button
		theHandler = new HandleButton(this, "Compile!");
		
		//attatch handler to go button
		compileButton.addActionListener(theHandler);
		
		//add the output window
		//out = new TextArea("S.A.R.A.H. compiler v0.00\nStructured Audio with Random Additions Hiarachies\nCharles Fox 1999-2000\n\nReady>\n\n", 10, 20, TextArea.SCROLLBARS_BOTH);
		//add("Center", out);
		
		//set the background color
		setBackground(Color.lightGray);
		
	}
	
	//====================COMPILATION USE CASES=========================
	
	
	/**
	 *  This is the behaviour for pressing the 'compile' button.
	 *  It will get the text from all the genes
	 *  and call the compiler, draw the tree, collect the notes etc.
	 *  returns true if sucessful
	 * 
	 *  this version looks at the seleect level and builds from there down.
	 *  NB the whole GENOME is still built, but the TREE and SCORE are cut down
	 */
	public void compileLevel() 
	{
		//build myGenome from the string input (and other data)
		if (buildGenome()) 
		{
			int depth = getDepth();
			
			//build score (and tree)
			buildScore(depth);
			
			//redraw everything
			updateDisplays();
			
		}
	}
	
	/** when window is first opened
	 */
	public void compile() 
	{
		//build myGenome from the string input (and other data)
		if (buildGenome()) 
		{
			//build score (and tree)
			buildScore();
			
			//redraw everything
			updateDisplays();
			
		}
	}
	
	/** compilation from file
	 */
	public void compileFromFile() 
	{
		color = myGenome.color;
		voices = myGenome.voices;
		
		buildScore();
		updateDisplays();
	}
	
	/** version fo compile for when editing finishes
	 */
	public void compileOnClose() {
	
		buildGenome();
			
		theManager.genomes.setElementAt(myGenome, 
										theGenomeWindow.genomePosition);	
		theManager.refresh();
	}
			
	//=============================================
	
	
	
	/** looks at the string window (and other display info) 
	 *  and builds the genome from it.
	 * 
	 *  doesn't change any screen displays! 
	 *  returns true if sucessful 
	 * */
	public boolean buildGenome() 
	{
		Vector nullVector = new Vector();
		Rational startTime = new Rational(0,1);
	
		//read in the genomeText from the input box
		String theInput = genomeInput.getText();
		
		try {
			//make the genome from the text input
			GenomeString gs = new GenomeString(theInput);
			
			myGenome = gs.makeGenome();
			
			//set view values to genome
			myGenome.genomeName = genomeNameBox.getText();
			myGenome.color = color;
			myGenome.voices = voices;
			
			
			return true;
			
			
		}
		
		catch (UserException e) {
			reportUserError("Error in genome", e.userReport);
			return false;
		}
	}
	
	
	//=============================================
	
	
	//* behaviour for play button */
	public void play() 
	{
		theManager.setVoices(voices);
		theManager.playScore(theScore);
	}
	
	//* behaviour for stop button */
	public void stop() 
	{
		theManager.stopPlay();
	}
	
	
	/** ask manager to save MIDI file
	 */
	public void saveMIDI(String fileName, String directory) {
		theManager.saveMIDI(theScore, fileName, directory);
	}
		
	
	/** using the current genome, 
	 *  builds the score and tree DEPRECATED
	 **/
	public void buildScore() {
		
	WaitBox status = new WaitBox("Compiling", "Building tree");
	status.show();	
		
		try {
			
			//TODO: Timing info
			long start = System.currentTimeMillis();
			
			//remake score and tree
			theTree = myGenome.getTree();
			
		
			status.setMessage("Generating score");
						
			theScore = theTree.getScore();
						
			status.dispose();
			
			
			long time = System.currentTimeMillis() - start;
			System.out.println("\nTree depth  "+theTree.getHeight());
			System.out.println("No of notes "+theScore.size());
			System.out.println("Length s "+(theTree.getLength().getDouble()/4));
			System.out.println("Compile time: "+time);
			
			
			}
		catch (BadGenomeException er) 
		{
			status.dispose();
			reportUserError("Error in mutated genome", er.userReport);
			write("error in mutation");
		}
	}
	
	
	/** using the current genome, builds the score and tree, 
	 *  with a specified top level rather than the whole genome 
	 */
	public void buildScore(int depth) {
		
		//make a new genome with max height as given
		Genome ng = (Genome)(myGenome.clone());
		
		
		//remove genes greater than height
		for (int i=depth+1; i<myGenome.genes.size(); i++) 
		{	
			ng.genes.removeElementAt(depth+1);	
		}
		
		//remove names greater than height
		ng.names = new String[depth+1];
		for (int i=0; i<depth+1; i++) {
			ng.names[i] = myGenome.names[i];
		}
		
		
		WaitBox status = new WaitBox("Compiling", "Building tree");
		status.show();	
		
		try {
			
			//TODO: Timing info
			long start = System.currentTimeMillis();
			
			//remake score and tree
			theTree = ng.getTree();
			status.setMessage("Generating score");
			theScore = ng.getScore();
			status.dispose();
			
			long time = System.currentTimeMillis() - start;
			System.out.println("Tree depth  "+theTree.getHeight());
			System.out.println("No of notes "+theScore.size());
			System.out.println("No of notes "+theTree.getLength());
			System.out.println("Compile time: "+time);
			
			}
		catch (BadGenomeException er) {
			reportUserError("Error in reduced genome", er.userReport);
		}
	}
	
	/** updates the choice box for current working level
	 */
	public void updateLevels() {
		level.removeAll();
		
		for (int i=1; i<myGenome.names.length; i++) {
			level.add(myGenome.names[i]);
		}
		//select lowest one
		level.select(myGenome.names.length-2);
	}
	
	/** returns number of spaces FROM BOTTOM of list (0=bottom)
	 */
	private int getDepth() {
		int index = level.getSelectedIndex() + 1; //add one since there is invisiuible NOTE before zeroth gene
		//int depth = myGenome.genes.size() - index;
		
		return index;
	}
	
	/** updates color of genome
	 */
	public void updateColor(Color c) {
		genomeNameBox.setBackground(c);
		myGenome.color = c;
	}
	
	/** Updates the score, genomeString and tree displays */
	public void updateDisplays() {
		
		long startTime = System.currentTimeMillis();
		
		
		//update the genomeString
		updateText();
		
		//update level selection box
		updateLevels();
		
		// make a vector of 3D branches in the tree, and draw it
		Point3D start = new Point3D(0, 
									(double)theTree.getHeight(), 
									0);
		
		Vector objects3D = theTree.makeObjects3D(start, 
												 myGenome.names, 
												 true,
												 0,
												 theManager.treeDetail);
		
		Rational lengthOfPiece = theTree.getLength();
		
		VirtualRectangle newTreeView = new VirtualRectangle(0, //-5f*lengthOfPiece.getDouble(),
															-20f*theTree.getHeight(),
															20f*lengthOfPiece.getDouble(),
															50f*theTree.getHeight());
		theTreeGraphics.setView(newTreeView);
		
		theTreeGraphics.setObjects(objects3D);
		//theTreeGraphics.rotateAboutY(0.2);
		theTreeGraphics.repaint();
				
		//draw the score onto the scoreDisplay...
		Vector scoreObjects = new Vector();
		
		// for each note n in the score, create new StaffNote2D from it
		// and add it to the vector
		for (int i=0; i<theScore.size(); i++) {
			Note n = (Note)theScore.elementAt(i);
			StaffNote2D sn = new StaffNote2D(n);
			scoreObjects.addElement(sn);
		}
				
		//now add the staff
		Object2D theStaff = new Staff(lengthOfPiece.getDouble());
		scoreObjects.addElement(theStaff);
		
		//scale the score view to accomadate the whole length
		VirtualRectangle newView = new VirtualRectangle(0,-20, lengthOfPiece.getDouble(), 40);
		scoreDisplay.setView(newView);
		
		//draw the score
		scoreDisplay.setObjects(scoreObjects);
		scoreDisplay.repaint();
		
		System.out.println("Display time: "+(System.currentTimeMillis()-startTime));
		
	}
	
	
	/** update the genome string displays */
	public void updateText() 
	{
		genomeInput.setText(myGenome.toString());
		genomeNameBox.setBackground(myGenome.color);
		genomeNameBox.setText(myGenome.genomeName);
			
		theGenomeWindow.setTitle(myGenome.genomeName+GenomeWindow.title);
	}
	
	
	/** listen for Play button */
	class PlayListen implements ActionListener {
		Controls c;
		public PlayListen(Controls c) {this.c=c;}
		
		public void actionPerformed(ActionEvent e) {
			//reportUserError();
			c.play();
		}
	}
	
	/** listen for Stop button */
	class StopListen implements ActionListener {
		Controls c;
		public StopListen(Controls c) {this.c=c;}
		
		public void actionPerformed(ActionEvent e) {
			c.stop();
		}
	}
	
	
	/** listen for Mutate button */
	class MutateListen implements ActionListener {
		Controls c;
		public MutateListen(Controls c) {this.c=c;}
		
		public void actionPerformed(ActionEvent e) {
			//mutate the genome
			myGenome = myGenome.mutate();
						
			buildScore();
			updateDisplays();
			
			write("mutated\n");
		}
	}
	
	/** listen for user selecting different tree heights
	 */
	class levelListen implements ItemListener {
		Controls c;
		public levelListen(Controls c) {this.c=c;}
		
		/** find height of selection 
		 *  and recompile at that height
		 */
		public void itemStateChanged(ItemEvent e) 
		{
			int height = level.getSelectedIndex();
			
			c.compileLevel();
		}
	}
	
	//a little wrapper to save checking all the exceptions
	public void write(String s) {
		System.out.println(s);
		/*
		try {
			out.append(s);
		}
		catch (NullPointerException e) {
			System.out.println("Error - Textout not initialised");
		}*/
	}
		
	/** displays error box on screen
	 */
	public void reportUserError(String title, String message) {
		ErrorBox eb = new ErrorBox(title, 
								   message, 
								   theGenomeWindow
								   );
	}


	
}
		   
