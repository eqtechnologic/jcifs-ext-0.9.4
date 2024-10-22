package jcifs.rap.user;

public interface UserConstants {

    public static final int UAS_ROLE_STANDALONE = 0;

    public static final int UAS_ROLE_MEMBER = 1;

    public static final int UAS_ROLE_BACKUP = 2;

    public static final int UAS_ROLE_PRIMARY = 3;

    public static final int USER_PRIV_GUEST = 0;

    public static final int USER_PRIV_USER = 1;

    public static final int USER_PRIV_ADMIN = 2;

    public static final int AF_OP_PRINT = 0x00000001;;

    public static final int AF_OP_COMM = 0x00000002;;

    public static final int AF_OP_SERVER = 0x00000004;;

    public static final int AF_OP_ACCOUNTS = 0x00000008;;

    public static final int UF_SCRIPT = 0x0001;

    public static final int UF_ACCOUNTDISABLE = 0x0002;

    public static final int UF_HOMEDIR_REQUIRED = 0x0008;

    public static final int UF_LOCKOUT = 0x0010;

    public static final int UF_PASSWD_NOTREQD = 0x0020;

    public static final int UF_PASSWD_CANT_CHANGE = 0x0040;

    public static final int UF_ENCRYPTED_TEXT_PASSWORD_ALLOWED = 0x0080;

    public static final int UF_TEMP_DUPLICATE_ACCOUNT = 0x0100;

    public static final int UF_NORMAL_ACCOUNT = 0x0200;

    public static final int UF_INTERDOMAIN_TRUST_ACCOUNT = 0x0800;

    public static final int UF_WORKSTATION_TRUST_ACCOUNT = 0x1000;

    public static final int UF_SERVER_TRUST_ACCOUNT = 0x2000;

    public static final int UF_DONT_EXPIRE_PASSWD = 0x10000;

    public static final int UF_USE_DES_KEY_ONLY = 0x200000;

}
