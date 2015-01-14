package cross;

/**
 * Created by Dar on 6/5/2014.
 */
public class TSymbol extends SType {
    public boolean matches(char c) {
        return CrossParser.isLetter(c);
    }

    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        char cur = expr[pointer];
        if(matches(cur)){
            int ptr = pointer+1;
            //may not be returning the correct offset because we are capturing just 'h'
            if(ptr < expr.length) {
                char temp = expr[ptr];
                while (CrossParser.isLetter(temp)) {
                    ptr++;
                    if (ptr >= expr.length) {
                        ptr = expr.length;
                        break;
                    }
                    temp = expr[ptr];
                }
            }
            int len=((ptr)-pointer);
            char[] tempc = new char[len];
            System.arraycopy(expr,pointer, tempc, 0, len); //need first " for scalar identification

            //current = insert(current,new String(tempc),0);
            pointer = ptr;

            Node node = new Node(this, new String(tempc), 0, cp.getScript());
            ConsumeResult cr = new ConsumeResult(node, pointer);
            return cr;
        }
        return new ConsumeResult(null, pointer);
    }

    @Override
    public String getName() {
        return "Symbol";
    }
}
