/* Copyright (c) 2000 The Legion Of The Bouncy Castle
 * (http://www.bouncycastle.org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jcifs.spnego.asn1;

import java.io.IOException;

/**
 * DER NumericString object - this is an ascii string of characters {0,1,2,3,4,5,6,7,8,9, }.
 */
public class DERNumericString
    extends DERObject
    implements DERString
{
    String  string;

    /**
     * return a Numeric string from the passed in object
     *
     * @exception IllegalArgumentException if the object cannot be converted.
     */
    public static DERNumericString getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERNumericString)
        {
            return (DERNumericString)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERNumericString(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return an Numeric String from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted.
     */
    public static DERNumericString getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * basic constructor - with bytes.
     */
    public DERNumericString(
        byte[]   string)
    {
        char[]  cs = new char[string.length];

        for (int i = 0; i != cs.length; i++)
        {
            cs[i] = (char)(string[i] & 0xff);
        }

        this.string = new String(cs);
    }

    /**
     * basic constructor - with string.
     */
    public DERNumericString(
        String   string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    public byte[] getOctets()
    {
        char[]  cs = string.toCharArray();
        byte[]  bs = new byte[cs.length];

        for (int i = 0; i != cs.length; i++)
        {
            bs[i] = (byte)cs[i];
        }

        return bs; 
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        out.writeEncoded(NUMERIC_STRING, this.getOctets());
    }

    public int hashCode()
    {
        return this.getString().hashCode();
    }

    public boolean equals(
        Object  o)
    {
        if (!(o instanceof DERNumericString))
        {
            return false;
        }

        DERNumericString  s = (DERNumericString)o;

        return this.getString().equals(s.getString());
    }
}
