package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserDel extends Operation {

    private static final int NET_USER_DEL = 55;

    private String user;

    public NetUserDel(String user) {
        this.user = user;
        setNumber(NET_USER_DEL);
        setParameterDescriptor("z");
        setMaxDataLength(200);
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) : user);
    }

}
