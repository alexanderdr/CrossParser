/*
 * Main.java
 *
 */

package cross;

/**
 *
 * @author Dar
 */
import java.io.*;

import java.lang.reflect.*;
import java.util.regex.*;
public class Main {
    
    /** Creates a new instance of Main */
    public TestObject tot = new TestObject();
    //String check;
    public Main(String[] args) {
        //System.out.println(Package.getPackage("java.util"));
        Script script = new Script(tot);
        CrossLibrary clib = new CrossLibrary();
        //TestObject test = new TestObject();
        SObject testObject = null;
        try{
            testObject = SObject.getJava(this,this.getClass().getField("tot"),script);
        } catch (Exception e){
            e.printStackTrace();
        }
        script.addScalar("test", testObject);

        //clib.loadOnScript(script);
        CrossLibrary.defaultLibrary.loadOnScript(script);
        
        CrossParser cp = new CrossParser(script);

        //For use as a command line tool
        /*if((args!=null)&&(args[0]!=null)){
            try{
                StringBuffer sb = new StringBuffer();
                BufferedReader in = new BufferedReader(new FileReader(args[0]));
                while(in.ready()){
                    sb.append(in.readLine());
                }
                script.exec(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
        try{
            //regression checks...
            //A good use-case for unit-testing.
            //It would be nice to move these into a seperate file once there's support for it.
            script.test("check 1:helloThar","Hello thar");

            //Checks for math, order of operations, parsing.
            script.test("check \"hello world\"","hello world");
            script.test("check 2**3","8");
            script.test("check (+ 1 3)","4"); //this is mostly just a side-effect of the parser
            script.test("if(1){check \"true\"}{check \"false\"}","true");
            script.test("if(0){check \"true\"}{check \"false\"}","false");
            script.test("check 5/2**2*4","5");
            script.test("check (5/2**2*4)","5");
            script.test("check (((((5/2)**2*4))))","25");
            //It's actually surprising to see what limitations "real" languages have with nested parentheses.
            script.test("check (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((1+1"+
                               ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))","2");
            script.test("check (5/2)**2","6.25");
            script.test("check 2+(3*4)","14");
            script.test("check (2+ 3 )*4", "20");
            script.test("check 5*(2+3)*4", "100");
            script.test("check 1+5*(2+3)*4", "101");
            script.test("check 2/5*(2+3)*4", "8");
            script.test("check 2/(5*(2+3))*4", "0.32");

            script.test("check a=1","1");
            script.test("check test.blah.length","10");
            script.test("check test.s.length","8");
            script.exec("test.s=\"how are you gentlemen?\"");
            script.exec("a=test.s");
            script.test("check a.length","22");
            script.test("a=(test.s);check a.length","22");

            //tests for java reflection and lists
            script.test("check test.s.length","22");
            script.test("check 1,2,3,4", "list (1,2,3,4)");
            script.test("check 1,2,3,(4,5),6", "list (1,2,3,list (4,5),6)");
            script.test("check (1,2),3,(4,5),6", "list (list (1,2),3,list (4,5),6)");
            script.test("check test.doit:(1,2,3,4,5)","15"); //it's a summation for varargs
            script.test("check test.doit:(7,8)","56");       //two args means multiply
            script.test("check (3)(7)","21"); //placeholder test for <<notok>> or implicit token
            script.test("check (1,2,3)(0)","1");

            script.test("check ((1,2,3),(4,5,6),(7,8,9))(1)(2)","6");
            script.exec("array = ((1,2,3),(4,5,6),(7,8,9))");
            script.test("check array(2)(2)","9");
            script.exec("array(1)(2)=42");
            script.test("check array(1)(2)", "42");
            script.test("test.i=1;check test.i","1");
            script.test("check this.s","how are you gentlemen?");
            script.test("stz=new (java.util.StringTokenizer) (\"All your base are belong to us\");while(stz.hasMoreTokens){check stz.nextToken}","Allyourbasearebelongtous");
            script.exec("{1+1}");

            script.exec("sys = load (\"java.lang.System\"); sys.out.println:(\"I\'m not confused.  I\'m just well mixed.\")");
            script.exec("(load (\"java.lang.System\")).out.println:(\"--Robert Frost\")");
            //Next issue to resolve:
            //script.exec("java.lang.System.out.println:(\"dosn't work\")");
            //This should work, but popping up a frame every time we run is inconvenient.
            //script.exec("f=new (java.awt.Frame) (\"framename\"); f.setSize:(500,500);f.setVisible:(true)");//;f.setSize:(500,500);f.setVisible:(true)","");

        } catch (Exception e){e.printStackTrace();}

    }
    
    public void blah(TestObject to){
        TestObject o = to;
        o.s= "changed";
        System.out.println(o.s);
    }

    public static void main(String[] args) {
        new Main(args);
    }
    
}
