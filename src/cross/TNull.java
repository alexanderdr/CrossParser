package cross;

/**
 * Created by Dar on 5/12/2014.
 */
public class TNull extends SType {

    public static TNull instance = new TNull();

    @Override
    public boolean matches(char c) {
        return false;
    }

    @Override
    public ConsumeResult consume(CrossParser cp, char[] expr, int pointer) {
        return new ConsumeResult(null, 0);
    }

    @Override
    public String getName() {
        return "NullType";
    }
}
