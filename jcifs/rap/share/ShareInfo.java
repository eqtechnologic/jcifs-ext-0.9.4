package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class ShareInfo extends Info {

    public String name;

    public String getDescriptor() {
        return "B13";
    }

    public void read(Buffer buffer) {
        name = buffer.readFixedString(12);
    }

    public void write(Buffer buffer) {
        buffer.writeFixedString(name, 12);
    }

    public String toString() {
        return "name=" + name;
    }

}
