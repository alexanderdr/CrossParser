/*
 * Scalar.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
public class Scalar {
    
    //String name = "undefined name";
    Script script;
    Type type = Type.NONE;
    static Scalar NULL = new Scalar();
    
    public static enum Type {BOOLEAN, FLOAT, INT, MAGIC, REFERENCE, STRING, JAVAOBJECT, JAVAMETHOD, JAVACLASS, LIST, CROSSOBJECT, SYMBOL, NONE};
    
    public Scalar(){
    }
    
    /** Creates a new instance of Scalar */
    //need a universal null scalar at some point to cut down on the absurd amount of object creation
    //now we have one: Scalar.NULL, although currently it may not be used in all locations
    public Scalar(Script s) {
        this.script = s;
    }
    
    public boolean isNull(){
        return true;
    }
    
    public static Scalar nullScalar(){
        return Scalar.NULL;
    }
    
    public static Scalar buildScalar(float f,Script scrip){
        if(((int)f)==f){
            return new SInt((int)f,scrip);
        }
        return new SFloat(f,scrip);
    }
        
    public static Scalar buildScalar(int i,Script scrip){
        return new SInt(i,scrip);
    }
    
    public static Scalar buildScalar(String s,Script scrip){
        return buildScalar(s,scrip,Type.NONE);
    }
    
    public static Scalar buildScalar(String s,Script scrip,Type t){
        if(s.equals("")){
            return Scalar.NULL;
        }
        Scalar temp;
        if(s.charAt(0)=='\"'){
            return new SString(s.substring(1),scrip); //don't want the opening quote..
        }
        try{
            return new SInt(Integer.parseInt(s),scrip);
        } catch (Exception e){
            try{
                return new SFloat(Float.parseFloat(s),scrip);
            } catch (Exception ex){
                if(s.equals("true")){
                    return new SBool(true,scrip);
                } else if(s.equals("false")){
                    return new SBool(false,scrip);
                } else if( ((temp = scrip.getScalar(s)) != null)&&(t!=Type.SYMBOL)&&(t!=Type.REFERENCE)){
                    return temp;
                } else {
                    return new SSymbol(s,scrip);
                }
            }
        }
    }
    
    public static Scalar buildScalar(Node n,Script s){
        return new SMagic(n,s);
    }
    
    //This area is under construction
    public Scalar recieveMessage(Scalar args){
        String messageName = "";
        if(args.type==Type.LIST){
            
            messageName = ((SReference)((SList)args).get(0)).getReferant();
        } else if(args.type==Type.SYMBOL) {
            messageName = args.getString();
        } else {
            System.out.println("??? recieve message of type "+args.type);
        }
        Class clazz = getClass();
        boolean trying = true;
        while(trying){
            try{
                return (Scalar)(clazz.getMethod(messageName, new Class<?>[0]).invoke(this, new Object[0]));
            } catch (Exception e){
                trying = false;
                if(e instanceof NoSuchMethodException){
                    if(super.getClass().isAssignableFrom(Scalar.class)){
                        //return ((Scalar)super).recieveMessage(args);
                        clazz=super.getClass();
                        trying = true;
                        continue;
                    }
                }
                e.printStackTrace();
            }
        }
        return NULL;
    }

    //Used in testing
    public Scalar helloThar(){
        return new SString("Hello thar",script);
    }
    
    public int getInt(){
        return 0;
    }
    
    public float getFloat(){
        return 0f;
    }
    
    public boolean getBoolean(){   
        return false;
    }
    
    public String getString(){
        return "null";
    }
    
    public Object getObject(){
        return this;
    }
    
    public void set(Scalar other){
        System.out.println("Trying to call set the scalar base class... "+ getString() +" "+other.getString());
    }
    
}
