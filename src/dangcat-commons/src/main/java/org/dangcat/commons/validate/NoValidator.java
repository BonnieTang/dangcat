package org.dangcat.commons.validate;

public class NoValidator extends RegexValidator
{
    private static final String PATTERN = "[a-zA-Z0-9_]{";

    public NoValidator(int minLength, int maxLength)
    {
        // ����5-16�ֽڣ�������ĸ�����»���
        super(PATTERN + minLength + "," + maxLength + "}$", true);
    }
}
