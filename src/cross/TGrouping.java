package cross;

/**
 * Created by Dar on 5/9/2014.
 */
public class TGrouping extends SType {
    public boolean matches(char c) {
        return c == '(';
    }
    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        char cur = expr[pointer];
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
            Node nod = cp.parse(tempc);

            nod.weight = Integer.MAX_VALUE; //no displacing this node.. ever.
            //current = insert(current,nod,false);
            pointer = ptr+1;

            ConsumeResult res = new ConsumeResult(nod, pointer);
            return res;
        }
        return new ConsumeResult(null, pointer); //an error has occured
    }

    @Override
    public String getName() {
        return "Grouping";
    }
}
