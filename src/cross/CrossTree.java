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
        head = new Node(script);
    }
    
    public void print(){
        rprint(head,0);
    }
    
    public static void rprint(Node n,int level){
        if(n==null) return;
        for(int x = 0;x<level;x++){
            System.out.print("    ");
        }
        n.print();
        if (n.left != null) {
            System.out.print("L:");
            rprint(n.left, level + 1);
            if(n.left.parent==null){System.out.println("parentage inconsistancy");}
        }
        if (n.right != null) {
            System.out.print("R:");
            rprint(n.right, level + 1);
            if(n.right.parent==null){System.out.println("parentage inconsistancy");}
        }
    }
}
