package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupGetInfo extends Operation {

    public static final int NET_GROUP_GET_INFO = 106;

    private String group;

    private GroupInfo info;

    private int availableBytes;

    public NetGroupGetInfo(String group, GroupInfo info) {
        this.group = group;
        this.info = info;
        setNumber(NET_GROUP_GET_INFO);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLh");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
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
