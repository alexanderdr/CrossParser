/*
 * Environment.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.HashMap;
public class Environment {
    
    HashMap<String, Scalar> scalars = new HashMap<String, Scalar>();
    /** Creates a new instance of Environment */
    public Environment() {
    }
    
    public void addScalar(String s,Scalar scal){
        scalars.put(s,scal);
    }
    
    public Scalar getScalar(String s){
        return scalars.get(s);
    }
    
}
