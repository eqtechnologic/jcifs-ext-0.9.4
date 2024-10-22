package jcifs.rap.server;

public interface ServerConstants {

    public static final int SV_TYPE_WORKSTATION = 0x00000001;

    public static final int SV_TYPE_SERVER = 0x00000002;

    public static final int SV_TYPE_SQLSERVER = 0x00000004;

    public static final int SV_TYPE_DOMAIN_CTRL = 0x00000008;

    public static final int SV_TYPE_DOMAIN_BAKCTRL = 0x00000010;

    public static final int SV_TYPE_TIME_SOURCE = 0x00000020;

    public static final int SV_TYPE_AFP = 0x00000040;

    public static final int SV_TYPE_NOVELL = 0x00000080;

    public static final int SV_TYPE_DOMAIN_MEMBER = 0x00000100;

    public static final int SV_TYPE_PRINTQ_SERVER = 0x00000200;

    public static final int SV_TYPE_DIALIN_SERVER = 0x00000400;

    public static final int SV_TYPE_SERVER_UNIX = 0x00000800;

    public static final int SV_TYPE_NT = 0x00001000;

    public static final int SV_TYPE_WFW = 0x00002000;

    public static final int SV_TYPE_SERVER_MFPN = 0x00004000;

    public static final int SV_TYPE_SERVER_NT = 0x00008000;

    public static final int SV_TYPE_POTENTIAL_BROWSER = 0x00010000;

    public static final int SV_TYPE_BACKUP_BROWSER = 0x00020000;

    public static final int SV_TYPE_MASTER_BROWSER = 0x00040000;

    public static final int SV_TYPE_DOMAIN_MASTER = 0x00080000;

    public static final int SV_TYPE_SERVER_OSF = 0x00100000;

    public static final int SV_TYPE_SERVER_VMS = 0x00200000;

    public static final int SV_TYPE_WIN95_PLUS = 0x00400000;

    public static final int SV_TYPE_DFS_SERVER = 0x00800000;

    public static final int SV_TYPE_ALTERNATE_XPORT = 0x20000000;  

    public static final int SV_TYPE_LOCAL_LIST_ONLY = 0x40000000;  

    public static final int SV_TYPE_DOMAIN_ENUM = 0x80000000;

    public static final int SV_TYPE_ALL = 0xFFFFFFFF;  

}
