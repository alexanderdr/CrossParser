/*
 * Node.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class Node {
    
    int weight = Integer.MIN_VALUE; //arbitrary small number, need to be smaller than any user defined values
    String expr;
    int children = 2;
    boolean op, temp;
    Node parent, left, right;
    Script script;
    public Node(String s,boolean o,Script scr){
        script = scr;
        expr = s;
        op = o;
        
        if(script.getToken(s)!=null){
            weight = script.getToken(s).priority;
        } else {
            assert false;
            //Parsing should never reach here
        }
    }
    
    public Node(String s,int childre,Script scr,boolean op){
        this(s,op,scr);
        children = childre;
    }
    
    public Node(String s,int childre,Script scr){
        this(s,scr);
        this.children = childre;
    }
    
    public Node(String s,Script scr){
        script = scr;
        op = false;
        expr = s;
        if(script.getToken(s)!=null){
            weight = script.getToken(s).priority;
        } else {
            assert false;
            //parsing should never reach here
        }
    }
    
    public Node(Script scr){
        script = scr;
        this.temp = true;
        expr = "<<undefinedNodeExpr>>";
    }

    public void init(String s, boolean o){
        if(script.getToken(s)!=null){
            weight = script.getToken(s).priority;
        }
        if(temp==false) System.out.println("Initing non-null node");
        expr = s;
        op = o;
        temp = false;
    }
    
    public void init(Node n){
        if(temp==false) System.out.println("Initing non-null node");
        expr = n.expr;
        op = n.op;
        left = n.left;
        right = n.right;
        parent = n.parent;
        weight = n.weight;
        children = n.children;
        temp = false;
    }

    public void print(){
        System.out.println(expr);
    }

    public Node insert(Node x){
        if((left==null)||(children==1)){
            left = x;
        } else {
            if(right!=null){
                System.out.println("Tried to insert on non-null");
                x.print();
                Thread.dumpStack();
                return this;
            }
            right = x;
        }
        x.parent = this;
        return this;
    }

    public boolean full(){
        int count = 0;
        if(left!=null) count++;
        if(right!=null) count++;

        return count>=children;
    }
    
    public Node takeLesserChild(){
        if(children==0){
            return null;
        }
        if(children==1){
            Node temp = left;
            left = null;
            return temp;
        } else {
            Node temp = right;
            right = null;
            return temp;
        }
    }
    
    //for overwriting nodes for order of operations and unary operators
    public Node rsert(Node x){
        x.parent = this;
        right = x;
        return this;
    }
}
