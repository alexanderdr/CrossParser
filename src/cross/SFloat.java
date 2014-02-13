/*
 * SFloat.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SFloat extends Scalar{
    float val;
    /** Creates a new instance of SFloat */
    public SFloat(float f,Script s) {
        super(s);
        val = f;
        type = Type.FLOAT;
    }
    
    public int getInt(){
        return (int)val;
    }
    
    public float getFloat(){
        return val;
    }
    
    public boolean getBoolean(){   
        return ((int)val)!=0;
    }
    
    public String getString(){
        return ""+val;
    }
    
    public void set(Scalar other){
        val = other.getFloat();
    }
    
}
