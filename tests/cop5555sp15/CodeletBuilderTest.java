package cop5555sp15;

import cop5555sp15.ast.BooleanLitExpression;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Carlos on 4/21/2015.
 */
public class CodeletBuilderTest
{
    @Test
    /**
     * Tests the get/set int methods of CodeletBuilder.
     * Expected output:
     * first time
     * 0
     * 2
     * second time
     */
    public void getSetInt() throws Exception
    {
        System.out.println(".......Running Test: getSetInt");
        String source;
        source = "class CallExecuteTwice{\n"
                + "def i1: int;\n"
                + "if (i1 == 0){print \"first time\";}\n"
                + "else {print \"second time\";};\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        int i1 = CodeletBuilder.getInt(codelet, "i1");
        System.out.println(i1);
        CodeletBuilder.setInt(codelet, "i1", i1 + 2);
        System.out.println(CodeletBuilder.getInt(codelet, "i1"));
        codelet.execute();
    }

    @Test
    /**
     * Tests the get/set int methods of CodeletBuilder.
     * Expected output:
     * boon ull!
     * null
     * gators
     * go gators!
     * boo noles!
     */
    public void getSetString() throws Exception
    {
        System.out.println(".......Running Test: getSetString");
        String source;
        source = "class CallExecuteTwice{\n"
                + "def s1: string;\n"
                + "if (s1 == \"gators\"){print \"go gators!\";}\n"
                + "else {print \"boo \" + s1 + \"!\";};\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        String s1 = CodeletBuilder.getString(codelet, "s1");
        System.out.println(s1);

        CodeletBuilder.setString(codelet, "s1", "gators");
        System.out.println(CodeletBuilder.getString(codelet, "s1"));
        codelet.execute();

        CodeletBuilder.setString(codelet, "s1", "noles");
        System.out.println(CodeletBuilder.getString(codelet, "s1"));
        codelet.execute();
    }

    @Test
    /**
     * Tests the get/set int methods of CodeletBuilder.
     * Expected output:
     * unfortunately false
     * false
     * true
     * so true
     * false
     * unfortunately false
     */
    public void getSetBoolean() throws Exception
    {
        System.out.println(".......Running Test: getSetBoolean");
        String source;
        source = "class CallExecuteTwice{\n"
                + "def b1: boolean;\n"
                + "if (b1){print \"so true\";}\n"
                + "else {print \"unfortunately false\";};\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        Boolean b1 = CodeletBuilder.getBoolean(codelet, "b1");
        System.out.println(b1);

        CodeletBuilder.setBoolean(codelet, "b1", true);
        System.out.println(CodeletBuilder.getBoolean(codelet, "b1"));
        codelet.execute();

        CodeletBuilder.setBoolean(codelet, "b1", false);
        System.out.println(CodeletBuilder.getBoolean(codelet, "b1"));
        codelet.execute();
    }
}