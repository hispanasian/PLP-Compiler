package cop5555sp15;

import static cop5555sp15.TokenStream.Kind.AND;
import static cop5555sp15.TokenStream.Kind.ARROW;
import static cop5555sp15.TokenStream.Kind.ASSIGN;
import static cop5555sp15.TokenStream.Kind.AT;
import static cop5555sp15.TokenStream.Kind.BAR;
import static cop5555sp15.TokenStream.Kind.BL_FALSE;
import static cop5555sp15.TokenStream.Kind.BL_TRUE;
import static cop5555sp15.TokenStream.Kind.COLON;
import static cop5555sp15.TokenStream.Kind.COMMA;
import static cop5555sp15.TokenStream.Kind.DIV;
import static cop5555sp15.TokenStream.Kind.DOT;
import static cop5555sp15.TokenStream.Kind.EOF;
import static cop5555sp15.TokenStream.Kind.EQUAL;
import static cop5555sp15.TokenStream.Kind.GE;
import static cop5555sp15.TokenStream.Kind.GT;
import static cop5555sp15.TokenStream.Kind.IDENT;
import static cop5555sp15.TokenStream.Kind.INT_LIT;
import static cop5555sp15.TokenStream.Kind.KW_BOOLEAN;
import static cop5555sp15.TokenStream.Kind.KW_CLASS;
import static cop5555sp15.TokenStream.Kind.KW_DEF;
import static cop5555sp15.TokenStream.Kind.KW_ELSE;
import static cop5555sp15.TokenStream.Kind.KW_IF;
import static cop5555sp15.TokenStream.Kind.KW_IMPORT;
import static cop5555sp15.TokenStream.Kind.KW_INT;
import static cop5555sp15.TokenStream.Kind.KW_PRINT;
import static cop5555sp15.TokenStream.Kind.KW_RETURN;
import static cop5555sp15.TokenStream.Kind.KW_STRING;
import static cop5555sp15.TokenStream.Kind.KW_WHILE;
import static cop5555sp15.TokenStream.Kind.KW_SIZE;
import static cop5555sp15.TokenStream.Kind.KW_KEY;
import static cop5555sp15.TokenStream.Kind.KW_VALUE;
import static cop5555sp15.TokenStream.Kind.LCURLY;
import static cop5555sp15.TokenStream.Kind.LE;
import static cop5555sp15.TokenStream.Kind.LPAREN;
import static cop5555sp15.TokenStream.Kind.LSHIFT;
import static cop5555sp15.TokenStream.Kind.LSQUARE;
import static cop5555sp15.TokenStream.Kind.LT;
import static cop5555sp15.TokenStream.Kind.MINUS;
import static cop5555sp15.TokenStream.Kind.MOD;
import static cop5555sp15.TokenStream.Kind.NOT;
import static cop5555sp15.TokenStream.Kind.NOTEQUAL;
import static cop5555sp15.TokenStream.Kind.PLUS;
import static cop5555sp15.TokenStream.Kind.RANGE;
import static cop5555sp15.TokenStream.Kind.RCURLY;
import static cop5555sp15.TokenStream.Kind.RPAREN;
import static cop5555sp15.TokenStream.Kind.RSHIFT;
import static cop5555sp15.TokenStream.Kind.RSQUARE;
import static cop5555sp15.TokenStream.Kind.SEMICOLON;
import static cop5555sp15.TokenStream.Kind.STRING_LIT;
import static cop5555sp15.TokenStream.Kind.TIMES;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import sun.java2d.pipe.SpanShapeRenderer;
import sun.reflect.annotation.ExceptionProxy;

public class SimpleParser
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

	SimpleParser(TokenStream tokens)
    {
		this.tokens = tokens;
		t = tokens.nextToken();
	}

	private Kind match(Kind kind) throws SyntaxException
    {
		if (isKind(kind))
        {
			consume();
			return kind;
		}
		throw new SyntaxException(t, kind);
	}

	private Kind match(Kind... kinds) throws SyntaxException
    {
		Kind kind = t.kind;
		if (isKind(kinds))
        {
			consume();
			return kind;
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

	public void parse() throws SyntaxException
    {
		Program();
		match(EOF);
	}

	private void Program() throws SyntaxException
    {
		ImportList();
		match(KW_CLASS);
		match(IDENT);
		Block();
	}

	private void ImportList() throws SyntaxException
    {
        while(isKind(KW_IMPORT))
        {
            match(KW_IMPORT);
            match(IDENT);
            while(isKind(DOT))
            {
                match(DOT);
                match(IDENT);
            }
            match(SEMICOLON);
        }
	}

	private void Block() throws SyntaxException
    {
		match(LCURLY);
        while(isKind(PREDICT_DECLARATION) || isKind(PREDICT_STATEMENT))
        {
            if(isKind(PREDICT_DECLARATION)) Declaration();
            else Statement();
            match(SEMICOLON); // The FOLLOW of Declaration.
        }
		match(RCURLY);
	}

    protected void TestBlock() throws SyntaxException { Block(); }

    protected void Declaration() throws SyntaxException
    {
        match(KW_DEF);
        // Look ahead by 1. If it is an '=' then it must be a Closure Declaration. Else, it must be
        // a Var Declaration. Assume that the current kind is correct (is a IDENT), this will be
        // matched by VarDec or ClosureDec
        if(aheadIs(ASSIGN, 1)) ClosureDec();
        else VarDec();
    }

    protected void VarDec() throws SyntaxException
    {
        match(IDENT);
        if(isKind(COLON))
        {
            match(COLON);
            Type();
        }
    }

    protected void Type() throws SyntaxException
    {
        if(isKind(AT))
        {
            match(AT);
            if(isKind(AT))
            {
                match(AT);
                KeyValueType();
            }
            else ListType();
        }
        else SimpleType();
    }

    protected void SimpleType() throws SyntaxException { match(SIMPLE_TYPE); }

    protected void KeyValueType() throws SyntaxException
    {
        match(LSQUARE);
        SimpleType();
        match(COLON);
        Type();
        match(RSQUARE);
    }

    protected void ListType() throws SyntaxException
    {
        match(LSQUARE);
        Type();
        match(RSQUARE);
    }

    protected void ClosureDec() throws SyntaxException
    {
        match(IDENT);
        match(ASSIGN);
        Closure();
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

    protected void Statement() throws SyntaxException
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
