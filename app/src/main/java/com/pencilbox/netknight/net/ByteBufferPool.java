package com.pencilbox.netknight.net;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 字节buffer池
 */
public class ByteBufferPool
{
    //待定16384 16K
    private static final int BUFFER_SIZE = 16384; // XXX: Is this ideal?
    private static ConcurrentLinkedQueue<ByteBuffer> pool = new ConcurrentLinkedQueue<>();
    public static ByteBuffer acquire()
    {
        ByteBuffer buffer = pool.poll();
        if (buffer == null)
            buffer = ByteBuffer.allocate(BUFFER_SIZE); // Using DirectBuffer for zero-copy
        return buffer;
    }

    //复用ByteBuffer咯
    public static void release(ByteBuffer buffer)
    {
        buffer.clear();
        pool.offer(buffer);
    }
    public static void clear()
    {
        pool.clear();
    }
}