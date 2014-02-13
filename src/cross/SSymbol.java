/*
 * SSymbol.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
class SSymbol extends Scalar{
    String value;
    
    public SSymbol(String s, Script scrip) {
        super(scrip);
        value = s;
        type=Type.SYMBOL;
    }
    
    public String getString(){
        return value;
    }
    
}
