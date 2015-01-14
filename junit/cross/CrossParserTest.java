package cross;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Dar
 * Date: 3/5/14
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrossParserTest {
    @Test
    public void testMathParses() throws Exception {
        Script script = new Script();
        CrossLibrary.defaultLibrary.loadOnScript(script);
        CrossParser cp = new CrossParser(script);

        TreeBuilder tb = new TreeBuilder(script);

        //what the linq-like syntax replaces
        /*Node root = new Node("+", script);
        root.insert(new Node("1", script));
        root.insert(new Node("2", script));*/
        Node root = tb.beginTree("+").addLeft("1").addRight("2").getRoot();
        CrossTree expected = new CrossTree(script, root);
        CrossTree result = cp.parse("1+2");
        assertEquals(expected.isTreeEqual(result), true);

        root = tb.beginTree("+").addRight("4").amLeft("+").addLeft("1").amRight("*").addLeft("2").addRight("3").getRoot();
        expected = new CrossTree(script, root);
        result = cp.parse("1+(2*3)+4");
        assertEquals(expected.isTreeEqual(result), true);

        result = cp.parse("1+2*3+4");
        assertEquals(expected.isTreeEqual(result), true);

        root = tb.beginTree("+").addRight("4").amLeft("*").addRight("3").amLeft("+").addLeft("1").addRight("2").getRoot();
        expected = new CrossTree(script, root);
        result = cp.parse("(1+2)*3+4");
        assertEquals(expected.isTreeEqual(result), true);
        result = cp.parse("((((1+2))))*3+4");
        assertEquals(expected.isTreeEqual(result), true);

    }

    //TODO: replace our assertEquals(tree.isEquals(other), true) with this, and produce the additional output that our natural test() method does
    public void assertTreesAreEquals(Node one, Node two) {

    }

}

class TreeBuilder{
    Script s;
    Node currentNode = null;
    Node rootNode = null;
    TreeBuilder(Script s){
        this.s = s;
    }

    TreeBuilder beginTree(String expr){
        currentNode = new Node(expr, s);
        rootNode = currentNode;
        return this;
    }

    TreeBuilder addNode(String expr){
        if(currentNode == null){
            currentNode = new Node(expr, s);
            rootNode = currentNode;
        } else {
            currentNode.insert(new Node(expr, s));
        }
        return this;
    }

    TreeBuilder addLeft(String expr){
        if(currentNode == null){
            //we have a problem, let's just NPE
        }
        Node temp = new Node(expr, s);
        currentNode.left = temp;
        temp.parent = currentNode;
        return this;
    }

    TreeBuilder addRight(String expr){
        if(currentNode == null){
            //we have a problem, let's just NPE
        }
        Node temp = new Node(expr, s);
        currentNode.right = temp;
        temp.parent = currentNode;
        return this;
    }
    //add and move
    TreeBuilder amLeft(String expr){
        addLeft(expr);
        moveLeft();
        return this;
    }

    TreeBuilder amRight(String expr){
        addRight(expr);
        moveRight();
        return this;
    }

    TreeBuilder moveLeft(){
        currentNode = currentNode.left;
        return this;
    }

    TreeBuilder moveRight(){
        currentNode = currentNode.right;
        return this;
    }

    TreeBuilder moveUp(){
        currentNode = currentNode.parent;
        return this;
    }

    TreeBuilder clearTree(){
        currentNode = null;
        return this;
    }

    Node getRoot(){
        return rootNode;
    }

}