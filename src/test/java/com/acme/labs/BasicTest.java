
package com.acme.labs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class BasicTest extends TestCase {
    private static final Logger LOG = Log.getLogger(BasicTest.class);
    private final String _testName;
    public BasicTest(String testName) {
        super(testName);
        _testName = testName;
        LOG.debug("test \"" + testName + "\" created: " + this);
    }
    public static Test suite() {
        LOG.debug("creating test suite");
        return new TestSuite(BasicTest.class);
    }
    public void testCastToByte() {
	short s = 0xff; /* cast int to short */
	int i = 0xff;
	assertTrue(s > 0);
	assertTrue(i > 0);
	assertEquals((byte)s, (byte)-1);
	assertEquals((byte)i, (byte)-1);
	assertEquals((byte)0xff, (byte)-1); /* likewise, cast from constant */
    }
    public void testLiteralByte() {
	byte i = (byte)0xff;  /* complaint without explicit cast: possible loss of precision */
	byte j = (byte)255;   /* consistent with the hexadecimal literal */
	byte k = -1;
	assertEquals(i,j);
	assertEquals(i,k);
    }
    public void testLiteralShort() {
	short i = (short)0xffff;  /* complaint without explicit cast: possible loss of precision */
	short j = (short)65535L;  /* consistent with the hexadecimal literal */
	short k = -1;
	assertEquals(i,j);
	assertEquals(i,k);
    }
    public void testLiteralIntA() {
	int i = (int)0xffffffffL; /* complaint without explicit cast: possible loss of precision */
	int j = (int)4294967295L; /* consistent with the hexadecimal literal */
	int k = -1;
	assertEquals(i,j);
	assertEquals(i,k);
    }
    public void testLiteralIntB() {
	int i = 0xffffffff; /* 0xffffffff = 4294967295, but no complaint, useful for bitmasks */
	int j = (int)4294967295L; /* inconsistent, since can't use literal 4294967295 as with the
				   * hexadecimal case */
	int k = -1;
	assertEquals(i,j);
	assertEquals(i,k);
    }
    public void testLiteralLong() {
	long i = 0xffffffffffffffffL;
	//long j = (long)18446744073709551615L;  too large, as expected
	long k = -1L;
	//assertEquals(i,j);
	assertEquals(i,k);
    }
    private static int negate_8bit(int v) { return (~v & 0xff) + 1; }
    private static int negate_16bit(int v) { return (~v & 0xffff) + 1; }
    private static int negate_32bit(int v) { return ~v + 1; }
    public void testNegate() {
	/*
	 * how two's complement negation works
	 *
	 * 1 = 0b01
	 * 2 = 0b010
	 * 3 = 0b011
	 *
	 * -1 = 0b11
	 * -2 = 0b110
	 * -3 = 0b101
	 *
	 *  31 = 0b011111
	 * -31 = 0b100001   (i.e., ~0b011111 + 1)
	 *
	 *  32 = 0b0100000
	 * -32 = 0b1100000  (i.e., (~0b0100000 = 0b1011111) + 1)
	 *
	 * reference: perl -le'print(0b011)'  # 3
	 * reference: perl -le'print((~0b101 & 0b111) + 1)'  # 3, but from -3
	 * reference: perl -le'print((~-3 & 0b111) + 1)'     # likewise
	 */

	/* 0xCAFEBABE is certainly negative, since 0xC > 0x7, how to negate
	 * and find it's absolute value? (~0xCAFEBABE + 1)
	 */
	assertEquals(negate_8bit(0xCA), 54);
	assertEquals((byte)0xCA,       -54);
	assertEquals(negate_16bit(0xCAFE), 13570);
	assertEquals((short)0xCAFE,       -13570);
	assertEquals(negate_32bit(0xCAFEBABE), 889275714);
	assertEquals(0xCAFEBABE,              -889275714);

	/* a consequence of the two's complement representation is that the
	 * least negative integer negation will yield itself, e.g.: 0b100 is the
	 * least negative number for a 3-bit two's complement system, negating
	 * it will lead to 0b100 again, since ((~0b100 = 0b011) + 1) = 0b100,
	 * i.e., there is no positive representation for -(-4) in a 3-bit two's
	 * complement system, another way to visualize this is that, in 8-bit
	 * two's complement, -1 is equidistant to 0 as -128 is equidistant to
	 * 127 in binary logic, but not in arithmetic, thus 0 can't negate to -0
	 * as -128 can't negate to 128.
	 *
	 * reference: perl -le'$least = (~0 >> 1) + 1; print($least - (~$least + 1));'
	 */

	/* int */
	assertEquals( Integer.MIN_VALUE, (~0 >>> 1) + 1);         // 0x80000000
	assertEquals( Integer.MIN_VALUE, -Integer.MIN_VALUE);     // no sign exchange
	assertEquals( Integer.MIN_VALUE, ~Integer.MIN_VALUE + 1); // no sign exchange
	assertEquals(-Integer.MAX_VALUE, ~Integer.MAX_VALUE + 1); // change sign
	/* long */
	assertEquals( Long.MIN_VALUE, (~0L >>> 1) + 1L);     // 0x8000000000000000L
	assertEquals( Long.MIN_VALUE, -Long.MIN_VALUE);      // no sign exchange
	assertEquals( Long.MIN_VALUE, ~Long.MIN_VALUE + 1L); // no sign exchange
	assertEquals(-Long.MAX_VALUE, ~Long.MAX_VALUE + 1L); // change sign
    }
    public void testPromotion() {
	/* in two's complement systems (i.e., all modern systems), type
	 * promotion just fills the left bits with the sign bit */
	byte b = (byte)0xca;
	assertEquals(0xca << 8, 0xca00); /* only shift (0xca is already an integer) */
	assertEquals(b << 8, 0xffffca00); /* promote and shift, a common mistake */
	assertEquals((b & 0xff) << 8, 0xca00); /* promote, filter and shift */
	assertEquals((int)b, (0xca | ~0xff)); /* cpu/compiler promotion and manual promotion */

	/* the smaller type gets promoted to the larger type implicitly */
	assertEquals(((long)Integer.MIN_VALUE) - 1L, Integer.MIN_VALUE - 1L);
	assertEquals(((long)Integer.MAX_VALUE) + 1L, Integer.MAX_VALUE + 1L);
    }
    /* readU*: consider only the absolute value from ordinary binary
     * representation, i.e., as two's complement promotion to larger
     * types extends the sign bit, we just chop that bits off after
     * promotion and keep the absolute value
     */
    private static int readU1(byte b) {
	return ((int)b) & 0xff;
    }
    private static int readU2(byte b1, byte b0) {
	return ((int)(((b1 & 0xff) << 8) | (b0 & 0xff))) & 0xffff;
    }
    public void testUnsigned() {
	assertEquals(readU1((byte)-1), 255);
	assertEquals(readU1((byte)127), 255 >> 1);
	assertEquals(readU1((byte)-128), (255 >> 1) + 1);

	assertEquals(readU2((byte)-1, (byte)-1), 65535);
	assertEquals(readU2((byte)127, (byte)-1), 65535 >> 1);
	assertEquals(readU2((byte)-128, (byte)0), (65535 >> 1) + 1);
    }
}
