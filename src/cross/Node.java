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

    SType type;

    /*public Node(String s,boolean o,Script scr){
        this(null, s, o, scr);
    }
    
    public Node(String s,int childre,Script scr,boolean op){
        this(null,s,op,scr);
        children = childre;
    }
    
    public Node(String s,int childre,Script scr){
        this(null,s,scr);
        this.children = childre;
    }*/
    
    public Node(String s, Script scr){
        this((SType)null, s, scr);
    }

    public Node(SType type, String s, boolean o, Script scr){
        this.type = type;

        script = scr;
        expr = s;
        op = o;

        Token token = null;

        if(script.getToken(s)!=null){
            //weight = script.getToken(s).priority;
            token = script.getToken(s);
        } else {
            if(type != null && type.getBinding(s) != null && type.getBinding(s).getToken() != null) {
                token = type.getBinding(s).getToken();
            } else {
                assert false;
            }
            //probably need to get priority here if we are coming from a type-local operator
            //Parsing should never reach here
        }
        if(token != null) {
            weight = token.priority;
            if(token.unary) {
                children = 1;
            }
        }
    }

    public Node(SType type, String s, boolean o, int weight, int children, Script scr) {
        this.type = type;

        script = scr;
        expr = s;
        op = o;

        this.children = children;

        this.weight = weight;
    }

    public Node(SType type,String s,int childre,Script scr,boolean op){
        this(type,s,op,scr);
        children = childre;
    }

    public Node(SType type, String s,int children,Script scr){
        this(type, s, children, scr, false);
        //this.children = childre;
    }

    public Node(SType type, String s,Script scr){
        script = scr;
        op = false;
        expr = s;
        this.type = type;
        if(script.getToken(s)!=null){
            weight = script.getToken(s).priority;
        } else {
            //we're parsing a literal of some kind (numeric or variable name)

        }
    }

    public Node(SType type, Script scr){
        script = scr;
        this.type = type;
        this.temp = true;
        expr = "<<undefinedNodeExpr>>";
    }

    public void init(String s, boolean o){
        if(temp==false) System.out.println("Initing non-null node");
        if(script.getToken(s)!=null){
            weight = script.getToken(s).priority;
        }
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
        type = n.type;
    }

    public void println(java.io.PrintStream out) {
        out.println(expr);
    }

    public void print(java.io.PrintStream out) {
        out.print(expr);
    }

    public void println(){
        println(System.out);
    }

    public void print(){
        print(System.out);
    }

    public Node insert(Node x){
        if((left==null)||(children==1)){
            left = x;
        } else {
            if(right!=null){
                System.out.println("Tried to insert on non-null");
                x.println();
                Thread.dumpStack();
                return this;
            }
            right = x;
        }
        x.parent = this;
        return this;
    }

    public Node reparent(Node other) {
        if(other.parent != null) {
            if(other.parent.left == other) {//this is not particularly pretty
                other.parent.left = this;
            } else {
                other.parent.right = this;
            }
        }
        parent = other.parent;
        insert(other);

        return this;
    }

    public SType getType() {
        return type;
    }

    public boolean full(){
        int count = 0;
        if(left!=null) count++;
        if(right!=null) count++;

        return count>=children;
    }
    
    public Node getLesserChild(){
        if(children==0){
            return null;
        }
        if(children==1){
            Node temp = left;
            return temp;
        } else {
            Node temp = right;
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
