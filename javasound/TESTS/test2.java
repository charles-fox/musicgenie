//test

import javax.media.sound.midi.*;
import java.net.*;
import java.io.*;


public class test2 {



 	public static void main(String args[]) throws MalformedURLException, InvalidMidiDataException, IOException, MidiUnavailableException {

		//get a sequencer
		Sequencer sequencer = MidiSystem.getSequencer(null);




		//make an output stream to a file so we can read the results

		DataOutputStream out = new DataOutputStream(
									new BufferedOutputStream (
										new FileOutputStream("Data.txt")));


		out.writeBytes("hello test");


		//create a track
		Track trackZero = new Track();

		//make some new objects
		int NOTE_ON = 144;
		int NOTE_OFF = 128;

		ShortEvent se = new ShortEvent();
		se.setMessage(NOTE_ON, 42, 127, 300l);

		trackZero.add(se);


		//add track to sequence
		Sequence mySequence = new Sequence(0.0f, 384);
		mySequence.add(trackZero);



		for (int i=0; i<trackZero.size(); i++) {

			MidiEvent me = trackZero.get(i);

			out.writeBytes("\nmidievent no "+i);
			out.writeBytes("\nme: "+me.toString());
			if (me instanceof ShortEvent) {
				out.writeBytes("\ncommand: "+((ShortEvent)me).getCommand());
				out.writeBytes("\nchannel: "+((ShortEvent)me).getChannel());
				out.writeBytes("\nstatus: "+((ShortEvent)me).getStatus());
				out.writeBytes("\ntick: "+((ShortEvent)me).getTick());
				out.writeBytes("\ndata1: "+((ShortEvent)me).getData1());
				out.writeBytes("\ndata2: "+((ShortEvent)me).getData2());
			}
			else {
				out.writeBytes("REMOVE!\n");
				trackZero.remove(me);

			}

			out.writeBytes("\n");
		}

		//must now OPEN sequencer ??
		sequencer.open();

		//play the sequence
		sequencer.setSequence(mySequence);
		sequencer.start();

		//close the output file
		out.close();

	}





}
