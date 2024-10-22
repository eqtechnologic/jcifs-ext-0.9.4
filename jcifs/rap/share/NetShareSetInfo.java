package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareSetInfo extends Operation {

    public static final int NET_SHARE_SET_INFO = 2;

    private String share;

    private ShareInfo info;

    public NetShareSetInfo(String share, ShareInfo info) {
        this.share = share;
        this.info = info;
        setNumber(NET_SHARE_SET_INFO);
        setParameterDescriptor("zWsTP");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(share.length() > 20 ? share.substring(0, 20) :
                share);
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
        // P?
        buffer.writeShort(getMaxDataLength());
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}


