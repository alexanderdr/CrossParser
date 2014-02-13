/*
 * SBool.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SBool extends Scalar{
    
    boolean val;
    /** Creates a new instance of SBool */
    public SBool(boolean b,Script s) {
        super(s);
        val = b;
    }
    
    public int getInt(){
        if(val){
            return 1;
        } else {
            return 0;
        }
    }
    
    public float getFloat(){
        if(val){
            return 1f;
        } else {
            return 0f;
        }
    }
    
    public boolean getBoolean(){   
        return val;
    }
    
    public String getString(){
        return ""+val;
    }
    
    public Object getObject(){
        return new Boolean(val);
    }
    
    public void set(Scalar other){
        val = other.getBoolean();
    }
    
}
