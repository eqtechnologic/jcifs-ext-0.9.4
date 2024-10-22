package jcifs.rap.server;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class ServerInfo extends Info {

    public String name;

    public String getDescriptor() {
        return "B16";
    }

    public void read(Buffer buffer) {
        name = buffer.readFixedString(15);
    }

    public void write(Buffer buffer) {
        buffer.writeFixedString(name, 15);
    }

    public String toString() {
        return "name=" + name;
    }

}
