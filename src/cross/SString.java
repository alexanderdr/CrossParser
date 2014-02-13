/*
 * SString.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SString extends Scalar{
    
    String val;
    /** Creates a new instance of SString */
    public SString(String s,Script script) {
        super(script);
        val = s;
        type = Scalar.Type.STRING;
    }
    
    public int getInt(){
        try{
            return Integer.parseInt(val);
        } catch (Exception e){
        }
        return 0;
    }
    
    public float getFloat(){
        try{
            return Float.parseFloat(val);
        } catch (Exception e){
        }
        return 0.0f;
    }
    
    public boolean getBoolean(){   
        try{
            return Boolean.parseBoolean(val);
        } catch (Exception e){
        }
        return false;
    }
    
    public Object getObject(){
        if(val.length()==1){
            return val.charAt(0);
        } else {
            return val;
        }
    }
    
    public Scalar length(){
        return new SInt(val.length(),script);
    }
    
    public String getString(){
        return val;
    }
    
    public void set(Scalar s){
        val = s.getString();
    }
    
}
