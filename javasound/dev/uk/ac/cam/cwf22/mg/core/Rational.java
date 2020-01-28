package uk.ac.cam.cwf22.mg.core;

/** Rational number representation
 */



public class Rational implements Cloneable
{
	private int numerator, denominator;

	//CONSTRUCTOR
	public Rational(int numerator, int denominator) {

		this.numerator = numerator;
		this.denominator = denominator;

		reduceFractions();
	}

	//returns numerator
	public int getNumerator() {
		return numerator;
	}

	//returns denominator
	public int getDenominator() {
		return denominator;
	}

	//Multiplies MYSELF by another rational
	public void multiplyBy(Rational m) {
		this.numerator *= m.getNumerator();
		this.denominator *= m.getDenominator();
		reduceFractions();

		return;
	}

	//returns a NEW rational given by adding me to another rational
	public Rational plus(Rational m) {
		int a = numerator*m.denominator + denominator*m.numerator;
		int b = denominator*m.denominator;
		Rational result = new Rational(a,b);
		return result;
	}


	//test to see if I'm greater than a given rational
	public boolean isGreaterThan(Rational m) {
		return (numerator * m.denominator > m.numerator * m.denominator);
	}

	/** returns a string representation "numerator/denominator"
	 *  eg. 7/2
	 * */
	public String toString() {
		String result = numerator + "/" + denominator;
		return result;
	}

	/** nicer version of toSimpleString which prints in junior school style
	 *  if there is a fraction component
	 *  eg. 3+1/2
	 * */
	public String toPrettyString() {
		int wholes = numerator/denominator;
		int newNumerator = numerator%denominator;

		String result = "";

		if (wholes != 0) result += wholes;
		if (newNumerator != 0) result += "+"+newNumerator+"/"+denominator;

		return result;
	}

	/**returns HCF of two ints using Euclid's algorithm */
	public int highestCommonFactor(int x, int y) {

		if (x==0 || y == 0) return 0; // prevent weird cases

		int a = (x>=y)? x:y;
		int b = (x<y)?  x:y;
		int n,r;
		for (;;) {
			n = a/b;
			r = a%b;
			if (r==0) return b; //terminate when zero remainder to return HCF
			a = b;
			b = r;
		}
	}

	// call this whenever we update the rational
	// to stop the numbers from getting silly
	private void reduceFractions() {
		//find HCF
		int HCF = highestCommonFactor(numerator, denominator);
		//if 0...
		// the number we are representing is zero
		// so set the denominator to 1 (numerator is already zero)
		// (NB this also happens if the denominator is zero, but this will never occur)
		if (HCF == 0) {
			numerator = 0;
			denominator = 1;
		}
		// else divide top and bottom by hcf
		else {
			numerator /= HCF;
			denominator /= HCF;
		}
	}

	public void test(){System.out.println("test");}

	//returns a floating point version of the rational number
	public float getFloat() {
		float result = ((float)numerator) / ((float)denominator);
		return result;
	}

	//returns a Long version of the rational number
	public long getScaledLong(long scale)
	{
		double 	n = (double)numerator,
		 		d = (double)denominator,
		 		s = (double)scale;

		double r = (n*s)/d;

		long result = (long)r;

		return result;
	}


	public Object clone() {
		Object result = null;
		try {
			result = super.clone();
		}
		catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException");
		}
		return result;
	}

}
