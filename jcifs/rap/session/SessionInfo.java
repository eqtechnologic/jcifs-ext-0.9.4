package jcifs.rap.session;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class SessionInfo extends Info {

    public String client;

    public String getDescriptor() {
        return "z";
    }

    public void read(Buffer buffer) {
        client = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        buffer.writeFreeString(client);
    }

    public String toString() {
        return "client=" + client;
    }

}
