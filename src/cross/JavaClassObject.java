/*
 * JavaClassObject.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.lang.reflect.*;
import java.util.ArrayList;
public class JavaClassObject extends SObject{
    
    Class clas;
    Constructor[] cons;
    /** Creates a new instance of JavaClassObject */
    public JavaClassObject(Class c, Script s) {
        super(s);
        clas = c;
        cons = clas.getConstructors();
    }
    
    public Scalar createInstance(Scalar argin){
        Constructor con = null;

        SList args;
        if(!(argin instanceof SList)){
            args = new SList(argin,script);
        } else {
            args = (SList)argin;
        }

        if(cons.length==1){
            con = cons[0];
        } else {
            for(Constructor c:cons){
                
                if(c.getParameterTypes().length==args.size()){

                    break;
                } else {
                }
            }
            if(con == null){
                out: for(Constructor m:cons){
                    if(m.getParameterTypes().length==args.size()){
                        int i = 0;
                        for(Class c:m.getParameterTypes()){
                            if(c==args.get(i).getObject().getClass()){
                                
                            } else {
                                continue out;
                            }
                        }
                        con = m;
                        break out;
                    }
                }
            }
        }
        
        Object[] tempargs = args.toJavaArray();
        ArrayList argz = new ArrayList();
        int index = 0;
        for(Class t:con.getParameterTypes()){
            //Oh noes
            if(t.isArray()){
                int x;
                for(x = 0;(index+x<tempargs.length)&&(tempargs[index+x].getClass()==t.getComponentType());x++){

                }
                Object test = Array.newInstance(t.getComponentType(),x);
                for(int y = 0;y<x;y++){
                    Array.set(test,index,tempargs[index]);
                    index++;
                }
                argz.add(test);
            } else {
                argz.add(tempargs[index]);
                index++;
            }
        }
        
        try{
            Object[] temp = argz.toArray();
            return SObject.toScalar(con.newInstance(temp),script);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Scalar(script);
    }
    
    public Scalar toScalar(){
        return Scalar.nullScalar();
    }
    
    public Object getObject(){
        return clas;
    }
    
    public void set(Scalar o){
        
    }
    
}
