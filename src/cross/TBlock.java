package cross;

/**
 * Created by Dar on 5/10/2014.
 */
public class TBlock extends SType {

    public boolean matches(char c) {
        return c == '{';
    }
    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        char cur = expr[pointer];
        if(matches(cur)){

            int count = 1;
            char temp = cur;
            int ptr = pointer;

            while (count > 0) {
                ptr++;
                temp = expr[ptr];
                if (temp == '{') {
                    count++;
                }
                if (temp == '}') {
                    count--;
                }
                if (ptr > expr.length) {
                    System.out.println("Error, no closing braces found in " + expr + " opening at char " + pointer);
                }
            }
            int len = (ptr - 1) - pointer;
            char[] tempc = new char[len];
            System.arraycopy(expr, pointer + 1, tempc, 0, len);
            /*BranchNode tempblock;
            if (!(current instanceof BranchNode)) {
                tempblock = new BranchNode(script);
            } else {
                tempblock = (BranchNode) current;
            }

            tempblock.insert(parse(tempc));

            if (!(current instanceof BranchNode)) {
                current = insert(current, tempblock, true);
            }*/

            pointer = ptr + 1;
            ConsumeResult cr = new ConsumeResult(new Node(this, new String(expr), cp.getScript()), pointer);
            return cr;
        }
        return new ConsumeResult(null, pointer); //an error has occured
    }

    @Override
    public String getName() {
        return "Block";
    }

}
