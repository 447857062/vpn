package com.pencilbox.netknight.pcap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Class for creating capture files in libcap format.<br>
 * <p>
 * if using java version less then 1.5 then the packet time resolution will
 * be in msec and no nanosec.<br>
 *
 * @author roni bar yanai
 * @since java 1.5
 */
public class PCapFileWriter implements CaptureFileWriter {
    private static final int MAX_PACKET_SIZE = 65356;

    public static final long DEFAULT_LIMIT = 100000000000l;

    // limit the file size
    private long myLimit = DEFAULT_LIMIT;

    // the out stream
    private OutputStream myOutStrm = null;

    private boolean _isopened = false;

    // used to calculate the packets time.
    private long myStartTime = 0;

    // total ~bytes written so far.
    private long myTotalBytes = 0;

    private boolean isAboveJave1_4 = true;

    /**
     * open new file
     *
     * @param file
     * @throws IOException - on file creation failure.
     */
    public PCapFileWriter(File file) throws IOException {
        this(file, false);
    }

    /**
     * open new file
     *
     * @param file
     * @param append
     * @throws IOException - on file creation failure.
     */
    public PCapFileWriter(File file, boolean append) throws IOException {
        if (file == null) throw new IllegalArgumentException("Got null file object");

        init(file, append);
        myStartTime = getNanoTime();
    }
    /**
     * @return time stamp in nano seconds
     */
    private long getNanoTime() {
        if (isAboveJave1_4) {
            return System.nanoTime();
        } else {
            return System.currentTimeMillis() * 1000000;
        }
    }

    /**
     * open the out stream and write the cap header.
     *
     * @param file
     * @throws IOException
     */
    private void init(File file, boolean append) throws IOException {
        boolean putHdr = !file.exists() || !append;

        myOutStrm = new FileOutputStream(file, append);

        // put hdr only if not appending or file not exits (new file).
        if (putHdr) {
            PCapFileHeader hdr = new PCapFileHeader();
            myOutStrm.write(hdr.getAsByteArray());
        }
        _isopened = true;

        myTotalBytes += PCapFileHeader.HEADER_SIZE;
    }
    /**
     * add packet to alreay opened cap.
     * if close method was called earlier then will not add it.
     *
     * @param thepkt
     * @return true if packet added and false otherwise
     * @throws IOException
     */
    public boolean addPacket(byte[] thepkt) throws IOException {
        if (thepkt == null || !_isopened || myTotalBytes > myLimit) return false;

        PCapPacketHeader hder = new PCapPacketHeader();

        long gap = getNanoTime() - myStartTime; // the gap since start in nano sec

        //14byte是以太帧头信息
        int sizeOfPkt = thepkt.length + 14;

        hder.setTimeValMsec32Uint((gap / 1000) % 1000000);
        hder.setTimeValSec32Uint(gap / 1000000000l);
        hder.setPktlenUint32(sizeOfPkt);
        hder.setCaplen32Uint(sizeOfPkt);

        if (sizeOfPkt > MAX_PACKET_SIZE)
            throw new IOException("Got illeagl packet size : " + thepkt.length);

        myOutStrm.write(hder.getAsByteArray());
        //TODO 这里是以太帧信息,伪造的
        myOutStrm.write(createFrameHeader());

        myOutStrm.write(thepkt);

        myTotalBytes += sizeOfPkt + PCapPacketHeader.HEADER_SIZE;

        return true;
    }

    //伪造的以太帧头部
    private byte[] createFrameHeader() {
        byte[] frames = new byte[14];
        frames[12] = 1 << 3;
        return frames;
    }

    /**
     * close file.
     * not reversible
     *
     * @throws IOException
     */
    public void close() {
        if (_isopened && myOutStrm != null) {
            try {
                myOutStrm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            _isopened = false;
            myOutStrm = null;
        }
    }
}
