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

    SType left, right, returns;

    /** Creates a new instance of BinaryBinding */
    public BinaryBinding(Script script) {
        super(script);
    }
    
    public BinaryBinding(){
    }

    public BinaryBinding(SType returns) {
        this.returns = returns;
    }

    @Override
    public void registerOwningType(SType owning) {
        if(returns == null){
            returns = owning;
        } else {
            assert false; //unlikely to be common, but just to be sure (with the test cases...)
        }
    }

    @Override
    public SType getReturnType() {
        return returns;
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
