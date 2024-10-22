package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupDel extends Operation {

    private static final int NET_GROUP_DEL = 49;

    private String group;

    public NetGroupDel(String group) {
        this.group = group;
        setNumber(NET_GROUP_DEL);
        setParameterDescriptor("z");
        setMaxDataLength(200);
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
    }

}
