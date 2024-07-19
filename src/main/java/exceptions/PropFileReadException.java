package exceptions;

import java.io.IOException;

public class PropFileReadException extends RuntimeException{
    public PropFileReadException(IOException e) {
        super(e);
    }
}
