package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class GroupInfo extends Info {

    public String name;

    public String getDescriptor() {
        return "B21";
    }

    public void read(Buffer buffer) {
        name = buffer.readFixedString(20);
    }

    public void write(Buffer buffer) {
        buffer.writeFixedString(name, 20);
    }

    public String toString() {
        return "name=" + name;
    }

}
