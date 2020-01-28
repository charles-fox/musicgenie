//test

import javax.media.sound.midi.*;
import java.net.*;
import java.io.*;


public class test {



 	public static void main(String args[]) throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {

		//get a sequencer
		Sequencer sequencer = MidiSystem.getSequencer(null);
		sequencer.open();



		Track newTrack = new Track();

		//set voice
		ShortEvent ve = new ShortEvent();
		ve.setMessage(192, 58, 0, 100l);
		newTrack.add(ve);

		//note 1
		ShortEvent se3 = new ShortEvent();
		se3.setMessage(144, 41, 90, 200l);
		newTrack.add(se3);

		ShortEvent se4 = new ShortEvent();
		se4.setMessage(128, 41, 90, 3000l);
		newTrack.add(se4);


		//note 2
		ShortEvent se2 = new ShortEvent();
		se2.setMessage(144, 48, 110, 800l);
		newTrack.add(se2);

		ShortEvent se5 = new ShortEvent();
		se5.setMessage(128, 48, 110, 3000l);
		newTrack.add(se5);


		//note 2
		ShortEvent se6 = new ShortEvent();
		se6.setMessage(144, 53, 127, 1600l);
		newTrack.add(se6);

		ShortEvent se7 = new ShortEvent();
		se7.setMessage(128, 53, 127, 3000l);
		newTrack.add(se7);






		Sequence newSequence = new Sequence(Sequence.PPQ, 384);

		newSequence.add(newTrack);

		//play the sequence
		sequencer.setSequence(newSequence);
		sequencer.start();

		//wait for playing
		for (int i=0; i<10000; i++) {p(""+i);}
		p("finished playing");

		//close the output file and midisystem
		sequencer.close();

		System.exit(1);
	}


	private static void p(String s) {
		System.out.println(s);
	}


}
