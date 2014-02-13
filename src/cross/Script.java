/*
 * Script.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.ArrayList;
import java.lang.reflect.*;
public class Script {
    Language lang = new Language();
    Environment env = new Environment();
    CrossParser cp;
    String checker = "0000000";
    Object target;
    /** Creates a new instance of Script */
    public Script() {
        cp = new CrossParser(this);
    }
    
    public Script(Object o){
        cp = new CrossParser(this);
        target = o;
        env.addScalar("this",SObject.toScalar(o,this));
    }
    
    public CrossTree exec(String s){
        CrossTree ct = cp.parse(s);
        new Evaluator(cp.master.head,this).eval();
        return ct;
    }

    //For regression checks
    public void test(String s,String check){
        checker = "0000000";
        CrossTree temp= null;
        try{
            temp = exec(s);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(!(checker.equals(check))){
            System.out.println("Failure with "+s);
            System.out.println("Value "+checker+" not equal to "+check);
            System.out.println("Tree is...");
            if(temp!=null){
                temp.print();
            } else {
                System.out.println("very very null");
            }
        } else {
            //If we want verbose regression checks
            //System.out.println("Check passed for ["+s+"] ["+check+"]");
        }
    }
    
    public void loadLibrary(CrossLibrary c){
        //NYI
    }
    
    public void addScalar(String s,Scalar scal){
        env.addScalar(s,scal);
    }
    
    public Scalar getScalar(String s){
        return env.getScalar(s);
    }
    
    public void addToken(String s,int pri){
        addToken(s,pri,false);
    }
    
    public void addToken(String s, int pri, boolean drop){
        lang.addToken(s,pri,drop,false);
    }
    
    public void addBinding(Token t, Binding b){
        lang.addBinding(t,b);
    }
    
    public void addBinding(String s,int pri,Binding b){
        lang.addBinding(s,pri,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,Binding b){
        lang.addBinding(s,pri,drop,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,boolean un,Binding b){
        lang.addBinding(s,pri,drop,un,b);
    }
    
    public Binding getBinding(String s){
        return lang.getBinding(s);
    }
    
    public Token getToken(String s){
        return lang.getToken(s);
    }
    
    public ArrayList<Token> getPossibleTokens(String s){
        return lang.getPossibleTokens(s);
    }
    
    public ArrayList<Token> getPossibleTokens(char c){
        return getPossibleTokens(c+"");
    }
}
