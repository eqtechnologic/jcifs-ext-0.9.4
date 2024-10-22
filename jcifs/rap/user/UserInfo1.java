package jcifs.rap.user;

import jcifs.rap.Buffer;

public class UserInfo1 extends UserInfo {

    public String password;

    public long passwordAge;

    public int privilegeLevel = UserConstants.USER_PRIV_USER;

    public String homeDirectory;

    public String comment;

    public int flags = UserConstants.UF_SCRIPT |
            UserConstants.UF_PASSWD_NOTREQD;

    public String logonScript;

    public String getDescriptor() {
        return super.getDescriptor() + "BB16DWzzWz";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        buffer.skip(1);
        password = buffer.readFixedString(15);
        passwordAge = buffer.readLong();
        privilegeLevel = buffer.readShort();
        homeDirectory = buffer.readFreeString();
        comment = buffer.readFreeString();
        flags = buffer.readShort();
        logonScript = buffer.readFreeString();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.pad(1);
        buffer.writeFixedString(password, 15);
        buffer.writeLong(passwordAge);
        buffer.writeShort(privilegeLevel);
        buffer.writeFreeString(homeDirectory);
        buffer.writeFreeString(comment);
        buffer.writeShort(flags);
        buffer.writeFreeString(logonScript);
    }

    public String toString() {
        return super.toString() +
                ";password=" + password +
                ";passwordAge=" + passwordAge +
                ";privilegeLevel=" + privilegeLevel +
                ";homeDirectory=" + homeDirectory +
                ";comment=" + comment +
                ";flags=" + flags +
                ";logonScript=" + logonScript;
    }

}
