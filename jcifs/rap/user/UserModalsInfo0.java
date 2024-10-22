package jcifs.rap.user;

import jcifs.rap.Buffer;

public class UserModalsInfo0 extends UserModalsInfo {

    public int minPasswordLength;

    public long maxPasswordAge;

    public long minPasswordAge;

    public long forceLogoffTime;

    public int passwordHistoryLength;

    public String getDescriptor() {
        return "WDDDWW";
    }

    public void read(Buffer buffer) {
        minPasswordLength = buffer.readShort();
        maxPasswordAge = buffer.readLong();
        minPasswordAge = buffer.readLong();
        forceLogoffTime = buffer.readLong();
        passwordHistoryLength = buffer.readShort();
        buffer.readShort(); // reserved
    }

    public void write(Buffer buffer) {
        buffer.writeShort(minPasswordLength);
        buffer.writeLong(maxPasswordAge);
        buffer.writeLong(minPasswordAge);
        buffer.writeLong(forceLogoffTime);
        buffer.writeShort(passwordHistoryLength);
        buffer.writeShort(0);
    }

    public String toString() {
        return "minPasswordLength=" + minPasswordLength + ";maxPasswordAge=" +
                maxPasswordAge + ";minPasswordAge=" + minPasswordAge +
                        ";forceLogoffTime=" + forceLogoffTime +
                                ";passwordHistoryLength=" +
                                        passwordHistoryLength;
    }

}
