package org.dangcat.commons.crypto;

/**
 * ���ܺͽ����㷨��
 * @author dangcat
 * 
 */
public interface CryptoProvider
{
    /**
     * �����㷨��
     * @param sourceText Դ���֡�
     * @param charsetName �ַ�����
     * @return �������֡�
     */
    String decrypt(String sourceText, String charsetName);

    /**
     * �����㷨��
     * @param sourceText Դ���֡�
     * @param charsetName �ַ�����
     * @return �������֡�
     */
    String encrypt(String sourceText, String charsetName);
}
