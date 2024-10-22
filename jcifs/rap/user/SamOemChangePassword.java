package jcifs.rap.user;

import java.lang.reflect.Constructor;

import java.security.Key;

import java.util.Random;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

import jcifs.util.DES;

public class SamOemChangePassword extends Operation {

    private static final boolean ARC4_SUPPORT = getArc4Support();

    private static final int SAM_OEM_CHANGE_PASSWORD = 214;

    private static final Random RANDOM = new Random();

	private static final byte[] S8 = {
		(byte) 0x4b, (byte) 0x47, (byte) 0x53, (byte) 0x21,
		(byte) 0x40, (byte) 0x23, (byte) 0x24, (byte) 0x25
	};

    private String user;

    private String oldPassword;

    private String newPassword;

    public SamOemChangePassword(String user, String oldPassword,
            String newPassword) {
        if (!ARC4_SUPPORT) {
            throw new UnsupportedOperationException(
                    "SamOemChangePassword is not supported: ARC4 not found.");
        }
        this.user = user;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        setNumber(SAM_OEM_CHANGE_PASSWORD);
        setParameterDescriptor("zsT");
        setDataDescriptor("B516B16");
        setMaxParameterLength(4);
        setMaxDataLength(0);
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user);
        buffer.writeShort(532);
    }

    public void writeRequestData(Buffer buffer) {
        try {
            byte[] passwordBytes = newPassword.getBytes(Buffer.ENCODING);
            int passwordLength = passwordBytes.length;
            if (passwordLength > 512) {
                throw new IndexOutOfBoundsException("Password too long.");
            }
            byte[] data = new byte[532];
            RANDOM.nextBytes(data);
            System.arraycopy(passwordBytes, 0, data, 512 - passwordLength,
                    passwordLength);
            new Buffer(data, 512).writeLong(passwordLength);
            byte[] oldHash = hash(oldPassword);
            arcFour(oldHash, data, 0, 516);
            byte[] newHash = hash(newPassword);
            byte[] key = new byte[7];
            byte[] value = new byte[8];
            System.arraycopy(newHash, 0, key, 0, 7);
            System.arraycopy(oldHash, 0, value, 0, 8);
            des(key, value, data, 516);
            System.arraycopy(newHash, 7, key, 0, 7);
            System.arraycopy(oldHash, 8, value, 0, 8);
            des(key, value, data, 524);
            System.arraycopy(data, 0, buffer.buffer, buffer.index, 532);
            buffer.index += 532;
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    private static byte[] hash(String password) throws Exception {
        byte[] passwordBytes = password.toUpperCase().getBytes(
                Buffer.ENCODING);
        int passwordLength = passwordBytes.length;
        if (passwordLength > 14) passwordLength = 14;
        byte[] pwd = new byte[14];
        System.arraycopy(passwordBytes, 0, pwd, 0, passwordLength);
        byte[] hash = new byte[16];
        des(pwd, S8, hash, 0);
        return hash;
    }

    private static void des(byte[] key, byte[] value, byte[] dest, int index) {
		byte[] key7 = new byte[7];
		byte[] e8 = new byte[8];
		for (int i = 0; i < key.length / 7; i++) {
			System.arraycopy(key, i * 7, key7, 0, 7);
			DES des = new DES(key7);
			des.encrypt(value, e8);
			System.arraycopy(e8, 0, dest, index + (i * 8), 8);
		}
    }

    private static void arcFour(byte[] key, byte[] data, int index, int length)
            throws Exception {
        Class cipher = Class.forName("javax.crypto.Cipher");
        Object arc4 = cipher.getMethod("getInstance",
                new Class[] { String.class }).invoke(null,
                        new Object[] { "ARC4" });
        Constructor constructor =
                Class.forName("javax.crypto.spec.SecretKeySpec").getConstructor(
                        new Class[] { key.getClass(), String.class });
        Key secretKey = (Key) constructor.newInstance(
                new Object[] { key, "ARC4" });
        cipher.getMethod("init",
                new Class[] { Integer.TYPE, Key.class }).invoke(arc4,
                new Object[] { cipher.getField("ENCRYPT_MODE").get(null),
                        secretKey });
        cipher.getMethod("doFinal", new Class[] { data.getClass(), Integer.TYPE,
                Integer.TYPE, data.getClass(), Integer.TYPE }).invoke(arc4,
                    new Object[] { data, new Integer(index),
                            new Integer(length), data, new Integer(index) });
    }

    private static boolean getArc4Support() {
        try {
            Class cipher = Class.forName("javax.crypto.Cipher");
            Object arc4 = cipher.getMethod("getInstance",
                    new Class[] { String.class }).invoke(null,
                            new Object[] { "ARC4" });
System.out.println("arc4 IS AVAILABLE.");
            return true;
        } catch (Throwable t) {
System.out.println("No arc4 support.");
            return false;
        }
    }

}
