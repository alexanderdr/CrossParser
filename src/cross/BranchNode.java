/*
 * BlockNode.java
 *
 * Control statements use this node.
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.*;
public class BranchNode extends Node{

    //string used rather than boolean value to allow switch statements
    Hashtable<String,Node> lines = new Hashtable<String,Node>();
    
    /** Creates a new instance of BranchNode */
    public BranchNode(SType type, Script scr) {
        super(type, scr);
        this.temp = false;
        this.expr = "Branch Node";
        this.weight = 100000;
    }
    
    public Node insert(Node n){
        if(lines.size()==0){
            lines.put("true", n);
        } else {
            lines.put("false", n);
        }
        n.parent = this;
        //lines.put(n);
        return this;
    }
    
    public Node getBranch(Scalar arg){
        //may only work for 'if' statements, needs more testing.
        //System.out.println(arg.getString());
        arg = new SBool(arg.getBoolean(),script);
        //System.out.println(arg.hashCode());
        //for(String s:lines.keySet()){
        //    System.out.println(s);
        //}
        
        return lines.get(arg.getString());
    }
    
    public boolean full(){
        return false;
    }

    public void println() {
        println(System.out);
    }

    public void println(java.io.PrintStream out){
        System.out.println("Branch node { " +this);
        for(Node n:lines.values()){
            CrossTree.rprint(n,0,out);
        }
        System.out.println("}");
    }
}
