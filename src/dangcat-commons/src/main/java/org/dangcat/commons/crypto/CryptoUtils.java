package org.dangcat.commons.crypto;

import org.dangcat.commons.reflect.ReflectUtils;
import org.dangcat.commons.utils.ValueUtils;

/**
 * ƽ̨�ļ��ܽ��ܹ��� http://zh.wikipedia.org/wiki/Base64
 * base64ʹ�õ��ַ�������Сд��ĸ��26��������10�����֣��ͼӺš�+����б�ܡ�/���� һ��64���ַ����Ⱥš�=��������Ϊ��׺��;��
 * ������base64����ɼ� RFC 1421�� RFC 2045�����������ݱ�ԭʼ�����Գ���Ϊԭ����4/3
 */
public class CryptoUtils {
    private static final String DANGCAT_CRYPTO_PROVIDER = "dangcat.Crypto.Provider";
    private static final String DEFAULT_CHARSETNAME = "UTF-8";
    private static CryptoProvider cryptoProvider = null;

    /**
     * ����
     *
     * @param src ����
     * @return ԭ�ģ�������null
     */
    public static String decrypt(String sourceText) {
        return decrypt(sourceText, DEFAULT_CHARSETNAME);
    }

    /**
     * ����
     *
     * @param src ����
     * @return ԭ�ģ�������null
     */
    public static String decrypt(String sourceText, String charsetName) {
        return getCryptoProvider().decrypt(sourceText, charsetName);
    }

    /**
     * ����,��֧��'\0'��β�Ĵ�
     *
     * @param text ԭ��
     * @return ���ģ�������null
     */
    public static String encrypt(String sourceText) {
        return encrypt(sourceText, DEFAULT_CHARSETNAME);
    }

    /**
     * ����,��֧��'\0'��β�Ĵ�
     *
     * @param text ԭ��
     * @return ���ģ�������null
     */
    public static String encrypt(String sourceText, String charsetName) {
        return getCryptoProvider().encrypt(sourceText, charsetName);
    }

    private static CryptoProvider getCryptoProvider() {
        if (cryptoProvider == null) {
            String cryptoProviderClass = System.getProperty(DANGCAT_CRYPTO_PROVIDER);
            if (!ValueUtils.isEmpty(cryptoProviderClass))
                cryptoProvider = (CryptoProvider) ReflectUtils.newInstance(cryptoProviderClass);
        }
        if (cryptoProvider == null)
            cryptoProvider = new DefaultCryptoProvider();
        return cryptoProvider;
    }

    public static void main(String[] args) {
        System.out.println(encrypt(args[0]));
    }
}
