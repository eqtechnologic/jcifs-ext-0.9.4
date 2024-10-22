package jcifs.rap.share;

import jcifs.rap.Buffer;

public class ConnectionInfo1 extends ConnectionInfo {

    public int type;

    public int files;

    public int users;

    public long time;

    public String username;

    public String netname;

    public String getDescriptor() {
        return super.getDescriptor() + "WWWDzz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        type = buffer.readShort();
        files = buffer.readShort();
        users = buffer.readShort();
        time = buffer.readLong();
        username = buffer.readFreeString();
        netname = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeShort(type);
        buffer.writeShort(files);
        buffer.writeShort(users);
        buffer.writeLong(time);
        buffer.writeFreeString(username);
        buffer.writeFreeString(netname);
    }

    public String toString() {
        return super.toString() + ";type=" + type + ";files=" + files +
                ";users=" + users + ";time=" + time + ";username=" + username +
                        ";netname=" + netname;
    }

}
