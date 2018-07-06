package com.pencilbox.netknight.utils;

/**
 * Class for supporting byte level operations.<br>
 * 1 .easy adding/getting numbers from<br>
 *    byte array at network order (AKA big-endian).<br>
 * <br>
 * 2. printing methods (ordering bytes into readable hex format)<br>
 * <br>
 * 3. transforming big-endian into little-endian and vice versa.<br>
 * <br>
 * In general, java programming language is problematic in dealing with low level representation.<br>
 * For example, no unsigned numbers. Therefore, in many cases we need to keep <br>
 * number in a bigger storage, for example, uint_32 must be handled as long.<br>
 * or else 0xFFFFFFFF will be treated as -1.
 * <br>
 * @author roni bar-yanai
 *
 */ 
public class ByteUtils
{
	/**
	 * put number in array (big endian way)
	 * @param toPutIn - the array to put in
	 * @param startidx - start index of the num
	 * @param theNumber - the number
	 * @param len - the number size in bytes.
	 */
	public static void setBigIndianInBytesArray(byte[] toPutIn,int startidx,long theNumber,int len)
	{
		for(int i=0 ; i < len ; i++)
		{
			long num = (theNumber >> (8*(len - (i+1)))) & 0xff;
			toPutIn[i+startidx] = (byte) num;
		}
	}
	
	/**
	 * put number in array (big endian way)
	 * @param toPutIn - the array to put in
	 * @param startidx - start index of the num
	 * @param theNumber - the number
	 * @param len - the number size in bytes.
	 */
	public static void setLittleIndianInBytesArray(byte[] toPutIn,int startidx,long theNumber,int len)
	{
		for(int i=0 ; i < len ; i++)
		{
			toPutIn[i+startidx] = (byte) (theNumber % 256);
			theNumber/=256;			
		}
	}
}
