package jcifs.rap.server;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetServerEnum2 extends Operation {

    public static final int NET_SERVER_ENUM2 = 104;

    public ServerInfo[] servers;

    private ServerInfo infoTemplate;

    private String domain;

    private int serverType;

    private int entryCount;

    private int availableBytes;

    public NetServerEnum2(ServerInfo infoTemplate, int serverType,
            String domain) {
        this.infoTemplate = infoTemplate;
        this.serverType = serverType;
        this.domain = domain;
        setNumber(NET_SERVER_ENUM2);
        setMaxParameterLength(8);
        setParameterDescriptor("WrLehDz");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
        buffer.writeLong(serverType);
        buffer.writeString(domain);
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        servers = new ServerInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            servers[i] = (ServerInfo) infoTemplate.clone();
            servers[i].read(buffer);
        }
    }

}
