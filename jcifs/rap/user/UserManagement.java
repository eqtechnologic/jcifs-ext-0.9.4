package jcifs.rap.user;

import java.io.IOException;

import jcifs.rap.LevelFactory;

import jcifs.rap.group.GroupUsersInfo;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.Rap;
import jcifs.smb.RapException;
import jcifs.smb.SmbFile;
import jcifs.smb.WinError;

public class UserManagement extends Rap implements UserConstants {

    public UserManagement() {
        super(null, null);
    }

    public UserManagement(String target) {
        super(target, null);
    }

    public UserManagement(NtlmPasswordAuthentication auth) {
        super(null, auth);
    }

    public UserManagement(String target, NtlmPasswordAuthentication auth) {
        super(target, auth);
    }

    public UserManagement(SmbFile target) {
        super(target);
    }

    public void netUserAdd(UserInfo info) throws IOException {
        UserInfo1 info1 = (UserInfo1) info;
        // NetUserAdd doesn't seem to work with the plaintext password;
        // so we create with an empty password and then try to set it.
        String password = info1.password;
        info1.flags |= UserConstants.UF_PASSWD_NOTREQD;
        info1.password = "";
        int result = call(new NetUserAdd(info1));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        SamOemChangePassword passwordChange = null;
        try {
            passwordChange = new SamOemChangePassword(info1.name, "", password);
        } catch (UnsupportedOperationException ex) { }
        if (passwordChange == null) return;
        result = call(passwordChange);
        if (result == WinError.ERROR_SUCCESS) info1.password = password;
    }

    public void netUserChangePassword(String user, String oldPassword,
            String newPassword) throws IOException {
        int result = call(new SamOemChangePassword(user, oldPassword,
                newPassword));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netUserDel(String user) throws IOException {
        int result = call(new NetUserDel(user));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public UserInfo[] netUserEnum(int level) throws IOException {
        UserInfo infoTemplate = (UserInfo)
                LevelFactory.createInformationLevel(UserInfo.class, level);
        NetUserEnum operation = new NetUserEnum(infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.users;
    }

    public GroupUsersInfo[] netUserGetGroups(String user, int level)
            throws IOException {
        GroupUsersInfo infoTemplate = (GroupUsersInfo)
                LevelFactory.createInformationLevel(GroupUsersInfo.class,
                        level);
        NetUserGetGroups operation = new NetUserGetGroups(user, infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.groups;
    }

    public UserInfo netUserGetInfo(String user, int level) throws IOException {
        UserInfo info = (UserInfo)
                LevelFactory.createInformationLevel(UserInfo.class, level);
        NetUserGetInfo operation = new NetUserGetInfo(user, info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

    public void netUserSetGroups(String user, GroupUsersInfo[] groups)
            throws IOException {
        int result = call(new NetUserSetGroups(user, groups));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public void netUserSetInfo(String user, UserInfo info) throws IOException {
        int result = call(new NetUserSetInfo(user, info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public UserModalsInfo netUserModalsGet(int level) throws IOException {
        UserModalsInfo info = (UserModalsInfo)
                LevelFactory.createInformationLevel(UserModalsInfo.class,
                        level);
        NetUserModalsGet operation = new NetUserModalsGet(info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

    public void netUserModalsSet(UserModalsInfo info) throws IOException {
        int result = call(new NetUserModalsSet(info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

}
