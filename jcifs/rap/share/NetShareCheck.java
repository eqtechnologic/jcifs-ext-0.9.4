package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareCheck extends Operation {

    private static final int NET_SHARE_CHECK = 5;

    public int type;

    private String share;

    public NetShareCheck(String share) {
        this.share = share;
        setNumber(NET_SHARE_CHECK);
        setParameterDescriptor("zh");
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(share.length() > 20 ? share.substring(0, 20) :
                share);
    }

    public void readResponseParameters(Buffer buffer) {
        type = buffer.readShort();
    }

}
