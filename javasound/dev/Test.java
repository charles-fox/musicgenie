import uk.ac.cam.cwf22.mg.midi.ScorePlayer;
import uk.ac.cam.cwf22.mg.core.*;
import javax.sound.midi.*;

public class Test {
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
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
			theScorePlayer.saveMIDI(testScore, "test.mid", "./");

			//wait for playing
			for (int i=0; i<5000; i++) {p(""+i);}
			p("finished playing");
			theScorePlayer.close();
			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public Test() {
	}
	private static void p(String s) {
		System.out.println(s);
	}
}
