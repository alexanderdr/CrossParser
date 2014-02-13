/*
 * DotBinding.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.lang.reflect.*;
import java.util.ArrayList;
public class DotBinding extends BinaryBinding{
    
    /** Creates a new instance of DotBinding */
    public DotBinding(Script script) {
        super(script);
    }
    
    public DotBinding(){
    }
    
    public Scalar exec(Node n, Evaluator e, Scalar.Type type){
        Scalar left = null, right = null;
        Scalar.Type t;
        if((type==Scalar.Type.JAVACLASS)||(type==Scalar.Type.STRING)){
            t = Scalar.Type.STRING;
        } else {
            t = Scalar.Type.JAVAOBJECT;
        }
        if(n.left != null){
            left = e.exec(n.left,t);
        }
        
        if(n.right != null){
            right = e.exec(n.right,Scalar.Type.NONE);
        }
        if(type==Scalar.Type.REFERENCE){
            return eval(left,right,Scalar.Type.JAVAOBJECT);
        }
        return eval(left,right,type);
    }
    
    public Scalar eval(Scalar lefts, Scalar rights, Scalar.Type type){
        
        Object left = null;
        if(lefts instanceof SObject){
            left = ((SObject)lefts).getObject();
        } else if (lefts.type == Scalar.Type.SYMBOL){
            if(type==Scalar.Type.STRING){
                left = lefts.getString();
            } else {
                left = script.getScalar(lefts.getString()).getObject();
            }
        }else if((type==Scalar.Type.STRING)||(type==Scalar.Type.JAVACLASS)){
            left = lefts.getString();
        } else {
            //System.out.println("Uhhh, I'm not sure what to do with "+lefts.getString()+" "+lefts+" "+type);
            left = lefts;
        }
        String right = rights.getString();
        if(type==Scalar.Type.STRING){
            //System.out.println(left.toString()+" "+right.toString());
            return new SString(left.toString()+"."+right.toString(),script);
        }
        if(type==Scalar.Type.JAVACLASS){
            String cstring = ((String)left)+"."+right;
            //System.out.println("I should return "+cstring);
            try{
                return SObject.toScalar(Class.forName(cstring),script);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
        Object o = null;
        Field f = null;
        try{
            
            if(type==Scalar.Type.JAVAMETHOD){
                Method[] meths = left.getClass().getMethods();
                ArrayList<Method> found = new ArrayList<Method>();
                for(Method m:meths){
                    if(m.getName().equals(right)){
                        found.add(m);
                    }
                }
                Method[] blah = new Method[found.size()];
                blah = found.toArray(blah);
                return new JavaMethod(left,blah,script);
            } else{
                if(!(left instanceof Class)){
                    f = left.getClass().getField(right);
                } else {
                    f = ((Class)left).getField(right);
                }
                o = f.get(left);
            }
        } catch(Exception e){
            try{
                if(e instanceof NoSuchFieldException){
                    String temp = "get"+right.substring(0,1).toUpperCase()+right.substring(1);
                    String tempset = "set"+right.substring(0,1).toUpperCase()+right.substring(1);
                    Method mget = null, mset = null;
                    try{
                        mget = left.getClass().getMethod(right);
                    } catch (NoSuchMethodException nsf){
                        try{
                            mget = left.getClass().getMethod(temp);
                        } catch (Exception exx){
                            exx.printStackTrace();
                        }
                    }
                    try{
                        Method[] tmethods = left.getClass().getMethods();
                        for(Method m:tmethods){
                            if(m.getName().equals(tempset)){
                                if(m.getParameterTypes().length==1){
                                    mset = m;
                                    break;
                                }
                            }
                        }
                    } catch (Exception exc){
                        exc.printStackTrace();
                    }
                    if(type.equals(Scalar.Type.JAVAOBJECT)){
                        return SObject.getJava(left,mget,mset,script);
                    }
                    o = mget.invoke(left);
                } else{
                    e.printStackTrace();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        if(!(type.equals(Scalar.Type.JAVAOBJECT))){
            return SObject.toScalar(o,script);
        } else {
            return SObject.getJava(left,f,script);
        }
    }
    
}
