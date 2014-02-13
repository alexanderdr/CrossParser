/*
 * SMagic.java
 *
 * Control statements use this class
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class SMagic extends Scalar{
    
    Node val;
    /** Creates a new instance of SMagic */
    public SMagic(Node n,Script s) {
        super(s);
        val = n;
    }
    
    public Scalar eval(){
        return (new Evaluator(val,script)).eval();
    }
    
    public Scalar eval(Scalar arg){
        if(!(val instanceof BranchNode)){
            System.out.println("Bad things are happening with a control statement");
            //This is a problem
            return new Scalar(script);
        } else {
            Node n = ((BranchNode)val).getBranch(arg);
            if(n==null){
                return new Scalar(script);
            }
            return new Evaluator(n,script).eval();
        }
    }
    
}
