/*
 * JavaMethod.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.*;
import java.lang.reflect.*;
public class JavaMethod extends Scalar{
    
    Object obj;
    Method[] meths;
    /** Creates a new instance of JavaMethod */
    public JavaMethod(Object o,Method m,Script s) {
        super(s);
        obj = o;
        meths = new Method[1];
        meths[0] = m;
    }
    
    public JavaMethod(Object o,Method[] m,Script s){
        super(s);
        obj = o;
        meths = m;
    }
    
    public boolean classesAreEquivilent(Class one, Class two){
        Class cone, ctwo;
        if(two.isPrimitive()){
            ctwo = one;
            cone = two;
        } else {
            cone = one;
            ctwo = two;
        }
        
        if((cone == int.class)&&(ctwo == Integer.class)){
            return true;
        }
        
        return one.isAssignableFrom(two);
    }
    
    public Scalar invoke(Scalar argin){
        Method meth = null;

        SList args;
        if(!(argin instanceof SList)){
            args = new SList(argin,script);
        } else {
            args = (SList)argin;
        }
        
        if(meths.length==1){
            meth = meths[0];
        } else {

            if(meth == null){
                out: for(Method m:meths){
                    if((m.getParameterTypes().length==args.size())||((m.getParameterTypes().length<=args.size())&&(m.isVarArgs()))){
                        int i = 0;
                        Class tempc = null;
                        for(Class c:m.getParameterTypes()){
                            tempc = c;
                            if(c.isArray()){
                                tempc = c.getComponentType();
                            }

                            if(classesAreEquivilent(tempc,args.get(i).getObject().getClass())){
                                i++;
                            } else {
                                continue out;
                            }
                        }
                        if(i<args.size()){ //Got out because of an array so good to verify the remainder of the arguments
                            while(i<args.size()){ //are of the same type
                                if(classesAreEquivilent(tempc,args.get(i).getObject().getClass())){
                                    i++;
                                } else {
                                    System.out.println("The remainder of the arguments are not of the same type, this is a problem");
                                }
                            }
                        }
                        
                        meth = m;
                        
                        if(m.getParameterTypes().length==args.size()){
                            break out;
                        }
                        
                        continue out;
                    }
                }

            }
        }
        
        Object[] tempargs = args.toJavaArray();
        ArrayList argz = new ArrayList();
        int index = 0;
        for(Class t:meth.getParameterTypes()){
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
            return SObject.toScalar(meth.invoke(obj,temp),script);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Scalar(script);
    }
    
}
