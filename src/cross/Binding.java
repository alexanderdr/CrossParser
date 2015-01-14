/*
 * Binding.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class Binding {
    
    Script script;
    /** Creates a new instance of Binding */
    public Binding(Script script) {
        this.script = script;
    }
    
    public Binding(){
    }
    
    public Scalar exec(Node n,Evaluator e,Scalar.Type type){
        return new Scalar(script);
    }

    public SType getReturnType() {
        return TNull.instance;
    }

    public void registerOwningType(SType type) {
    }
    
}
