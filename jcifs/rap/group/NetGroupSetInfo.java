package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupSetInfo extends Operation {

    public static final int NET_GROUP_SET_INFO = 107;

    private String group;

    private GroupInfo info;

    public NetGroupSetInfo(String group, GroupInfo info) {
        this.group = group;
        this.info = info;
        setNumber(NET_GROUP_SET_INFO);
        setParameterDescriptor("zWsTP");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
        // P?
        buffer.writeShort(getMaxDataLength());
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}


