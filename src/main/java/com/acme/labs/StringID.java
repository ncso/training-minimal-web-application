
package com.acme.labs;

import java.math.BigInteger;
import java.nio.charset.Charset;

@SuppressWarnings("serial")
public class StringID extends BigInteger {
    public static final Charset charsetUTF8 = Charset.forName("UTF-8");

    private String _id;

    public static final int RADIX = Character.MAX_RADIX; /* this is expected to be 36 */

    public StringID(byte b[]) {
	/* public BigInteger(int signum, byte[] magnitude)
	 */
	super(1 /* always positive */, _verify(b));
	_id = super.toString(RADIX);
    }

    public StringID(String s) {
	this(s.getBytes(charsetUTF8));
    }

    public String toString() {
	return _id;
    }

    public String toString(int radix) {
        throw new UnsupportedOperationException();
    }

    public static StringID valueOf(String s) {
	return new StringID((new BigInteger(s, RADIX)));
    }

    public static StringID valueOf(StringID s) {
	return new StringID((BigInteger)s);
    }

    public String getSource() {
	byte[] b = toByteArray();
	if (b.length > 1 && b[0] == 0) {
	    /* discards the zero most significant byte that ensure the
	     * value is positive (we don't use negative values), see
	     * StringID(byte b[]) constructor
	     */
	    return new String(b, 1, b.length - 1, charsetUTF8);
	}
	return new String(b, charsetUTF8);
    }

    /*
     */

    private StringID(BigInteger i) {
	super(_verify(i).toByteArray());
	_id = super.toString(RADIX);
    }

    private static byte[] _verify(byte b[]) {
	if (b.length == 0) {
	    throw new IllegalArgumentException("empty byte array");
	}
	return b;
    }

    private static BigInteger _verify(BigInteger i) {
	assert i.signum() >= 0;
	return i;
    }
}
