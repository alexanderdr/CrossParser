/*
 * JavaObjectObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class JavaObjectObject extends SObject{
    Object obj;
    /** Creates a new instance of JavaObjectObject */
    public JavaObjectObject(Object o,Script s) {
        super(s);
        obj = o;
        type = Scalar.Type.JAVAOBJECT;
    }
    
    public Scalar toScalar(){
        return SObject.toScalar(obj,script);
    }
    
    public void set(Scalar s){
        if((s instanceof JavaObjectObject)){
            obj = s.getObject();
        } else {
            System.out.println("ut oh");
        }
    }
    
    public Object getObject(){
        return obj;
    }
    
    public String getString(){
        return obj.toString();
    }
    
    public float getFloat(){
        if(obj instanceof Number){
            return ((Number)obj).floatValue();
        } else if(obj instanceof String){
            return Float.parseFloat(((String)obj));
        } else if(obj instanceof Character){
            return Float.parseFloat(((Character)obj).toString());
        }
        return 0f;
    }
    
    public int getInt(){
        if(obj instanceof Number){
            return ((Number)obj).intValue();
        } else if(obj instanceof String){
            return Integer.parseInt(((String)obj));
        } else if(obj instanceof Character){
            return Integer.parseInt(((Character)obj).toString());
        }
        return 0;
    }
    
}
