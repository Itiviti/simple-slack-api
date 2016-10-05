package com.ullink.slack.simpleslackapi.utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Holds utilities for {@link java.io.Reader}s.
 */
public final class ReaderUtils
{

    /**
     * Size of char buffer that is read to.
     */
    private static final int BUFFER_SIZE = 1024; // 1KB

    /**
     * Reads everything from reader and returns as string.
     * @param reader the object read from
     * @return a string containing all the information
     */
    public static String readAll(final Reader reader) throws IOException
    {
        if(reader == null)
        {
            throw new NullPointerException("Reader is null...");
        }

        final StringBuilder stringBuilder = new StringBuilder();
        copy(reader, stringBuilder);
        return stringBuilder.toString();

    }

    private static void copy(final Reader from, final Appendable to) throws IOException
    {
        final CharBuffer charBuffer = CharBuffer.allocate(BUFFER_SIZE);
        while (from.read(charBuffer) != -1) {
            charBuffer.flip();
            to.append(charBuffer);
            charBuffer.clear();
        }
    }

    private ReaderUtils()
    {

    }
}
