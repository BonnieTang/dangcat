package org.dangcat.commons.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * TripleDes���ܹ��ߵķ�װ��
 */
class TripleDes {
    /**
     * �����㷨������
     */
    private static final String ALGORITHM = "DESede";

    /**
     * ���������
     */
    private static final int BLOCK_SIZE = 8;

    // ��Կ������Ϊ24�ֽ�
    // private static final byte[] KEYBYTES = { (byte) 0x43, (byte) 0x29,
    // (byte) 0x7F, (byte) 0xAD, (byte) 0x38, (byte) 0xE3, (byte) 0x73,
    // (byte) 0xFE, (byte) 0x1F, (byte) 0x08, (byte) 0x26, (byte) 0x0D,
    // (byte) 0x1A, (byte) 0xC2, (byte) 0x46, (byte) 0x5E, (byte) 0x58,
    // (byte) 0x40, (byte) 0x23, (byte) 0x64, (byte) 0x1A, (byte) 0xBA,
    // (byte) 0x61, (byte) 0x76 };
    private static final byte[] KEYBYTES = {(byte) 0x5A, (byte) 0x84, (byte) 0x78, (byte) 0x9F, (byte) 0xC7, (byte) 0x94, (byte) 0xC7, (byte) 0x48, (byte) 0xC0, (byte) 0x3A, (byte) 0x09,
            (byte) 0xB6, (byte) 0xB6, (byte) 0x05, (byte) 0xF6, (byte) 0xB9, (byte) 0x66, (byte) 0x09, (byte) 0x86, (byte) 0x6A, (byte) 0x3D, (byte) 0x7B, (byte) 0x3F, (byte) 0x4B};

    /**
     * ת�������������㷨�����ƣ����磬DES����������ܸ���һ������ģʽ����䷽���� DESede/ECB/NoPadding
     */
    private static final String TRANSFORMATION = ALGORITHM + "/ECB/NoPadding";

    /**
     * ����
     *
     * @param sourceBytes ����
     * @return ԭ�ģ�������null
     */
    protected static byte[] decrypt(byte[] sourceBytes) {
        return decrypt(KEYBYTES, sourceBytes);
    }

    /**
     * ����
     *
     * @param keys        ��Կ
     * @param sourceBytes ����
     * @return ԭ�ģ�������null
     */
    private static byte[] decrypt(byte[] keys, byte[] sourceBytes) {
        if (keys == null || keys.length != 24)
            return null;

        // ���볤�ȱ�����8��������
        if (sourceBytes == null || sourceBytes.length % 8 != 0)
            return null;

        try {
            // ������Կ
            SecretKey deskey = new SecretKeySpec(keys, ALGORITHM);

            // �������ܶ���
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // ����
            cipher.init(Cipher.DECRYPT_MODE, deskey); // ����ģʽ

            byte[] decryptBytes = cipher.doFinal(sourceBytes);

            // ��䡮\0������ɽ��ܺ�Ĵ�����\0��,trim '\0'
            int index = findLastNotof(decryptBytes, (byte) 0);

            if (index == -1)
                return null;

            if (index == decryptBytes.length - 1)
                return decryptBytes;

            byte[] returnBytes = new byte[index + 1];
            System.arraycopy(decryptBytes, 0, returnBytes, 0, index + 1);
            return returnBytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����
     *
     * @param sourceBytes ԭ��
     * @return ���ģ�������null
     */
    protected static byte[] encrypt(byte[] sourceBytes) {
        return encrypt(KEYBYTES, sourceBytes);
    }

    /**
     * ����
     *
     * @param keys        ��Կ
     * @param sourceBytes ԭ��
     * @return ���ģ�������null
     */
    private static byte[] encrypt(byte[] keys, byte[] sourceBytes) {
        if (keys == null || keys.length != 24)
            return null;

        if (sourceBytes == null)
            return null;

        try {
            // ������Կ
            SecretKey deskey = new SecretKeySpec(keys, ALGORITHM);

            // �������ܶ���
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // ����
            cipher.init(Cipher.ENCRYPT_MODE, deskey); // ����ģʽ

            // �������BLOCK_SIZE����������ĩβ��䡮\0��
            int mod = sourceBytes.length % BLOCK_SIZE;
            if (mod == 0)
                return cipher.doFinal(sourceBytes);

            int lenngth = sourceBytes.length - mod + BLOCK_SIZE;
            byte[] srcPadded = new byte[lenngth]; // ��ʼΪȫ0
            System.arraycopy(sourceBytes, 0, srcPadded, 0, sourceBytes.length);
            // ��䡮\0����ɽ��ܺ�Ĵ�����\0��
            return cipher.doFinal(srcPadded);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * �ҳ����һ����Ϊָ���ַ���λ��
     *
     * @param bytes     ��
     * @param byteValue ָ���ַ�
     * @return ����, �Ҳ�������-1
     */
    private static int findLastNotof(byte[] bytes, byte byteValue) {
        for (int index = bytes.length - 1; index >= 0; --index) {
            if (bytes[index] != byteValue)
                return index;
        }
        return bytes.length - 1;
    }
}
