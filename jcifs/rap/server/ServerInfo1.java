package jcifs.rap.server;

import jcifs.rap.Buffer;

public class ServerInfo1 extends ServerInfo {

    public int versionMajor;

    public int versionMinor;

    public int serverType;

    public String comment;

    public String getDescriptor() {
        return super.getDescriptor() + "BBDz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        versionMajor = buffer.buffer[buffer.index++] & 0xff;
        versionMinor = buffer.buffer[buffer.index++] & 0xff;
        serverType = buffer.readLong();
        comment = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.buffer[buffer.index++] = (byte) versionMajor;
        buffer.buffer[buffer.index++] = (byte) versionMinor;
        buffer.writeLong(serverType);
        buffer.writeFreeString(comment);
    }

    public String toString() {
        return super.toString() + ";versionMajor=" + versionMajor +
                ";versionMinor=" + versionMinor + ";serverType=" + serverType +
                        ";comment=" + comment;
    }

}
