package uk.ac.cam.cwf22.mg.core;

import java.util.Random;

/** An abstract class with some useful functions
 *  eg. gaussian distributions
 */

public class Stats
{
	
	/** test method */
	public static void main(String[] args) {
		int mean =10, sd=0;
		for (int i=0; i<60; i++) {
			System.out.println(getRandomInt(5,10));
		}
	}
	
	/** TODO: guassian distribution
	 * (util.Random not workign properly?) 
	 * for now, just pick at random from mean+-sd
	 */
	public static int  getGaussian(int mean, int sd) {
		
		int dev =  (int)( 2* (double)(sd+1) * (Math.random()-0.5) ) ;
		
		int result = mean+dev;	
		
		//System.out.println("Gauss: mean:"+mean+"  sd:"+sd+"  result:"+result);
		
		return result;
	}
	
	/** generate random int - inclusing base, excluding top */
	public static int getRandomInt(int rangeBase, int rangeTop) {
		double r = Math.random();
		int length = rangeTop - rangeBase;
		
		r*=length;
		r+=rangeBase;
		return (int)r;
	}
	
	public static Rational getRandomRational(Rational rangeBase,
											 Rational rangeTop) {
		//TODO: getRandomRational - nice gausssian with sd - currently hacked
		return new Rational( getRandomInt(0,12),
							 getRandomInt(1,12) );
	}
	
	public static int mutatedColorMean(int[] parentVals) {
		int r = 0;
		for (int i=0; i<parentVals.length; i++) r+= parentVals[i];
		r = r/parentVals.length;
		r += getRandomInt(-30, 30);
		if (r>254 || r<1) r = getRandomInt(0, 255);
		return r;
	}
	
	public static int mutatedVoiceMean(int[] parentVals) {
		int r=0;
		for (int i=0; i<parentVals.length; i++) r+= parentVals[i];
		r = r/parentVals.length;
		r += getRandomInt(-5,5);
		if (r>90 || r<1) r = getRandomInt(0, 90);
		return r;
	}
		
}
