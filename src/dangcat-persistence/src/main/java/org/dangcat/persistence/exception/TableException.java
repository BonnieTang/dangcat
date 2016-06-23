package org.dangcat.persistence.exception;

/**
 * ���ݱ�������쳣��
 * @author dangcat
 * 
 */
public class TableException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public TableException()
    {
    }

    public TableException(Exception cause)
    {
        super(cause);
    }

    public TableException(String message)
    {
        super(message);
    }

    public TableException(String message, Exception cause)
    {
        super(message, cause);
    }
}
