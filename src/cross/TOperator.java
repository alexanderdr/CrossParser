package cross;

/**
 * Created by Dar on 6/9/2014.
 */

public class TOperator extends SType {
    public boolean matches(char c) {
        throw new RuntimeException(); //shouldn't be called...
    }

    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        throw new RuntimeException(); //shouldn't be called...
    }

    @Override
    public String getName() {
        return "Op";
    }

    public SType getReturnType() {
        assert false; //unsure how we would ever end up attempting to check the return type of an op rather than it's result...
        return TNull.instance;
    }
}
