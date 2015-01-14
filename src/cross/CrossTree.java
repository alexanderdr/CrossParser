/*
 * CrossTree.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class CrossTree {
    
    Node head;
    Script script;
    /** Creates a new instance of CrossTree */
    public CrossTree(Script script) {
        this.script = script;
        head = new Node(TNull.instance, script);
    }

    public CrossTree(Script script, Node head){
        this.script = script;
        this.head = head;
    }

    public Node getHead(){
        return head;
    }

    //don't want to override hashcode, just care about the binary tree
    public boolean isTreeEqual(CrossTree other){
        Node otherHead = other.head;
        return areNodesEqual(head, otherHead);
    }

    private boolean areNodesEqual(Node one, Node two){
        if(one == null && two == null) return true;
        if(one == null ^ two == null) return false;
        return one.expr.equals(two.expr) && areNodesEqual(one.left, two.left) && areNodesEqual(one.right, two.right);
    }

    public void print(){
        rprint(head, 0, System.out);
    }

    public void print(java.io.PrintStream out) {
        rprint(head, 0, out);
    }

    public static void rprint(Node n,int level, java.io.PrintStream out){
        if(n==null) return;
        for(int x = 0;x<level;x++){
            out.print("    ");
        }
        n.println(out);
        if (n.left != null) {
            out.print("L:");
            rprint(n.left, level + 1, out);
            if(n.left.parent==null){out.println("parentage inconsistancy");}
        }
        if (n.right != null) {
            out.print("R:");
            rprint(n.right, level + 1, out);
            if(n.right.parent==null){out.println("parentage inconsistancy");}
        }
    }

    public void inprint(){
        inprint(System.out);
    }

    public void inprint(java.io.PrintStream out) {
        inprintr(head, out);
        out.println();
    }

    public static void inprintr(Node n, java.io.PrintStream out){
        if(n == null) return;
        boolean wrap = n.left != null || n.right != null;
        if(wrap) out.print("(");
        inprintr(n.left, out);
        n.print(out);
        inprintr(n.right, out);
        if(wrap) out.print(")");
    }
}
