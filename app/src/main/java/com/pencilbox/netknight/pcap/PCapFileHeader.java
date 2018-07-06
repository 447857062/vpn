package com.pencilbox.netknight.pcap;


import com.pencilbox.netknight.utils.ByteUtils;

/**
 * Class for holding libcap file header data structure.<br>
 * Each libcap start with this header.<br>
 * 
 * @author roni bar yanai
 *
 */
public class PCapFileHeader
{
	public static final int HEADER_SIZE = 24;

	long uint32MagicNum = 0;

	int ushort16VersionMajor = 0;

	int ushort16VersionMinor = 0;

	long uint32ThisTimeZone = 0;

	long uint32Sigfigs = 0;

	long uint32snaplen = 0;

	long uint32LinkType = 0;

	boolean isflip = false;
	
	byte[] mySourceByteArr = null;

	//Magic信息
	protected static final long MAGIC_NUMBER_DONT_FLIP = 0xa1b2c3d4L;

	/**
	 * create pcap file header with defaults.
	 *
	 */
	public PCapFileHeader()
    {
		uint32MagicNum = MAGIC_NUMBER_DONT_FLIP;
		ushort16VersionMajor = 2;
		ushort16VersionMinor = 4;
		uint32ThisTimeZone = 0;
		uint32Sigfigs = 0;
		uint32snaplen = 0xffff;
		uint32LinkType = 1;
    }

	/**
	 * @return the header in big indian order.
	 */
	public byte[] getAsByteArray()
	{
		byte[] tmp = new byte[24];
		ByteUtils.setBigIndianInBytesArray(tmp,0,uint32MagicNum,4);
		ByteUtils.setBigIndianInBytesArray(tmp,4,ushort16VersionMajor,2);
		ByteUtils.setBigIndianInBytesArray(tmp,6,ushort16VersionMinor,2);
		ByteUtils.setBigIndianInBytesArray(tmp,8,uint32ThisTimeZone,4);
		ByteUtils.setBigIndianInBytesArray(tmp,12,uint32Sigfigs,4);
		ByteUtils.setBigIndianInBytesArray(tmp,16,uint32snaplen,4);
		ByteUtils.setBigIndianInBytesArray(tmp,20,uint32LinkType,4);
		return tmp;
    }

	/**
	 *  (non-Javadoc)
	 * @see Object#toString()
	 */
	public String toString()
	{
		return "flip : " + isflip + "\n" + "major : " + ushort16VersionMajor + "\n" + "minor : " + ushort16VersionMinor + "\n" + "time zone : " + Long.toHexString(uint32ThisTimeZone) + "\n" + "sig figs : " + Long.toHexString(uint32Sigfigs) + "\n" + "snap length : " + uint32snaplen + "\n"
				+ "link type : " + uint32LinkType + "\n";
	}

	private long pcapRead32(long num)
	{
		long tmp = num;
		if (isflip)
		{
			tmp = ((tmp & 0x000000FF) << 24) + ((tmp & 0x0000FF00) << 8) + ((tmp & 0x00FF0000) >> 8) + ((tmp & 0xFF000000) >> 24);

			return tmp;
		}
		return num;
	}

	private int pcapRead16(int num)
	{
		int tmp = num;
		if (isflip)
		{
			tmp = ((tmp & 0x00FF) << 8) + ((tmp & 0xFF00) >> 8);
			return tmp;
		}
		return num;
	}

}
