package jcifs.rap.share;

import jcifs.rap.Buffer;

public class ShareInfo2 extends ShareInfo1 {

    public int permissions;

    public int maximumUsers = -1;

    public int activeUsers;

    public String path;

    public String password;

    public String getDescriptor() {
        return super.getDescriptor() + "WWWzB9B";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        permissions = buffer.readShort();
        maximumUsers = buffer.readShort();
        activeUsers = buffer.readShort();
        path = buffer.readFreeString();
        password = buffer.readFixedString(8);
        buffer.skip(1);
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeShort(permissions);
        buffer.writeShort(maximumUsers);
        buffer.writeShort(activeUsers);
        buffer.writeFreeString(path);
        buffer.writeFixedString(password, 8);
        buffer.buffer[buffer.index++] = (byte) 0x0a;
    }

    public String toString() {
        return super.toString() + ";permissions=" + permissions +
                ";maximumUsers=" + maximumUsers + ";activeUsers=" +
                        activeUsers + ";path=" + path + ";password=" + password;
    }

}
