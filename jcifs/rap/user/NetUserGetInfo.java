package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserGetInfo extends Operation {

    public static final int NET_USER_GET_INFO = 56;

    private String user;

    private UserInfo info;

    private int availableBytes;

    public NetUserGetInfo(String user, UserInfo info) {
        this.user = user;
        this.info = info;
        setNumber(NET_USER_GET_INFO);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLh");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) : user);
        buffer.writeShort(info.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        info.read(buffer);
    }

}
