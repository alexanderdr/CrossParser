/*
 * CrossParser.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.*;
import java.util.regex.*;
public class CrossParser {
    
    CrossTree master;
    Script script;
    /** Creates a new instance of CrossParser */
    public CrossParser(Script s) {
        script = s;
        //master = new PTree();
    }

    public Script getScript() {
        return script;
    }

    public CrossTree parse(String s){
        master = new CrossTree(script);
        Node temp = null;
        try{
            temp = parse(s.toCharArray(),master.head);
        } catch (Exception e){
            if(e instanceof NoSuchTokenException){
                System.err.println(((NoSuchTokenException)e).token);
            }
            e.printStackTrace();
        }
        master.head = temp;
        return master;
    }
    
    public Node parse(char[] expr){
        try{
            return parse(expr,new Node(TNull.instance, script));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public Node parse(char[] expr, Node n) throws NoSuchTokenException{
        //System.out.println("Parsing [["+new String(expr)+"]]");
        Node current = n;
        int pointer = 0;
        char cur;
        out: while(pointer<expr.length){
            cur = expr[pointer];
            if((cur==' ')||(cur=='\n')||(cur=='\t')||(cur=='\r')){ //this should handle whitespace characters
                pointer++;
                continue;
            }

            SType s = script.getCandidateType(cur);
            if(s != null) {
                ConsumeResult cr = s.consume(this, expr, pointer);
                if(cr.getNode() != null) {
                    current = insert(current, cr.getNode(), false);
                    pointer = cr.getPointer();
                    continue;
                }
            }

            ArrayList<Token> list = null;
            //Dealing with string based tokens including operators

            boolean scriptLevelFunction = false;

            if(current != null) {
                list = (ArrayList<Token>)current.getType().getPossibleTokens(cur).clone();
                filterImpossibleTokens(list, expr, pointer);
                //this is mostly a secondary path until we transition to purely typed operators, then it can be removed
                if (list.size() < 1) {
                    list = script.getPossibleTokens(cur);
                    if(list != null) {
                        list = (ArrayList<Token>)list.clone();
                    }
                    scriptLevelFunction = true;
                    filterImpossibleTokens(list, expr, pointer);
                }
            }
            //can remove the null check once we no longer use script.getPossibleTokens()
            if(list != null && list.size() > 0) {
                //found a script-level function, or a type-local operator
                //if it's a script-level function, we need to insert current as a child of the function (making sure to preserve parent)
                //if it's an operator, we need to insert current as a child of the operator, taking into account precedence.

                if(list.size() > 1) {
                    //list = (ArrayList<Token>)list.clone();
                    int consideredBytes = 1;
                    ArrayList<Token> toRemove = new ArrayList<Token>();
                    Token bestMatch = null;
                    while(list.size() > 1) {

                        char[] tempAr = new char[consideredBytes];
                        System.arraycopy(expr, pointer, tempAr, 0, consideredBytes);
                        String substr = new String(tempAr);

                        for(Token t: list) {
                            if(!t.couldMatch(substr)) {
                                toRemove.add(t);
                            }
                            //this should clean out smaller tokens which match our substring as we get increasingly long matches
                            if(t.exactMatch(substr) && (bestMatch == null || t.length() >= bestMatch.length())) {
                                if(bestMatch != null) {
                                    if(t.length() == bestMatch.length()) {
                                        throw new RuntimeException("We seem to have a type with two identical tokens \'"+t.getString()+"\' \'"+bestMatch.getString()+"\'");
                                    }
                                    toRemove.add(bestMatch);
                                }

                                bestMatch = t;
                            }
                        }

                        list.removeAll(toRemove);
                        consideredBytes++;
                    }
                }

                if(list.size() == 1) {
                    SType opResult = TNull.instance;
                    if(!scriptLevelFunction) {
                        opResult = current.getType().getBinding(list.get(0).getString()).getReturnType();
                    }
                    String tokenStr = list.get(0).getString();
                    if(!tokenStr.equals(new String(Arrays.copyOfRange(expr, pointer, pointer+tokenStr.length())))) {
                        //this is unusual
                        System.out.println("This should be rare");
                        current.getType().getPossibleTokens(cur);
                    }
                    Node tempNode = new Node(opResult/*script.getOpType()*/, tokenStr, true, script);
                    current = insert(current, tempNode, false);
                    pointer += tokenStr.length();
                } else {
                    assert false; //this should never happen, we should never eliminate all possible ops
                }

            } else {
                //found a symbol, consume until we reach a non-letter
                ConsumeResult cr = script.getSymbolType().consume(this, expr, pointer);
                current = insert(current, cr.getNode(), false);
                pointer = cr.getPointer();
            }

            //print tree after every pass
            /*Node t = current;
            while(t.parent != null) t = t.parent;
            CrossTree.rprint(t, 0, System.out);*/


        }
        while(current.parent!=null){
            current = current.parent;
        }
        return current;
    }

    public void filterImpossibleTokens(ArrayList<Token> list, char[] expr, int pointer) {
        if(list == null) {
            return;
        }
        ArrayList<Token> toRemove = new ArrayList<Token>();
        for(int i = 0; i < list.size(); i++) {
            Token t = list.get(i);
            int tokenLength = t.getString().length();
            if(pointer + tokenLength >= expr.length) {
                toRemove.add(t);
                break;
            }
            String candidate = new String(expr, pointer, tokenLength);
            if(!t.couldMatch(candidate)) {
                toRemove.add(t);
                break;
            }
        }
        list.removeAll(toRemove);
    }

    public Node insert(Node current, Node ins, boolean override){

        if(ins == null) {
            throw new RuntimeException("Trying to insert a null node");
        }

        Node toRet = current;

        //first node in the tree, just replace the placeholder node with a real one
        if(current.temp) {
            current.init(ins);
            toRet = current;
        } else if(current.op && !current.full()) {
            //easy case, we are looking at a non-full op and have a non-op, we should insert the non-op into the op
            current.insert(ins);
            toRet = ins;
        } else if(current.full() && current.op){
            //have to decide based on weight and type of ins, do we displace the current right node or what
            if(ins.op) {
                if(ins.weight > current.weight) {
                    ins.reparent(current.getLesserChild());
                    current.insert(ins);
                    toRet = ins;
                } else {
                    //ins.insert(current);
                    //toRet = ins;
                    toRet = climbInsert(current, ins);
                }
            } else {
                //make <<notok>> operator
                //(SType type, String s, boolean o, int weight, int children, Script scr)
                Node n = new Node(script.getOpType(), "<<notok>>", true, 32767, 2, script);
                n.reparent(current);
                n.insert(ins);
                toRet = n;
                //throw new RuntimeException("Don't know how to handle non-op in full tree");
            }
        } else if(!current.op && ins.op) {
            if(current.parent != null && current.parent.weight < ins.weight) {
                ins.reparent(current);
                toRet = ins;
            } else {
                //we don't have priority, so climb the tree until we hit null or we have priority over the current op or we hit an empty node
                toRet = climbInsert(current, ins);
            }
        } else if(!current.op && !ins.op) {
            Node n = new Node(script.getOpType(), "<<notok>>", true, 32767, 2, script);
            n.reparent(current);
            n.insert(ins);
            toRet = n;
        }

        /*if(((t!=null)&&(t.drop))||(ins instanceof BranchNode)||debugdrop){
            return ins;
        } else {
            return current;
        }*/

        return toRet;
    }

    private Node climbInsert(Node current, Node ins) {
        Node temp = current.parent;
        Node last = current;
        while (temp != null && temp.full() && (temp.op && ins.weight <= temp.weight)) {
            last = temp;
            temp = temp.parent;
        }
        if (temp == null) {
            ins.insert(last);
        } else if (!temp.full()) {
            temp.insert(ins);
        } else if (temp.weight < ins.weight){
            ins.reparent(temp.getLesserChild());
        }else {
            assert false;
        }
        Node toRet = ins;

        return toRet;
    }

    public static boolean isLetter(char x){
        int keyCode = (int)(x);
        //A-Z, a-z on the ascii table
        return ((65 <= keyCode && keyCode <= 90) || (97 <= keyCode && keyCode <= 122));
    }

}
