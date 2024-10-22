package jcifs.rap.session;

import jcifs.rap.Buffer;
import jcifs.rap.Info;

public class SessionInfo10 extends SessionInfo {

    private String username;

    private long activeTime;

    private long idleTime;

    public String getDescriptor() {
        return super.getDescriptor() + "zDD";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        //username = buffer.readString();
        username = buffer.readFreeString();
        activeTime = buffer.readLong();
        idleTime = buffer.readLong();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        //buffer.writeString(username);
        buffer.writeFreeString(username);
        buffer.writeLong(activeTime);
        buffer.writeLong(idleTime);
    }

    public String toString() {
        return super.toString() + ";username=" + username + ";activeTime=" +
                activeTime + ";idleTime=" + idleTime;
    }

}
