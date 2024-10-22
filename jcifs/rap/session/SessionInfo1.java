package jcifs.rap.session;

import jcifs.rap.Buffer;

public class SessionInfo1 extends SessionInfo {

    public String username;

    public int connectionCount;

    public int fileCount;

    public int userCount;

    public long activeTime;

    public long idleTime;

    public long userFlags;

    public String getDescriptor() {
        return super.getDescriptor() + "zWWWDDD";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        username = buffer.readFreeString();
        connectionCount = buffer.readShort();
        fileCount = buffer.readShort();
        userCount = buffer.readShort();
        activeTime = buffer.readLong();
        idleTime = buffer.readLong();
        userFlags = buffer.readLong();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeFreeString(username);
        buffer.writeShort(connectionCount);
        buffer.writeShort(fileCount);
        buffer.writeShort(userCount);
        buffer.writeLong(activeTime);
        buffer.writeLong(idleTime);
        buffer.writeLong(userFlags);
    }

    public String toString() {
        return super.toString() + ";username=" + username +
                ";connectionCount=" + connectionCount + ";fileCount=" +
                        fileCount + ";userCount=" + userCount + ";activeTime=" +
                                activeTime + ";idleTime=" + idleTime +
                                        ";userFlags=" +
                                                Long.toHexString(userFlags);
    }

}
