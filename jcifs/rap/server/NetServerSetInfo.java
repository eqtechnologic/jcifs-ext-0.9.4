package jcifs.rap.server;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetServerSetInfo extends Operation {

    public static final int NET_SERVER_SET_INFO = 14;

    private ServerInfo info;

    public NetServerSetInfo(ServerInfo info) {
        this.info = info;
        setNumber(NET_SERVER_SET_INFO);
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


