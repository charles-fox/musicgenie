//test

import javax.sound.midi.*;
import java.net.*;
import java.io.*;


public class Test {



 	public static void main(String args[]) throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {



		System.out.println("hello");

		//get a sequencer
		Sequencer sequencer = MidiSystem.getSequencer();

		sequencer.open();

		//make an output stream to a file so we can read the results
		DataOutputStream out = new DataOutputStream(
									new BufferedOutputStream (
										new FileOutputStream("Data.txt")));


		Sequence mySeq = new Sequence(Sequence.PPQ, 384);

		Track trackZero = mySeq.createTrack();
		Track newTrack = mySeq.createTrack();


		//mySeq.add(trackZero);
		//mySeq.add(newTrack);

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





		//play the sequence
		sequencer.setSequence(mySeq);
		sequencer.start();

		File file = new File("D:/js/tests/mid.mid");

		/*MidiFileFormat format = new MidiFileFormat(
					1,
					  mySeq.getDivisionType(),
                      mySeq.getResolution(),
                      MidiFileFormat.UNKNOWN_LENGTH,
                      MidiFileFormat.UNKNOWN_LENGTH
                      );
*/
		int type = 1;
		MidiSystem.write(mySeq, type, file);

		//wait for playing
		for (int i=0; i<10000; i++) {p(""+i);}
		p("finished playing");

		//close the output file and midisystem
		out.close();
		sequencer.close();

		System.exit(1);

	}


	private static void p(String s) {
		System.out.println(s);
	}


}
