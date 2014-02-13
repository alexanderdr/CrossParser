/*
 * SFieldObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.lang.reflect.*;
public class JavaFieldObject extends SObject{
    
    Field val;
    Object obj;
    /** Creates a new instance of SFieldObject */
    public JavaFieldObject(Object o,Field f,Script s) {
        super(s);
        obj = o;
        val = f;
    }
    
    public Object getObject(){
        try{
            return val.get(obj);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void set(Scalar o){
        Object temp = null;
        if(val.getType().equals(String.class)){
            temp = o.getString();
        } else if(val.getType().equals(int.class)){
            temp = o.getInt();
        } else{
            temp = o;
        }
        try{
            val.set(obj,temp);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public Scalar toScalar(){
        Object o = null;
        try{
            o = val.get(obj);
        } catch (Exception e){
            e.printStackTrace();
        }
        return SObject.toScalar(o,script);
    }
    
}
