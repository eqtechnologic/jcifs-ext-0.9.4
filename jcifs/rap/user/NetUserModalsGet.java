package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserModalsGet extends Operation {

    public static final int NET_USER_MODALS_GET = 110;

    private UserModalsInfo info;

    private int availableBytes;

    public NetUserModalsGet(UserModalsInfo info) {
        this.info = info;
        setNumber(NET_USER_MODALS_GET);
        setMaxParameterLength(8);
        setParameterDescriptor("WrLh");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
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
