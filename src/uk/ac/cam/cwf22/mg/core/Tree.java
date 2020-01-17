/** The core musical tree
 * a very early version!
 * */

package uk.ac.cam.cwf22.mg.core;


import uk.ac.cam.cwf22.mg.graphics.*;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;

import java.awt.*;
import java.util.*;

public class Tree implements Cloneable
{
	//The list of transforms applied to this tree
	Vector myTransforms;
	
	//The gene corresponding to my type of tree
	Gene myGene;
	
	//type of phrase that the tree is
	int myId;
	
	//time at start of this tree
	Rational startTime;
	
	//the children.  Empty vector = leaf
	public Vector children;
	
	//0=melody, 1=harmony
	//(celver static var here later)
	int orientation;
	
	public Vector inversions;
	
	public static int MELODY=0, HARMONY=1;
		
	//detail for tree graphics
	public int MAX_GENERATIONS = 5;
	
	
	
	//=====================================
	//  CONSTRUCTOR
	//=====================================
	/**
	 * almost all the work is done by the constructor
	 * sort out my own transform list and vars
	 * then create my children
	 * by looking at the genome for their types and transforms
	 */
	public Tree(int myId,
				Vector myInheritedTransforms,
				Vector myNewTransforms,
				Genome theGenome,
				Rational startTime
			
				)
				
				throws BadGenomeException
		
	{
		this.myId = myId;
		
		children = new Vector();
		
		
		inversions = new Vector();
		
		//get instructions for making my children
		myGene = theGenome.getGene(myId);
		
		//find my own orientation from my gene
		this.orientation = myGene.orientation;
		
		//set my startTime from arguements
		this.startTime = (Rational)startTime.clone();
	
		//store the result of consing the transforms in mytransforms 
		myTransforms = consVectors(myInheritedTransforms, myNewTransforms);
		
		
		//=====================
		//create children
		//=====================
		
		//go through the transform list and see if I'm retrograde
		// ie. is there an odd number of retrograde transforms in my list?
		// if I am retrograde, then I just create my children backwards.
		boolean retrograde = this.isRetrograde();
		
		boolean isNewlyInverted = this.isInverted(myNewTransforms);
		
		int numberOfChildren = myGene.getLength();
		
		//var used to keep track of current child's startTime
		//(beginning at my own startTime for first child)
		Rational childStartTime = (Rational)startTime.clone();
		
		for (int i=0; i<numberOfChildren; i++) {
			
			//get pointer to the transformedPhrase describing this child from the gene
			// working forwards through my child list,
			// or backwards if I am a retrograde tree
			TransformedPhrase childTP;
			if (!retrograde) childTP = myGene.getTransformedPhrase(i);
			else			 childTP = myGene.getTransformedPhrase(numberOfChildren-1 - i);
			
			//the number of the child
			int childId = childTP.Id;
			
			//get the new transforms from the TP
			Vector childNewTransforms = childTP.transforms;
			
			Tree child = new Tree (childId, 
									(Vector)(myTransforms.clone()), 
									(Vector)(childNewTransforms.clone()), 
									theGenome,
									childStartTime									
									);
			
			children.addElement(child);
			
			//increment the next child's startTime by the duration of this child
			// - but only if I'm a melody tree.  Harmony trees keep the same start for every child
			if (orientation==MELODY) childStartTime = childStartTime.plus(child.getLength());
							
		}
		
		//if the NEW transforms include an invert, then find the first degree
		//then invert each child on it
		if (isInverted(myNewTransforms))
		{
			Degree d = this.getFirstDegree();
			this.invert(d);
		}
	}
	
	
	/** returns degree of first note in tree
	 */
	public Degree getFirstDegree() {
		if (leaf()) {
			Score s = this.getScore();
			Note n = (Note)(s.elementAt(0));
			Degree d = new Degree(n.degree, 0); //HACK: Note should contain Degree object anyway
			return d;
		}
		else {
			Tree firstChild = (Tree)(children.elementAt(0));
			return firstChild.getFirstDegree();
		}
	}
		
	/**inverts me on given degree - puts the degree in a vector for later
	 */
	public void invert(Degree d) {
		if (leaf())	inversions.addElement(d);
		else {
			for (int i=0; i<children.size(); i++) {
				Tree t = (Tree)(children.elementAt(i));
				t.invert(d);
			}
		}	
	}
		
	/** String representation of my transforms
	 */
	private String stringTransforms() {
		String result = "";
	
		for (int i=0; i<myTransforms.size(); i++) {
			result += (((Transform)(myTransforms.elementAt(i))).toString());
		}
		return result+"\n";
	}
	

	/** 
	 *  returns the time duration of this tree
	 */ 
	public Rational getLength() {
		Rational result = new Rational(1,1);
		
		//if leaf, work out length from transforms.
		//otherwise, sum the lengths of children
		if (leaf()) {
			//for each transform
			for (int i=0; i<myTransforms.size(); i++) {
				//if its a timestretch
				Transform currentTransform = (Transform)(myTransforms.elementAt(i));
				if (currentTransform instanceof TimeStretch) {
					//multiply result by stretch
					result.multiplyBy(((TimeStretch)currentTransform).stretchFactor);
				}
			}
		}
		// if not leaf, then recurse on children
		else {
			int numberOfChildren = children.size();
			Rational [] childLengths = new Rational [numberOfChildren];
			// build array of child durations
			for (int i=0; i<numberOfChildren; i++) {
				childLengths[i] = ((Tree)children.elementAt(i)).getLength();
			}
			// if harmony choose max
			if (orientation==HARMONY) {
				result = maxRational(childLengths);
			}
			// if melody sum them
			else {
				result = sumRational(childLengths);
			}
		}
		return result;
	}
	
	//--------------------------------------------
	//returns the maximum polyphony width of the tree
	//--------------------------------------------
	public int getWidth() {
		//if leaf, width is 1
		if (leaf()) {return 1;}
		//if not leaf...
		else {
			//make a vector of my childrens widths and fill it
			int[] childWidths = new int [children.size()];
			for (int i=0; i<childWidths.length; i++) {
				childWidths[i] = ((Tree)children.elementAt(i)).getWidth();
			}
			
			//if melody, width is the max width from my children
			if (orientation==MELODY) {
				int result = maxInt(childWidths);
				return result;
			}
			//if harmony, width is the sum of my children's widths
			else {
				int result = sumInt(childWidths);
				return result;
			}
		}
	}
	
	//--------------------------------------------
	//returns the number of generations in the tree
	//--------------------------------------------
	public int getHeight() {
		//if leaf, height is 0
		if (leaf()) {return 0;}
		//else height is the max height from my children
		else {
			int [] childHeights = new int [children.size()];
			for (int i=0; i<childHeights.length; i++) {
				childHeights[i] = ((Tree)children.elementAt(i)).getHeight();
			}
			int result = maxInt(childHeights) + 1; //adding 1 for my own height
			return result;
		}
	}
	
	
	/**
	 * returns a score of all the notes in the tree leaves
	 * (eventually a time-ordered score)
	 */
	public Score getScore() {
		
		Score result = new Score();
		
		// if leaf
		// result is a vector containing the note in the tree
		if (leaf()) 
		{	
			//create note at my startTime, with default other values
			Note myNote = new Note(startTime);
			
			//apply transforms FROM RIGHT TO LEFT!
			// (a la lambda calculus)
			for (int transform=myTransforms.size()-1; transform >= 0; transform--)
			{
				((Transform)myTransforms.elementAt(transform)).apply(myNote);
			}
			result.addElement(myNote);
			
			//now apply inversions
			//(ignore accidentals on pivot note)
			for (int i=0; i<inversions.size(); i++)
			{
				Degree pivot = (Degree)(inversions.elementAt(i));
				
				//find dist from degree
				int distance = myNote.degree - pivot.degree;
				//and change my note appropriately
				myNote.degree -= 2*distance;
			}
			
		}
		// if node
		// result is a vector made by consing child getScore vectors
		else 
		{
			//for each child, cons its score onto the result
			for (int i=0; i<children.size(); i++) {
				Tree child = (Tree)children.elementAt(i);
				Score childScore = child.getScore();
				
				result.cons(childScore);
			}
			//then sort the result?
			// result.timeSort();
		}
		return result;
	}
	
	
	public String toString() {
		String result;
		if (leaf()) {
			result ="LEAF\n";
			return result;
		}
		else {
			result ="TREE ID "+myId+" ori ";
			result += (orientation==MELODY)? "MELODY" : "HARMONY";
					
			result += " time "+startTime+"\n";
			
			result += stringTransforms();
			result += "children: <\n";
			
			for (int i=0; i<children.size(); i++) {
				result += ((Tree)children.elementAt(i)).toString();
			}
			result += "END of tree "+myId+">\n";
		}
		return result;
	}

	//-------------------------------------------------------
	// Makes a vector of branches, leaves and textlabels etc
	// (which can draw themselves on a graphics context)
	//-------------------------------------------------------
	public Vector makeObjects3D(Point3D start, 
								String[] names, 
								boolean realNames,
								int generation,
								int maxGenerations) 
	{
		
		Vector result = new Vector();
		
		//find the voice and color of this tree section
		int currentVoice = getVoice();
		Color c = StaffNote2D.getMyColor(currentVoice);
		
		//if past resolution, return empty vector
		if (generation > maxGenerations) {
			return result;
		}
		
		//if leaf, return a Leaf3D at the start location
		if (leaf()) {
			Leaf3D leaf = new Leaf3D(start, c);
			result.addElement(leaf);
			
			return result;
		}
		
		//otherwise, make the branches, and recurse on children
		else {
			//a text label for the Id
			String myLabel = (realNames)? names[myId] : ""+myId;
			
			Object3D textLabel = new Label3D(start, myLabel);
			result.addElement(textLabel);
			
			// if melody, make a x-axis branch, with child branches
			if (orientation==MELODY) {
				
				double length = getLength().getDouble();
				//this chops of the end of the branch after the final child
				// to make it look nicer:
				length -= ((Tree)children.lastElement()).getLength().getDouble();
				
				
				//make HORIZONTAL parent branch
				Point3D end = (Point3D)start.clone();
				end.x += length;	
				Object3D xBranch = new Branch3D(start, end, c);
				
				//add it to the result vector (currently empty)
				result.addElement(xBranch);
				
				//for each child, make a branch grow down to it,
				//and add its own tree to my result vector
				
				double currentX = start.x;
				
				for (int i=0; i<children.size(); i++) {
					
					Tree child = (Tree)children.elementAt(i);
					
					//copy the start point, and move it along the x axis
					Point3D childStart = (Point3D)start.clone();
					childStart.x = currentX;
					
					// this branch grows downwards, from myHeight to childHeight
					Point3D childEnd = (Point3D)childStart.clone();
					double heightOfThisBranch = (double)(this.getHeight() - child.getHeight());
					childEnd.y -= heightOfThisBranch;
					
					//make the vertical branch and add it to the vector
					Object3D yBranch = new Branch3D(childStart, childEnd, c);
					result.addElement(yBranch);					
					
					//now make the rest of the tree for this child, using the end of the 
					//vertical branch as its starting point
				
					Vector childTree = child.makeObjects3D(childEnd, 
														   names, 
														   realNames,
														   generation+1,
														   maxGenerations);
					
					//cons the child objects onto the result
					result = consVectors(result, childTree);
					
					//now move along to the next child and repeat
					currentX += child.getLength().getDouble();
				}
			}
			//if HARMONY, make Z-axis branch with child branches
			else {
				double width = (double)getWidth();
				
				//this chops of the end of the branch after the final child
				// to make it look nicer:
				width -= (double)((Tree)children.lastElement()).getWidth();
				
				
				//make HORIZONTAL parent branch
				Point3D end = (Point3D)start.clone();
				end.z += width;	
				Object3D zBranch = new Branch3D(start, end, c);
				
				//add it to the result vector (currently empty)
				result.addElement(zBranch);
								
				//for each child, make a branch grow down to it,
				//and add its own tree to my result vector
				
				double currentZ = start.z;
				
				for (int i=0; i<children.size(); i++) {
					
					Tree child = (Tree)children.elementAt(i);
					
					//copy the start point, and move it along the z axis
					Point3D childStart = (Point3D)start.clone();
					childStart.z = currentZ;
					
					// this branch grows downwards, from myHeight to childHeight
					Point3D childEnd = (Point3D)childStart.clone();
					double heightOfThisBranch = (double)(this.getHeight() - child.getHeight());
					childEnd.y -= heightOfThisBranch;
					
					//make the vertical branch and add it to the vector
					Object3D yBranch = new Branch3D(childStart, childEnd, c);
					result.addElement(yBranch);	
					
					//now make the rest of the tree for this child, using the end of the 
					//vertical branch as its starting point
				
					Vector childTree = child.makeObjects3D(childEnd, 
														   names, 
														   realNames,
														   generation+1,
														   maxGenerations);
					
					//cons the child objects onto the result
					result = consVectors(result, childTree);
					
					//now move along to the next child and repeat
					currentZ += (double)child.getWidth();
				}
			}
			return result;	
		}
	}
	
	//===================================
	// SUPPORT FUNCTIONS
	//===================================
	// (maybe move some of these into the classes they operate on?)
	
	/**is this tree a leaf or not? */
	private boolean leaf() {
		return (children.size() == 0);
	}
	
	/** appends b to a and RETURNS the result
	 * has no external effects
	 */
	private Vector consVectors(Vector a, Vector b) {
		
		Vector result = new Vector();
		
		for (int i=0; i<a.size(); i++) {
			result.addElement(a.elementAt(i));
		}
		for (int i=0; i<b.size(); i++) {
			result.addElement(b.elementAt(i));
		}
		return result;
	}
	
	//Returns the max of an array of Rationals
	// (should we clone the result?)
	private Rational maxRational(Rational[] rs) {
		Rational result = (Rational)rs[0].clone();
		for (int i=0; i<rs.length; i++) {
			if (rs[i].isGreaterThan(result)) result = rs[i];
		}
		return (Rational)result.clone();
	}
	
	//Sums an array of rationals and returns the result
	// (should we clone the result?)
	private Rational sumRational(Rational [] rs) {
		Rational result = (Rational)rs[0].clone();
		for (int i=1; i<rs.length; i++) {
			result = result.plus(rs[i]);
		}
		return (Rational)result.clone();
	}
	
	/**Returns the max of an array of Ints
	 * (should we clone the result?)
	 */
	private int maxInt(int[] rs) {
		int result = rs[0];
		for (int i=0; i<rs.length; i++) {
			if (rs[i] > result) result = rs[i];
		}
		return result;
	}
	
	/**Sums an array of ints and returns the result
	 * (should we clone the result?)
	 */
	private int sumInt(int [] rs) {
		int result = rs[0];
		for (int i=1; i<rs.length; i++) {
			result = result + rs[i];
		}
		return result;
	}
	
	/** Goes though the current voice transforms and finds
	 * the 'working voice' at this point of the tree
	 */
	private int getVoice() {
		// this is clever :)
		// make a temporary note, and apply this tree's transforms
		// then read off the note's voice
		// (that way, if new transforms are added, we won't have to change this code)
		Note temp = new Note();
		
		for (int transform=myTransforms.size()-1; transform >= 0; transform--)
		{
			Transform current = ((Transform)myTransforms.elementAt(transform));
			current.apply(temp);
		}
		return temp.voice;
	}
	
	/** Is there an odd number of retrograde transforms in my list?
	 */
	private boolean isRetrograde() {
		
		boolean retro = false;
		
		for (int transform=myTransforms.size()-1; transform >= 0; transform--)
		{
			Transform current = ((Transform)myTransforms.elementAt(transform));
			if (current instanceof Retrograde) retro = !retro;
		}
		return retro;
	}
	
	/** true if odd no if inverts in vector
	 */
	private boolean isInverted(Vector newTs) {
		boolean result = false;	
		for (int transform=newTs.size()-1; transform >= 0; transform--)
		{
			Transform current = ((Transform)newTs.elementAt(transform));
			if (current instanceof Inversion) result = !result;
		}
		return result;
	}
	
}
