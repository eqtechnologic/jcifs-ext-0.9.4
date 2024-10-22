package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupDelUser extends Operation {

    private static final int NET_GROUP_DEL_USER = 51;

    private String group;

    private String user;

    public NetGroupDelUser(String group, String user) {
        this.group = group;
        this.user = user;
        setNumber(NET_GROUP_DEL_USER);
        setParameterDescriptor("zz");
        setMaxDataLength(200);
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) :
                user);
    }

}
