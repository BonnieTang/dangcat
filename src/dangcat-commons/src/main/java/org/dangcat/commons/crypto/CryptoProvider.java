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
    public String decrypt(String sourceText, String charsetName);

    /**
     * �����㷨��
     * @param sourceText Դ���֡�
     * @param charsetName �ַ�����
     * @return �������֡�
     */
    public String encrypt(String sourceText, String charsetName);
}
