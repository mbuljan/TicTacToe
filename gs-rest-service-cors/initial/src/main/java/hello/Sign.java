package hello;

//public enum Sign {UNDEFINED, X, O}

public final class Sign {
	public static final Sign UNDEFINED = new Sign('U');
	public static final Sign X = new Sign('X');
	public static final Sign O = new Sign('O');
	public static final Sign OUT_OF_RANGE_SIGN = new Sign('A');

	private char charValue = 'U';

	public Sign(char charValue) {
		this.charValue = charValue;
	}

	public char getCharValue() {return charValue;}
}
