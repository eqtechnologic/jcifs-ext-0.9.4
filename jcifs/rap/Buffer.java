package jcifs.rap;

import java.io.UnsupportedEncodingException;

import jcifs.Config;

public class Buffer {

	public static final String ENCODING =
            Config.getProperty("jcifs.smb.client.codepage",
                    Config.getProperty("jcifs.encoding",
                            System.getProperty("file.encoding")));

    public byte[] buffer;

    public int index;

    public int offset;

    public int start;

    private int base;

    public Buffer(byte[] buffer, int index) {
        this(buffer, index, -1);
    }

    public Buffer(byte[] buffer, int index, int offset) {
        this.buffer = buffer;
        this.index = index;
        this.offset = offset;
        start = index;
        base = offset;
    }

    public int length() {
        return Math.max(index, offset) - start;
    }

    public void pad(int count) {
        for (int i = 0; i < count; i++) buffer[index++] = (byte) 0x00;
    }

    public void skip(int count) {
        index += count;
    }

    public int readShort() {
        return (buffer[index++] & 0xff) + ((buffer[index++] & 0xff) << 8);
    }

    public void writeShort(long value) {
		buffer[index++] = (byte) ((int) value & 0xff);
		buffer[index++] = (byte) ((int) (value >>> 8) & 0xff);
    }

    public int readLong() {
		return (buffer[index++] & 0xff) + ((buffer[index++] & 0xff) << 8) +
				((buffer[index++] & 0xff) << 16) +
                        ((buffer[index++] & 0xff) << 24);
    }

    public void writeLong(long value) {
		buffer[index++] = (byte) ((int) value & 0xff);
		buffer[index++] = (byte) ((int) (value >>> 8) & 0xff);
		buffer[index++] = (byte) ((int) (value >>> 16) & 0xff);
		buffer[index++] = (byte) ((int) (value >>> 24) & 0xff);
    }

    public String readString() {
        return readString(Integer.MAX_VALUE);
    }

    public String readString(int maxLength) {
		int length = 0;
        while (buffer[index + length] != (byte) 0x00) {
            if (length++ > maxLength) throw new IndexOutOfBoundsException();
        }
		try {
            return new String(buffer, index, length, ENCODING);
		} catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Encoding \"" + ENCODING +
                    "\" not supported.");
		} finally {
            index += length + 1;
        }
    }

    public void writeString(String string) {
        if (string == null) string = "";
		try {
            byte[] bytes = string.getBytes(ENCODING);
            System.arraycopy(bytes, 0, buffer, index, bytes.length);
            index += bytes.length;
            buffer[index++] = (byte) 0x00;
		} catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Encoding \"" + ENCODING +
                    "\" not supported.");
		}
    }

    public String readFixedString(int length) {
        int start = index;
        try {
            return readString(length);
        } finally {
            index = start + length + 1;
        }
    }

    public void writeFixedString(String string, int length) {
        if (string == null) string = "";
        if (string.length() > length) string = string.substring(0, length);
        int pos = index;
        writeString(string);
        pos += length + 1;
        while (index < pos) buffer[index++] = (byte) 0x00;
    }

    public void writeFreeString(String string) {
        if (string == null) string = "";
        writeLong(offset - base);
        try {
            byte[] bytes = string.getBytes(ENCODING);
            System.arraycopy(bytes, 0, buffer, offset, bytes.length);
            offset += bytes.length;
            buffer[offset++] = (byte) 0x00;
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Encoding \"" + ENCODING +
                    "\" not supported.");
        }
    }

    public String readFreeString() {
        int pointer = readLong() & 0xffff;
        if (pointer == 0) return null;
        pointer += base;
		int length;
        for (length = 0; buffer[pointer + length] != (byte) 0x00; length++);
		try {
            return new String(buffer, pointer, length, ENCODING);
		} catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("Encoding \"" + ENCODING +
                    "\" not supported.");
		}
    }

    public void writeDataBuffer(byte[] data) {
        writeDataBuffer(data, 0, data.length);
    }

    public void writeDataBuffer(byte[] data, int i, int length) {
        if (data == null || length == 0) {
            writeLong(0);
        } else {
            writeLong(offset - base);
            System.arraycopy(data, i, buffer, offset, length);
            offset += length;
        }
    }

    public byte[] readDataBuffer(int length) {
        byte[] data = new byte[length];
        readDataBuffer(data, 0, length);
        return data;
    }

    public void readDataBuffer(byte[] data) {
        readDataBuffer(data, 0, data.length);
    }

    public void readDataBuffer(byte[] data, int i, int length) {
        int pointer = readLong() & 0xffff;
        if (pointer == 0) return;
        pointer += base;
        System.arraycopy(buffer, pointer, data, i, length);
    }

}
