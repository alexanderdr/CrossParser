/*
 * BinaryBinding.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public abstract class BinaryBinding extends Binding{
    
    /** Creates a new instance of BinaryBinding */
    public BinaryBinding(Script script) {
        super(script);
    }
    
    public BinaryBinding(){
    }
    
    public abstract Scalar eval(Scalar left, Scalar right, Scalar.Type type);
    
    @Override
    public Scalar exec(Node n, Evaluator e,Scalar.Type type){
        Scalar left = null, right = null;
        if(n.left != null){
            left = e.exec(n.left,Scalar.Type.NONE);
        }   
        if(n.right != null){
            right = e.exec(n.right,Scalar.Type.NONE);
        }
        return eval(left,right,type);
    }
}
