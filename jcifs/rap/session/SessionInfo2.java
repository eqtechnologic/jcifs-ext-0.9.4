package jcifs.rap.session;

import jcifs.rap.Buffer;

public class SessionInfo2 extends SessionInfo1 {

    public String clientType;

    public String getDescriptor() {
        return super.getDescriptor() + "z";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        clientType = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeFreeString(clientType);
    }

    public String toString() {
        return super.toString() + ";clientType=" + clientType;
    }

}
