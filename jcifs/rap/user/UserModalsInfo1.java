package jcifs.rap.user;

import jcifs.rap.Buffer;

public class UserModalsInfo1 extends UserModalsInfo {

    public int serverRole;

    public String pdc;

    public String getDescriptor() {
        return "Wz";
    }

    public void read(Buffer buffer) {
        serverRole = buffer.readShort();
        pdc = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        buffer.writeShort(serverRole);
        buffer.writeFreeString(pdc);
    }

    public String toString() {
        return "serverRole=" + serverRole + ";pdc=" + pdc;
    }

}
