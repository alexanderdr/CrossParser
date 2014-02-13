/*
 * Evaluator.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class Evaluator {
    
    Node head;
    Script script;
    /** Creates a new instance of Evaluator */
    public Evaluator(Node head,Script s) {
        script = s;
        this.head = head;
    }
    
    public Scalar eval(){
        return exec(head,Scalar.Type.NONE);
    }
    
    public Scalar exec(Node n,Scalar.Type type){
        Scalar left = null, right = null;
        Binding b = null;
        try{
            b = script.getBinding(n.expr);
        } catch (NullPointerException npe){
            System.err.println("crap ["+n.expr+"]");
            npe.printStackTrace();
        }
        
        if(b==null){
            if(n instanceof BranchNode){
                return Scalar.buildScalar(n,script); //magic scalar
            } else {
                return Scalar.buildScalar(n.expr,script,type);
            }
        }
        
        return b.exec(n,this,type);
        

    }
    
}
