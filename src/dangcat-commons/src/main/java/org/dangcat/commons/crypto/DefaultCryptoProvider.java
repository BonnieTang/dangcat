package org.dangcat.commons.crypto;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DefaultCryptoProvider implements CryptoProvider
{
    @Override
    public String decrypt(String sourceText, String charsetName)
    {
        if (sourceText == null)
            return null;

        if (sourceText.length() == 0)
            return "";

        try
        {
            // Base64����
            byte[] decodeBytes = BASE64Coder.decode(sourceText);

            // TripleDes����
            byte[] decryptBytes = TripleDes.decrypt(decodeBytes);

            // ���ܴ���
            if (decryptBytes == null)
                return null;

            return new String(decryptBytes, charsetName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String encrypt(String sourceText, String charsetName)
    {
        if (sourceText == null)
            return null;

        if (sourceText.length() == 0)
            return "";

        try
        {
            // TripleDES����
            byte[] encryptBytes = TripleDes.encrypt(sourceText.getBytes(charsetName));
            // ���ܴ���
            if (encryptBytes == null)
                return null;
            // Base64����
            return new String(BASE64Coder.encode(encryptBytes));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
