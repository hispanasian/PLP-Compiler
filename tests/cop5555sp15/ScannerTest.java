package cop5555sp15;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for ScannerTest
 */
public class ScannerTest
{
    Scanner makeScanner(String string) { return new Scanner(new TokenStream(string.toCharArray())); }
    @Test
    public void testScan() throws Exception
    {

    }

    /**
     * Tests the eof method in Scanner
     * @throws Exception
     */
    @Test
    public void testEof() throws Exception
    {
        Scanner scanner = makeScanner("");
        assertEquals("eof should return true when code is an empty array", true, scanner.eof());

        scanner = makeScanner("123");
        assertEquals("eof should return false when cur is at the beginning of a non-empty code", false, scanner.eof());

        scanner.cur = 2;
        assertEquals("eof should return false when cur is at the last character of code", false, scanner.eof());

        scanner.cur = 3;
        assertEquals("eof should return true when cur has passed the last character of code", true, scanner.eof());

        scanner.cur = 50;
        assertEquals("eof should return true when cur has surpassed the last character", true, scanner.eof());
    }

    /**
     * Tests the newLine method in Scanner.
     */
    @Test
    public void testNewLine()
    {
        Scanner scanner = makeScanner("\r");
        assertEquals("\\r should return true", true, scanner.newLine());
        assertEquals("\\r should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\r should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("\n");
        assertEquals("\\n should return true", true, scanner.newLine());
        assertEquals("\\n should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\n should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("\r\n");
        assertEquals("\\r\\n should return true", true, scanner.newLine());
        assertEquals("\\r\\n should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\r\\n should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("\n\r");
        assertEquals("\\n\\r should return true", true, scanner.newLine());
        assertEquals("\\n\\r should increment the line counter by one", 1, scanner.line);
        assertEquals("\\n\\r should increment cur by one", 1, scanner.cur);

        scanner = makeScanner("\t");
        assertEquals("\\t should return false", false, scanner.newLine());
        assertEquals("\\t should not increment the line counter", 0, scanner.line);
        assertEquals("\\t should not increment cur", 0, scanner.cur);

        scanner = makeScanner("\t\r");
        assertEquals("\\t\\r should return false", false, scanner.newLine());
        assertEquals("\\t\\r should not increment the line counter", 0, scanner.line);
        assertEquals("\\t\\r should not increment cur", 0, scanner.cur);
    }

    /**
     * Tests the reservedLiteral method in Scanner. reservedLiteral should take a string and return the Kind of literal
     * that is represented by the String (if any).
     */
    @Test
    public void testReservedLiteral()
    {
        /* Test for key words */
        assertEquals("the string \"int\" should return Kind.KW_INT", TokenStream.Kind.KW_INT, Scanner.reservedLiteral("int"));
        assertEquals("the string \"string\" should return Kind.KW_STRING", TokenStream.Kind.KW_STRING, Scanner.reservedLiteral("string"));
        assertEquals("the string \"boolean\" should return Kind.KW_BOOLEAN", TokenStream.Kind.KW_BOOLEAN, Scanner.reservedLiteral("boolean"));
        assertEquals("the string \"import\" should return Kind.KW_IMPORT", TokenStream.Kind.KW_IMPORT, Scanner.reservedLiteral("import"));
        assertEquals("the string \"class\" should return Kind.KW_CLASS", TokenStream.Kind.KW_CLASS, Scanner.reservedLiteral("class"));
        assertEquals("the string \"def\" should return Kind.KW_DEF", TokenStream.Kind.KW_DEF, Scanner.reservedLiteral("def"));
        assertEquals("the string \"while\" should return Kind.KW_WHILE", TokenStream.Kind.KW_WHILE, Scanner.reservedLiteral("while"));
        assertEquals("the string \"if\" should return Kind.KW_IF", TokenStream.Kind.KW_IF, Scanner.reservedLiteral("if"));
        assertEquals("the string \"else\" should return Kind.KW_ELSE", TokenStream.Kind.KW_ELSE, Scanner.reservedLiteral("else"));
        assertEquals("the string \"return\" should return Kind.KW_RETURN", TokenStream.Kind.KW_RETURN, Scanner.reservedLiteral("return"));
        assertEquals("the string \"print\" should return Kind.KW_PRINT", TokenStream.Kind.KW_PRINT, Scanner.reservedLiteral("print"));

        /* Test for boolean literals */
        assertEquals("the string \"true\" should return Kind.BL_TRUE", TokenStream.Kind.BL_TRUE, Scanner.reservedLiteral("true"));
        assertEquals("the string \"false\" should return Kind.BL_FALSE", TokenStream.Kind.BL_FALSE, Scanner.reservedLiteral("false"));

        /* Test for the null literal */
        assertEquals("the string \"null\" should return Kind.NL_NULL", TokenStream.Kind.NL_NULL, Scanner.reservedLiteral("null"));

        /* Tests that should return null */
        assertEquals("the string \"junk\" should return null", null, Scanner.reservedLiteral("junk"));
        assertEquals("the string \"\r\" should return null", null, Scanner.reservedLiteral("\r"));
        assertEquals("the string \"654\" should return null", null, Scanner.reservedLiteral("654"));
        assertEquals("the string \"there is a space\" should return null", null, Scanner.reservedLiteral("there is a space"));
        assertEquals("the string \"i have a null\" should return null", null, Scanner.reservedLiteral("i have a null"));
        assertEquals("the string \"printnot\" should return null", null, Scanner.reservedLiteral("printnot"));
    }

    /**
     * Tests the keyword method in Scanner. It should return the Kind of keyword that the argument represents (if any)
     */
    @Test
    public void testKeyword()
    {
        assertEquals("the string \"int\" should return Kind.KW_INT", TokenStream.Kind.KW_INT, Scanner.keyword("int"));
        assertEquals("the string \"string\" should return Kind.KW_STRING", TokenStream.Kind.KW_STRING, Scanner.keyword("string"));
        assertEquals("the string \"boolean\" should return Kind.KW_BOOLEAN", TokenStream.Kind.KW_BOOLEAN, Scanner.keyword("boolean"));
        assertEquals("the string \"import\" should return Kind.KW_IMPORT", TokenStream.Kind.KW_IMPORT, Scanner.keyword("import"));
        assertEquals("the string \"class\" should return Kind.KW_CLASS", TokenStream.Kind.KW_CLASS, Scanner.keyword("class"));
        assertEquals("the string \"def\" should return Kind.KW_DEF", TokenStream.Kind.KW_DEF, Scanner.keyword("def"));
        assertEquals("the string \"while\" should return Kind.KW_WHILE", TokenStream.Kind.KW_WHILE, Scanner.keyword("while"));
        assertEquals("the string \"if\" should return Kind.KW_IF", TokenStream.Kind.KW_IF, Scanner.keyword("if"));
        assertEquals("the string \"else\" should return Kind.KW_ELSE", TokenStream.Kind.KW_ELSE, Scanner.keyword("else"));
        assertEquals("the string \"return\" should return Kind.KW_RETURN", TokenStream.Kind.KW_RETURN, Scanner.keyword("return"));
        assertEquals("the string \"print\" should return Kind.KW_PRINT", TokenStream.Kind.KW_PRINT, Scanner.keyword("print"));

        /* Test the nulls */
        assertEquals("the string \"printnot\" should return null", null, Scanner.keyword("printnot"));
        assertEquals("the string \"if contains else\" should return null", null, Scanner.keyword("if containselse"));
        assertEquals("the string \"int\r\" should return null", null, Scanner.keyword("int\r"));
    }

    /**
     * Tests the booleanLiteral method in Scanner.
     */
    @Test
    public void testBooleanLiteral()
    {
        assertEquals("the string \"true\" should return Kind.BL_TRUE", TokenStream.Kind.BL_TRUE, Scanner.booleanLiteral("true"));
        assertEquals("the string \"false\" should return Kind.BL_FALSE", TokenStream.Kind.BL_FALSE, Scanner.booleanLiteral("false"));

        /* Test the nulls */
        assertEquals("the string \"falseify\" should return Kind.BL_FALSE", null, Scanner.booleanLiteral("falseify"));
        assertEquals("the string \"not true\" should return Kind.BL_FALSE", null, Scanner.booleanLiteral("not true"));
    }

    /**
     * Tests the nullLiteral method in Scanner.
     */
    @Test
    public void testNullLiteral()
    {
        assertEquals("the string \"null\" should return Kind.NL_NULL", TokenStream.Kind.NL_NULL, Scanner.nullLiteral("null"));

        /* Test the nulls */
        assertEquals("the string \"nullify\" should return Kind.NL_NULL", null, Scanner.nullLiteral("nullify"));
    }

    @Test
    public void testTokenize() throws Exception {

    }
}