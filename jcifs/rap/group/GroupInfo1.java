package jcifs.rap.group;

import jcifs.rap.Buffer;

public class GroupInfo1 extends GroupInfo {

    public String comment;

    public String getDescriptor() {
        return super.getDescriptor() + "Bz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        buffer.skip(1);
        comment = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.pad(1);
        buffer.writeFreeString(comment);
    }

    public String toString() {
        return super.toString() + ";comment=" + comment;
    }

}
