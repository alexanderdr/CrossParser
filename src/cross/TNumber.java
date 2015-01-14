package cross;

import java.util.regex.Pattern;

/**
 * Created by Dar on 5/9/2014.
 */
public class TNumber extends SType{
    public boolean matches(char c) {
        return Pattern.matches("(\\.?\\d+)|(\\d+.?\\d*)", (c+""));
    }

    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        char cur = expr[pointer];
        if(Pattern.matches("(\\.?\\d+)|(\\d+.?\\d*)",new String(expr,pointer,1))){
            int diff = 0;
            String temp = new String(expr,pointer,diff+1);
            while(Pattern.matches("(\\.?\\d+)|(\\d+\\.?\\d*)",(temp = new String(expr,pointer,diff+1)))){
                diff++;
                if((pointer+diff+1>expr.length)){
                    break;
                }
            }
            //current = insert(current,new String(expr,pointer,diff),0);
            Node node = new Node(this, new String(expr, pointer, diff), 0, cp.getScript());
            pointer += diff;

            ConsumeResult cr = new ConsumeResult(node, pointer);
            return cr;
        }
        return new ConsumeResult(null, pointer);
    }

    @Override
    public String getName() {
        return "Number";
    }
}
