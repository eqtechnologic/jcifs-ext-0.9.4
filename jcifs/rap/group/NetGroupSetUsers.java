package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupSetUsers extends Operation {

    public static final int NET_GROUP_SET_USERS = 108;

    private String group;

    private GroupUsersInfo infoTemplate;

    private GroupUsersInfo[] users;

    public NetGroupSetUsers(String group, GroupUsersInfo[] users) {
        this.group = group;
        this.users = users;
        this.infoTemplate = (users.length > 0) ?  users[0] :
                new GroupUsersInfo();
        setNumber(NET_GROUP_SET_USERS);
        setParameterDescriptor("zWsTW");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(0);
        buffer.writeShort(users.length);
    }

    public void writeRequestData(Buffer buffer) {
        for (int i = 0; i < users.length; i++) users[i].write(buffer);
    }

}


