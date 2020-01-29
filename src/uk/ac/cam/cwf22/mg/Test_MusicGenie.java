package uk.ac.cam.cwf22.mg;

/**
 * TEST HARNESS
 */

import uk.ac.cam.cwf22.mg.core.*;
import uk.ac.cam.cwf22.mg.compiler.*;
import uk.ac.cam.cwf22.mg.gui.*;
import uk.ac.cam.cwf22.mg.graphics.*;
import uk.ac.cam.cwf22.mg.midi.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.sound.midi.*;
import java.net.*;
import java.io.*;


public class Test_MusicGenie
{
	/**
	 * The main entry point for the TEST application. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args) throws Exception
	{
		testProperties();
		testDegree();
		testNaming();
		testKeyRoll();
		//testAwt();
		//test3D();
		//testMidiFile();
		testScorePlayer();
		//Test_MusicGenie t = new Test_MusicGenie();
	}
	
	private static void testProperties() {
		Properties p = System.getProperties();
		p(""+p);
		String s = System.getProperty("user.dir");
		p(s);
	}
		
	private static void testDegree() {
		try {
			Degree d = new Degree("5bb#b");
			p(""+d);
		}
		catch (BadDegreeStringException e) {
			p("degree failed");
		}
	}

	private static void testNaming() 
	{
		Color testc = new Color(100,100,100);
		String[] names = {"a", "a0" , "a1", "a2","a3"};
		String test = "a";
		String r = Genome.findNewUniqueName(test, names);
		p(r);
	}
		
	private static void testKeyRoll() {
		Key k = new Key(5);
		p(k.toString());	
		k.rollMode(-26);
		p(k.toString());
	}

	public static void testAwt() {
		Frame f = new Frame("Test awt function");
		Button OK = new Button("Exit");	
		f.add(OK);  //TODO this is failing on 2020 openjdk9, why?
		f.setSize(200,150);
		f.setLocation(20,220);
		f.show();
	}

	private static void test3D() {
		Frame f = new Frame();
		f.show();
		TreeGraphics tg = new TreeGraphics(new VirtualRectangle(0,0,10,10), Color.white);
		f.add(tg);
		Vector ob3D = new Vector();
		for (double x = 0; x< 10; x++) {
			for (double y=0; y<10; y++) {
				for (double z=0; z<10; z++) 
				{	
					ob3D.addElement(new Leaf3D(new Point3D(x,y,x), Color.red));
				}
			}
		}
		tg.drawTree(ob3D);
	}
	
	

	/** wrapper to provide OK button at the end of the test
	 */
	public Test_MusicGenie() {
		Frame f = new Frame("Test awt class");
		Button OK = new Button("Exit");	
		f.add(OK);  //TODO this is failing on 2020 openjdk9, why?
		OK.addActionListener(new OKListen(f));
		f.setSize(200,150);
		f.setLocation(20,220);
		f.show();
	}

	class OKListen implements ActionListener {
		Frame f;
		public OKListen(Frame f) {this.f=f;}
		public void actionPerformed(ActionEvent e) {
			f.dispose();
			System.exit(7);
		}
	}



//sudo apt install fludsynth
//sudo apt install fluid-soundfont-gm
//fluidsynth --audio-driver=alsa -o audio.alsa.device=hw:1 /usr/share/sounds/sf2/FluidR3_GM.sf2 mid.mid


        public static void testMidiFile() throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {

                System.out.println("testing MIDI");

                Sequence mySeq = new Sequence(Sequence.PPQ, 384);
                Track trackZero = mySeq.createTrack();
                Track newTrack = mySeq.createTrack();
                //set voice
                ShortMessage ve = new ShortMessage();
                ve.setMessage(192, 58, 0);
                newTrack.add( new MidiEvent(ve, 0l) );
                //note 1 on
                ShortMessage se3 = new ShortMessage();
                se3.setMessage(144, 41, 127);
                newTrack.add( new MidiEvent(se3, 0l) );
                //note 1 off
                ShortMessage se4 = new ShortMessage();
                se4.setMessage(128, 41, 127);
                newTrack.add(new MidiEvent(se4, 900l));
                //set voice on other track
                ShortMessage vez = new ShortMessage();
                vez.setMessage(193, 4, 0);
                newTrack.add(new MidiEvent(vez, 0l));
                //note 2 on
                ShortMessage se2 = new ShortMessage();
                se2.setMessage(145, 48, 127);
                trackZero.add(new MidiEvent(se2, 800l));
                //note 2 off
                ShortMessage se5 = new ShortMessage();
                se5.setMessage(129, 48, 127);
                trackZero.add(new MidiEvent (se5, 1600l));

                File file = new File("testMidiFile.mid");
                int type = 1;
                MidiSystem.write(mySeq, type, file);




		//TODO 2020 not working, maybe JRE has no soundbank
                //https://stackoverflow.com/questions/380103/simple-java-midi-example-not-producing-any-sound

                //play the sequence  
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.setSequence(mySeq);
                sequencer.start();

                //wait for playing
                for (int i=0; i<100000; i++) {p(""+i);}
                p("finished playing");

                sequencer.close();
                System.exit(1);
	}



	public static void testScorePlayer() {
                try {
                        //make an instance of this class
                        ScorePlayer theScorePlayer = new ScorePlayer();

                        //initialise the voice list
                        theScorePlayer.setVoices();

                        //theScorePlayer.playDemo();
                        //instead of playDemo, lets make a MusicGenie score and play that

                        Score testScore = new Score();

                        Note n1 = new Note();
                        testScore.addElement(n1);
/*
                        Note n3 = new Note();
                        n3.voice = 2;
                        n3.time = new Rational(5,1);
                        testScore.addElement(n3);

                        Note n2 = new Note();
                        n2.degree = 5;
                        n2.sharp = 0;
                        n2.voice = 3;
                        n2.time = new Rational(3,1);
                        n2.duration = new Rational(3,5);
                        testScore.addElement(n2);
*/
                        theScorePlayer.playScore(testScore);
                        theScorePlayer.saveMIDI(testScore, "testScorePlayer.mid", "./");

                        //wait for playing
                        for (int i=0; i<500000; i++) {p(""+i);}
                        p("finished playing");
                        theScorePlayer.close();
                        System.exit(0);
                }
                catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                }
        }



	//=======================
	/** shorthand for system.out.println
	 */
	private static void p(String s) {
		System.out.println(s);
	}
}
