package jcifs.rap.session;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetSessionGetInfo extends Operation {

    public static final int NET_SESSION_GET_INFO = 7;

    private String client;

    private SessionInfo info;

    private int availableBytes;

    public NetSessionGetInfo(String client, SessionInfo info) {
        this.client = client;
        this.info = info;
        setNumber(NET_SESSION_GET_INFO);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLh");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(client.length() > 20 ? client.substring(0, 20) :
                client);
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
