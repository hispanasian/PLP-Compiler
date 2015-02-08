package cop5555sp15;

import static org.junit.Assert.*;

import org.junit.Test;
import cop5555sp15.SimpleParser.SyntaxException;
import cop5555sp15.TokenStream.Kind;
import static cop5555sp15.TokenStream.Kind.*;
import static cop5555sp15.TestSimpleParser.Phrase.*;

public class TestSimpleParser
{
    protected static enum Phrase
    {
        DECLARATION, VAR_DEC, TYPE, SIMPLE_TYPE, KEY_VALUE_TYPE, LIST_TYPE, CLOSURE_DEC, CLOSURE,
        FORMAL_ARG_LIST, STATEMENT, CLOSURE_EVAL_EXPRESSION, LVALUE, LIST, EXPRESSION_LIST,
        KEY_VALUE_EXPRESSION, KEY_VALUE_LIST, MAP_LIST, RANGE_EXPR, EXPRESSION, TERM, ELEM, THING,
        FACTOR, REL_OP, WEAK_OP, STRONG_OP, VERY_STRONG_OP, BLOCK
    }

    private void processPhrase(SimpleParser parser, Phrase phrase) throws SyntaxException
    {
        switch (phrase)
        {
            case DECLARATION: parser.Declaration();
                break;
            case VAR_DEC: parser.VarDec();
                break;
            case TYPE: parser.Type();
                break;
            case SIMPLE_TYPE: parser.SimpleType();
                break;
            case KEY_VALUE_TYPE: parser.KeyValueType();
                break;
            case LIST_TYPE: parser.ListType();
                break;
            case CLOSURE_DEC: parser.ClosureDec();
                break;
            case CLOSURE: parser.Closure();
                break;
            case FORMAL_ARG_LIST: parser.FormalArgList();
                break;
            case STATEMENT: parser.Statement();
                break;
            case CLOSURE_EVAL_EXPRESSION: parser.ClosureEvalExpression();
                break;
            case LVALUE: parser.LValue();
                break;
            case LIST: parser.List();
                break;
            case EXPRESSION_LIST: parser.ExpressionList();
                break;
            case KEY_VALUE_EXPRESSION: parser.KeyValueExpression();
                break;
            case KEY_VALUE_LIST: parser.KeyValueList();
                break;
            case MAP_LIST: parser.MapList();
                break;
            case RANGE_EXPR: parser.RangeExpr();
                break;
            case EXPRESSION: parser.Expression();
                break;
            case TERM: parser.Term();
                break;
            case ELEM: parser.Elem();
                break;
            case THING: parser.Thing();
                break;
            case FACTOR: parser.Factor();
                break;
            case REL_OP: parser.RelOp();
                break;
            case WEAK_OP: parser.WeakOp();
                break;
            case STRONG_OP: parser.StrongOp();
                break;
            case VERY_STRONG_OP: parser.VeryStrongOp();
                break;
            case BLOCK: parser.TestBlock();
                break;
        }
    }
	
	private void parseIncorrectInput(String input,
			Kind ExpectedIncorrectTokenKind)
    {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		SimpleParser parser = new SimpleParser(stream);
		System.out.println(stream);
		try {
			parser.parse();
			fail("expected syntax error");
		} catch (SyntaxException e) {
			assertEquals(ExpectedIncorrectTokenKind, e.t.kind); // class is the incorrect token
		}
	}

    private void parseIncorrectInput(String input,
                                    Kind ExpectedIncorrectTokenKind,
                                    Phrase phrase)
    {
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        SimpleParser parser = new SimpleParser(stream);
        System.out.println(stream);
        try {
            processPhrase(parser, phrase);
            fail("expected syntax error");
        } catch (SyntaxException e) {
            assertEquals(ExpectedIncorrectTokenKind, e.t.kind); // class is the incorrect token
        }
    }
	
	private void parseCorrectInput(String input) throws SyntaxException
    {
		TokenStream stream = new TokenStream(input);
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		System.out.println(stream);
		SimpleParser parser = new SimpleParser(stream);
		parser.parse();
	}

    private void parseCorrectInput(String input, Phrase phrase) throws SyntaxException
    {
        TokenStream stream = new TokenStream(input);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        System.out.println(stream);
        SimpleParser parser = new SimpleParser(stream);
        processPhrase(parser, phrase);
    }

	/**This is an example of testing correct input
	 * Just call parseCorrectInput
	 * @throws SyntaxException
	 */
	@Test
	public void almostEmpty() throws SyntaxException
    {
		System.out.println("almostEmpty");
		String input = "class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	/**This is an example of testing incorrect input.
	 * The second parameter to parseIncorrectInput is
	 * the Kind of the erroneous token.
	 * For example, in this test, the ] should be a },
	 * so the parameter is RSQUARE

	 * @throws SyntaxException
	 */
	@Test
	public void almostEmptyIncorrect() throws SyntaxException
    {
		System.out.println("almostEmpty");
		String input = "class A { ] ";
		System.out.println(input);
		parseIncorrectInput(input,RSQUARE);		
	}

	@Test
	public void import1() throws SyntaxException
    {
		System.out.println("import1");
		String input = "import X; class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void import2() throws SyntaxException
    {
		System.out.println("import2");
		String input = "import X.Y.Z; import W.X.Y; class A { } ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void import3() throws SyntaxException
    {
		System.out.println("import2");
		String input = "import class A { } "; // this input is wrong.
		System.out.println(input);
		Kind ExpectedIncorrectTokenKind = KW_CLASS;
		parseIncorrectInput(input, ExpectedIncorrectTokenKind);
	}

    @Test
    public void import4() throws SyntaxException
    {
        System.out.println("import4");
        String input = "import I.Am.An.Import; import not; class A { } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void import5() throws SyntaxException
    {
        System.out.println("import5");
        String input = "import I.Am.An.Import; import not class A { } ";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = KW_CLASS;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind);
    }

    @Test
    public void import6() throws SyntaxException
    {
        System.out.println("import6");
        String input = "import I.Am.An.Import import not; class A { } ";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = KW_IMPORT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind);
    }

    @Test
    public void import7() throws SyntaxException
    {
        System.out.println("import7");
        String input = "import Import; import not; class A { } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void import8() throws SyntaxException
    {
        System.out.println("import8");
        String input = "import ; import not; class A { } ";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = SEMICOLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind);
    }

    @Test
    public void import9() throws SyntaxException
    {
        System.out.println("import9");
        String input = "import import not; class A { } ";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = KW_IMPORT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind);
    }

    @Test
    public void import10() throws SyntaxException
    {
        System.out.println("import10");
        String input = "class A { } "; // empty imports are allowed
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void block1() throws SyntaxException
    {
        System.out.println("block1");
        System.out.println("SimpleParser.Block should allow an empty block");
        String input = "{ } ";
        System.out.println(input);
        parseCorrectInput(input, BLOCK);
    }

    @Test
    public void block2() throws SyntaxException
    {
        System.out.println("block2");
        System.out.println();
        String input = "import Import; import not; class A { } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void block3() throws SyntaxException
    {
        System.out.println("block3");
        String input = "{ def x; }";
        System.out.format("Block should accept '%s'\n", input);
        parseCorrectInput(input, BLOCK);
    }

    @Test
    public void block4() throws SyntaxException
    {
        System.out.println("block4");
        String input = "{ print(x); }";
        System.out.format("Block should accept '%s'\n", input);
        parseCorrectInput(input, BLOCK);
    }

    @Test
    public void block5() throws SyntaxException
    {
        System.out.println("block5");
        String input = "{ def x }";
        System.out.format("Block should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RCURLY;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, BLOCK);
    }

    @Test
    public void block6() throws SyntaxException
    {
        System.out.println("block6");
        String input = " def x; }";
        System.out.format("Block should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_DEF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, BLOCK);
    }

    @Test
    public void block7() throws SyntaxException
    {
        System.out.println("block3");
        String input = "{ def x;";
        System.out.format("Block should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, BLOCK);
    }

    @Test
    public void block8() throws SyntaxException
    {
        System.out.println("block8");
        String input = "";
        System.out.format("Block should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, BLOCK);
    }

    @Test
    public void block9() throws SyntaxException
    {
        System.out.println("block9");
        String input = "{  }";
        System.out.format("Block should accept '%s'\n", input);
        parseCorrectInput(input, BLOCK);
    }

    @Test
    public void declaration1() throws SyntaxException
    {
        System.out.println("declaration1");
        String input = "def x";
        System.out.format("Declaration should accept '%s'\n", input);
        parseCorrectInput(input, DECLARATION);
    }

    @Test
    public void declaration2() throws SyntaxException
    {
        System.out.println("declaration2");
        String input = "def x={->}";
        System.out.format("Declaration should accept '%s'\n", input);
        parseCorrectInput(input, DECLARATION);
    }

    @Test
    public void declaration3() throws SyntaxException
    {
        System.out.println("declaration3");
        String input = "def x:string";
        System.out.format("Declaration should accept '%s'\n", input);
        parseCorrectInput(input, DECLARATION);
    }

    @Test
    public void varDec1() throws SyntaxException
    {
        System.out.println("varDec1");
        String input = "x";
        System.out.format("VarDec should accept '%s'\n", input);
        parseCorrectInput(input, VAR_DEC);
    }

    @Test
    public void varDec2() throws SyntaxException
    {
        System.out.println("varDec2");
        String input = "";
        System.out.format("VarDec should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, VAR_DEC);
    }

    @Test
    public void varDec3() throws SyntaxException
    {
        System.out.println("varDec3");
        String input = "x:";
        System.out.format("VarDec should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, VAR_DEC);
    }

    @Test
    public void varDec4() throws SyntaxException
    {
        System.out.println("varDec4");
        String input = "3";
        System.out.format("VarDec should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = INT_LIT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, VAR_DEC);
    }

    @Test
    public void varDec5() throws SyntaxException
    {
        System.out.println("varDec5");
        String input = "x:string";
        System.out.format("VarDec should accept '%s'\n", input);
        parseCorrectInput(input, VAR_DEC);
    }

    @Test
    public void varDec6() throws SyntaxException
    {
        System.out.println("varDec6");
        String input = "x:int";
        System.out.format("VarDec should accept '%s'\n", input);
        parseCorrectInput(input, VAR_DEC);
    }

    @Test
    public void varDec7() throws SyntaxException
    {
        System.out.println("varDec5");
        String input = "x:boolean";
        System.out.format("VarDec should accept '%s'\n", input);
        parseCorrectInput(input, VAR_DEC);
    }

    @Test
    public void varDec8() throws SyntaxException
    {
        System.out.println("varDec8");
        String input = "x:x";
        System.out.format("VarDec should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, VAR_DEC);
    }

    @Test
    public void varDec9() throws SyntaxException
    {
        System.out.println("varDec9");
        String input = "x:9";
        System.out.format("VarDec should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = INT_LIT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, VAR_DEC);
    }

    @Test
    public void type1() throws SyntaxException
    {
        System.out.println("type1");
        String input = "@@[boolean:@[int]]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type2() throws SyntaxException
    {
        System.out.println("type2");
        String input = "int";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type3() throws SyntaxException
    {
        System.out.println("type3");
        String input = "boolean";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type4() throws SyntaxException
    {
        System.out.println("type4");
        String input = "string";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type5() throws SyntaxException
    {
        System.out.println("type5");
        String input = "@@[boolean:int]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type6() throws SyntaxException
    {
        System.out.println("type6");
        String input = "@[int]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type7() throws SyntaxException
    {
        System.out.println("type7");
        String input = "@@[boolean:@@[int:string]]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type8() throws SyntaxException
    {
        System.out.println("type8");
        String input = "@[boolean:@[int:string]]";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void type9() throws SyntaxException
    {
        System.out.println("type9");
        String input = "";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void type10() throws SyntaxException
    {
        System.out.println("type10");
        String input = "@[string";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void type11() throws SyntaxException
    {
        System.out.println("type11");
        String input = "@[string]]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type12() throws SyntaxException
    {
        System.out.println("type10");
        String input = "@@[int:@@[boolean:@@[string:@[int]]]]";
        System.out.format("Type should accept '%s'\n", input);
        parseCorrectInput(input, TYPE);
    }

    @Test
    public void type13() throws SyntaxException
    {
        System.out.println("type13");
        String input = "@string";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_STRING;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void type14() throws SyntaxException
    {
        System.out.println("type14");
        String input = "@@[@[string]:int]";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = AT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void type15() throws SyntaxException
    {
        System.out.println("type15");
        String input = "@@[boolean:@[int:string]]";
        System.out.format("Type should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TYPE);
    }

    @Test
    public void simpleType1() throws SyntaxException
    {
        System.out.println("simpleType1");
        System.out.println("SimpleType should accept 'int'");
        String input = "int";
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType2() throws SyntaxException
    {
        System.out.println("simpleType2");
        System.out.println("SimpleType should accept 'boolean'");
        String input = "boolean";
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType3() throws SyntaxException
    {
        System.out.println("simpleType3");
        System.out.println("SimpleType should accept 'string'");
        String input = "string";
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType4() throws SyntaxException
    {
        System.out.println("simpleType5");
        System.out.println("SimpleType should not accept 'Boolean'");
        String input = "Boolean";
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, SIMPLE_TYPE);
    }

    @Test
    public void simpleType5() throws SyntaxException
    {
        System.out.println("simpleType5");
        System.out.println("SimpleType should not accept 'STRING'");
        String input = "STRING";
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, SIMPLE_TYPE);
    }

    @Test
    public void keyValueType1() throws SyntaxException
    {
        System.out.println("keyValueType1");
        String input = "[int:string]";
        System.out.format("KeyValueType should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType2() throws SyntaxException
    {
        System.out.println("keyValueType2");
        String input = "[boolean]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType3() throws SyntaxException
    {
        System.out.println("keyValueType3");
        String input = "[boolean:@[boolean]]";
        System.out.format("KeyValueType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType4() throws SyntaxException
    {
        System.out.println("keyValueType4");
        String input = "[boolean:@@[boolean:int]]";
        System.out.format("KeyValueType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType5() throws SyntaxException
    {
        System.out.println("keyValueType5");
        String input = "[boolean:int:string]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType6() throws SyntaxException
    {
        System.out.println("keyValueType6");
        String input = "[boolean:@@[[int:string]]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = LSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType7() throws SyntaxException
    {
        System.out.println("keyValueType7");
        String input = "[boolean:@@[boolean:@[string]]]";
        System.out.format("KeyValueType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType8() throws SyntaxException
    {
        System.out.println("keyValueType8");
        String input = "[boolean:@@[boolean:@@[string:int]]]";
        System.out.format("KeyValueType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType9() throws SyntaxException
    {
        System.out.println("keyValueType9");
        String input = "[boolean:@@[int]]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType10() throws SyntaxException
    {
        System.out.println("keyValueType10");
        String input = "[boolean:@@[int:string]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType11() throws SyntaxException
    {
        System.out.println("keyValueType11");
        String input = "boolean";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_BOOLEAN;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void keyValueType12() throws SyntaxException
    {
        System.out.println("keyValueType12");
        String input = "[]";
        System.out.format("KeyValueType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_TYPE);
    }

    @Test
    public void listType1() throws SyntaxException
    {
        System.out.println("listType1");
        String input = "[@[string]]";
        System.out.format("ListType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, LIST_TYPE);
    }

    @Test
    public void listType2() throws SyntaxException
    {
        System.out.println("listType2");
        String input = "[]";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void listType3() throws SyntaxException
    {
        System.out.println("listType3");
        String input = "[boolean]";
        System.out.format("ListType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, LIST_TYPE);
    }

    @Test
    public void listType4() throws SyntaxException
    {
        System.out.println("listType4");
        String input = "[@[boolean]]";
        System.out.format("ListType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, LIST_TYPE);
    }

    @Test
    public void listType5() throws SyntaxException
    {
        System.out.println("listType5");
        String input = "[boolean:string]";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void listType6() throws SyntaxException
    {
        System.out.println("listType6");
        String input = "[@[@@[string:int]]]";
        System.out.format("ListType should accept '%s'\n", input);
        System.out.println(input);
        parseCorrectInput(input, LIST_TYPE);
    }

    @Test
    public void listType7() throws SyntaxException
    {
        System.out.println("listType7");
        String input = "[@@[boolean]]";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void listType8() throws SyntaxException
    {
        System.out.println("listType8");
        String input = "";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void listType9() throws SyntaxException
    {
        System.out.println("listType9");
        String input = "[@@[boolean::string";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void listType10() throws SyntaxException
    {
        System.out.println("listType10");
        String input = "[@@[boolean:string]]";
        System.out.format("ListType should accept '%s'\n", input);
        parseCorrectInput(input, LIST_TYPE);
    }

    @Test
    public void listType11() throws SyntaxException
    {
        System.out.println("listType11");
        String input = "[@@[boolean:string";
        System.out.format("ListType should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LIST_TYPE);
    }

    @Test
    public void closureDec1() throws SyntaxException
    {
        System.out.println("closureDec1");
        String input = "x= {->}";
        System.out.format("ClosureDec should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE_DEC);
    }

    @Test
    public void closureDec2() throws SyntaxException
    {
        System.out.println("closureDec2");
        String input = "";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closureDec3() throws SyntaxException
    {
        System.out.println("closureDec3");
        String input = "x";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closureDec4() throws SyntaxException
    {
        System.out.println("closureDec4");
        String input = "x=";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closureDec5() throws SyntaxException
    {
        System.out.println("closureDec5");
        String input = "x={";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closureDec6() throws SyntaxException
    {
        System.out.println("closureDec6");
        String input = "x=}";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RCURLY;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closureDec7() throws SyntaxException
    {
        System.out.println("closureDec7");
        String input = "x=->";
        System.out.format("ClosureDec not should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = ARROW;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_DEC);
    }

    @Test
    public void closure1() throws SyntaxException
    {
        System.out.println("closure1");
        String input = "{->}";
        System.out.format("Closure should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE);
    }

    @Test
    public void closure2() throws SyntaxException
    {
        System.out.println("closure2");
        String input = "{x->}";
        System.out.format("Closure should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE);
    }

    @Test
    public void closure3() throws SyntaxException
    {
        System.out.println("closure3");
        String input = "{x->print(x);}";
        System.out.format("Closure should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE);
    }

    @Test
    public void closure4() throws SyntaxException
    {
        System.out.println("closure4");
        String input = "{->print(x);}";
        System.out.format("Closure should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE);
    }

    @Test
    public void closure5() throws SyntaxException
    {
        System.out.println("closure5");
        String input = "";
        System.out.format("Closure should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE);
    }

    @Test
    public void closure6() throws SyntaxException
    {
        System.out.println("closure6");
        String input = "{";
        System.out.format("Closure should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE);
    }

    @Test
    public void closure7() throws SyntaxException
    {
        System.out.println("closure7");
        String input = "{->";
        System.out.format("Closure should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE);
    }

    @Test
    public void closure8() throws SyntaxException
    {
        System.out.println("closure8");
        String input = "->";
        System.out.format("Closure should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = ARROW;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE);
    }

    @Test
    public void closure9() throws SyntaxException
    {
        System.out.println("closure9");
        String input = "}";
        System.out.format("Closure should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RCURLY;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE);
    }

//    @Test
//    public void closure10() throws SyntaxException
//    {
//        System.out.println("closure10");
//        String input = "{->x=y print x while(true) { x=x+1; } while*(true) { x=x+1; } " +
//                "while*(x..y) { x=x+y; } if(false) { def x; } }";
//        System.out.format("Closure should accept '%s'\n", input);
//        parseCorrectInput(input, CLOSURE);
//    }

    @Test
    public void formalArgList1() throws SyntaxException
    {
        System.out.println("formalArgList1");
        String input = "x:int";
        System.out.format("FormalArgList should accept '%s'\n", input);
        parseCorrectInput(input, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList2() throws SyntaxException
    {
        System.out.println("formalArgList1");
        String input = "";
        System.out.format("FormalArgList should accept '%s'\n", input);
        parseCorrectInput(input, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList3() throws SyntaxException
    {
        System.out.println("formalArgList1");
        String input = "x:int, y:string, x:boolean";
        System.out.format("FormalArgList should accept '%s'\n", input);
        parseCorrectInput(input, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList4() throws SyntaxException
    {
        System.out.println("formalArgList1");
        String input = "x:int, lkajsd123, lkj";
        System.out.format("FormalArgList should accept '%s'\n", input);
        parseCorrectInput(input, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList5() throws SyntaxException
    {
        System.out.println("formalArgList5");
        String input = "x:int, int";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_INT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList6() throws SyntaxException
    {
        System.out.println("formalArgList6");
        String input = "x::";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList7() throws SyntaxException
    {
        System.out.println("formalArgList7");
        String input = "x:int, ";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList8() throws SyntaxException
    {
        System.out.println("formalArgList8");
        String input = "x:,";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COMMA;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList9() throws SyntaxException
    {
        System.out.println("formalArgList9");
        String input = "x:INT";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void formalArgList10() throws SyntaxException
    {
        System.out.println("formalArgList10");
        String input = "x:10";
        System.out.format("FormalArgList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = INT_LIT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, FORMAL_ARG_LIST);
    }

    @Test
    public void statements1()throws SyntaxException
    {
        System.out.println("statements1");
        String input = "class A {x = y; z[1] = b; print a+b; print (x+y-z);} ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void statements2()throws SyntaxException
    {
        System.out.println("statements2");
        String input = "class A  {\n while (x) {};  \n while* (1..4){}; } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void statements3()throws SyntaxException
    {
        System.out.println("statements3");
        String input = "class A  {\n if (x) {};  \n if (y){} else {}; \n if (x) {} else {if (z) {} else {};} ; } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void emptyStatement()throws SyntaxException
    {
        System.out.println("emptyStatement");
        String input = "class A  { ;;; } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void statements4()throws SyntaxException
    {
        System.out.println("statements4");
        String input = "class A  { %a(1,2,3); } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void statements5()throws SyntaxException
    {
        System.out.println("statements5");
        String input = "class A  { x = a(1,2,3); } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void statements6() throws SyntaxException
    {
        System.out.println("statements6");
        String input = "x=5";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements7() throws SyntaxException
    {
        System.out.println("statements7");
        String input = "print(x)";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements8() throws SyntaxException
    {
        System.out.println("statements8");
        String input = "while*(x..5) { x=x+1; }";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements9() throws SyntaxException
    {
        System.out.println("statements9");
        String input = "while*(true) { x=x+1; }";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements10() throws SyntaxException
    {
        System.out.println("statements10");
        String input = "while*(true) { x=x+1; }";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements11() throws SyntaxException
    {
        System.out.println("statements11");
        String input = "if(true) { x=x+1; }";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements12() throws SyntaxException
    {
        System.out.println("statements12");
        String input = "if(true) { x=x+1; } else { x=x-1; }";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements13() throws SyntaxException
    {
        System.out.println("statements13");
        String input = "%x";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements14() throws SyntaxException
    {
        System.out.println("statements14");
        String input = "return 5";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements15() throws SyntaxException
    {
        System.out.println("statements15");
        String input = ";";
        System.out.format("Statement should accept '%s'\n", input);
        parseCorrectInput(input, STATEMENT);
    }

    @Test
    public void statements16() throws SyntaxException
    {
        System.out.println("statements16");
        String input = "while(x..5) { x=x+1; }";
        System.out.format("Statement should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RANGE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, STATEMENT);
    }

    @Test
    public void closureEvalExpression1() throws SyntaxException
    {
        System.out.println("closureEvalExpression1");
        String input = "x(a+b-c*d/e)";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression2() throws SyntaxException
    {
        System.out.println("closureEvalExpression2");
        String input = "x(a, a+b, x-d, q/g)";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression3() throws SyntaxException
    {
        System.out.println("closureEvalExpression3");
        String input = "x()";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        parseCorrectInput(input, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression4() throws SyntaxException
    {
        System.out.println("closureEvalExpression4");
        String input = "";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression5() throws SyntaxException
    {
        System.out.println("closureEvalExpression5");
        String input = "x(";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression6() throws SyntaxException
    {
        System.out.println("closureEvalExpression6");
        String input = "x)";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RPAREN;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void closureEvalExpression7() throws SyntaxException
    {
        System.out.println("closureEvalExpression7");
        String input = "x";
        System.out.format("ClosureEvalExpression should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, CLOSURE_EVAL_EXPRESSION);
    }

    @Test
    public void lValue1() throws SyntaxException
    {
        System.out.println("lValue1");
        String input = "x";
        System.out.format("LValue should accept '%s'\n", input);
        parseCorrectInput(input, LVALUE);
    }

    @Test
    public void lValue2() throws SyntaxException
    {
        System.out.println("lValue2");
        String input = "x[y+2]";
        System.out.format("LValue should accept '%s'\n", input);
        parseCorrectInput(input, LVALUE);
    }

    @Test
    public void lValue3() throws SyntaxException
    {
        System.out.println("lValue3");
        String input = "";
        System.out.format("LValue should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LVALUE);
    }

    @Test
    public void lValue4() throws SyntaxException
    {
        System.out.println("lValue4");
        String input = "x[a";
        System.out.format("LValue should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, LVALUE);
    }

    @Test
    public void list1()throws SyntaxException
    {
        System.out.println("list1");
        String input = "class A  { \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void list2()throws SyntaxException
    {
        System.out.println("list2");
        String input = "[true, false]";
        System.out.format("List should accept '%s'\n", input);
        parseCorrectInput(input, LIST);
    }

    @Test
    public void list3()throws SyntaxException
    {
        System.out.println("list3");
        String input = "[!5, -false, 5+a(stuff)]";
        System.out.format("List should accept '%s'\n", input);
        parseCorrectInput(input, LIST);
    }

    @Test
    public void expressionList1() throws SyntaxException
    {
        System.out.println("expressionList1");
        String input = "]";
        System.out.format("ExpressionList should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION_LIST);
    }

    @Test
    public void expressionList2() throws SyntaxException
    {
        System.out.println("expressionList2");
        String input = "x, x+5, x-5, u/w, a*qm, a[stuff], 5, true, false, \"this\", x(pi+3), " +
                "!true, -false, size(hey), key(thing), value(x), x(y,z), {x->}, @[], @@[]";
        System.out.format("ExpressionList should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION_LIST);
    }

    @Test
    public void expressionList3() throws SyntaxException
    {
        System.out.println("expressionList3");
        String input = "x,";
        System.out.format("ExpressionList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION_LIST);
    }

    @Test
    public void keyValueExpression1() throws SyntaxException
    {
        System.out.println("keyValueExpression1");
        String input = "x:5";
        System.out.format("KeyValueExpression should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_EXPRESSION);
    }

    @Test
    public void keyValueExpression2() throws SyntaxException
    {
        System.out.println("keyValueExpression2");
        String input = "x";
        System.out.format("KeyValueExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_EXPRESSION);
    }

    @Test
    public void keyValueExpression3() throws SyntaxException
    {
        System.out.println("keyValueExpression3");
        String input = "x:";
        System.out.format("KeyValueExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_EXPRESSION);
    }

    @Test
    public void keyValueExpression4() throws SyntaxException
    {
        System.out.println("keyValueExpression4");
        String input = "x::";
        System.out.format("KeyValueExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_EXPRESSION);
    }

    @Test
    public void keyValueExpression5() throws SyntaxException
    {
        System.out.println("keyValueExpression5");
        String input = "";
        System.out.format("KeyValueExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_EXPRESSION);
    }

    @Test
    public void keyValueList1() throws SyntaxException
    {
        System.out.println("keyValueList1");
        String input = "]";
        System.out.format("KeyValueList should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList2() throws SyntaxException
    {
        System.out.println("keyValueList2");
        String input = "x:y";
        System.out.format("KeyValueList should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList3() throws SyntaxException
    {
        System.out.println("keyValueList3");
        String input = "key(true):5, thing:x+5";
        System.out.format("KeyValueList should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList4() throws SyntaxException
    {
        System.out.println("keyValueList4");
        String input = "x:y, w:z, hey:there";
        System.out.format("KeyValueList should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList5() throws SyntaxException
    {
        System.out.println("keyValueList5");
        String input = "x:y, w";
        System.out.format("KeyValueList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList6() throws SyntaxException
    {
        System.out.println("keyValueList6");
        String input = "x:y, w:key";
        System.out.format("KeyValueList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList7() throws SyntaxException
    {
        System.out.println("keyValueList7");
        String input = "x, w";
        System.out.format("KeyValueList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COMMA;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList8() throws SyntaxException
    {
        System.out.println("keyValueList8");
        String input = "x:+5, w";
        System.out.format("KeyValueList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = PLUS;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList9() throws SyntaxException
    {
        System.out.println("keyValueList9");
        String input = "x:x, x[y]:x, 5:x, true:x, false:x, \"stuff\":x, (x):x, !true:x, -5:x, " +
                "size(arr[5]):x, key(-5):x, value(x):x, x(x):x, {x -> y=5;}:x, @[]:x, @@[]:x";
        System.out.format("KeyValueList should accept '%s'\n", input);
        parseCorrectInput(input, KEY_VALUE_LIST);
    }

    @Test
    public void keyValueList10() throws SyntaxException
    {
        System.out.println("keyValueList10");
        String input = "key(boolean):string, thing:x+5";
        System.out.format("KeyValueList should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_BOOLEAN;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, KEY_VALUE_LIST);
    }

    @Test
    public void maplist1()throws SyntaxException
    {
        System.out.println("maplist1");
        String input = "class A  { x = @@[x:y]; y = @@[x:y,4:5]; } ";
        System.out.println(input);
        parseCorrectInput(input);
    }

    @Test
    public void maplist2() throws SyntaxException
    {
        System.out.println("maplist2");
        String input = "[]";
        System.out.format("MapList should accept '%s'\n", input);
        parseCorrectInput(input, MAP_LIST);
    }

    @Test
    public void maplist3() throws SyntaxException
    {
        System.out.println("maplist3");
        String input = "";
        System.out.format("MapList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, MAP_LIST);
    }

    @Test
    public void maplist4() throws SyntaxException
    {
        System.out.println("maplist4");
        String input = "]";
        System.out.format("MapList should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RSQUARE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, MAP_LIST);
    }

    @Test
    public void maplist5() throws SyntaxException
    {
        System.out.println("maplist5");
        String input = "[x:5]";
        System.out.format("MapList should accept '%s'\n", input);
        parseCorrectInput(input, MAP_LIST);
    }

    @Test
    public void rangeExpr1() throws SyntaxException
    {
        System.out.println("rangeExpr1");
        String input = "x..x";
        System.out.format("RangeExpression should accept '%s'\n", input);
        parseCorrectInput(input, RANGE_EXPR);
    }

    @Test
    public void rangeExpr2() throws SyntaxException
    {
        System.out.println("rangeExpr2");
        String input = "x+5..true";
        System.out.format("RangeExpression should accept '%s'\n", input);
        parseCorrectInput(input, RANGE_EXPR);
    }

    @Test
    public void rangeExpr3() throws SyntaxException
    {
        System.out.println("rangeExpr3");
        String input = "ident..x+5";
        System.out.format("RangeExpression should accept '%s'\n", input);
        parseCorrectInput(input, RANGE_EXPR);
    }

    @Test
    public void rangeExpr4() throws SyntaxException
    {
        System.out.println("rangeExpr4");
        String input = "x";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr5() throws SyntaxException
    {
        System.out.println("rangeExpr5");
        String input = "x ..";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr6() throws SyntaxException
    {
        System.out.println("rangeExpr6");
        String input = "x:a";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COLON;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr7() throws SyntaxException
    {
        System.out.println("rangeExpr7");
        String input = "x+..7";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RANGE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr8() throws SyntaxException
    {
        System.out.println("rangeExpr8");
        String input = "key..boolean";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = RANGE;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr9() throws SyntaxException
    {
        System.out.println("rangeExpr9");
        String input = "x y";
        System.out.format("RangeExpression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void rangeExpr10() throws SyntaxException
    {
        System.out.println("rangeExpr10");
        String input = "x+5..boolean";
        System.out.format("RangeExpression should accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = KW_BOOLEAN;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, RANGE_EXPR);
    }

    @Test
    public void expression1() throws SyntaxException
    {
        System.out.println("expression1");
        String input = "x";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression2() throws SyntaxException
    {
        System.out.println("expression2");
        String input = "x|a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression3() throws SyntaxException
    {
        System.out.println("expression3");
        String input = "x&a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression4() throws SyntaxException
    {
        System.out.println("expression4");
        String input = "x==a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression5() throws SyntaxException
    {
        System.out.println("expression5");
        String input = "x!=a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression6() throws SyntaxException
    {
        System.out.println("expression6");
        String input = "x<a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression7() throws SyntaxException
    {
        System.out.println("expression7");
        String input = "x>a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression8() throws SyntaxException
    {
        System.out.println("expression8");
        String input = "x<=a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression9() throws SyntaxException
    {
        System.out.println("expression9");
        String input = "x>=a";
        System.out.format("Expression should accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void expression10() throws SyntaxException
    {
        System.out.println("expression10");
        String input = "";
        System.out.format("Expression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION);
    }

    @Test
    public void expression11() throws SyntaxException
    {
        System.out.println("expression11");
        String input = "x[+]";
        System.out.format("Expression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = PLUS;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION);
    }

    @Test
    public void expression12() throws SyntaxException
    {
        System.out.println("expression2");
        String input = "x&";
        System.out.format("Expression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION);
    }

    @Test
    public void expression13() throws SyntaxException
    {
        System.out.println("expression13");
        String input = "c&5+";
        System.out.format("Expression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION);
    }

    @Test
    public void expression14() throws SyntaxException
    {
        System.out.println("expression15");
        String input = "x[a,b]";
        System.out.format("Expression should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = COMMA;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, EXPRESSION);
    }

    @Test
    public void expression15() throws SyntaxException
    {
        System.out.println("expression15");
        String input = "x()";
        System.out.format("Expression should not accept '%s'\n", input);
        parseCorrectInput(input, EXPRESSION);
    }

    @Test
    public void term1() throws SyntaxException
    {
        System.out.println("term1");
        String input = "x";
        System.out.format("Term should accept '%s'\n", input);
        parseCorrectInput(input, TERM);
    }

    @Test
    public void term2() throws SyntaxException
    {
        System.out.println("term2");
        String input = "x+1";
        System.out.format("Term should accept '%s'\n", input);
        parseCorrectInput(input, TERM);
    }

    @Test
    public void term3() throws SyntaxException
    {
        System.out.println("term3");
        String input = "1-2";
        System.out.format("Term should accept '%s'\n", input);
        parseCorrectInput(input, TERM);
    }

    @Test
    public void term4() throws SyntaxException
    {
        System.out.println("term4");
        String input = "x+1-3*5/6";
        System.out.format("Term should accept '%s'\n", input);
        parseCorrectInput(input, TERM);
    }

    @Test
    public void term5() throws SyntaxException
    {
        System.out.println("term5");
        String input = "";
        System.out.format("Term should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TERM);
    }

    @Test
    public void term6() throws SyntaxException
    {
        System.out.println("term6");
        String input = "x+";
        System.out.format("Term should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, TERM);
    }

    @Test
    public void elem1() throws SyntaxException
    {
        System.out.println("elem1");
        String input = "x";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, ELEM);
    }

    @Test
    public void elem2() throws SyntaxException
    {
        System.out.println("elem2");
        String input = "x*a";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, ELEM);
    }

    @Test
    public void elem3() throws SyntaxException
    {
        System.out.println("elem3");
        String input = "x/b";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, ELEM);
    }

    @Test
    public void elem4() throws SyntaxException
    {
        System.out.println("elem4");
        String input = "x*a+c/d";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, ELEM);
    }

    @Test
    public void elem5() throws SyntaxException
    {
        System.out.println("elem5");
        String input = "";
        System.out.format("Thing should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, ELEM);
    }

    @Test
    public void elem6() throws SyntaxException
    {
        System.out.println("elem6");
        String input = "x*";
        System.out.format("Thing should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, ELEM);
    }

    @Test
    public void thing1() throws SyntaxException
    {
        System.out.println("thing1");
        String input = "x";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, THING);
    }

    @Test
    public void thing2() throws SyntaxException
    {
        System.out.println("thing3");
        String input = "x<<false";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, THING);
    }

    @Test
    public void thing3() throws SyntaxException
    {
        System.out.println("thing3");
        String input = "x >> 2";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, THING);
    }

    @Test
    public void thing4() throws SyntaxException
    {
        System.out.println("thing4");
        String input = "x <<";
        System.out.format("Thing should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, THING);
    }

    @Test
    public void thing5() throws SyntaxException
    {
        System.out.println("thing5");
        String input = "";
        System.out.format("Thing should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, THING);
    }

    @Test
    public void thing6() throws SyntaxException
    {
        System.out.println("thing6");
        String input = "x >> 2 >> a << b";
        System.out.format("Thing should accept '%s'\n", input);
        parseCorrectInput(input, THING);
    }

    @Test
    public void thing7() throws SyntaxException
    {
        System.out.println("thing7");
        String input = "x << b >>";
        System.out.format("Thing should not accept '%s'\n", input);
        Kind ExpectedIncorrectTokenKind = EOF;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, THING);
    }

    @Test
    public void relOp1() throws SyntaxException
    {
        System.out.println("relOp1");
        System.out.println("RelOp should accept a '|'");
        String input = "|";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp2() throws SyntaxException
    {
        System.out.println("relOp2");
        System.out.println("RelOp should accept a '&'");
        String input = "&";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp3() throws SyntaxException
    {
        System.out.println("relOp3");
        System.out.println("RelOp should accept a '=='");
        String input = "==";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp4() throws SyntaxException
    {
        System.out.println("relOp4");
        System.out.println("RelOp should accept a '!='");
        String input = "!=";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp5() throws SyntaxException
    {
        System.out.println("relOp5");
        System.out.println("RelOp should accept a '<'");
        String input = "<";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp6() throws SyntaxException
    {
        System.out.println("relOp6");
        System.out.println("RelOp should accept a '>'");
        String input = ">";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp7() throws SyntaxException
    {
        System.out.println("relOp7");
        System.out.println("RelOp should accept a '<='");
        String input = "<=";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp8() throws SyntaxException
    {
        System.out.println("relOp8");
        System.out.println("RelOp should accept a '>='");
        String input = ">=";
        System.out.println(input);
        parseCorrectInput(input, REL_OP);
    }

    @Test
    public void relOp9() throws SyntaxException
    {
        System.out.println("relOp9");
        System.out.println("RelOp should not accept a '");
        String input = "'";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = ILLEGAL_CHAR;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, REL_OP);
    }

    @Test
    public void relOp10() throws SyntaxException
    {
        System.out.println("relOp10");
        System.out.println("RelOp should not accept a '\\'");
        String input = "\\";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = ILLEGAL_CHAR;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, REL_OP);
    }

    @Test
    public void relOp11() throws SyntaxException
    {
        System.out.println("relOp11");
        System.out.println("RelOp should not accept a '*'");
        String input = "*";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = TIMES;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, REL_OP);
    }

    @Test
    public void weakOp1() throws SyntaxException
    {
        System.out.println("weakOp1");
        System.out.println("strongOp should accept a '+'");
        String input = "+";
        System.out.println(input);
        parseCorrectInput(input, WEAK_OP);
    }

    @Test
    public void weakOp2() throws SyntaxException
    {
        System.out.println("weakOp2");
         System.out.println("strongOp should accept a '-'");
        String input = "-";
        System.out.println(input);
        parseCorrectInput(input, WEAK_OP);
    }

    @Test
    public void weakOp3() throws SyntaxException
    {
        System.out.println("weakOp3");
        System.out.println("WeakOp should not accept a '\\'");
        String input = "\\";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = ILLEGAL_CHAR;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, WEAK_OP);
    }

    @Test
    public void weakOp4() throws SyntaxException
    {
        System.out.println("weakOp4");
        System.out.println("WeakOp should not accept a '/'");
        String input = "/";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = DIV;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, WEAK_OP);
    }

    @Test
    public void strongOp1() throws SyntaxException
    {
        System.out.println("strongOp1");
        System.out.println("StrongOp should accept a '*'");
        String input = "*";
        System.out.println(input);
        parseCorrectInput(input, STRONG_OP);
    }

    @Test
    public void strongOp2() throws SyntaxException
    {
        System.out.println("strongOp2");
        System.out.println("StrongOp should accept a '/'");
        String input = "/";
        System.out.println(input);
        parseCorrectInput(input, STRONG_OP);
    }

    @Test
    public void strongOp3() throws SyntaxException
    {
        System.out.println("strongOp3");
        System.out.println("StrongOp should not accept a '\\'");
        String input = "\\";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = ILLEGAL_CHAR;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, STRONG_OP);
    }

    @Test
    public void veryStrongOp1() throws SyntaxException
    {
        System.out.println("veryStrongOp1");
        System.out.println("VeryStrongOp should accept a '<<'");
        String input = "<<";
        System.out.println(input);
        parseCorrectInput(input, VERY_STRONG_OP);
    }

    @Test
    public void veryStrongOp2() throws SyntaxException
    {
        System.out.println("veryStrongOp2");
        System.out.println("VeryStrongOp should accept a '<<'");
        String input = ">>";
        System.out.println(input);
        parseCorrectInput(input, VERY_STRONG_OP);
    }

    @Test
    public void veryStrongOp3() throws SyntaxException
    {
        System.out.println("veryStrongOp3");
        System.out.println("VeryStrongOp should accept a '+'");
        String input = "+";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = PLUS;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, STRONG_OP);
    }

	@Test
	public void def_simple_type1() throws SyntaxException
    {
		System.out.println("def_simple_type1");
		String input = "class A {def B:int; def C:boolean; def S: string;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_simple_type2() throws SyntaxException
    {
		System.out.println("def_simple_type2");
		String input = "class A {def B:int; def C:boolean; def S: string} ";
		System.out.println(input);
		parseIncorrectInput(input, RCURLY);
	}
	
	@Test
	public void def_key_value_type1() throws SyntaxException
    {
		System.out.println("def_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:boolean];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_key_value_type2() throws SyntaxException
    {
		System.out.println("def_key_value_type1");
		String input = "class A {def C:@[string]; def S:@@[int:@@[string:boolean]];} ";
		System.out.println(input);
		parseCorrectInput(input);
	}	
	
	@Test
	public void def_closure1() throws SyntaxException
    {
		System.out.println("def_closure1");
		String input = "class A {def C={->};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}

	@Test
	public void def_closure2() throws SyntaxException
    {
		System.out.println("def_closure2");
		String input = "class A {def C={->};  def z:string;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor1() throws SyntaxException
    {
		System.out.println("factor1");
		String input = "class A {def C={->x=y;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	@Test
	public void factor2() throws SyntaxException
    {
		System.out.println("factor2");
		String input = "class A {def C={->x=y[z];};  def D={->x=y[1];};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor3() throws SyntaxException
    {
		System.out.println("factor3");
		String input = "class A {def C={->x=3;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor4() throws SyntaxException
    {
		System.out.println("factor4");
		String input = "class A {def C={->x=\"hello\";};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor5() throws SyntaxException
    {
		System.out.println("factor5");
		String input = "class A {def C={->x=true; z = false;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	
	@Test
	public void factor6() throws SyntaxException
    {
		System.out.println("factor6");
		String input = "class A {def C={->x=-y; z = !y;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void factor7() throws SyntaxException
    {
		System.out.println("factor7");
		String input = "class A {def C={->x= &y; z = !y;};} ";
		System.out.println(input);
		parseIncorrectInput(input,AND);
	}

    @Test
    public void factor8() throws SyntaxException
    {
        System.out.println("factor8");
        String input = "x";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor9() throws SyntaxException
    {
        System.out.println("factor8");
        String input = "x[a]";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor10() throws SyntaxException
    {
        System.out.println("factor10");
        String input = "5";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor11() throws SyntaxException
    {
        System.out.println("factor11");
        String input = "true";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor12() throws SyntaxException
    {
        System.out.println("factor12");
        String input = "false";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor13() throws SyntaxException
    {
        System.out.println("factor13");
        String input = "string";
        System.out.format("Factor should accept '%s'\n", input);
        parseIncorrectInput(input, KW_STRING, FACTOR);
    }

    @Test
    public void factor14() throws SyntaxException
    {
        System.out.println("factor14");
        String input = "(x+5)";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }


    @Test
    public void factor15() throws SyntaxException
    {
        System.out.println("factor15");
        String input = "!true";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor16() throws SyntaxException
    {
        System.out.println("factor16");
        String input = "-false";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor17() throws SyntaxException
    {
        System.out.println("factor17");
        String input = "size(x)";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor18() throws SyntaxException
    {
        System.out.println("factor18");
        String input = "key(5)";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor19() throws SyntaxException
    {
        System.out.println("factor19");
        String input = "value(\"stuff\")";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor20() throws SyntaxException
    {
        System.out.println("factor20");
        String input = "x()";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor21() throws SyntaxException
    {
        System.out.println("factor21");
        String input = "{->}";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor22() throws SyntaxException
    {
        System.out.println("factor22");
        String input = "@[]";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

    @Test
    public void factor23() throws SyntaxException
    {
        System.out.println("factor23");
        String input = "@@[]";
        System.out.format("Factor should accept '%s'\n", input);
        parseCorrectInput(input, FACTOR);
    }

	@Test
	public void expressions1() throws SyntaxException
    {
		System.out.println("expressions1");
		String input = "class A {def C={->x=x+1; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions2() throws SyntaxException
    {
		System.out.println("expressions2");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions3() throws SyntaxException
    {
		System.out.println("expressions3");
		String input = "class A {def C={->x=x+1/2*3--4; z = 3-4-5;};} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void expressions4() throws SyntaxException
    {
		System.out.println("expressions4");
		String input = "class A {x = a<<b; c = b>>z;} ";
		System.out.println(input);
		parseCorrectInput(input);
	}
	
	@Test
	public void closureEval()throws SyntaxException
    {
		System.out.println("closureEval");
		String input = "class A  { x[z] = a(1,2,3); } ";
		System.out.println(input);
		parseCorrectInput(input);
	}
}
