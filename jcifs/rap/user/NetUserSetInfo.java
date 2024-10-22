package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserSetInfo extends Operation {

    public static final int NET_USER_SET_INFO = 57;

    private String user;

    private UserInfo info;

    public NetUserSetInfo(String user, UserInfo info) {
        this.user = user;
        this.info = info;
        setNumber(NET_USER_SET_INFO);
        setParameterDescriptor("zWsTPW");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) : user);
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
        // P?
        buffer.writeShort(getMaxDataLength());
        // unknown
        buffer.writeShort(0);
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}


