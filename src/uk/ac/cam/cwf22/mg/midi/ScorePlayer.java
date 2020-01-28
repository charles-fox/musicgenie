package uk.ac.cam.cwf22.mg.midi;

import javax.sound.midi.*;
import java.io.*;
import uk.ac.cam.cwf22.mg.core.Score;
import uk.ac.cam.cwf22.mg.core.Note;
import uk.ac.cam.cwf22.mg.core.Rational;


/** the ScorePlayer is a singleton.
*   Once created, you can pass it Scores to play them
*/

//NB - we're only using a single track with 16 channels at the moment

public class ScorePlayer {

	private boolean output = false;

	private Sequence sequence;

	private Sequencer sequencer;

	//list of 16 MIDI voices - one for each channel
	private int[] voices;

	//to scale between MusicGenie beats and MIDI ticks
	public long timeScaleFactor = 128l;

	//MusicGenie pitches have 0 as middle C, so shift appropriately
	public int  pitchShift = 48;


	//CONSTRUCTOR
 	public ScorePlayer() throws MidiUnavailableException, InvalidMidiDataException {
		//get a sequencer
		sequencer = MidiSystem.getSequencer();
		sequencer.open();

		setVoices();
	}


	/** takes a MusicGenie Score and turns it into internal playable form
	 * ready to be played
	 */
	public void playScore(Score score) throws InvalidMidiDataException, MidiUnavailableException {
		sequence = makeSequence(score);
		play();
	}

	public void stopPlay() throws Exception {
		sequencer.stop();
	}


	/**
	 * save a MIDI file of the given score
	*/
	public void saveMIDI(	Score score,
							String fileName,
							String directory) throws InvalidMidiDataException, MidiUnavailableException, IOException
	{
		Sequence saveSeq = makeSequence(score);

		int type = 1; //MIDI filetype for multi tracks

		File file = new File(directory, fileName);

		MidiSystem.write(saveSeq,type,file);

		//sequence = saveSeq;
		//play();
	}

	/** makes a sequnce from a score*/
	private Sequence makeSequence(Score score) throws InvalidMidiDataException {

		//set up 16 blank tracks in a new sequence
		Sequence newSeq = new Sequence(Sequence.PPQ, 256);

		//track zero seems to to appear in cubase?
		for (int i=0; i<16; i++) {
			Track track = newSeq.createTrack();
		}
		addVoices(newSeq);

		//for each note, add it to the sequence
		for (int i=0; i<score.size(); i++) {
			Note note = (Note)(score.elementAt(i));
			addNote(note, newSeq);
		}
		return newSeq;
	}


	/** sets MIDI voices for each channel */
	public void setVoices(int[] voices) {
		this.voices = voices;
	}

	/** sets a particular to channel to a given voice */
	public void setVoices(int n, int voice) {
		voices[n] = voice;
	}

	/** sets voices to default values */
	public void setVoices() {
		voices = new int [] { 1, 57, 24, 60,
		 					  13, 2, 6, 73,
		 					  74, 71, 72, 69,
		 		 			  34, 35, 45, 57};
	}


	//============INTERNAL METHODS======================



	private void addNote(Note note, Sequence seq) throws InvalidMidiDataException{

		int track 		= (note.voice)%16;
		int pitch 		= note.getPitch() + pitchShift;
		int velocity 	= (int)(note.amp.getFloat() * 127.0f);
		long duration 	= note.duration.getScaledLong(timeScaleFactor);
		long time 		= note.time.getScaledLong(timeScaleFactor);

		addNote( track, pitch,  velocity,  duration,  time, seq);
	}


	/** adds all the voice-channel assignments to the beginning of the sequence */
	private void addVoices(Sequence seq) throws InvalidMidiDataException {
		for (int i=0; i<16; i++) {
			addVoiceChange(i, voices[i], 0l, seq);
		}
	}

	//play the sequence
	private void play() throws MidiUnavailableException, InvalidMidiDataException {
		sequencer.setSequence(sequence);
		sequencer.start();
	}

	//===============UGLY LOW LEVEL STUFF ================

	//=== abandon hope all ye who enter here =============


	/** adds note on and off events, based on a slightly higher-level representation
	 *   duration and time are in MIDI ticks
	 *
	 *   tracks go from 0 to 15
	 *   we only use channel n+1 on each track n.
	 */
	private void addNote(int track, int pitch, int velocity, long duration, long time, Sequence s) throws InvalidMidiDataException {

		if (output) p("adding to track "+track);

		//add a Note On event
		ShortMessage on = new ShortMessage();
		on.setMessage(144+track, pitch, velocity);
		s.getTracks()[track].add(new MidiEvent(on, time));

		//add a Note Off event
		ShortMessage off = new ShortMessage();
		off.setMessage(128+track, pitch, velocity);
		s.getTracks()[track].add(new MidiEvent(off, time+duration));
	}

	/**
	 * adds a single voice change MIDI event at the specified time
	 */
	private void addVoiceChange(int track, int voiceNumber, long time, Sequence s) throws InvalidMidiDataException {
		//set voice
		ShortMessage v = new ShortMessage();
		v.setMessage(192+track, voiceNumber, 0);
		s.getTracks()[track].add(new MidiEvent(v, time));
	}

	//closes the MIDI system
	// - must call this before program finishes so others can use MIDI afterwards
	public void close() {
		//midisystem
		sequencer.close();
	}

	private void p(String s){System.out.println(s);}

}
