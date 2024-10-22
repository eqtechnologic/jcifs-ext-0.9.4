package jcifs.rap.user;

import jcifs.rap.Buffer;

import jcifs.util.Hexdump;

public class UserInfo2 extends UserInfo1 {

    public long authFlags;

    public String fullName;

    public String userComment;

    public String applicationParameters;

    public String workstations;

    public long lastLogon;

    public long lastLogoff;

    public long expirationTime;

    public long maxStorage;

    public int unitsPerWeek;

    public final byte[] logonHours = new byte[21];

    public int logonFailureCount;

    public int logonSuccessCount;

    public String logonServer;

    public int countryCode;

    public int codePage;

    public String getDescriptor() {
        return super.getDescriptor() + "DzzzzDDDDWb21WWzWW";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        authFlags = buffer.readLong();
        fullName = buffer.readFreeString();
        userComment = buffer.readFreeString();
        applicationParameters = buffer.readFreeString();
        workstations = buffer.readFreeString();
        lastLogon = buffer.readLong();
        lastLogoff = buffer.readLong();
        expirationTime = buffer.readLong();
        maxStorage = buffer.readLong();
        unitsPerWeek = buffer.readShort();
        // probably need to do some endian swapping to make this useful.
        buffer.readDataBuffer(logonHours);
        logonFailureCount = buffer.readShort();
        logonSuccessCount = buffer.readShort();
        logonServer = buffer.readFreeString();
        countryCode = buffer.readShort();
        codePage = buffer.readShort();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeLong(authFlags);
        buffer.writeFreeString(fullName);
        buffer.writeFreeString(userComment);
        buffer.writeFreeString(applicationParameters);
        buffer.writeFreeString(workstations);
        buffer.writeLong(lastLogon);
        buffer.writeLong(lastLogoff);
        buffer.writeLong(expirationTime);
        buffer.writeLong(maxStorage);
        buffer.writeShort(unitsPerWeek);
        buffer.writeDataBuffer(logonHours);
        buffer.writeShort(logonFailureCount);
        buffer.writeShort(logonSuccessCount);
        buffer.writeFreeString(logonServer);
        buffer.writeShort(countryCode);
        buffer.writeShort(codePage);
    }

    public String toString() {
        return super.toString() +
                ";authFlags=" + authFlags +
                ";fullName=" + fullName +
                ";userComment=" + userComment +
                ";applicationParameters=" + applicationParameters +
                ";workstations=" + workstations +
                ";lastLogon=" + lastLogon +
                ";lastLogoff=" + lastLogoff +
                ";expirationTime=" + expirationTime +
                ";maxStorage=" + maxStorage +
                ";unitsPerWeek=" + unitsPerWeek +
                ";logonHours=" + Hexdump.toHexString(logonHours, 0, 21) +
                ";logonFailureCount=" + logonFailureCount +
                ";logonSuccessCount=" + logonSuccessCount +
                ";logonServer=" + logonServer +
                ";countryCode=" + countryCode +
                ";codePage=" + codePage;
    }

}
