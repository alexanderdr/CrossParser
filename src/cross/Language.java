/*
 * Language.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */

import java.util.Hashtable;
import java.util.*;
public class Language {
    
    Hashtable<String,Token> tokenset = new Hashtable<String,Token>();
    Hashtable<String,ArrayList<Token>> tokens = new Hashtable<String,ArrayList<Token>>();
    Hashtable<String,Binding> bindings = new Hashtable<String,Binding>();

    ArrayList<SType> types = new ArrayList<SType>();

    TSymbol symbolType = new TSymbol();
    TOperator opType = new TOperator();

    /** Creates a new instance of Language */
    public Language() {
    }

    public void addType(SType type) {
        types.add(type);
    }

    public ArrayList<SType> getTypes() {
        return types;
    }

    public void addToken(String s,int pri){
        addToken(s,pri,false,false);
    }
    
    public void addToken(String s, int pri, boolean drop,boolean un){
        Token t = new Token(s,pri,drop,un);
        String firstchar = s.charAt(0)+"";
        if(tokens.get(firstchar)==null){
            tokens.put(firstchar,new ArrayList<Token>());
        }
        tokens.get(firstchar).add(t);
        tokenset.put(s, t);
    }
    
    public void addToken(Token t){
        String firstchar = t.str.charAt(0)+"";
        if(tokens.get(firstchar)==null){
            tokens.put(firstchar,new ArrayList<Token>());
        }
        tokens.get(firstchar).add(t);
        tokenset.put(t.str, t);
    }
    
    public void addBinding(Token t, Binding b){
        addToken(t);
        bindings.put(t.str,b);
    }
    
    public void addBinding(String s,int pri,Binding b){
        addToken(s,pri);
        bindings.put(s,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,Binding b){
        addToken(s,pri,drop,false);
        bindings.put(s,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,boolean un,Binding b){
        addToken(s,pri,drop,un);
        bindings.put(s,b);
    }
    
    public Binding getBinding(String s){
        return bindings.get(s);
    }
    
    public Token getToken(String s){
        return tokenset.get(s);
    }
    
    public ArrayList<Token> getPossibleTokens(String s){
        return tokens.get(s);
    }
    
    public ArrayList<Token> getPossibleTokens(char c){
        return getPossibleTokens(c+"");
    }

    public SType getSymbolType() {
        return symbolType;
    }

    public SType getOpType() {
        return opType;
    }
}
