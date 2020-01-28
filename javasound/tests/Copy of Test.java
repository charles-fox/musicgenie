//test

import javax.media.sound.midi.*;
import java.net.*;
import java.io.*;


public class Test {



 	public static void main(String args[]) throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {

		System.out.println("hello");

		//get a sequencer
		Sequencer sequencer = MidiSystem.getSequencer(null);

		MidiDevice.Info[] info = MidiSystem.getSequencerInfo();
		for (int i=0; i<info.length; i++) {
			p(""+info[i].toString());
		}

		sequencer.open();

		//make an output stream to a file so we can read the results
		DataOutputStream out = new DataOutputStream(
									new BufferedOutputStream (
										new FileOutputStream("Data.txt")));
		//get a sequence from my webpage
		URL myURL = new URL("http","thor.cam.ac.uk",80,"/~cwf22/scale.mid");
		Sequence mySequence = MidiSystem.getSequence(myURL);

		//print some info about the sequence
		out.writeBytes("div type: "+mySequence.getDivisionType()+"\n");
		out.writeBytes("res: "+mySequence.getResolution()+"\n");


		out.writeBytes("file format type:"+MidiSystem.getMidiFileFormat(myURL).getType()+"/n");

		out.writeBytes("\n\navailable formats:\n");
		for (int i=0; i<MidiSystem.getMidiFileTypes(mySequence).length; i++) {

			out.writeBytes(""+MidiSystem.getMidiFileTypes(mySequence)[i]);
	}
		out.writeBytes("\n\n");

		//look at the tracks in the sequence
		Track[] tracks = mySequence.getTracks();
		//look at the zeroth track
		Track trackZero = tracks[0];


		ShortEvent se = new ShortEvent();
		se.setMessage(144, 41, 127, 300l);
		trackZero.add(se);





		System.out.println("size before removing: "+trackZero.size()+"\n");
		//strip out the meta-events
		for (int i=0; i<trackZero.size(); i++) {
			MidiEvent me = trackZero.get(i);
			if (me instanceof ShortEvent) {;}
			else {
				out.writeBytes("REMOVING "+i+"\n");
				trackZero.remove(me);
			}
		}
		System.out.println("size after removing: "+trackZero.size()+"\n");


		//now write the midi events

		for (int i=0; i<trackZero.size(); i++) {
			MidiEvent me = trackZero.get(i);
			if (me instanceof ShortEvent) {
				out.writeBytes("\ncommand: "+((ShortEvent)me).getCommand());
				out.writeBytes("\nchannel: "+((ShortEvent)me).getChannel());

				out.writeBytes("\ndata1: "+((ShortEvent)me).getData1());
				out.writeBytes("\ndata2: "+((ShortEvent)me).getData2());
				out.writeBytes("\ntick: "+((ShortEvent)me).getTick()+"\n");
		}
		else {
			out.writeBytes("non-midi event found: "+me+" "+i+"\n");
			out.writeBytes("data:"+((MetaEvent)me).getData());
		}
		}




		Track newTrack = new Track();

		//set voice
		ShortEvent ve = new ShortEvent();
		ve.setMessage(192, 58, 0, 100l);
		newTrack.add(ve);

		//note 1
		ShortEvent se3 = new ShortEvent();
		se3.setMessage(144, 41, 127, 200l);
		newTrack.add(se3);

		ShortEvent se4 = new ShortEvent();
		se4.setMessage(128, 41, 127, 800l);
		newTrack.add(se4);


		//note 2
		ShortEvent se2 = new ShortEvent();
		se2.setMessage(144, 48, 127, 800l);
		newTrack.add(se2);

		ShortEvent se5 = new ShortEvent();
		se5.setMessage(128, 48, 127, 1600l);
		newTrack.add(se5);





		newTrack.add(se);


		Sequence newSequence = new Sequence(Sequence.PPQ, 384);
		p("PPQ: "+Sequence.PPQ);
		newSequence.add(newTrack);

		//play the sequence
		sequencer.setSequence(newSequence);
		sequencer.start();

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
