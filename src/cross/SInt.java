/*
 * SInt.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SInt extends Scalar{
    
    int val;
    /** Creates a new instance of SInt */
    public SInt(int i,Script s) {
        super(s);
        val = i;
        type = Type.INT;
    }
    
    public int getInt(){
        return val;
    }
    
    public float getFloat(){
        return (float)val;
    }
    
    public boolean getBoolean(){   
        return val!=0;
    }
    
    public String getString(){
        return ""+val;
    }
    
    public Object getObject(){
        return new Integer(val);
    }
    
    public void set(Scalar other){
        val = other.getInt();
    }
    
}
