package jcifs.rap.session;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetSessionEnum extends Operation {

    private static final int NET_SESSION_ENUM = 6;

    public SessionInfo[] sessions;

    private SessionInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetSessionEnum(SessionInfo infoTemplate) {
        this.infoTemplate = infoTemplate;
        setNumber(NET_SESSION_ENUM);
        setMaxParameterLength(8);
        setParameterDescriptor("WrLeh");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        sessions = new SessionInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            sessions[i] = (SessionInfo) infoTemplate.clone();
            sessions[i].read(buffer);
        }
    }

}
