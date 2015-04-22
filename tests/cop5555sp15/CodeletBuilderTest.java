package cop5555sp15;

import cop5555sp15.ast.BooleanLitExpression;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Carlos on 4/21/2015.
 */
public class CodeletBuilderTest
{
    @Test
    /**
     * Expected output:
     * 3
     *
     */
    public void getList() throws Exception
    {
        System.out.println(".......Running Test: getList");
        String source;
        source = "class ListInit{\n"
                + "def l1: @[int];\n"
                + "def l2: @[string];\n"
                + "def i1: int;\n"
                + "l1 = @[300,400,500];\n"
                + "l2 = @[\"go\", \"gators\"];\n"
                + "i1 = 42;\n"
                + "}";

        @SuppressWarnings("rawtypes")
        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();

        List l1 = CodeletBuilder.getList(codelet, "l1");
        assertEquals(3, l1.size());
        assertEquals(300, l1.get(0));
        assertEquals(400, l1.get(1));
        assertEquals(500, l1.get(2));

        List l2 = CodeletBuilder.getList(codelet, "l2");
        assertEquals(2, l2.size());
        assertEquals("go", l2.get(0));
        assertEquals("gators", l2.get(1));
    }

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
        source = "class Test{\n"
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
    public void getInt() throws Exception
    {
        System.out.println(".......Running Test: getInt");
        String source;
        source = "class Test{\n"
                + "def i1: int;\n"
                + "i1 = 5;\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        int i1 = CodeletBuilder.getInt(codelet, "i1");
        assertEquals(i1, 5);
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
        source = "class Test{\n"
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
    public void getString() throws Exception
    {
        System.out.println(".......Running Test: getString");
        String source;
        source = "class Test{\n"
                + "def s: string;\n"
                + "s = \"hello\";\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        String s = CodeletBuilder.getString(codelet, "s");
        assertEquals(s, "hello");
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
        source = "class Test{\n"
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

    @Test
    public void getBoolean() throws Exception
    {
        System.out.println(".......Running Test: getBoolean");
        String source;
        source = "class CallExecuteTwice{\n"
                + "def b: boolean;\n"
                + "b = true;\n"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);
        codelet.execute();
        Boolean b = CodeletBuilder.getBoolean(codelet, "b");
        assertEquals(b, true);
    }
}