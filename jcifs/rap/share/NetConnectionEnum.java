package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetConnectionEnum extends Operation {

    private static final int NET_CONNECTION_ENUM = 9;

    public ConnectionInfo[] connections;

    private String qualifier;

    private ConnectionInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetConnectionEnum(String qualifier, ConnectionInfo infoTemplate) {
        this.infoTemplate = infoTemplate;
        setNumber(NET_CONNECTION_ENUM);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLeh");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        // not really sure of max qualifier length
        buffer.writeString(qualifier.length() > 20 ?
                qualifier.substring(0, 20) : qualifier);
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        connections = new ConnectionInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            connections[i] = (ConnectionInfo) infoTemplate.clone();
            connections[i].read(buffer);
        }
    }

}
