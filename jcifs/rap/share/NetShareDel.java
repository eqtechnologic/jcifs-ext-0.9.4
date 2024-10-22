package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareDel extends Operation {

    private static final int NET_SHARE_DEL = 4;

    private String share;

    public NetShareDel(String share) {
        this.share = share;
        setNumber(NET_SHARE_DEL);
        setParameterDescriptor("zW");
        setMaxDataLength(200);
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(share.length() > 20 ? share.substring(0, 20) :
                share);
        buffer.writeShort(0);
    }

}
