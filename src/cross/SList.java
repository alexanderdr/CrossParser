/*
 * SList.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.ArrayList;
public class SList extends Scalar{
    
    ArrayList<Scalar> list = new ArrayList<Scalar>();
    /** Creates a new instance of SList */
    public SList(Scalar left, Scalar right,Script s) {
        super(s);
        list.add(left);
        list.add(right);
        type = Type.LIST;
    }
    
    public SList(Scalar one,Script s) {
        super(s);
        list.add(one);
        type = Type.LIST;
    }
    
    public void append(Scalar s){
        list.add(s);
    }
    
    public Scalar get(int x){
        return list.get(x);
    }
    
    public String getString(){
        StringBuilder sb = new StringBuilder();
        sb.append("list (");
        for(Scalar s:list){
            sb.append(s.getString());
            sb.append(',');
        }
        return sb.deleteCharAt(sb.length()-1).append(')').toString();
    }
    
    public Object[] toJavaArray(){
        
        Object[] temp = new Object[list.size()];
        int i = 0;
        for(Scalar scal:list){
            temp[i] = scal.getObject();
            i++;
        }
        return temp;
    }
    
    public Object getObject(){
        return toJavaArray();
    }
    
    public int size(){
        return list.size();
    }
    
    public ArrayList<Scalar> getList(){
        return list;
    }
    
    public void set(Scalar other){
        list = ((SList)other).getList();
    }
    
}
