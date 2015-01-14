package cross;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Dar on 5/9/2014.
 */
public abstract class SType {
    public abstract boolean matches(char c);
    public abstract ConsumeResult consume(CrossParser cp, char[] expr, int pointer);
    public String getName() {
        return "EmptyType";
    }
    public boolean hasOperator(String s) {
        return false;
    }

    Hashtable<String, TokenBinding> bindings = new Hashtable<String, TokenBinding>();
    Hashtable<Character, ArrayList<Token>> tokens = new Hashtable<Character, ArrayList<Token>>();
    public ArrayList<Token> getPossibleTokens(char c) {
        if(tokens.get(c) == null) {
            return new ArrayList<Token>();
        } else {
            return tokens.get(c);
        }
    }

    public TokenBinding getBinding(String s) {
        return bindings.get(s);
    }

    public void addBinding(String s,int pri,Binding b){
        Token t = new Token(s,pri);
        char firstCharacter = s.charAt(0);
        if(tokens.get(firstCharacter) == null) {
            tokens.put(firstCharacter, new ArrayList<Token>());
        } else {
            System.out.println("yay?");
        }
        tokens.get(firstCharacter).add(t);
        bindings.put(s, new TokenBinding(t, b));

        b.registerOwningType(this);
    }
}

class ConsumeResult {
    Node n;
    int pointerLoc;

    public ConsumeResult(Node n, int pointer) {
        this.n = n;
        this.pointerLoc = pointer;
    }

    public Node getNode() {
        return n;
    }

    public int getPointer() {
        return pointerLoc;
    }
}
