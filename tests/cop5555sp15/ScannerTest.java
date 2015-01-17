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
     * Tests the whitespace method in Scanner
     */
    @Test
    public void testWhitespace()
    {
        /* New line tests */
        Scanner scanner = makeScanner("\r");
        assertEquals("\\r should return true", true, scanner.whitespace());
        assertEquals("\\r should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\r should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("\n");
        assertEquals("\\n should return true", true, scanner.whitespace());
        assertEquals("\\n should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\n should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("\r\n");
        assertEquals("\\r\\n should return true", true, scanner.whitespace());
        assertEquals("\\r\\n should increment the line counter by 1", 1, scanner.line);
        assertEquals("\\r\\n should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("\n\r");
        assertEquals("\\n\\r should return true", true, scanner.whitespace());
        assertEquals("\\n\\r should increment the line counter by one", 1, scanner.line);
        assertEquals("\\n\\r should increment cur by one", 1, scanner.cur);

        /* Other whitespace tests */
        scanner = makeScanner("\t");
        assertEquals("\\t should return true", true, scanner.whitespace());
        assertEquals("\\t should not increment the line counter", 0, scanner.line);
        assertEquals("\\t should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("\t\r");
        assertEquals("\\t\\r should return true", true, scanner.whitespace());
        assertEquals("\\t\\r should not increment the line counter", 0, scanner.line);
        assertEquals("\\t\\r should increment cur by 1", 1, scanner.cur);


        scanner = makeScanner("\t\r");
        assertEquals("\\t\\r should return true", true, scanner.whitespace());
        assertEquals("\\t\\r should not increment the line counter", 0, scanner.line);
        assertEquals("\\t\\r should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner(" ");
        assertEquals("\" \" should return true", true, scanner.whitespace());
        assertEquals("\" \" should not increment the line counter", 0, scanner.line);
        assertEquals("\" \" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("a");
        assertEquals("\"a\" should return true", false, scanner.whitespace());
        assertEquals("\"a\" should not increment the line counter", 0, scanner.line);
        assertEquals("\"a\" should not increment cur", 0, scanner.cur);

        scanner = makeScanner("a space");
        assertEquals("\"a space\" should return true", false, scanner.whitespace());
        assertEquals("\"a space\" should not increment the line counter", 0, scanner.line);
        assertEquals("\"a space\" should not increment cur", 0, scanner.cur);
    }

    /**
     * Tests the operator method in Scanner.
     */
    @Test
    public void testOperator()
    {
        Scanner scanner = makeScanner("=");
        assertEquals("\"=\" should return Kind.ASSIGN", TokenStream.Kind.ASSIGN, scanner.operator());
        assertEquals("\"=\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("|");
        assertEquals("\"|\" should return Kind.BAR", TokenStream.Kind.BAR, scanner.operator());
        assertEquals("\"|\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("&");
        assertEquals("\"&\" should return Kind.AND", TokenStream.Kind.AND, scanner.operator());
        assertEquals("\"&\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("==");
        assertEquals("\"==\" should return Kind.EQUAL", TokenStream.Kind.EQUAL, scanner.operator());
        assertEquals("\"==\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("!=");
        assertEquals("\"!=\" should return Kind.NOTEQUAL", TokenStream.Kind.NOTEQUAL, scanner.operator());
        assertEquals("\"!=\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("<");
        assertEquals("\"<\" should return Kind.LT", TokenStream.Kind.LT, scanner.operator());
        assertEquals("\"<\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner(">");
        assertEquals("\">\" should return Kind.GT", TokenStream.Kind.GT, scanner.operator());
        assertEquals("\">\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("<=");
        assertEquals("\"<=\" should return Kind.LE", TokenStream.Kind.LE, scanner.operator());
        assertEquals("\"<=\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner(">=");
        assertEquals("\">=\" should return Kind.GE", TokenStream.Kind.GE, scanner.operator());
        assertEquals("\">=\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("+");
        assertEquals("\"+\" should return Kind.PLUS", TokenStream.Kind.PLUS, scanner.operator());
        assertEquals("\"+\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("-");
        assertEquals("\"-\" should return Kind.MINUS", TokenStream.Kind.MINUS, scanner.operator());
        assertEquals("\"-\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("*");
        assertEquals("\"*\" should return Kind.TIMES", TokenStream.Kind.TIMES, scanner.operator());
        assertEquals("\"*\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("/");
        assertEquals("\"/\" should return Kind.BAR", TokenStream.Kind.DIV, scanner.operator());
        assertEquals("\"/\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("/*");
        assertEquals("\"/*\" should return null", null, scanner.operator());
        assertEquals("\"/*\" should not increment cur", 0, scanner.cur);

        scanner = makeScanner("%");
        assertEquals("\"%\" should return Kind.MOD", TokenStream.Kind.MOD, scanner.operator());
        assertEquals("\"%\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("!");
        assertEquals("\"!\" should return Kind.NOT", TokenStream.Kind.NOT, scanner.operator());
        assertEquals("\"!\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("<<");
        assertEquals("\"<<\" should return Kind.LSHIFT", TokenStream.Kind.LSHIFT, scanner.operator());
        assertEquals("\"<<\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner(">>");
        assertEquals("\">>\" should return Kind.RSHIFT", TokenStream.Kind.RSHIFT, scanner.operator());
        assertEquals("\">>\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("->");
        assertEquals("\"->\" should return Kind.ARROW", TokenStream.Kind.ARROW, scanner.operator());
        assertEquals("\"->\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("@");
        assertEquals("\"@\" should return Kind.AT", TokenStream.Kind.AT, scanner.operator());
        assertEquals("\"@\" should increment cur by 1", 1, scanner.cur);
    }

    /**
     * Tests the separator method in Scanner.
     */
    @Test
    public void testSeparator()
    {
        Scanner scanner = makeScanner(".");
        assertEquals("\".\" should return Kind.DOT", TokenStream.Kind.DOT, scanner.separator());
        assertEquals("\".\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("..");
        assertEquals("\"..\" should return Kind.RANGE", TokenStream.Kind.RANGE, scanner.separator());
        assertEquals("\"..\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner(";");
        assertEquals("\";\" should return Kind.SEMICOLON", TokenStream.Kind.SEMICOLON, scanner.separator());
        assertEquals("\";\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner(",");
        assertEquals("\",\" should return Kind.COMMA", TokenStream.Kind.COMMA, scanner.separator());
        assertEquals("\",\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("(");
        assertEquals("\"(\" should return Kind.LPAREN", TokenStream.Kind.LPAREN, scanner.separator());
        assertEquals("\"(\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner(")");
        assertEquals("\")\" should return Kind.RPAREN", TokenStream.Kind.RPAREN, scanner.separator());
        assertEquals("\")\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("[");
        assertEquals("\"[\" should return Kind.LSQUARE", TokenStream.Kind.LSQUARE, scanner.separator());
        assertEquals("\"[\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("]");
        assertEquals("\"]\" should return Kind.RSQUARE", TokenStream.Kind.RSQUARE, scanner.separator());
        assertEquals("\"]\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("{");
        assertEquals("\"{\" should return Kind.LCURLY", TokenStream.Kind.LCURLY, scanner.separator());
        assertEquals("\"{\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("}");
        assertEquals("\"}\" should return Kind.RCURLY", TokenStream.Kind.RCURLY, scanner.separator());
        assertEquals("\"}\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner(":");
        assertEquals("\":\" should return Kind.COLON", TokenStream.Kind.COLON, scanner.separator());
        assertEquals("\":\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("?");
        assertEquals("\"?\" should return Kind.QUESTION", TokenStream.Kind.QUESTION, scanner.separator());
        assertEquals("\"?\" should increment cur by 1", 1, scanner.cur);
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

    /**
     * Tests the identity method in Scanner.
     */
    @Test
    public void testIdentity()
    {
        Scanner scanner = makeScanner("int");
        assertEquals("the string \"int\" should return Kind.KW_INT", TokenStream.Kind.KW_INT, scanner.identity());
        assertEquals("\"int\" should increment cur by 3", 3, scanner.cur);

        scanner = makeScanner("string");
        assertEquals("the string \"string\" should return Kind.KW_STRING", TokenStream.Kind.KW_STRING, scanner.identity());
        assertEquals("\"string\" should increment cur by 6", 6, scanner.cur);

        scanner = makeScanner("boolean");
        assertEquals("the string \"boolean\" should return Kind.KW_BOOLEAN", TokenStream.Kind.KW_BOOLEAN, scanner.identity());
        assertEquals("\"boolean\" should increment cur by 7", 7, scanner.cur);

        scanner = makeScanner("import");
        assertEquals("the string \"import\" should return Kind.KW_IMPORT", TokenStream.Kind.KW_IMPORT, scanner.identity());
        assertEquals("\"import\" should increment cur by 6", 6, scanner.cur);

        scanner = makeScanner("class");
        assertEquals("the string \"class\" should return Kind.KW_CLASS", TokenStream.Kind.KW_CLASS, scanner.identity());
        assertEquals("\"class\" should increment cur by 5", 5, scanner.cur);

        scanner = makeScanner("def");
        assertEquals("the string \"def\" should return Kind.KW_DEF", TokenStream.Kind.KW_DEF, scanner.identity());
        assertEquals("\"def\" should increment cur by 3", 3, scanner.cur);

        scanner = makeScanner("while");
        assertEquals("the string \"while\" should return Kind.KW_WHILE", TokenStream.Kind.KW_WHILE, scanner.identity());
        assertEquals("\"wihle\" should increment cur by 5", 5, scanner.cur);

        scanner = makeScanner("if");
        assertEquals("the string \"if\" should return Kind.KW_IF", TokenStream.Kind.KW_IF, scanner.identity());
        assertEquals("\"if\" should increment cur by 2", 2, scanner.cur);

        scanner = makeScanner("else");
        assertEquals("the string \"else\" should return Kind.KW_ELSE", TokenStream.Kind.KW_ELSE, scanner.identity());
        assertEquals("\"else\" should increment cur by 4", 4, scanner.cur);

        scanner = makeScanner("return");
        assertEquals("the string \"return\" should return Kind.KW_RETURN", TokenStream.Kind.KW_RETURN, scanner.identity());
        assertEquals("\"return\" should increment cur by 6", 6, scanner.cur);

        scanner = makeScanner("print");
        assertEquals("the string \"print\" should return Kind.KW_PRINT", TokenStream.Kind.KW_PRINT, scanner.identity());
        assertEquals("\"print\" should increment cur by 5", 5, scanner.cur);

        /* Test for boolean literals */
        scanner = makeScanner("true");
        assertEquals("the string \"true\" should return Kind.BL_TRUE", TokenStream.Kind.BL_TRUE, scanner.identity());
        assertEquals("\"true\" should increment cur by 4", 4, scanner.cur);

        scanner = makeScanner("false");
        assertEquals("the string \"false\" should return Kind.BL_FALSE", TokenStream.Kind.BL_FALSE, scanner.identity());
        assertEquals("\"false\" should increment cur by 5", 5, scanner.cur);

        /* Test for the null literal */
        scanner = makeScanner("null");
        assertEquals("the string \"null\" should return Kind.NL_NULL", TokenStream.Kind.NL_NULL, scanner.identity());
        assertEquals("\"null\" should increment cur by 4", 4, scanner.cur);

        /* Test for general identity */
        scanner = makeScanner("someIdent");
        assertEquals("the string \"someIdent\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"someIdent\" should increment cur by 9", 9, scanner.cur);

        scanner = makeScanner("this2works");
        assertEquals("the string \"this2works\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"this2works\" should increment cur by 10", 10, scanner.cur);

        scanner = makeScanner("this\risweird");
        assertEquals("the string \"this\risweird\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"this\risweird\" should increment cur by 4", 4, scanner.cur);

        scanner = makeScanner("this2works");
        assertEquals("the string \"this2works\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"this2works\" should increment cur by 10", 10, scanner.cur);

        scanner = makeScanner("def_startwithkeywrod");
        assertEquals("the string \"def_startwithkeyword\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"def_startwithkeyword\" should increment cur by 20", 20, scanner.cur);

        scanner = makeScanner("endkw_print");
        assertEquals("the string \"endkw_print\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"endkw_print\" should increment cur by 11", 11, scanner.cur);

        scanner = makeScanner("has.separator");
        assertEquals("the string \"has.separator\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"has.separator\" should increment cur by 3", 3, scanner.cur);

        scanner = makeScanner("has=operator");
        assertEquals("the string \"has=operator\" should return Kind.IDENT", TokenStream.Kind.IDENT, scanner.identity());
        assertEquals("\"has=operator\" should increment cur by 3", 3, scanner.cur);

        scanner = makeScanner("null operator");
        assertEquals("the string \"null operator\" should return Kind.NL_NULL", TokenStream.Kind.NL_NULL, scanner.identity());
        assertEquals("\"null operator\" should increment cur by 4", 4, scanner.cur);

        /* Tests that should return null */
        scanner = makeScanner("123a4");
        assertEquals("the string \"123a4\" should return null", null, scanner.identity());
        assertEquals("\"123a4\" should increment cur by 0", 0, scanner.cur);

        scanner = makeScanner("+operator");
        assertEquals("the string \"+operator\" should return null", null, scanner.identity());
        assertEquals("\"+operator\" should increment cur by 0", 0, scanner.cur);

        scanner = makeScanner(" space");
        assertEquals("the string \" space\" should return Kind.IDENT", null, scanner.identity());
        assertEquals("\" space\" should increment cur by 0", 0, scanner.cur);

        scanner = makeScanner("");
        assertEquals("the string \"\" should return null", null, scanner.identity());
        assertEquals("\"\" should increment cur by 0", 0, scanner.cur);
    }

    /**
     * Tests the intLiteral method in Scanner.
     */
    @Test
    public void testIntLiteral()
    {
        /* The Kind.INT_LIT cases */
        Scanner scanner = makeScanner("5");
        assertEquals("\"5\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"5\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("65454");
        assertEquals("\"65454\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"65454\" should increment cur by 5", 5, scanner.cur);

        scanner = makeScanner("0654");
        assertEquals("\"0654\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"0654\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("1.789");
        assertEquals("\"1.789\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"1.789\" should increment cur by 1", 1, scanner.cur);

        scanner = makeScanner("100,000");
        assertEquals("\"100,000\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"100,000\" should increment cur by 3", 3, scanner.cur);

        scanner = makeScanner("00");
        assertEquals("\"00\" should return Kind.INT_LIT", TokenStream.Kind.INT_LIT, scanner.intLiteral());
        assertEquals("\"00\" should increment cur by 1", 1, scanner.cur);

        /* The null cases */
        scanner = makeScanner("-654");
        assertEquals("\"-654\" should return null", null, scanner.intLiteral());
        assertEquals("\"-654\" should not increment cur", 0, scanner.cur);

        scanner = makeScanner(".123");
        assertEquals("\".123\" should return null", null, scanner.intLiteral());
        assertEquals("\".123\" should not increment cur", 0, scanner.cur);

        scanner = makeScanner("s54");
        assertEquals("\"s54\" should return null", null, scanner.intLiteral());
        assertEquals("\"s54\" should not increment cur", 0, scanner.cur);
    }

    /**
     * Tests the stringLiteral method in Scanner
     */
    @Test
    public void testStringLiteral()
    {
        /* The Kind.STRING_LIT cases */
        Scanner scanner = makeScanner("\"string\"");
        assertEquals("\"string\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"string\" should increment cur by 8", 8, scanner.cur);
        assertEquals("\"string\" should increment line by 0", 0, scanner.line);

        scanner = makeScanner("\"\"");
        assertEquals("\"\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"\" should increment cur by 2", 2, scanner.cur);
        assertEquals("\"\" should increment line by 0", 0, scanner.line);

        scanner = makeScanner("\"new\rline\"");
        assertEquals("\"new\rline\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"new\rline\" should increment cur by 10", 10, scanner.cur);
        assertEquals("\"new\rline\" should increment line by 1", 1, scanner.line);

        scanner = makeScanner("\"operators+-5485\"");
        assertEquals("\"operators+-5485\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"operators+-5485\" should increment cur by 17", 17, scanner.cur);
        assertEquals("\"operators+-5485\" should increment line by 0", 0, scanner.line);

        scanner = makeScanner("\"i have a escape seq \\n\"");
        assertEquals("\"i have a escape seq \\n\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"i have a escape seq \\n\" should increment cur by 24", 24, scanner.cur);
        assertEquals("\"i have a escape seq \\n\" should increment line by 0", 0, scanner.line);

        scanner = makeScanner("\"another \r\nnew line\"");
        assertEquals("\"another new \r\nline\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"another new \r\nline\" should increment cur by 20", 20, scanner.cur);
        assertEquals("\"another new \r\nline\" should increment line by 1", 1, scanner.line);

        scanner = makeScanner("\"Some asterisks**\"");
        assertEquals("\"Some asterisks**\" should return Kind.STRINT_LIT", TokenStream.Kind.STRING_LIT, scanner.stringLiteral());
        assertEquals("\"Some asterisks**\" should increment cur by 18", 18, scanner.cur);
        assertEquals("\"Some asterisks**\" should increment line by 0", 0, scanner.line);

        /* The UNTERMINATED_STRING cases */
        scanner = makeScanner("\"not terminated string");
        assertEquals("\"not terminated string should return Kind.UNTERMINATED_STRING", TokenStream.Kind.UNTERMINATED_STRING, scanner.stringLiteral());
        assertEquals("\"not terminated string should increment cur by 23", 22, scanner.cur);
        assertEquals("\"not terminated string should increment line by 0", 0, scanner.line);

        scanner = makeScanner("\"not terminated\rnewline");
        assertEquals("\"not terminated\rnewline should return Kind.UNTERMINATED_STRING", TokenStream.Kind.UNTERMINATED_STRING, scanner.stringLiteral());
        assertEquals("\"not terminated\rnewline should increment cur by 23", 23, scanner.cur);
        assertEquals("\"not terminated\rnewline should increment line by 1", 1, scanner.line);
    }

    @Test
    public void testComment()
    {
        Scanner scanner = makeScanner("/*  */");
        assertEquals("\"/*  */\" should return null", null, scanner.comment());
        assertEquals("\"/*  */\" should increment cur by 6", 6, scanner.cur);
        assertEquals("\"/*  */\" should increment the line counter by 0", 0, scanner.line);

        scanner = makeScanner("/**/");
        assertEquals("\"/**/\" should return null", null, scanner.comment());
        assertEquals("\"/**/\" should increment cur by 4", 4, scanner.cur);
        assertEquals("\"/**/\" should increment the line counter by 0", 0, scanner.line);

        scanner = makeScanner("/* Some stuff */");
        assertEquals("\"/* Some stuff */\" should return null", null, scanner.comment());
        assertEquals("\"/* Some stuff */\" should increment cur by 16", 16, scanner.cur);
        assertEquals("\"/* Some stuff */\" should increment the line counter by 0", 0, scanner.line);

        scanner = makeScanner("/*******/");
        assertEquals("\"/*******/\" should return null", null, scanner.comment());
        assertEquals("\"/*******/\" should increment cur by 9", 9, scanner.cur);
        assertEquals("\"/*******/\" should increment the line counter by 0", 0, scanner.line);

        scanner = makeScanner("/*\r\n*/");
        assertEquals("\"/*\r\n*/\" should return null", null, scanner.comment());
        assertEquals("\"/*\r\n*/\" should increment cur by 6", 6, scanner.cur);
        assertEquals("\"/*\r\n*/\" should increment the line counter by 1", 1, scanner.line);

        scanner = makeScanner("/* operators +*-/*= */");
        assertEquals("\"/* operators +*-/*= */\" should return null", null, scanner.comment());
        assertEquals("\"/* operators +*-/*= */\" should increment cur by 22", 22, scanner.cur);
        assertEquals("\"/* operators +*-/*= */\" should increment the line counter by 0", 0, scanner.line);

        /* Test for Kind.UNTERMINATED_COMMENT */
        scanner = makeScanner("/* operators +-=");
        assertEquals("\"/* operators +-=\" should return Kind.UNTERMINATED_COMMENT", TokenStream.Kind.UNTERMINATED_COMMENT, scanner.comment());
        assertEquals("\"/* operators +-=\" should increment cur by 16", 16, scanner.cur);
        assertEquals("\"/* operators +-=\" should increment the line counter by 0", 0, scanner.line);

        scanner = makeScanner("/* \r");
        assertEquals("\"/* \r\" should return Kind.UNTERMINATED_COMMENT", TokenStream.Kind.UNTERMINATED_COMMENT, scanner.comment());
        assertEquals("\"/* \r\" should increment cur by 4", 4, scanner.cur);
        assertEquals("\"/* \r\" should increment the line counter by 1", 1, scanner.line);

        scanner = makeScanner("/* ***");
        assertEquals("\"/* ***\" should return Kind.UNTERMINATED_COMMENT", TokenStream.Kind.UNTERMINATED_COMMENT, scanner.comment());
        assertEquals("\"/* ***\" should increment cur by 6", 6, scanner.cur);
        assertEquals("\"/* ***\" should increment the line counter by 0", 0, scanner.line);

        /* Test for non-comment cases */
        scanner = makeScanner("I am not a comment");
        assertEquals("\"I am not a comment\" should return null", null, scanner.comment());
        assertEquals("\"I am not a comment\" should increment cur by 0", 0, scanner.cur);
        assertEquals("\"I am not a comment\" should increment the line counter by 0", 0, scanner.line);
    }

    @Test
    public void testTokenize() throws Exception
    {

    }
}