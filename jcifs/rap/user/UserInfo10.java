package jcifs.rap.user;

import jcifs.rap.Buffer;

public class UserInfo10 extends UserInfo {

    public String comment;

    public String userComment;

    public String fullName;

    public String getDescriptor() {
        return super.getDescriptor() + "Bzzz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        buffer.skip(1);
        comment = buffer.readFreeString();
        userComment = buffer.readFreeString();
        fullName = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.pad(1);
        buffer.writeFreeString(comment);
        buffer.writeFreeString(userComment);
        buffer.writeFreeString(fullName);
    }

    public String toString() {
        return super.toString() +
                ";comment=" + comment +
                ";userComment=" + userComment +
                ";fullName=" + fullName;
    }

}
