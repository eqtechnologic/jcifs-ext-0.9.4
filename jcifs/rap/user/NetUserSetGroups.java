package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

import jcifs.rap.group.GroupUsersInfo;

public class NetUserSetGroups extends Operation {

    public static final int NET_USER_SET_GROUPS = 109;

    private String user;

    private GroupUsersInfo infoTemplate;

    private GroupUsersInfo[] groups;

    public NetUserSetGroups(String user, GroupUsersInfo[] groups) {
        this.user = user;
        this.groups = groups;
        this.infoTemplate = (groups.length > 0) ?  groups[0] :
                new GroupUsersInfo();
        setNumber(NET_USER_SET_GROUPS);
        setParameterDescriptor("zWsTW");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) :
                user);
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(0);
        buffer.writeShort(groups.length);
    }

    public void writeRequestData(Buffer buffer) {
        for (int i = 0; i < groups.length; i++) groups[i].write(buffer);
    }

}


