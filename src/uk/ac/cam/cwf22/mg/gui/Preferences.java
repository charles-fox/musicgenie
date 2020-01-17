package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;

public class Preferences extends Dialog
{
	
	Checkbox voiceMutate = new Checkbox("Mutate MIDI voices",true);
	TextField mut = new TextField();
	TextField splice = new TextField();
	TextField tree = new TextField();
	
	Button about = new Button("About...");
	
	Manager theManager;
	
	public Preferences(Frame owner, Manager theManager, int x, int y) 
	
	{
		super(owner, "Preferences", true);
		this.theManager = theManager;
		
		//set size and panels
		setSize(220,220);
		setLocation(x,y);
		Panel p = new Panel(); //new GridLayout(3,1));
		add("Center", p);
		p.setBackground(Color.lightGray);
		
		//add mutation amount
		Panel m = new Panel();
		m.add(new Label("No of mutations per child"));
		mut.setText(""+theManager.generationMutations);
		m.add(mut);
		p.add(m);
		
		//add splice amount
		Panel m2 = new Panel();
		m2.add(new Label("Max gene splice length"));
		splice.setText(""+theManager.maxSpliceLength);
		m2.add(splice);
		p.add(m2);
		
		//add tree detail amount
		Panel m3 = new Panel();
		m3.add(new Label("Detail on tree graphics"));
		tree.setText(""+theManager.maxSpliceLength);
		m3.add(tree);
		p.add(m3);
		
		//voice mutation option
		p.add(voiceMutate);
		voiceMutate.setState(theManager.MUTATE_VOICES);
		
		
		
		p.add(new Label("Version "+theManager.versionInfo));
		about.addActionListener(new AboutListen(owner));
		p.add(about);
		about.setSize(20,10);
		
		
		
		Button OK = new Button("OK");
		OK.addActionListener(new OKListen());
		add("South", OK);
		
		show();
	}
		
		
	/** class to listen for OK */
	class OKListen implements ActionListener {
		
		public void actionPerformed(ActionEvent e) 
		{
			theManager.MUTATE_VOICES = voiceMutate.getState();
				
			theManager.generationMutations = (new Integer(mut.getText())).intValue();
			theManager.maxSpliceLength = (new Integer(splice.getText())).intValue();
			theManager.treeDetail = (new Integer(tree.getText())).intValue();
			
			
			dispose();
		}
	}
	
	
	/** class to listen for OK */
	class AboutListen implements ActionListener {
		private Frame f;
		public AboutListen(Frame f) {this.f=f;}
		public void actionPerformed(ActionEvent e) 
		{
			AboutBox a = new AboutBox(f, theManager);
		}
	}
	
}
