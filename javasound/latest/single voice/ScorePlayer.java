package uk.ac.cam.cwf22.mg.midi;

import javax.media.sound.midi.*;
import java.io.*;
import uk.ac.cam.cwf22.mg.core.Score;
import uk.ac.cam.cwf22.mg.core.Note;
import uk.ac.cam.cwf22.mg.core.Rational;


/** the ScorePlayer is a singleton.  Once created, you can pass it Scores to play them */

//NB - we're only using a single track with 16 channels at the moment

public class ScorePlayer {

	private Sequence sequence;

	private Sequencer sequencer;

	//list of 16 MIDI voices - one for each channel
	private int[] voices;

	//to scale between MusicGenie beats and MIDI ticks
	public long timeScaleFactor = 200l;
	//MusicGenie pitches have 0 as middle C, so shift appropriately
	public int  pitchShift = 50;


	//CONSTRUCTOR
 	public ScorePlayer() throws MidiUnavailableException, InvalidMidiDataException {
		//get a sequencer
		sequencer = MidiSystem.getSequencer(null);
		sequencer.open();

		setVoices();
	}


	/** makes a demo sequence and plays it */
	public void playDemo() throws MidiUnavailableException, InvalidMidiDataException {

		sequence = new Sequence(Sequence.PPQ, 384);

		//make a test track and add it
		Track trackZero = new Track();
		sequence.add(trackZero);

		addVoices(sequence);

		//notes
		for (int i=0; i<50; i++) {
			addNote( 1+i%16 , 41+i, 127, 10l, i*50l, sequence);
		}

		play();
	}


	// takes a MusicGenie Score and turns it into internal playable form
	// ready to be played
	public void playScore(Score s) throws InvalidMidiDataException, MidiUnavailableException {
		sequence = makeSequence(s);
		play();
	}


	//makes a sequnce from a score
	private Sequence makeSequence(Score s) throws InvalidMidiDataException {

		//set up a blank track
		Sequence newSeq = new Sequence(Sequence.PPQ, 384);
		Track trackZero = new Track();
		newSeq.add(trackZero);

		addVoices(newSeq);

		//for each note, add it to the sequence
		for (int i=0; i<s.size(); i++) {
			Note n = (Note)(s.elementAt(i));
			addNote(n, newSeq);
		}

		return newSeq;
	}


	/** save a MIDI file of the given score */
	public void saveMIDI(	Score score,
							String fileName,
							String directory) throws InvalidMidiDataException, IOException
	{

		Sequence saveSeq = makeSequence(score);

		int type 			= 0;
		float divisionType 	= sequence.getDivisionType();
		int resolution 		= sequence.getResolution();
		int length 			= MidiFileFormat.UNKNOWN_LENGTH;
		long duration 		= sequence.getDuration();

		MidiFileFormat format = new  MidiFileFormat(type,
		                      						divisionType,
		                     						resolution,
		                      						length,
		                      						duration);

		File file = new File(directory, fileName);

		MidiSystem.write(	saveSeq,
							format,
                    		file);
	}


	/** sets MIDI voices for each channel */
	public void setVoices(int[] voices) {
		this.voices = voices;
	}

	/** sets a particular to channel to a given voice */
	public void setVoices(int channel, int voice) {
		voices[channel] = voice;
	}

	/** sets voices to default values */
	public void setVoices() {
		voices = new int [] { 13, 57, 24, 60,
		 					  1, 2, 6, 73,
		 					  74, 71, 72, 69,
		 		 			  34, 35, 45, 57};
	}


	//============INTERNAL METHODS======================



	private void addNote(Note n, Sequence s) throws InvalidMidiDataException{

		int channel = 1 + (n.voice)%16;
		int pitch = n.pitch + pitchShift;
		int velocity = (int)(n.amp.getFloat() * 127.0f);
		long duration = n.duration.getScaledLong(timeScaleFactor);
		long time = n.time.getScaledLong(timeScaleFactor);

		addNote( channel, pitch,  velocity,  duration,  time, s);
	}


	/** adds all the voice-channel assignments to the beginning of the sequence */
	private void addVoices(Sequence s) throws InvalidMidiDataException {
		for (int i=1; i<17; i++) {
			addVoiceChange(i, voices[i-1], 100l, s);
		}
	}

	//play the sequence
	private void play() throws MidiUnavailableException, InvalidMidiDataException {
		sequencer.setSequence(sequence);
		sequencer.start();
	}

	//===============UGLY LOW LEVEL STUFF ================

	/** adds note on and off events, based on a slightly higher-level representation */
	// duration and time are in MIDI ticks
	// currently just adds to track0
	// channel goes from 1 to 16  (no zero!)
	private void addNote(int channel, int pitch, int velocity, long duration, long time, Sequence s) throws InvalidMidiDataException {

		//add a Note On event
		ShortEvent on = new ShortEvent();
		on.setMessage(144-1+channel, pitch, velocity, time);
		s.getTracks()[0].add(on);

		//add a Note Off event
		ShortEvent off = new ShortEvent();
		off.setMessage(128-1+channel, pitch, velocity, time+duration);
		s.getTracks()[0].add(off);
	}

	/**adds a single voice change MIDI event at the specified time */
	private void addVoiceChange(int channel, int voiceNumber, long time, Sequence s) throws InvalidMidiDataException {
		//set voice
		ShortEvent v = new ShortEvent();
		v.setMessage(192-1+channel, voiceNumber, 0, time);
		s.getTracks()[0].add(v);
	}

	//closes the MIDI system
	// - must call this before program finishes so others can use MIDI afterwards
	public void close() {
		//midisystem
		sequencer.close();
	}


}
