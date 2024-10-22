package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class ConnectionInfo extends Info {

    public int connectionId;

    public String getDescriptor() {
        return "W";
    }

    public void read(Buffer buffer) {
        connectionId = buffer.readShort();
    }

    public void write(Buffer buffer) {
        buffer.writeShort(connectionId);
    }

    public String toString() {
        return "connectionId=" + connectionId;
    }

}
