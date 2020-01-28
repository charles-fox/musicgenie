package uk.ac.cam.cwf22.mg.core;

import java.util.*;

//score is a vector of notes
//
//maybe with extra methods for sorting, displaying etc.

public class Score extends Vector
{
	//TODO: timeSort?  (maybe not need now?)
	//sorts my notes in order of startTimes
	public void timeSort() {
	}
	
	public String toScoreString()  {
		String result = "";
		for (int i=0; i<this.size(); i++) {
			result += ((Note)this.elementAt(i)).toString();
		}
		result += "\n";
		return result;
	}
	
	//conses another score onto ME
	public void cons(Score s) {
		for (int i=0; i<s.size(); i++) {
			this.addElement(s.elementAt(i));
		}
	}
}
