package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupEnum extends Operation {

    private static final int NET_GROUP_ENUM = 47;

    public GroupInfo[] groups;

    private GroupInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetGroupEnum(GroupInfo infoTemplate) {
        this.infoTemplate = infoTemplate;
        setNumber(NET_GROUP_ENUM);
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
        groups = new GroupInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            groups[i] = (GroupInfo) infoTemplate.clone();
            groups[i].read(buffer);
        }
    }

}
