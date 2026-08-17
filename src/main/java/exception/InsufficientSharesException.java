package exception;

public class InsufficientSharesException extends Exception
{
    public InsufficientSharesException(String message)
    {
        super(message);
    }
}