/*
 * SReference.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SReference extends Scalar{
    
    //maybe a proxy class would be useful?
    
    String val;
    /** Creates a new instance of SReference */
    public SReference(String s,Script script) {
        super(script);
        val = s;
        Scalar temp = script.getScalar(val);
        if(temp!=null){
            type = temp.type;
        } else {
            type = Type.REFERENCE;
        }
    }
    
    public int getInt(){
        return script.getScalar(val).getInt();
    }
    
    public float getFloat(){
        return script.getScalar(val).getFloat();
    }
    
    public boolean getBoolean(){   
        return script.getScalar(val).getBoolean();
    }
    
    public String getString(){
        return script.getScalar(val).getString();
    }
    
    public Object getObject(){
        return script.getScalar(val).getObject();
    }
    
    //public Scalar get(){
     //   return script.getScalar(val).get();
    //}
    
    public String getReferant(){
        return val;
    }
    
    public Scalar getRefScalar(){
        return script.getScalar(val);
    }
    
    /*public Scalar set(Scalar sc){
        script.addScalar(val,sc);
        return this;
    }*/
    
}
