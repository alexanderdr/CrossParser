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

    public TestObject tot = new TestObject();

    /** Creates a new instance of Main */
    public Main(String[] args) {
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

            //It's actually surprising to see what limitations "real" languages have with nested parentheses.
            //script.test("check (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((1+1"+
            //                   ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))","2");

            //regression checks...
            //A good use-case for unit-testing.
            //It would be nice to move these into a seperate file once there's support for it.
            //script.test("check 1:helloThar","Hello thar");
            //script.test("check \"hello world\"","hello world");

            //script.test("check (+ 1 3)","4"); //this is mostly just a side-effect of the parser
            //script.test("if(1){check \"true\"}{check \"false\"}","true");
            //script.test("if(0){check \"true\"}{check \"false\"}","false");



            //if(true) return;

            //Checks for math, order of operations, parsing.
            script.test("check 2**3","8");
            script.test("check 5/2**2*4","5");
            script.test("check (5/2**2*4)","5");
            script.test("check (((((5/2)**2*4))))","25");
            script.test("check (5/2)**2","6.25");
            script.test("check 2+(3*4)","14");
            script.test("check (2+ 3 )*4", "20");
            script.test("check 5*(2+3)*4", "100");
            script.test("check 1+5*(2+3)*4", "101");
            script.test("check 2/5*(2+3)*4", "8");
            script.test("check 2/(5*(2+3))*4", "0.32");

            script.test("check (3)*(7)","21");
            script.test("check (3)(7)", "21");//placeholder test for <<notok>> or implicit token

            //tests for lists
            script.test("check 1,2,3,4", "list (1,2,3,4)");
            script.test("check 1,2,3,(4,5),6", "list (1,2,3,list (4,5),6)");
            script.test("check (1,2),3,(4,5),6", "list (list (1,2),3,list (4,5),6)");
            script.test("check (1,2,3)(0)","1");
            script.test("check ((1,2,3),(4,5,6),(7,8,9))(1)(2)","6");

            //working down to here for 2.0

            //assignment and reflection
            script.test("check a=1","1");

            script.test("check test.blah.length","10");
            script.test("check test.s.length","8");
            script.exec("test.s=\"how are you gentlemen?\"");
            script.exec("a=test.s");
            script.test("check a.length","22");
            script.test("a=(test.s);check a.length","22");
            script.test("check test.s.length","22");
            script.test("test.i=1;check test.i","1");
            script.test("check this.s","how are you gentlemen?");
            script.exec("array = ((1,2,3),(4,5,6),(7,8,9))");
            script.test("check array(2)(2)","9");
            script.exec("array(1)(2)=42");
            script.test("check array(1)(2)", "42");
            script.test("check test.doit:(1,2,3,4,5)","15"); //it's a summation for varargs
            script.test("check test.doit:(7,8)","56");       //two args means multiply

            //this is working for 2.0

            if(true) return;

            //script.test("stz=new (java.util.StringTokenizer) (\"All your base are belong to us\");while(stz.hasMoreTokens){check stz.nextToken}","Allyourbasearebelongtous");
            script.exec("{1+1}");

            script.exec("sys = load (\"java.lang.System\"); sys.out.println:(\"I\'m not confused.  I\'m just well mixed.\")");
            script.exec("(load (\"java.lang.System\")).out.println:(\"--Robert Frost\")");
            //Next issue to resolve:
            //script.exec("java.lang.System.out.println:(\"doesn't work\")");
            //This should work, but popping up a frame every time we run is inconvenient.
            //script.exec("f=new (java.awt.Frame) (\"framename\"); f.setSize:(500,500);f.setVisible:(true)");//;f.setSize:(500,500);f.setVisible:(true)","");


            //script.exec("printlnz = (sys.out.println)");
            //script.exec("printlnz:(\"this is working?\")");

            //script.exec("java.lang.System.out.println:(\"doesn't work\")");
            //we have two problems, 1) "load" isn't forming the correct tree (getting load-notok-java) should get (load-java) (this interferes with the return results and others)
            //We are using Class.forName which only finds a Class when we need to be examining things at a Package level if we want to store a package as a variable which is the only
            //way that java.lang.System.out.println will resolve (we need some kind of java <- javaPackage operation, then we need to be able to traverse java packages)
            //we want a "var" operation to explicitly create variables rather than implicitly for better compile time checking

            //this should generate a parse-warning in the presence of a "println" CrossLibrary token
            //something like "Warning, assigning value to token"
            //CrossTree ct = cp.parse("println = sys.out.println");

            /*script.exec("java.lang.System.out.println:(\"doesn't work\")");

            CrossTree ct = cp.parse("java.lang.System.out.println:(\"doesn't work\")");
            ct.inprint();
            //System.out.println();
            ct.print();*/

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
