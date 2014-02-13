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
            return parse(expr,new Node(script));
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
            //Dealing with parentheses
            if(cur=='('){
                int count = 1;
                char temp=cur;
                int ptr = pointer;
                while(count>0){
                    ptr++;
                    temp = expr[ptr];
                    if(temp=='('){
                        count++;
                    }
                    if(temp==')'){
                        count--;
                    }
                    if(ptr>expr.length){
                        System.out.println("Error, no closing parentheses found "+expr);
                    }
                }
                int len = (ptr-1)-pointer;
                char[] tempc = new char[len];
                System.arraycopy(expr,pointer+1, tempc, 0, len);
                Node nod = parse(tempc);

                nod.weight = Integer.MAX_VALUE; //no displacing this node.. ever.
                current = insert(current,nod,false);
                pointer = ptr+1;
                continue;
            }
            //blocks
            if(cur=='{'){
                int count = 1;
                char temp = cur;
                int ptr = pointer;

                while(count>0){
                    ptr++;
                    temp = expr[ptr];
                    if(temp=='{'){
                        count++;
                    }
                    if(temp=='}'){
                        count--;
                    }
                    if(ptr>expr.length){
                        System.out.println("Error, no closing braces found "+expr+" opening at char "+ptr);
                    }
                }
                int len = (ptr-1)-pointer;
                char[] tempc = new char[len];
                System.arraycopy(expr,pointer+1, tempc, 0, len);
                BranchNode tempblock;
                if(!(current instanceof BranchNode)){
                    tempblock = new BranchNode(script);
                } else {
                    tempblock = (BranchNode)current;
                }
                
                tempblock.insert(parse(tempc));
                
                if(!(current instanceof BranchNode)){
                    current = insert(current,tempblock,true);
                }

                pointer = ptr+1;
                continue;
            }

            //New way of dealing with numeric values.
            if(Pattern.matches("(\\.?\\d+)|(\\d+.?\\d*)",new String(expr,pointer,1))){
                int diff = 0;
                String temp = new String(expr,pointer,diff+1);
                while(Pattern.matches("(\\.?\\d+)|(\\d+\\.?\\d*)",(temp = new String(expr,pointer,diff+1)))){
                    diff++;
                    if((pointer+diff+1>expr.length)){
                      break;
                    }
                }
                current = insert(current,new String(expr,pointer,diff),0);
                pointer += diff;
                continue;
            }
            
            if(cur=='\"'){
                int ptr = pointer+1;
                char temp = expr[ptr];
                while(temp!='\"'){
                    ptr++;
                    if(ptr>=expr.length){
                        ptr = expr.length;
                        break;
                    }
                    temp = expr[ptr];
                }
                int len=((ptr)-pointer);
                char[] tempc = new char[len];
                System.arraycopy(expr,pointer, tempc, 0, len); //need first " for scalar identification

                current = insert(current,new String(tempc),0);
                pointer = ptr+1;
                continue;
            }
            
            ArrayList<Token> list;
            //Dealing with string based tokens including operators
            if((list = script.getPossibleTokens(cur))!=null){
                int len = 1;
                String temp;
                find: while(list.size()>=1){

                    temp = new String(expr,pointer,len);
                    boolean nomatches = true;
                    Token found = null;

                    for(Token t:list){
                        String tstring = t.str;
                        if(t.str.length()>temp.length()){
                            if(expr.length<=(pointer+len+1)){
                                //System.out.println("eep");
                                continue;
                            }
                            if(t.str.substring(0,temp.length()+1).equals(new String(expr,pointer,len+1))){
                                len++;
                                continue find;
                            }
                        }
                        
                        if(t.str.equals(temp)){
                            nomatches = false;
                            found = t;
                        }
                    }
                    if(nomatches){
                        if((pointer+len>=expr.length)||(!isLetter(expr[pointer + len]))){
                            if(len<=0){
                                throw new NoSuchTokenException(cur);
                            }
                            current = insert(current,temp,0);
                            pointer = pointer+len;
                            continue out;
                        }
                    }
                    if(found!=null){
                        int childLimit;
                        if(found.unary){
                            childLimit = 1;
                        } else {
                            childLimit = 2;
                        }
                        current = insert(current,temp,childLimit,true);
                        //current.insert(new Node(temp));
                        pointer = pointer + len;
                        continue out;
                    } else{
                        //System.out.println("Found a ghostly token "+temp);
                    }
                    len++;
                }
                //Execution should never reach here, but we might be able to handle it anyway
                //list.size should always be >= 1 or the list should be null
                assert false;
                current = insert(current,new String(expr,pointer,len),0);
                pointer = pointer + len;
                continue;
            } else {
                //lets get a token anyway
                //There are problems if this happens with a non-letter character, which will cause a heap overflow
                //This problem can happen with non-standard characters, such as \033
                int ptr = pointer;
                char temp = expr[ptr];
                while(isLetter(temp)){
                    ptr++;
                    if(ptr>=expr.length){
                        ptr = expr.length;
                        break;
                    }
                    temp = expr[ptr];
                }
                int len=(ptr-pointer);
                if(len<=0){
                    throw new NoSuchTokenException(cur);
                }
                char[] tempc = new char[len];
                System.arraycopy(expr,pointer, tempc, 0, len);
                current = insert(current,new String(tempc),0,false);
                pointer = ptr;
                continue;
            }
        }
        while(current.parent!=null){
            current = current.parent;
        }
        return current;
    }
    
    public Node insert(Node current, String val, int childCount){
        Node temp = new Node(val,childCount,script);
        return insert(current,temp,false);
    }
    
    public Node insert(Node current, String val, int childCount,boolean op){
        Node temp = new Node(val,childCount,script,op);
        return insert(current,temp,false);
    }
    
    public Node insert(Node current,String val){
        Node temp = new Node(val,script);
        return insert(current,temp,false);
    }
    
    public Node insert(Node current, Node ins, boolean override){
        Node temp;
        boolean debugdrop = false;
        if(current.temp){
            current.init(ins);
            return current;
        }
        //This is not an ideal use of instanceof... probably should extend Node to abstract this away
        if((!override)&&(current instanceof BranchNode)){
            current = current.parent;
            //can't mess with the right node on the control statement
            if(current.parent!=null){
                current = current.parent;
            }
            while((current.full())&&(current.weight>=ins.weight)){
                if(current.parent!=null)
                    current = current.parent;
                else
                    break;
            }
        }
        if(current.full()){

            while((current.full())&&(current.weight>=ins.weight)){
                if(current.parent!=null){
                    current = current.parent;
                }else{
                    if(ins.full()){
                        //System.out.println("This is interesting, creating a null op");
                        Node nonOp = new Node("<<notok>>",true,script);
                        nonOp.insert(ins);
                        nonOp.insert(current);
                        return nonOp;
                    }

                    ins.insert(current);
                    return ins;
                }
            }

            if(!current.full()){
                //return current; fall through to return the correct node
            } else { //current.full()

                temp = current.takeLesserChild();//current.right;

                //this means the node is full even after having a child removed..
                //which means the child that was removed, will almost certainly be null
                //which means that inserting it will cause a null pointer exception at x.parent = this
                if(current.full()){
                    if(!ins.full()){
                        ins.insert(current);
                    } else {
                        Node nonOp = new Node("<<notok>>",true,script);
                        nonOp.insert(current);
                        nonOp.insert(ins);
                        return nonOp;
                    }
                } else {
                    if(ins.full()){
                        //System.out.println("This is interesting, creating a null op "+temp.expr);
                        Node nonOp = new Node("<<notok>>",true,script);
                        if(current.expr.equals("<<notok>>")){
                            current.insert(temp);
                            nonOp.insert(current);
                            nonOp.insert(ins);
                            return nonOp;
                        }
                        nonOp.insert(temp);
                        nonOp.insert(ins);
                        current.insert(nonOp);
                        return current;//nonOp;
                    }
                    ins.insert(temp);
                    current.insert(ins);
                }
                return ins;
            }
        }

        Token t = script.getToken(ins.expr);
        if((t!=null)&&(t.displace)&&(!ins.full())){

            if(!current.left.op||current.left.expr.equals(ins.expr)){
                ins.insert(current.left);
                current.left = null;
                current.insert(ins);
            }
        } else {
            current.insert(ins);
        }
        if(((t!=null)&&(t.drop))||(ins instanceof BranchNode)||debugdrop){
            return ins;
        } else {
            return current;
        }

    }

    public boolean isLetter(char x){
        int keyCode = (int)(x);
        //A-Z, a-z on the ascii table
        return ((65 <= keyCode && keyCode <= 90) || (97 <= keyCode && keyCode <= 122));
    }

}
