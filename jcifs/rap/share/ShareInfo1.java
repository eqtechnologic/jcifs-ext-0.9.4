package jcifs.rap.share;

import jcifs.rap.Buffer;

public class ShareInfo1 extends ShareInfo {

    public int shareType;

    public String comment;

    public String getDescriptor() {
        return super.getDescriptor() + "BWz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        buffer.skip(1);
        shareType = buffer.readShort();
        comment = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.pad(1);
        buffer.writeShort(shareType);
        buffer.writeFreeString(comment);
    }

    public String toString() {
        return super.toString() + ";shareType=" + shareType + ";comment=" +
                comment;
    }

}
