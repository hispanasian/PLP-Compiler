package cop5555sp15;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import static cop5555sp15.TokenStream.Kind.*;

/**
 * Test cases for ScannerTest
 */
public class TestScanner
{
    Scanner makeScanner(String string) { return new Scanner(new TokenStream(string.toCharArray())); }

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

    /**
     * Test the scan method in Scanner
     */
    @Test
    public void testScan()
    {
        Scanner scanner;
        ArrayList<TokenStream.Token> tokens;

        /* Trivial tests */
        scanner = makeScanner("int");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"int\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_INT", TokenStream.Kind.KW_INT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 3", 3, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 3", 3, tokens.get(1).beg);
        assertEquals("Token 1 should end at 3", 3, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("string");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"string\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_STRING", TokenStream.Kind.KW_STRING, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 6", 6, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 6", 6, tokens.get(1).beg);
        assertEquals("Token 1 should end at 6", 6, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("boolean");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"boolean\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_BOOLEAN", TokenStream.Kind.KW_BOOLEAN, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 7", 7, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 7", 7, tokens.get(1).beg);
        assertEquals("Token 1 should end at 7", 7, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("import");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"import\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_IMPORT", TokenStream.Kind.KW_IMPORT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 6", 6, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 6", 6, tokens.get(1).beg);
        assertEquals("Token 1 should end at 6", 6, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("class");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"class\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_CLASS", TokenStream.Kind.KW_CLASS, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 5", 5, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("def");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"def\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_DEF", TokenStream.Kind.KW_DEF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 3", 3, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 3", 3, tokens.get(1).beg);
        assertEquals("Token 1 should end at 3", 3, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("while");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"while\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_WHILE", TokenStream.Kind.KW_WHILE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 5", 5, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("if");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"if\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_IF", TokenStream.Kind.KW_IF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("else");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"else\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_ELSE", TokenStream.Kind.KW_ELSE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 4", 4, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 4", 4, tokens.get(1).beg);
        assertEquals("Token 1 should end at 4", 4, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("return");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"return\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_RETURN", TokenStream.Kind.KW_RETURN, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 6", 6, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 6", 6, tokens.get(1).beg);
        assertEquals("Token 1 should end at 6", 6, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("print");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"print\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_PRINT", TokenStream.Kind.KW_PRINT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 5", 5, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("true");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"true\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.BL_TRUE", TokenStream.Kind.BL_TRUE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 4", 4, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 4", 4, tokens.get(1).beg);
        assertEquals("Token 1 should end at 4", 4, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("false");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"false\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.BL_FALSE", TokenStream.Kind.BL_FALSE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 5", 5, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(".");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\".\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.DOT", TokenStream.Kind.DOT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("..");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"..\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.RANGE", TokenStream.Kind.RANGE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(";");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\";\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.SEMICOLON", TokenStream.Kind.SEMICOLON, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(",");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\",\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.COMMA", TokenStream.Kind.COMMA, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("(");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"(\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LPAREN", TokenStream.Kind.LPAREN, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(")");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\")\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.RPAREN", TokenStream.Kind.RPAREN, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("[");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"[\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LSQUARE", TokenStream.Kind.LSQUARE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("]");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"]\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.RSQUARE", TokenStream.Kind.RSQUARE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("{");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"{\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LCURLY", TokenStream.Kind.LCURLY, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("}");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"}\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.RCURLY", TokenStream.Kind.RCURLY, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(":");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\":\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.COLON", TokenStream.Kind.COLON, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("?");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"?\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.QUESTION", TokenStream.Kind.QUESTION, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("=");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"=\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ASSIGN", TokenStream.Kind.ASSIGN, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("|");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"|\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.BAR", TokenStream.Kind.BAR, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("&");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"&\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.AND", TokenStream.Kind.AND, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("==");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"==\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EQUAL", TokenStream.Kind.EQUAL, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("!=");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"!=\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.NOTEQUAL", TokenStream.Kind.NOTEQUAL, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("<");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"<\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LT", TokenStream.Kind.LT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(">");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\">\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.GT", TokenStream.Kind.GT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("<=");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"<=\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LE", TokenStream.Kind.LE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(">=");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\">=\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.GE", TokenStream.Kind.GE, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("+");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"+\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.PLUS", TokenStream.Kind.PLUS, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("-");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"-\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.MINUS", TokenStream.Kind.MINUS, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("*");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"*\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.TIMES", TokenStream.Kind.TIMES, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("%");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"%\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.MOD", TokenStream.Kind.MOD, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("!");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"!\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.NOT", TokenStream.Kind.NOT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("<<");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"<<\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.LSHIFT", TokenStream.Kind.LSHIFT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner(">>");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\">>\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.RSHIFT", TokenStream.Kind.RSHIFT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("->");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"->\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ARROW", TokenStream.Kind.ARROW, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("@");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"@\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.AT", TokenStream.Kind.AT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("\"\"");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.STRING_LIT", TokenStream.Kind.STRING_LIT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("\"");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.UNTERMINATED_STRING", TokenStream.Kind.UNTERMINATED_STRING, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("0");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"0\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("/**/");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"/**/\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 4", 4, tokens.get(0).beg);
        assertEquals("Token 0 should end at 4", 4, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);

        scanner = makeScanner("/*");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"/*\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.UNTERMINATED_COMMENT", TokenStream.Kind.UNTERMINATED_COMMENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 2", 2, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("\r");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\r\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 1", 1, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 2", 2, tokens.get(0).lineNumber);

        scanner = makeScanner("\n");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\n\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 1", 1, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 2", 2, tokens.get(0).lineNumber);

        scanner = makeScanner("\r\n");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\r\n\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 2", 2, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 2", 2, tokens.get(0).lineNumber);

        scanner = makeScanner(" ");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 1", 1, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);

        scanner = makeScanner("\t");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 1", 1, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);

        scanner = makeScanner("\r\n");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\r\n\" should provide 1 token", 1, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 2", 2, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 2", 2, tokens.get(0).lineNumber);

        scanner = makeScanner("$");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"$\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("_");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"_\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("#");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"#\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ILLEGAL_CHAR", TokenStream.Kind.ILLEGAL_CHAR, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("^");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"^\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ILLEGAL_CHAR", TokenStream.Kind.ILLEGAL_CHAR, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("`");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"`\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ILLEGAL_CHAR", TokenStream.Kind.ILLEGAL_CHAR, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("~");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"~\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.ILLEGAL_CHAR", TokenStream.Kind.ILLEGAL_CHAR, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 1", 1, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        /* Some basic tests */
        scanner = makeScanner("");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"\" should only contain an EOF token", TokenStream.Kind.EOF, tokens.get(0).kind);

        scanner = makeScanner("acd456asd");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"acd456asd\" should provide 2 tokens", 2, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 9", 9, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.EOF", TokenStream.Kind.EOF, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 9", 9, tokens.get(1).beg);
        assertEquals("Token 1 should end at 9", 9, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);

        scanner = makeScanner("45asd45");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"45asd45\" should provide 3 tokens", 3, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 7", 7, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 7", 7, tokens.get(2).beg);
        assertEquals("Token 2 should end at 7", 7, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);

        scanner = makeScanner("01654");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"01654\" should provide 3 tokens", 3, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 1", 1, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 1", 1, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 5", 5, tokens.get(2).beg);
        assertEquals("Token 2 should end at 5", 5, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);

        scanner = makeScanner("45asd45");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"45asd45\" should provide 3 tokens", 3, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 2", 2, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 2", 2, tokens.get(1).beg);
        assertEquals("Token 1 should end at 7", 7, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 7", 7, tokens.get(2).beg);
        assertEquals("Token 2 should end at 7", 7, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);

        scanner = makeScanner("test /**");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"test /**\" should provide 3 tokens", 3, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 4", 4, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.UNTERMINATED_COMMENT", TokenStream.Kind.UNTERMINATED_COMMENT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 8", 8, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 8", 8, tokens.get(2).beg);
        assertEquals("Token 2 should end at 8", 8, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);

        scanner = makeScanner("something \"something something /*");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"something \"something something /*\" should provide 3 tokens", 3, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 9", 9, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.UNTERMINATED_STRING", TokenStream.Kind.UNTERMINATED_STRING, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 10", 10, tokens.get(1).beg);
        assertEquals("Token 1 should end at 33", 33, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 33", 33, tokens.get(2).beg);
        assertEquals("Token 2 should end at 33", 33, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);

        /* Intermediate test cases */
        scanner = makeScanner("here is a \"string\" /* this is a comment */ */");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"here is a \"string\" /* this is a comment */ */\" should provide 7 tokens", 7, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 4", 4, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 5", 5, tokens.get(1).beg);
        assertEquals("Token 1 should end at 7", 7, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 8", 8, tokens.get(2).beg);
        assertEquals("Token 2 should end at 9", 9, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);
        assertEquals("Token 3 should provide a single token of type Kind.STRING_LIT", TokenStream.Kind.STRING_LIT, tokens.get(3).kind);
        assertEquals("Token 3 should begin at 10", 10, tokens.get(3).beg);
        assertEquals("Token 3 should end at 18", 18, tokens.get(3).end);
        assertEquals("Token 3 should be on line 1", 1, tokens.get(3).lineNumber);
        assertEquals("Token 4 should provide a single token of type Kind.TIMES", TokenStream.Kind.TIMES, tokens.get(4).kind);
        assertEquals("Token 4 should begin at 43", 43, tokens.get(4).beg);
        assertEquals("Token 4 should end at 44", 44, tokens.get(4).end);
        assertEquals("Token 4 should be on line 1", 1, tokens.get(4).lineNumber);
        assertEquals("Token 5 should provide a single token of type Kind.DIV", TokenStream.Kind.DIV, tokens.get(5).kind);
        assertEquals("Token 5 should begin at 44", 44, tokens.get(5).beg);
        assertEquals("Token 5 should end at 45", 45, tokens.get(5).end);
        assertEquals("Token 5 should be on line 1", 1, tokens.get(5).lineNumber);
        assertEquals("Token 6 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(6).kind);
        assertEquals("Token 6 should begin at 45", 45, tokens.get(6).beg);
        assertEquals("Token 6 should end at 45", 45, tokens.get(6).end);
        assertEquals("Token 6 should be on line 1", 1, tokens.get(6).lineNumber);

        scanner = makeScanner("int x = 5;\r\nprint x;");
        scanner.scan();
        tokens = scanner.stream.tokens;
        assertEquals("\"int x = 5l\r\nprintx\" should provide 9 tokens", 9, tokens.size());
        assertEquals("Token 0 should provide a single token of type Kind.KW_INT", TokenStream.Kind.KW_INT, tokens.get(0).kind);
        assertEquals("Token 0 should begin at 0", 0, tokens.get(0).beg);
        assertEquals("Token 0 should end at 3", 3, tokens.get(0).end);
        assertEquals("Token 0 should be on line 1", 1, tokens.get(0).lineNumber);
        assertEquals("Token 1 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(1).kind);
        assertEquals("Token 1 should begin at 4", 4, tokens.get(1).beg);
        assertEquals("Token 1 should end at 5", 5, tokens.get(1).end);
        assertEquals("Token 1 should be on line 1", 1, tokens.get(1).lineNumber);
        assertEquals("Token 2 should provide a single token of type Kind.ASSIGN", TokenStream.Kind.ASSIGN, tokens.get(2).kind);
        assertEquals("Token 2 should begin at 6", 6, tokens.get(2).beg);
        assertEquals("Token 2 should end at 7", 7, tokens.get(2).end);
        assertEquals("Token 2 should be on line 1", 1, tokens.get(2).lineNumber);
        assertEquals("Token 3 should provide a single token of type Kind.INT_LIT", TokenStream.Kind.INT_LIT, tokens.get(3).kind);
        assertEquals("Token 3 should begin at 8", 8, tokens.get(3).beg);
        assertEquals("Token 3 should end at 9", 9, tokens.get(3).end);
        assertEquals("Token 3 should be on line 1", 1, tokens.get(3).lineNumber);
        assertEquals("Token 4 should provide a single token of type Kind.SEMICOLON", TokenStream.Kind.SEMICOLON, tokens.get(4).kind);
        assertEquals("Token 4 should begin at 9", 9, tokens.get(4).beg);
        assertEquals("Token 4 should end at 10", 10, tokens.get(4).end);
        assertEquals("Token 4 should be on line 2", 1, tokens.get(4).lineNumber);
        assertEquals("Token 5 should provide a single token of type Kind.KW_PRINT", TokenStream.Kind.KW_PRINT, tokens.get(5).kind);
        assertEquals("Token 5 should begin at 12", 12, tokens.get(5).beg);
        assertEquals("Token 5 should end at 17", 17, tokens.get(5).end);
        assertEquals("Token 5 should be on line 2", 2, tokens.get(5).lineNumber);
        assertEquals("Token 6 should provide a single token of type Kind.IDENT", TokenStream.Kind.IDENT, tokens.get(6).kind);
        assertEquals("Token 6 should begin at 18", 18, tokens.get(6).beg);
        assertEquals("Token 6 should end at 19", 19, tokens.get(6).end);
        assertEquals("Token 6 should be on line 2", 2, tokens.get(6).lineNumber);
        assertEquals("Token 7 should provide a single token of type Kind.SEMICOLON", TokenStream.Kind.SEMICOLON, tokens.get(7).kind);
        assertEquals("Token 7 should begin at 19", 19, tokens.get(7).beg);
        assertEquals("Token 7 should end at 20", 20, tokens.get(7).end);
        assertEquals("Token 7 should be on line 2", 2, tokens.get(7).lineNumber);
        assertEquals("Token 8 should provide a single token of type Kind.IDENT", TokenStream.Kind.EOF, tokens.get(8).kind);
        assertEquals("Token 8 should begin at 20", 20, tokens.get(8).beg);
        assertEquals("Token 8 should end at 20", 20, tokens.get(8).end);
        assertEquals("Token 8 should be on line 2", 2, tokens.get(8).lineNumber);
    }

    /**********************************************************************************************************/
    /********************************************* Provided Tests *********************************************/
    /**********************************************************************************************************/
    @Test
    public void emptyInput() {
        System.out.println("Test: emptyInput");
        String input = "";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(1, stream.tokens.size()); // creates EOF token
        assertEquals(EOF, stream.nextToken().kind);

    }

    @Test
    public void noWhiteSpace() {
        System.out.println("Test: noWhitespace");
        String input = "@%";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(3, stream.tokens.size()); // one each for @ and %, plus the
        // eof
        // token
        assertEquals(AT, stream.nextToken().kind);
        assertEquals(MOD, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);

    }

    @Test
    public void errorToken() {
        System.out.println("Test: noWhitespace");
        String input = "@#  *";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(4, stream.tokens.size()); // one each for @,#, and *, plus
        // the eof token
        assertEquals(AT, stream.nextToken().kind);
        assertEquals(ILLEGAL_CHAR, stream.nextToken().kind);
        assertEquals(TIMES, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);

    }

    @Test
    public void onlySpaces() {
        System.out.println("Test: onlySpaces");
        String input = "     "; // five spaces
        System.out.println("input is five spaces");
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        assertEquals(1, stream.tokens.size()); // creates EOF token
        Token t = stream.nextToken();
        System.out.println(stream);
        assertEquals(EOF, t.kind);
        assertEquals(5, t.beg);
    }

    @Test
    public void skipWhiteSpace() {
        System.out.println("skipWhiteSpace");
        String input = "   ;;;   %@%\n  \r   \r\n ;;;";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(SEMICOLON, stream.nextToken().kind);
        assertEquals(SEMICOLON, stream.nextToken().kind);
        assertEquals(SEMICOLON, stream.nextToken().kind);
        assertEquals(MOD, stream.nextToken().kind);
        assertEquals(AT, stream.nextToken().kind);
        assertEquals(MOD, stream.nextToken().kind);
        assertEquals(SEMICOLON, stream.nextToken().kind);
        assertEquals(SEMICOLON, stream.nextToken().kind);
        Token t = stream.nextToken();
        assertEquals(SEMICOLON, t.kind);
        assertEquals(4,t.getLineNumber());
    }

    @Test
    public void dotsAndRanges() {
        System.out.println("dotsAndRanges");
        String input = ".\n..\n.. . . ..\n...\n";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(DOT, stream.nextToken().kind);
        assertEquals(RANGE, stream.nextToken().kind);
        assertEquals(RANGE, stream.nextToken().kind);
        assertEquals(DOT, stream.nextToken().kind);
        assertEquals(DOT, stream.nextToken().kind);
        assertEquals(RANGE, stream.nextToken().kind);
        assertEquals(RANGE, stream.nextToken().kind);
        assertEquals(DOT, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);
        assertEquals(3, stream.tokens.get(5).getLineNumber());// 5th token is on
        // line 3
    }

    @Test
    public void firstPartAtEndOfInput() {
        System.out.println("firstPartATEndOfInput");
        String input = "!";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(NOT, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);
    }

    @Test
    public void twoStateTokens() {
        System.out.println("twoStateTokens");
        String input = "= == =\n= ! != - -> -! =!!";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(ASSIGN, stream.nextToken().kind);
        assertEquals(EQUAL, stream.nextToken().kind);
        assertEquals(ASSIGN, stream.nextToken().kind);
        assertEquals(ASSIGN, stream.nextToken().kind);
        assertEquals(NOT, stream.nextToken().kind);
        assertEquals(NOTEQUAL, stream.nextToken().kind);
        assertEquals(MINUS, stream.nextToken().kind);
        assertEquals(ARROW, stream.nextToken().kind);
        assertEquals(MINUS, stream.nextToken().kind);
        assertEquals(NOT, stream.nextToken().kind);
        assertEquals(ASSIGN, stream.nextToken().kind);
        assertEquals(NOT, stream.nextToken().kind);
        assertEquals(NOT, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);
    }

    // This test constructs the exptected token list and compares to the one
    // created by the Scanner
    @Test
    public void compareTokenList() {
        System.out.println("compareTokenList");
        String input = "= ==";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Token t0 = stream.new Token(ASSIGN, 0, 1, 1);
        Token t1 = stream.new Token(EQUAL, 2, 4, 1);
        Token t2 = stream.new Token(EOF, 4, 4, 1);
        ArrayList<Token> expected_tokens = new ArrayList<Token>();
        expected_tokens.add(t0);
        expected_tokens.add(t1);
        expected_tokens.add(t2);
        assertArrayEquals(expected_tokens.toArray(), stream.tokens.toArray());
    }

    @Test
    public void lessAndGreater() {
        System.out.println("lessAndGreater");
        String input = " < << <= > >> >= -> <>";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        assertEquals(LT, stream.nextToken().kind);
        assertEquals(LSHIFT, stream.nextToken().kind);
        assertEquals(LE, stream.nextToken().kind);
        assertEquals(GT, stream.nextToken().kind);
        assertEquals(RSHIFT, stream.nextToken().kind);
        assertEquals(GE, stream.nextToken().kind);
        assertEquals(ARROW, stream.nextToken().kind);
        assertEquals(LT, stream.nextToken().kind);
        assertEquals(GT, stream.nextToken().kind);
        assertEquals(EOF, stream.nextToken().kind);
    }

    @Test
    public void intLiterals() {
        System.out.println("lessAndGreater");
        String input = "0 1 23 45+ 67<=9";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { INT_LIT, INT_LIT, INT_LIT, INT_LIT, PLUS,
                INT_LIT, LE, INT_LIT, EOF };
        String[] expectedTexts = { "0", "1", "23", "45", "+", "67", "<=", "9",
                "" }; // need empty string for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        makeTokenTextArray(stream);
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void stringLiterals() {
        System.out.println("stringLiterals");
        String input = " \"abc\" \"def\" \"ghijk\" \"123\" \"&^%$\" ";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { STRING_LIT, STRING_LIT, STRING_LIT,
                STRING_LIT, STRING_LIT, EOF };
        String[] expectedTexts = { "abc", "def", "ghijk", "123", "&^%$", "" }; // need
        // empty
        // string
        // for
        // eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void identifiers() {
        System.out.println("identifiers");
        String input = " abc ddef ghijk 123 a234 32a";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { IDENT, IDENT, IDENT, INT_LIT, IDENT, INT_LIT,
                IDENT, EOF };
        String[] expectedTexts = { "abc", "ddef", "ghijk", "123", "a234", "32",
                "a", "" }; // need empty string for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void keywords() {
        System.out.println("keywords");
        String input = " int  string  boolean import  class  def  while if  else  return  print aaa";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { KW_INT, KW_STRING, KW_BOOLEAN, KW_IMPORT,
                KW_CLASS, KW_DEF, KW_WHILE, KW_IF, KW_ELSE, KW_RETURN,
                KW_PRINT, IDENT, EOF };
        String[] expectedTexts = { "int", "string", "boolean", "import",
                "class", "def", "while", "if", "else", "return", "print",
                "aaa", "" }; // need empty string for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void boolAndNullLiterals() {
        System.out.println("boolAndNullLiterals");
        String input = " true false\n null";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { BL_TRUE, BL_FALSE, NL_NULL, EOF };
        String[] expectedTexts = { "true", "false", "null", "" }; // need empty
        // string
        // for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void multiLineString() {
        System.out.println("multiLineString");
        String input = " \"true false\n null\" ";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { STRING_LIT, EOF };
        String[] expectedTexts = { "true false\n null", "" }; // need empty
        // string for
        // eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));

    }

    @Test
    public void comments() {
        System.out.println("comments");
        String input = "/**/ 0 1 45+ 67<=9";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { INT_LIT, INT_LIT, INT_LIT, PLUS, INT_LIT, LE,
                INT_LIT, EOF };
        String[] expectedTexts = { "0", "1", "45", "+", "67", "<=", "9", "" }; // need
        // empty
        // string
        // for
        // eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void comments2() {
        System.out.println("comments2");
        String input = "/**/ 0 1 /** ***/ 45+ 67<=9";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { INT_LIT, INT_LIT, INT_LIT, PLUS, INT_LIT, LE,
                INT_LIT, EOF };
        String[] expectedTexts = { "0", "1", "45", "+", "67", "<=", "9", "" }; // need
        // empty
        // string
        // for
        // eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void comments3() {
        System.out.println("comments3");
        String input = "/**/ 0 1 /** ***/ 45+ 67<=9/*";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { INT_LIT, INT_LIT, INT_LIT, PLUS, INT_LIT, LE,
                INT_LIT, UNTERMINATED_COMMENT, EOF };
        String[] expectedTexts = { "0", "1", "45", "+", "67", "<=", "9", "/*",
                "" }; // need empty string for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void onlyComment() {
        System.out.println("onlyComment");
        String input = "/**/";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        Kind[] expectedKinds = { EOF };
        String[] expectedTexts = { "" }; // need empty string for eof
        assertArrayEquals(expectedKinds, makeKindArray(stream));
        assertArrayEquals(expectedTexts, makeTokenTextArray(stream));
    }

    @Test
    public void singleSlash(){
        System.out.println("singleSlash");
        String input = "/";
        System.out.println(input);
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
    }

    // Creates an array containing the kinds of the tokens in the token list
    Kind[] makeKindArray(TokenStream stream) {
        Kind[] kinds = new Kind[stream.tokens.size()];
        for (int i = 0; i < stream.tokens.size(); ++i) {
            kinds[i] = stream.tokens.get(i).kind;
        }
        return kinds;

    }

    // Creates an array containing the texts of the tokens in the token list
    String[] makeTokenTextArray(TokenStream stream) {
        String[] kinds = new String[stream.tokens.size()];
        for (int i = 0; i < stream.tokens.size(); ++i) {
            kinds[i] = stream.tokens.get(i).getText();
        }
        return kinds;
    }
}