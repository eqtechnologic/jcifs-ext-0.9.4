package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareAdd extends Operation {

    private static final int NET_SHARE_ADD = 3;

    private ShareInfo info;

    public NetShareAdd(ShareInfo info) {
        this.info = info;
        setNumber(NET_SHARE_ADD);
        setParameterDescriptor("WsT");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}
