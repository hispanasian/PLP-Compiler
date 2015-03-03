package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import cop5555sp15.ast.*;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

import java.util.ArrayList;
import java.util.List;

import static cop5555sp15.TokenStream.Kind.*;

public class Parser
{
	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;
		Kind[] expected;
		String msg;

		SyntaxException(Token t, Kind expected) {
			this.t = t;
			msg = "";
			this.expected = new Kind[1];
			this.expected[0] = expected;

		}

		public SyntaxException(Token t, String msg) {
			this.t = t;
			this.msg = msg;

		}

		public SyntaxException(Token t, Kind[] expected) {
			this.t = t;
			msg = "";
			this.expected = expected;
		}

		public String getMessage() {
            if(expected != null)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(" error at token ").append(t.toString()).append(" ")
                        .append(msg);
                sb.append(". Expected: ");
                for (Kind kind : expected) {
                    sb.append(kind).append(" ");
                }
                return sb.toString();
            }
			else return msg;
		}
	}

	TokenStream tokens;
	Token t;
    List<SyntaxException> exceptionList = new ArrayList<SyntaxException>();

	Parser(TokenStream tokens)
    {
		this.tokens = tokens;
		t = tokens.nextToken();
	}

	private Token match(Kind kind) throws SyntaxException
    {
        Token token = t;
		if (isKind(kind))
        {
			consume();
			return token;
		}
		throw new SyntaxException(t, kind);
	}

	private Token match(Kind... kinds) throws SyntaxException
    {
		Token token = t;
		if (isKind(kinds))
        {
			consume();
			return token;
		}
		StringBuilder sb = new StringBuilder();
		for (Kind kind1 : kinds) { sb.append(kind1).append(kind1).append(" "); }
        sb.append("instead of " + t);
		throw new SyntaxException(t, "expected one of " + sb.toString());
	}

	private boolean isKind(Kind kind)
    {
		return (t.kind == kind);
	}

    private boolean aheadIs(Kind kind, int x)
    {
        if(t.kind != EOF) return tokens.lookAhead(x).kind == kind;
        return false;
    }

	private void consume()
    {
		if (t.kind != EOF)
			t = tokens.nextToken();
	}

	private boolean isKind(Kind... kinds)
    {
		for (Kind kind : kinds)
        {
			if (t.kind == kind)
				return true;
		}
		return false;
	}

    private boolean aheadIs(int x, Kind... kinds)
    {
        if(t.kind != EOF)
        {
            for(Kind kind : kinds)
            {
                if(tokens.lookAhead(x) != null &&  tokens.lookAhead(x).kind == kind) return true;
            }
        }
        return false;
    }

	//This is a convenient way to represent fixed sets of
	//token kinds.  You can pass these to isKind.
	static final Kind[] REL_OPS = { BAR, AND, EQUAL, NOTEQUAL, LT, GT, LE, GE };
	static final Kind[] WEAK_OPS = { PLUS, MINUS };
	static final Kind[] STRONG_OPS = { TIMES, DIV };
	static final Kind[] VERY_STRONG_OPS = { LSHIFT, RSHIFT };
    static final Kind[] SIMPLE_TYPE = { KW_INT, KW_BOOLEAN, KW_STRING };

    // Kinds used by Factor
    static final Kind[] FACTOR_FIRST = { IDENT, INT_LIT, BL_TRUE, BL_FALSE, STRING_LIT };
    static final Kind[] FACTOR_FACTOR = { NOT, MINUS };
    static final Kind[] FACTOR_EXPRESSION = {KW_SIZE, KW_KEY, KW_VALUE};
    static final Kind[] FIRST_FACTOR = { IDENT, INT_LIT, BL_TRUE, BL_FALSE, STRING_LIT,
            LPAREN, NOT, MINUS, KW_SIZE, KW_KEY, KW_VALUE, LCURLY, AT};

    // Some firsts, follows, and predicts
    static final Kind[] FIRST_THING = FIRST_FACTOR;
    static final Kind[] FIRST_ELEM = FIRST_THING;
    static final Kind[] FIRST_TERM = FIRST_ELEM;
    static final Kind[] FIRST_EXPRESSION = FIRST_TERM;
    static final Kind[] FIRST_DECLARATION = { KW_DEF };
    static final Kind[] PREDICT_DECLARATION = { KW_DEF };
    static final Kind[] FIRST_STATEMENT = { IDENT, KW_PRINT, KW_WHILE, KW_IF, MOD, KW_RETURN };
    static final Kind[] FOLLOW_STATEMENT = { SEMICOLON };
    static final Kind[] PREDICT_STATEMENT = {IDENT, KW_PRINT, KW_WHILE, KW_IF, MOD, KW_RETURN, SEMICOLON};

    // Kinds used by KeyValueList
    static final Kind[] FIRST_KEY_VALUE_LIST = { IDENT, INT_LIT, BL_TRUE, BL_FALSE, STRING_LIT,
        LPAREN, NOT, MINUS, KW_SIZE, KW_KEY, KW_VALUE, LCURLY, AT};
    static final Kind[] FOLLOW_KEY_VALUE_LIST = { RSQUARE };

    // Kinds used by ExpressionList
    static final Kind[] FIRST_EXPRESSION_LIST = FIRST_FACTOR;
    static final Kind[] FOLLOW_EXPRESSION_LIST = { RPAREN, RSQUARE } ;


    public Program parse()
    {
        Program p = null;

        try {
            p = Program();
            if(p != null) match(EOF);
        } catch(SyntaxException e) {
            exceptionList.add(e);
        }

        if(exceptionList.isEmpty()) return p;
        else return null;
    }

	private Program Program() throws SyntaxException
    {
        Token start = t;

        List<QualifiedName> imports = ImportList();
        match(KW_CLASS);
        String name = tokens.getString(match(IDENT));
        Block block = Block();
        return new Program(start, imports, name, block);
	}

	private List<QualifiedName> ImportList() throws SyntaxException
    {
        List<QualifiedName> imports = new ArrayList<QualifiedName>();
        StringBuilder sb;
        Token start;

        while(isKind(KW_IMPORT))
        {
            start = t;
            sb = new StringBuilder();

            match(KW_IMPORT);
            sb.append(tokens.getString(match(IDENT)));
            while(isKind(DOT))
            {
                sb.append("/");
                match(DOT);
                sb.append(tokens.getString(match(IDENT)));
            }
            match(SEMICOLON);
            imports.add(new QualifiedName(start, sb.toString()));
        }
        return imports;
	}

	private Block Block() throws SyntaxException
    {
        List<BlockElem> elems = new ArrayList<BlockElem>();
        Token start = t;

		match(LCURLY);
        while(isKind(PREDICT_DECLARATION) || isKind(PREDICT_STATEMENT))
        {
            if(isKind(PREDICT_DECLARATION)) elems.add(Declaration());
            else elems.add(Statement());
            match(SEMICOLON); // The FOLLOW of Declaration.
        }
		match(RCURLY);
        return new Block(start, elems);
	}

    protected Declaration Declaration() throws SyntaxException
    {
        match(KW_DEF);
        // Look ahead by 1. If it is an '=' then it must be a Closure Declaration. Else, it must be
        // a Var Declaration. Assume that the current kind is correct (is a IDENT), this will be
        // matched by VarDec or ClosureDec
        if(aheadIs(ASSIGN, 1)) return ClosureDec();
        else return VarDec();
    }

    protected VarDec VarDec() throws SyntaxException
    {
        Token start = t;
        Token ident = match(IDENT);
        Type type = null;

        if(isKind(COLON))
        {
            match(COLON);
            type = Type();
        }

        return new VarDec(start, ident, type);
    }

    protected Type Type() throws SyntaxException
    {
        Token start = t;
        if(isKind(AT))
        {
            match(AT);
            if(isKind(AT))
            {
                match(AT);
                return KeyValueType(start);
            }
            else return ListType(start);
        }
        else return SimpleType();
    }

    protected SimpleType SimpleType() throws SyntaxException { match(SIMPLE_TYPE); return null; }

    protected KeyValueType KeyValueType(Token start) throws SyntaxException
    {
        match(LSQUARE);
        SimpleType();
        match(COLON);
        Type();
        match(RSQUARE);
        return null;
    }

    protected ListType ListType(Token start) throws SyntaxException
    {
        match(LSQUARE);
        Type();
        match(RSQUARE);
        return null;
    }

    protected ClosureDec ClosureDec() throws SyntaxException
    {
        match(IDENT);
        match(ASSIGN);
        Closure();

        return null;
    }

    protected void Closure() throws SyntaxException
    {
        match(LCURLY);
        FormalArgList();
        match(ARROW);
        while(isKind(FIRST_STATEMENT))
        {
            Statement();
            match(SEMICOLON);
        }
        match(RCURLY);
    }

    protected void FormalArgList() throws SyntaxException
    {
        if(isKind(IDENT))
        {
            VarDec();
            while(isKind(COMMA))
            {
                match(COMMA);
                VarDec();
            }
        }
    }

    protected Statement Statement() throws SyntaxException
    {
        if(isKind(IDENT))
        {
            LValue();
            match(ASSIGN);
            Expression();
        }
        else if(isKind(KW_PRINT))
        {
            match(KW_PRINT);
            Expression();
        }
        else if(isKind(KW_WHILE))
        {
            match(KW_WHILE);
            if(isKind(TIMES))
            {
                match(TIMES);
                match(LPAREN);
                Expression();
                // Manually check for Range Expression
                if(isKind(RANGE))
                {
                    match(RANGE);
                    Expression();
                }
                match(RPAREN);
                Block();
            }
            else
            {
                match(LPAREN);
                Expression();
                match(RPAREN);
                Block();
            }
        }
        else if(isKind(KW_IF))
        {
            match(KW_IF);
            match(LPAREN);
            Expression();
            match(RPAREN);
            Block();
            if(isKind(KW_ELSE))
            {
                match(KW_ELSE);
                Block();
            }
        }
        else if(isKind(MOD))
        {
            match(MOD);
            Expression();
        }
        else if(isKind(KW_RETURN))
        {
            match(KW_RETURN);
            Expression();
        }
        else if(isKind(FIRST_STATEMENT)) throw new SyntaxException(t, "Error: Unused " + t);
        else if(!isKind(FOLLOW_STATEMENT)) throw new SyntaxException(t, PREDICT_STATEMENT);

        return null;
    }

    protected void ClosureEvalExpression() throws SyntaxException
    {
        match(IDENT);
        match(LPAREN);
        ExpressionList();
        match(RPAREN);
    }

    protected void LValue() throws SyntaxException
    {
        match(IDENT);
        if(isKind(LSQUARE))
        {
            match(LSQUARE);
            Expression();
            match(RSQUARE);
        }
    }

    protected void List() throws SyntaxException
    {
        match(LSQUARE);
        ExpressionList();
        match(RSQUARE);
    }

    protected void ExpressionList() throws SyntaxException
    {
        // Check FIRST(ExpressionList)
        if(isKind(FIRST_EXPRESSION_LIST))
        {
            Expression();
            while(isKind(COMMA))
            {
                match(COMMA);
                Expression();
            }
        }
        else if(isKind(FOLLOW_EXPRESSION_LIST)) { /* Do nothing, empty */ }
        else
        { /* unknown, throw error or let next match deal with it? */
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected token " + t + " found. Expected one of:");
            for (Kind kinds : FIRST_EXPRESSION_LIST) { sb.append(kinds).append(" "); }
            for (Kind kinds : FOLLOW_EXPRESSION_LIST) { sb.append(kinds).append(" "); }
            throw new SyntaxException(t, sb.toString());
        }
    }

    protected void KeyValueExpression() throws SyntaxException
    {
        Expression();
        match(COLON);
        Expression();
    }

    protected void KeyValueList() throws SyntaxException
    {
        // Check FIRST(KeyValueList)
        if(isKind(FIRST_KEY_VALUE_LIST))
        {
            KeyValueExpression();
            while(isKind(COMMA))
            {
                match(COMMA);
                KeyValueExpression();
            }
        }
        // Check FOLLOW(KeyValueList)
        else if(isKind(FOLLOW_KEY_VALUE_LIST)) { /* Do nothing, empty */ }
        else
        {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected token " + t + " found. Expected one of:");
            for (Kind kinds : FIRST_KEY_VALUE_LIST) { sb.append(kinds).append(" "); }
            for (Kind kinds : FOLLOW_KEY_VALUE_LIST) { sb.append(kinds).append(" "); }
            throw new SyntaxException(t, sb.toString());
        }
    }

    protected void MapList() throws SyntaxException
    {
        match(LSQUARE);
        KeyValueList();
        match(RSQUARE);
    }

    protected void RangeExpr() throws SyntaxException
    {
        Expression();
        match(RANGE);
        Expression();
    }

    protected void Expression() throws SyntaxException
    {
        Term();
        while(isKind(REL_OPS))
        {
            RelOp();
            Term();
        }
    }

    protected void Term() throws SyntaxException
    {
        Elem();
        while(isKind(WEAK_OPS))
        {
            WeakOp();
            Elem();
        }
    }

    protected void Elem() throws SyntaxException
    {
        Thing();
        while(isKind(STRONG_OPS))
        {
            StrongOp();
            Thing();
        }
    }

    protected void Thing() throws SyntaxException
    {
        Factor();
        while(isKind(VERY_STRONG_OPS))
        {
            VeryStrongOp();
            Factor();
        }
    }

    protected void Factor() throws SyntaxException
    {
        if(isKind(IDENT))
        {
            if(aheadIs(1, LPAREN)) ClosureEvalExpression();
            else if (aheadIs(1, LSQUARE))
            {
                match(IDENT);
                match(LSQUARE);
                Expression();
                match(RSQUARE);
            }
            else match(IDENT);
        }
        else if(isKind(FACTOR_FACTOR))
        {
            match(FACTOR_FACTOR);
            Factor();
        }
        else if(isKind(LCURLY)) Closure();
        else if(isKind(AT))
        {
            match(AT);
            if(isKind(AT))
            {
                match(AT);
                MapList();
            }
            else List();
        }
        else if(isKind(FACTOR_EXPRESSION) || isKind(LPAREN))
        {
            if(isKind(FACTOR_EXPRESSION)) match(FACTOR_EXPRESSION);
            match(LPAREN);
            Expression();
            match(RPAREN);
        }
        else match(FACTOR_FIRST);
    }

    protected void RelOp() throws SyntaxException { match(REL_OPS); }

    protected void WeakOp() throws SyntaxException { match(WEAK_OPS); }

    protected void StrongOp() throws SyntaxException { match(STRONG_OPS); }

    protected void VeryStrongOp() throws SyntaxException { match(VERY_STRONG_OPS); }
}
