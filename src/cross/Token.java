/*
 * Token.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class Token {
    int priority;
    String str;
    boolean drop = false;
    boolean unary = false;
    boolean displace = false; //displaces symbols and makes them it's children
    /** Creates a new instance of Token */
    public Token(String s, int pri) {
        priority = pri;
        str = s;
    }
    
    public Token(String s, int pri, boolean drop){
        this(s,pri);
        this.drop = drop;
    }
    
    public Token(String s, int pri, boolean drop, boolean un){
        this(s,pri,drop);
        unary = un;
    }
    
    public Token(String s, int pri, boolean drop, boolean un, boolean displace){
        this(s,pri,drop,un);
        this.displace = displace;
    }
    
}
