/*
 * SObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.lang.reflect.*;
public abstract class SObject extends Scalar{
    
    public SObject(Script s){
        super(s);
    }
    
    public abstract Object getObject();
    public abstract void set(Scalar o);
    public abstract Scalar toScalar();
    
    public static Scalar toScalar(Object o, Script script){
        if(o instanceof Float){
            return new SFloat(((Float)o).floatValue(),script);
        } else if (o instanceof Integer){
            return new SInt(((Integer)o).intValue(),script);
        } else if (o instanceof Boolean){
            return new SBool(((Boolean)o).booleanValue(),script);
        } else if (o instanceof String){
            return new SString((String)o,script);
        } else if (o instanceof Class){
            return new JavaClassObject((Class)o,script);
        } else if(o instanceof Scalar){
            return ((Scalar)o);
        } else {
            return new JavaObjectObject(o,script);
        }
    }
    
    public static SObject getJava(Object o, Field f, Script s){
        return new JavaFieldObject(o,f,s);
    }
    
    public static SObject getJava(Object o, Method getter, Method setter, Script s){
        return new JavaMethodObject(o,getter,setter,s);
    }
    
}
