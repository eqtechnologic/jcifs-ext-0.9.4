package jcifs.rap.group;

import java.io.IOException;

import jcifs.rap.LevelFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.Rap;
import jcifs.smb.RapException;
import jcifs.smb.SmbFile;
import jcifs.smb.WinError;

public class GroupManagement extends Rap implements GroupConstants {

    public GroupManagement() {
        super(null, null);
    }

    public GroupManagement(String target) {
        super(target, null);
    }

    public GroupManagement(NtlmPasswordAuthentication auth) {
        super(null, auth);
    }

    public GroupManagement(String target, NtlmPasswordAuthentication auth) {
        super(target, auth);
    }

    public GroupManagement(SmbFile target) {
        super(target);
    }

    public void netGroupAdd(GroupInfo info) throws IOException {
        int result = call(new NetGroupAdd(info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netGroupAddUser(String group, String user) throws IOException {
        int result = call(new NetGroupAddUser(group, user));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netGroupDel(String group) throws IOException {
        int result = call(new NetGroupDel(group));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netGroupDelUser(String group, String user) throws IOException {
        int result = call(new NetGroupDelUser(group, user));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public GroupInfo[] netGroupEnum(int level) throws IOException {
        GroupInfo infoTemplate = (GroupInfo)
                LevelFactory.createInformationLevel(GroupInfo.class, level);
        NetGroupEnum operation = new NetGroupEnum(infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.groups;
    }

    public GroupInfo netGroupGetInfo(String group, int level)
            throws IOException {
        GroupInfo info = (GroupInfo)
                LevelFactory.createInformationLevel(GroupInfo.class, level);
        NetGroupGetInfo operation = new NetGroupGetInfo(group, info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

    public GroupUsersInfo[] netGroupGetUsers(String group, int level)
            throws IOException {
        GroupUsersInfo infoTemplate = (GroupUsersInfo)
                LevelFactory.createInformationLevel(GroupUsersInfo.class,
                        level);
        NetGroupGetUsers operation = new NetGroupGetUsers(group, infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.users;
    }

    public void netGroupSetInfo(String group, GroupInfo info)
            throws IOException {
        int result = call(new NetGroupSetInfo(group, info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netGroupSetUsers(String group, GroupUsersInfo[] users)
            throws IOException {
        int result = call(new NetGroupSetUsers(group, users));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

}
