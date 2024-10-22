package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupAdd extends Operation {

    public static final int NET_GROUP_ADD = 48;

    private GroupInfo info;

    public NetGroupAdd(GroupInfo info) {
        this.info = info;
        setNumber(NET_GROUP_ADD);
        setParameterDescriptor("WsT");
        setDataDescriptor(info.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(info.getLevel());
        buffer.writeShort(0);
    }

    public void writeRequestData(Buffer buffer) {
        info.write(buffer);
    }

}


