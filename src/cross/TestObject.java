/*
 * TestObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.StringTokenizer;
public class TestObject extends Object {
    public int i = 3;
    public float f = 3.14f;
    public String s = "Hi there";
    private String blah = "hehehehehe";
    public StringTokenizer stz = new StringTokenizer(s);
    /** Creates a new instance of TestObject */
    public TestObject() {
        
    }
    
    public String getBlah(){return blah;}
    public void setBlah(String s){blah = s;}
    public int doit(Integer... fun){int sum = 0;for(int i:fun){sum+=i;}return sum;}
    public int doit(Integer one, Integer fun){/*int sum = 0;for(int i:fun){sum+=i;}return sum;*/return one.intValue()*fun.intValue();}
}
