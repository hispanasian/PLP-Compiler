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
        KEY_VALUE_EXPRESSION, KEY_VALYE_LIST, MAP_LIST, RANGE_EXPR, EXPRESSION, TERM, ELEM, THING,
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
            case KEY_VALYE_LIST: parser.KeyValueList();
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
        parseCorrectInput(input, BLOCK);
    }

    @Test
    public void block3() throws SyntaxException
    {

    }

    @Test
    public void block4() throws SyntaxException
    {

    }

    @Test
    public void block5() throws SyntaxException
    {

    }

    @Test
    public void declaration1() throws SyntaxException
    {

    }

    @Test
    public void varDec1() throws SyntaxException
    {

    }

    @Test
    public void type1() throws SyntaxException
    {

    }

    @Test
    public void simpleType1() throws SyntaxException
    {
        System.out.println("simpleType1");
        System.out.println("SimpleType should accept 'int'");
        String input = "int";
        System.out.println(input);
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType2() throws SyntaxException
    {
        System.out.println("simpleType2");
        System.out.println("SimpleType should accept 'boolean'");
        String input = "boolean";
        System.out.println(input);
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType3() throws SyntaxException
    {
        System.out.println("simpleType3");
        System.out.println("SimpleType should accept 'string'");
        String input = "string";
        System.out.println(input);
        parseCorrectInput(input, SIMPLE_TYPE);
    }

    @Test
    public void simpleType4() throws SyntaxException
    {
        System.out.println("simpleType5");
        System.out.println("SimpleType should not accept 'Boolean'");
        String input = "Boolean";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, SIMPLE_TYPE);
    }

    @Test
    public void simpleType5() throws SyntaxException
    {
        System.out.println("simpleType5");
        System.out.println("SimpleType should not accept 'STRING'");
        String input = "STRING";
        System.out.println(input);
        Kind ExpectedIncorrectTokenKind = IDENT;
        parseIncorrectInput(input, ExpectedIncorrectTokenKind, SIMPLE_TYPE);
    }

    @Test
    public void keyValueType1() throws SyntaxException
    {

    }

    @Test
    public void listType1() throws SyntaxException
    {

    }

    @Test
    public void closureDec1() throws SyntaxException
    {

    }

    @Test
    public void closure1() throws SyntaxException
    {

    }

    @Test
    public void formalArgList1() throws SyntaxException
    {

    }

    @Test
    public void statement1() throws SyntaxException
    {

    }

    @Test
    public void closureEvalExpression1() throws SyntaxException
    {

    }

    @Test
    public void lValue1() throws SyntaxException
    {

    }

    @Test
    public void expressionList1() throws SyntaxException
    {

    }

    @Test
    public void keyValueExpression1() throws SyntaxException
    {

    }

    @Test
    public void keyValueList1() throws SyntaxException
    {

    }

    @Test
    public void mapList1() throws SyntaxException
    {

    }

    @Test
    public void rangeExpr1() throws SyntaxException
    {

    }

    @Test
    public void expression1() throws SyntaxException
    {

    }

    @Test
    public void term1() throws SyntaxException
    {

    }

    @Test
    public void elem1() throws SyntaxException
    {

    }

    @Test
    public void thing1() throws SyntaxException
    {

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
	public void closureEval()throws SyntaxException
    {
		System.out.println("closureEva");
		String input = "class A  { x[z] = a(1,2,3); } ";
		System.out.println(input);
		parseCorrectInput(input);
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
	public void maplist1()throws SyntaxException
    {
		System.out.println("maplist1");
		String input = "class A  { x = @@[x:y]; y = @@[x:y,4:5]; } ";
		System.out.println(input);
		parseCorrectInput(input);
	} 	
}
