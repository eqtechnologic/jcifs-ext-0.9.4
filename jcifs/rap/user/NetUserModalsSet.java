package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserModalsSet extends Operation {

    public static final int NET_USER_MODALS_SET = 111;

    private UserModalsInfo info;

    public NetUserModalsSet(UserModalsInfo info) {
        this.info = info;
        setNumber(NET_USER_MODALS_SET);
        setParameterDescriptor("WsTP");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
        // P?
        buffer.writeShort(getMaxDataLength());
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}


