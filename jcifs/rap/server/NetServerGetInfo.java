package jcifs.rap.server;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetServerGetInfo extends Operation {

    public static final int NET_SERVER_GET_INFO = 13;

    private ServerInfo info;

    private int availableBytes;

    public NetServerGetInfo(ServerInfo info) {
        this.info = info;
        setNumber(NET_SERVER_GET_INFO);
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
