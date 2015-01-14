/*
 * Library.java
 *
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.HashMap;

public class CrossLibrary {
    
    public static CrossLibrary defaultLibrary = new CrossLibrary();
    static {

        TNumber numeric = new TNumber();

        numeric.addBinding("<",0,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return new SBool(left.getInt()<right.getInt(),script);
                } else {
                    return new SBool(left.getFloat()<right.getFloat(),script);
                }
            }
        });
        numeric.addBinding("<=",0,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return new SBool(left.getInt()<=right.getInt(),script);
                } else {
                    return new SBool(left.getFloat()<=right.getFloat(),script);
                }
            }
        });
        numeric.addBinding("==",0,new BinaryBinding(){
            public Scalar eval(Scalar left,Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.JAVAOBJECT)||(left.type==Scalar.Type.JAVAOBJECT)){
                    return new SBool(left.getObject().equals(right.getObject()),script);
                } else {
                    return new SBool(left.equals(right),script);
                }
            }
        });

        numeric.addBinding("+",10,new BinaryBinding(numeric){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.INT)&&(right.type==Scalar.Type.INT)){
                    return Scalar.buildScalar(left.getInt()+right.getInt(),script);
                } else if((left.type==Scalar.Type.STRING)||(right.type==Scalar.Type.STRING)){
                    Scalar temp = Scalar.buildScalar('\"'+left.getString()+right.getString(),script);
                    return temp;
                } else {
                    return Scalar.buildScalar(left.getFloat()+right.getFloat(),script);
                }
            }
        });

        numeric.addBinding("-",10,new BinaryBinding(numeric){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.INT)&&(right.type==Scalar.Type.INT)){
                    return Scalar.buildScalar(left.getInt()-right.getInt(),script);
                } else {
                    return Scalar.buildScalar(left.getFloat()-right.getFloat(),script);
                }
            }
        });

        numeric.addBinding("*",20,new BinaryBinding(numeric){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return Scalar.buildScalar(left.getInt()*right.getInt(),script);
                } else {
                    return Scalar.buildScalar(left.getFloat()*right.getFloat(),script);
                }
            }
        });

        numeric.addBinding("/",20,new BinaryBinding(numeric){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)&&(left.getInt()%right.getInt()==0)){
                    return Scalar.buildScalar(left.getInt()/right.getInt(),script);
                } else {
                    Scalar s = Scalar.buildScalar(left.getFloat()/right.getFloat(),script);
                    return s;
                }
            }
        });

        numeric.addBinding("**",30,new BinaryBinding(numeric){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                return Scalar.buildScalar((float)Math.pow((double)left.getFloat(),(double)right.getFloat()),script);
            }
        });



        //symbol.addBinding(".",500,new DotBinding());

        defaultLibrary.addType(new TGrouping());
        defaultLibrary.addType(new TBlock());
        defaultLibrary.addType(numeric);
        defaultLibrary.addType(new TString());
        //defaultLibrary.addType(symbol);

                defaultLibrary.addBinding(",", -10, true, false, true, new BinaryBinding() {
                    public Scalar exec(Node n, Evaluator e, Scalar.Type type) {
                        Scalar left = null, right = null;
                        if (n.left != null) {
                            left = e.exec(n.left, Scalar.Type.NONE);
                        }
                        if (n.right != null) {
                            right = e.exec(n.right, Scalar.Type.NONE);
                        }

                        if (n.left.weight > -10) { //This hack checks to see if there were parentheses around the list during the binding
                            return eval(left, right, type, false);
                        } else {
                            return eval(left, right, type, true);
                        }
                    }

                    public Scalar eval(Scalar left, Scalar right, Scalar.Type type, boolean append) {
                        if (left instanceof SList) {
                            if (append) {
                                ((SList) left).append(right);
                                return left;
                            } else {
                                return new SList(left, right, script);
                            }
                        } else {
                            return new SList(left, right, script);
                        }
                    }

                    public Scalar eval(Scalar left, Scalar right, Scalar.Type type) {
                        if (left instanceof SList) {
                            ((SList) left).append(right);
                            return left;
                        } else {
                            return new SList(left, right, script);
                        }
                    }
                });
        
        defaultLibrary.addBinding("load ",-25,true,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                
                Class c = null;
                try{
                    c = Class.forName(left.getString());
                } catch (Exception e){
                    e.printStackTrace();
                }
                return SObject.toScalar(c,script);
            }
        });
        
        defaultLibrary.addBinding("new ",250,true,false,new BinaryBinding(){
            public Scalar exec(Node n, Evaluator e,Scalar.Type type){
                Scalar left = null, right = null;
                if(n.left != null){
                    left = e.exec(n.left,Scalar.Type.JAVACLASS);
                }   
                if(n.right != null){
                    right = e.exec(n.right,Scalar.Type.LIST);
                }
                return eval(left,right,type);
            }
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                return ((JavaClassObject)left).createInstance(right);
            }
        });
        
        defaultLibrary.addBinding(":",-5,false,false,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if(left instanceof JavaMethod){
                    return ((JavaMethod)left).invoke(right);
                } else {
                    return left.recieveMessage(right);
                }
            }
            public Scalar exec(Node n, Evaluator e,Scalar.Type type){
                Scalar left = null, right = null;
                if(n.left != null){
                    left = e.exec(n.left,Scalar.Type.JAVAMETHOD);
                }   
                if(n.right != null){
                    right = e.exec(n.right,Scalar.Type.LIST);
                }
                return eval(left,right,type);
            }
        });
        
        defaultLibrary.addBinding("println ",-25,true,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                System.out.print(left.getString());
                return Scalar.nullScalar();
            }
        });
        
        defaultLibrary.addBinding("println ",-25,true,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                System.out.println(left.getString());
                return Scalar.nullScalar();
            }
        });
        
        defaultLibrary.addBinding("check ",-25,true,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if(!script.checker.equals("0000000")){
                    script.checker = script.checker+left.getString(); //need to append
                } else {
                    script.checker = left.getString();
                }
                return Scalar.nullScalar();
            }
        });
        
        defaultLibrary.addBinding("if",-50,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                    ((SMagic)right).eval(left);
                return Scalar.nullScalar();
            }
        });
        
        defaultLibrary.addBinding("while",-50,true,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                ((SMagic)right).eval(left).getBoolean();
                return Scalar.nullScalar();
            }
            
            public Scalar exec(Node n, Evaluator e,Scalar.Type type){
                Scalar left = null, right = null;
                if(n.right != null){
                    right = e.exec(n.right,Scalar.Type.NONE);
                }
                Scalar temp = Scalar.buildScalar("",script);
                try{
                    while((left=e.exec(n.left,Scalar.Type.NONE)).getBoolean()){ //check left, set left each iteration
                        temp = eval(left,right,type);
                    }
                } catch (Exception ex){
                    System.err.println("Error in loop, breaking out with exception");
                    ex.printStackTrace();
                }
                return temp;
            }
        });
        //defaultLibrary.addToken("bind ", -100,script);
        
        defaultLibrary.addBinding(";",-10000,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                return Scalar.nullScalar();
            }
        });
        defaultLibrary.addBinding("=",-10,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if(left instanceof SObject){
                    ((SObject)left).set(right);
                    return left;
                }
                if(left == null) {
                    //throw an exception with information, because we're about to throw one anyway
                    throw new EvalException("Null left-hand value when parsing \"=\"");
                }
                if(left.type == Scalar.Type.SYMBOL){
                    script.addScalar(left.getString(), right);
                    return right;
                } else {
                    left.set(right);
                    return left;
                }
            }
            public Scalar exec(Node n, Evaluator e,Scalar.Type type){
                Scalar left = null, right = null;
                if(n.left != null){
                    left = e.exec(n.left,Scalar.Type.REFERENCE); //JAVAOBJECT
                }
                if(n.right != null){
                    right = e.exec(n.right,Scalar.Type.NONE);
                }
                return eval(left,right,type);
            }
        });

        /*defaultLibrary.addBinding("<",0,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return new SBool(left.getInt()<right.getInt(),script);
                } else {
                    return new SBool(left.getFloat()<right.getFloat(),script);
                }
            }
        });
        defaultLibrary.addBinding("<=",0,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return new SBool(left.getInt()<=right.getInt(),script);
                } else {
                    return new SBool(left.getFloat()<=right.getFloat(),script);
                }
            }
        });
        defaultLibrary.addBinding("==",0,new BinaryBinding(){
            public Scalar eval(Scalar left,Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.JAVAOBJECT)||(left.type==Scalar.Type.JAVAOBJECT)){
                    return new SBool(left.getObject().equals(right.getObject()),script);
                } else {
                    return new SBool(left.equals(right),script);
                }
            }
        });
        
        defaultLibrary.addBinding("+",10,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.INT)&&(right.type==Scalar.Type.INT)){
                    return Scalar.buildScalar(left.getInt()+right.getInt(),script);
                } else if((left.type==Scalar.Type.STRING)||(right.type==Scalar.Type.STRING)){
                    Scalar temp = Scalar.buildScalar('\"'+left.getString()+right.getString(),script);
                    return temp;
                } else {
                    return Scalar.buildScalar(left.getFloat()+right.getFloat(),script);
                }
            }
        });
        
        defaultLibrary.addBinding("-",10,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left.type==Scalar.Type.INT)&&(right.type==Scalar.Type.INT)){
                    return Scalar.buildScalar(left.getInt()-right.getInt(),script);
                } else {
                    return Scalar.buildScalar(left.getFloat()-right.getFloat(),script);
                }
            }
        });
        
        defaultLibrary.addBinding("*",20,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return Scalar.buildScalar(left.getInt()*right.getInt(),script);
                } else {
                    return Scalar.buildScalar(left.getFloat()*right.getFloat(),script);
                }
            }
        });
        
        defaultLibrary.addBinding("/",20,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if((left instanceof SInt)&&(right instanceof SInt)&&(left.getInt()%right.getInt()==0)){
                    return Scalar.buildScalar(left.getInt()/right.getInt(),script);
                } else {
                    Scalar s = Scalar.buildScalar(left.getFloat()/right.getFloat(),script);
                    return s;
                }
            }
        });
        
        defaultLibrary.addBinding("**",30,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                return Scalar.buildScalar((float)Math.pow((double)left.getFloat(),(double)right.getFloat()),script);
            }
        });*/

        //Null/implicit token binding.
        defaultLibrary.addBinding("<<notok>>",20,new BinaryBinding(){
            public Scalar eval(Scalar left, Scalar right, Scalar.Type type){
                if(left.type==Scalar.Type.LIST&&right.type==Scalar.Type.INT){
                    return ((SList)left).get(right.getInt());
                }
                if((left instanceof SInt)&&(right instanceof SInt)){
                    return Scalar.buildScalar(left.getInt()*right.getInt(),script);
                } else {
                    return Scalar.buildScalar(left.getFloat()*right.getFloat(),script);
                }
            }
        });

        //Tokens can be used to see if the tree is build correctly before a binding is written fo rit.
        //script.addToken("-", 10);
        //script.addToken("*", 20);
        //script.addToken("/", 20);
        //script.addToken("**", 30);
        defaultLibrary.addBinding(".",500,true,false,true,new DotBinding());
    }

    ArrayList<SType> types = new ArrayList<SType>();
    
    ArrayList<TokenBinding> bindings = new ArrayList<TokenBinding>();
    //would be nice to be able to use a HashMap, but the parser searches by the start of the token's string
    //so we would need a custom data structure to get any value here, because otherwise we'll just be doing
    //an exhaustive search of the keys every time.
    //HashMap<Token,Binding> bindings = new HashMap<Token,Binding>();
    
    /** Creates a new instance of Library */
    public CrossLibrary() {
    }

    public void addType(SType type) {
        types.add(type);
    }

    public void addBinding(String s,int pri,Binding b){
        Token t = new Token(s,pri);
        bindings.add(new TokenBinding(t,b));
        //lang.addBinding(s,pri,b);
    }
    
    //drop means that it immediately tries to fill the child nodes of the token
    //even if the parent isn't full yet, mostly used for prefixes
    public void addBinding(String s,int pri,boolean drop,Binding b){
        Token t = new Token(s,pri,drop);
        bindings.add(new TokenBinding(t,b));
        //lang.addBinding(s,pri,drop,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,boolean un,Binding b){
        Token t = new Token(s,pri,drop,un);
        bindings.add(new TokenBinding(t,b));
        //lang.addBinding(s,pri,drop,un,b);
    }
    
    public void addBinding(String s,int pri,boolean drop,boolean un,boolean ds,Binding b){
        Token t = new Token(s,pri,drop,un,ds);
        //System.out.println(t.displace);
        bindings.add(new TokenBinding(t,b));
        //lang.addBinding(s,pri,drop,un,b);
    }
    
    public void loadOnScript(Script s){
        for(TokenBinding tb : bindings){
            Token t = tb.t;
            tb.b.script = s;
            //s.addBinding(t.str,t.priority,t.drop,t.unary,tb.b);
            s.addBinding(t,tb.b);
        }

        for(SType t: types) {
            s.addType(t);
        }
    }
}

class TokenBinding{
    Token t;
    Binding b;
    TokenBinding(Token tok, Binding bin){
        t = tok;
        b = bin;
    }

    public Binding getBinding() {
        return b;
    }

    public Token getToken() {
        return t;
    }

    public SType getReturnType() {
        return b.getReturnType();
    }
}