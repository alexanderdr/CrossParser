/*
 * NoSuchTokenException.java
 *
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class NoSuchTokenException extends ParseException{
    
    public char token;
    /** Creates a new instance of NoSuchTokenException */
    public NoSuchTokenException(char c) {
        token = c;
    }
    
}
