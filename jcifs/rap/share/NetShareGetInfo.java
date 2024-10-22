package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareGetInfo extends Operation {

    public static final int NET_SHARE_GET_INFO = 1;

    private String share;

    private ShareInfo info;

    private int availableBytes;

    public NetShareGetInfo(String share, ShareInfo info) {
        this.share = share;
        this.info = info;
        setNumber(NET_SHARE_GET_INFO);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLh");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(share.length() > 20 ? share.substring(0, 20) :
                share);
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
