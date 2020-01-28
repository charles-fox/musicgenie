

package uk.ac.cam.cwf22.mg.midi;

import uk.ac.cam.cwf22.mg.core.Score;
import java.io.*;


//dummy class for ScorePlayer

public class ScorePlayerDummy
{
	
	//to scale between MusicGenie beats and MIDI ticks
	public long timeScaleFactor = 200l;
	//MusicGenie pitches have 0 as middle C, so shift appropriately
	public int  pitchShift = 50;
	
	public ScorePlayer() throws Exception {
		System.out.println("Dummy Scoreplayer constructor caller");
	}
	
	public void playScore(Score s) throws Exception {
		System.out.println("PLAY SCORE (DUMMY METHOD)");
		System.out.println(s.toString());
	}
	
	public void stopPlay() throws Exception {
		System.out.println("STOP SCORE (DUMMY METHOD)");
	}
	
	public void saveMIDI(Score s, String fileName, String directory) throws Exception {
		System.out.println("Save as MIDI file "+directory+fileName+" (DUMMY)");
	}
	
	public void setVoices(int[] voices) {
		System.out.println("Set voices (DUMMY)");
		for (int i=0; i<voices.length; i++) p(""+voices[i]);
	}
	
	private void p(String s) {System.out.println(s);}
}

