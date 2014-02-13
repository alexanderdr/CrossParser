/*
 * SMethodObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.lang.reflect.*;
public class JavaMethodObject extends SObject{
    
    Object obj;
    Method getter, setter;
    /** Creates a new instance of SMethodObject */
    public JavaMethodObject(Object o, Method get, Method set,Script s) {
        super(s);
        setter = set;
        getter = get;
        obj = o;
    }
    
    public Object getObject(){
        try{
            return getter.invoke(obj);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void set(Scalar o){
        Object temp = null;
        Class[] types = setter.getParameterTypes();
        if(types.length==0){
            System.out.println("Bad, trying to set something on a function with 0 parameters");
        }
        if(types[0].equals(String.class)){
            temp = o.getString();
        } else{
            temp = o;
        }
        try{
            setter.invoke(obj,temp);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public Scalar toScalar(){
        Object o = null;
        try{
            o = getter.invoke(obj);
        } catch (Exception e){
            e.printStackTrace();
        }
        return SObject.toScalar(o,script);
    }
    
}
