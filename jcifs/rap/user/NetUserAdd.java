package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserAdd extends Operation {

    private static final int NET_USER_ADD = 113;

    private UserInfo1 info;

    public NetUserAdd(UserInfo1 info) {
        this.info = info;
        setNumber(NET_USER_ADD);
        setParameterDescriptor("WsTWW");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(info.getLevel());
        buffer.writeShort(getMaxDataLength());
        String password = info.password;
        if (password == null) password = "";
        buffer.writeShort(Math.min(password.length() + 1, 16));
        buffer.writeShort(0);
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}
