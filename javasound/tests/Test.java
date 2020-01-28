//sudo apt install fludsynth
//sudo apt install fluid-soundfont-gm
//fluidsynth --audio-driver=alsa -o audio.alsa.device=hw:1 /usr/share/sounds/sf2/FluidR3_GM.sf2 mid.mid

import javax.sound.midi.*;
import java.net.*;
import java.io.*;

public class Test {

 	public static void main(String args[]) throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {

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

		File file = new File("out.mid");
		int type = 1;
		MidiSystem.write(mySeq, type, file);




		//play the sequence  TODO 2020 not working
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

	private static void p(String s) {
		System.out.println(s);
	}
}
