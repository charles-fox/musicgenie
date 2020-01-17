package uk.ac.cam.cwf22.mg.gui;

import java.awt.*;
import java.awt.event.*;
import uk.ac.cam.cwf22.mg.graphics.*;
import uk.ac.cam.cwf22.mg.core.*;

public class InstrumentWindow extends Dialog
{
	
	int[] voices;
	Controls c;
	
	TextField[] texts;
	
	public static int VOICE_MAX = 100;
	public static int VOICE_MIN = 0;
	
		
	/** CONSTRUCTOR
	 */
	public InstrumentWindow(Controls c) 
	{
		super(c.theGenomeWindow, "MIDI", true);
		
		this.c = c;
		this.voices = c.voices;
		
		texts= new TextField[voices.length];
		
		this.addWindowListener( new WindowAdapter() {
								   public void windowClosing(WindowEvent e) {
									dispose();
								   }
								}
							   );
	
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		
		Panel v = new Panel(new GridLayout(1+voices.length,2));
		
		v.add(new Label("voice"));
		v.add(new Label("inst"));
		
		//make the list of voice labels and boxes
		for (int i=0; i<voices.length; i++) 
		{
			Label l = new Label("voice "+(i+1));
			l.setBackground(StaffNote2D.getMyColor(i));
			v.add(l);
			texts[i] = new TextField(""+voices[i]);
			v.add(texts[i]);
		}
		
		add("Center", v);
		
		Button apply = new Button("Apply");
		add("South", apply);
		apply.addActionListener(new applyListener());
		
		this.setBackground(Color.lightGray);
		this.setLocation(200,100);
		this.setSize(100,400);
		this.show();
	}
	
	/** apply changes
	 */
	private void apply() {
		for (int i=0; i<voices.length; i++) 
		{
			int newVoice = new Integer(texts[i].getText()).intValue() - 1; //MIDI voices start from 1, not 0
			
			if (newVoice<VOICE_MAX && newVoice>VOICE_MIN) voices[i] = newVoice;
		}
		
		this.dispose();
	}
	/** inner class to listen for apply click
	 */
	class applyListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			apply();
		}
	}
	
	
}
