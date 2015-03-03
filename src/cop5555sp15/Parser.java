package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;
import cop5555sp15.ast.*;
import jdk.nashorn.internal.ir.Assignment;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
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
        BlockElem e;

		match(LCURLY);
        while(isKind(PREDICT_DECLARATION) || isKind(PREDICT_STATEMENT))
        {
            if(isKind(PREDICT_DECLARATION)) elems.add(Declaration());
            else
            {
                e = Statement();
                if(e != null) elems.add(e);
            }
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

    protected SimpleType SimpleType() throws SyntaxException
    {
        Token start = t;
        Token type = match(SIMPLE_TYPE);
        return new SimpleType(start, type);
    }

    protected KeyValueType KeyValueType(Token start) throws SyntaxException
    {
        match(LSQUARE);
        SimpleType simple = SimpleType();
        match(COLON);
        Type type = Type();
        match(RSQUARE);
        return new KeyValueType(start, simple, type);
    }

    protected ListType ListType(Token start) throws SyntaxException
    {
        match(LSQUARE);
        Type type = Type();
        match(RSQUARE);
        return new ListType(start, type);
    }

    protected ClosureDec ClosureDec() throws SyntaxException
    {
        Token start = t;
        Token ident = match(IDENT);
        match(ASSIGN);
        Closure closure = Closure();

        return new ClosureDec(start, ident, closure);
    }

    protected Closure Closure() throws SyntaxException
    {
        Token start = t;
        List<Statement> statements = new ArrayList<Statement>();

        match(LCURLY);
        List<VarDec> formalArgList = FormalArgList();
        match(ARROW);
        while(isKind(FIRST_STATEMENT))
        {
            statements.add(Statement());
            match(SEMICOLON);
        }
        match(RCURLY);

        return new Closure(start, formalArgList, statements);
    }

    protected List<VarDec> FormalArgList() throws SyntaxException
    {
        List<VarDec> formalArgList = new ArrayList<VarDec>();
        if(isKind(IDENT))
        {
            formalArgList.add(VarDec());
            while(isKind(COMMA))
            {
                match(COMMA);
                formalArgList.add(VarDec());
            }
        }
        return formalArgList;
    }

    protected Statement Statement() throws SyntaxException
    {
        Token start = t;

        if(isKind(IDENT))
        {
            LValue lvalue = LValue();
            match(ASSIGN);
            Expression expression = Expression();
            return new AssignmentStatement(start, lvalue, expression);
        }
        else if(isKind(KW_PRINT))
        {
            match(KW_PRINT);
            Expression expression = Expression();
            return new PrintStatement(start, expression);
        }
        else if(isKind(KW_WHILE))
        {
            match(KW_WHILE);
            if(isKind(TIMES))
            {
                Expression upperExpression = null;
                match(TIMES);
                match(LPAREN);
                Expression expression = Expression();
                // Manually check for Range Expression
                if(isKind(RANGE))
                {
                    match(RANGE);
                    upperExpression = Expression();
                }
                match(RPAREN);
                Block block = Block();

                if(upperExpression == null) return new WhileStarStatement(start, expression, block);
                else return new WhileRangeStatement(start, new RangeExpression(start, expression, upperExpression), block);
            }
            else
            {
                match(LPAREN);
                Expression expression = Expression();
                match(RPAREN);
                Block block = Block();
                return new WhileStarStatement(start, expression, block);
            }
        }
        else if(isKind(KW_IF))
        {
            match(KW_IF);
            match(LPAREN);
            Expression expression = Expression();
            match(RPAREN);
            Block ifBlock = Block();
            if(isKind(KW_ELSE))
            {
                match(KW_ELSE);
                Block elseBlock = Block();
                return new IfElseStatement(start, expression, ifBlock, elseBlock);
            }
            return new IfStatement(start, expression, ifBlock);
        }
        else if(isKind(MOD))
        {
            match(MOD);
            Expression expression = Expression();
            return new ExpressionStatement(start, expression);
        }
        else if(isKind(KW_RETURN))
        {
            match(KW_RETURN);
            Expression expression = Expression();
            return new ReturnStatement(start, expression);
        }
        else if(isKind(FIRST_STATEMENT)) throw new SyntaxException(t, "Error: Unused " + t);
        else if(isKind(FOLLOW_STATEMENT)) return null;
        else if(!isKind(FOLLOW_STATEMENT)) throw new SyntaxException(t, PREDICT_STATEMENT);

        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected token " + t + " found. Expected one of:");
        for (Kind kinds : FIRST_STATEMENT) { sb.append(kinds).append(" "); }
        for (Kind kinds : FOLLOW_STATEMENT) { sb.append(kinds).append(" "); }
        sb.append("for a Statement");
        throw new SyntaxException(t, sb.toString());
    }

    protected ClosureEvalExpression ClosureEvalExpression() throws SyntaxException
    {
        Token start = t;
        Token ident = match(IDENT);
        match(LPAREN);
        List<Expression> expressionList = ExpressionList();
        match(RPAREN);
        return new ClosureEvalExpression(start, ident, expressionList);
    }

    protected LValue LValue() throws SyntaxException
    {
        Token start = t;
        Token ident = match(IDENT);
        if(isKind(LSQUARE))
        {
            match(LSQUARE);
            Expression expression = Expression();
            match(RSQUARE);
            return new ExpressionLValue(start, ident, expression);
        }
        return new IdentLValue(start, ident);
    }

    protected List<Expression> List() throws SyntaxException
    {
        match(LSQUARE);
        List<Expression> expression = ExpressionList();
        match(RSQUARE);
        return expression;
    }

    protected List<Expression> ExpressionList() throws SyntaxException
    {
        List<Expression> expressionList = new ArrayList<Expression>();
        // Check FIRST(ExpressionList)
        if(isKind(FIRST_EXPRESSION_LIST))
        {
            expressionList.add(Expression());
            while(isKind(COMMA))
            {
                match(COMMA);
                expressionList.add(Expression());
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
        return expressionList;
    }

    protected KeyValueExpression KeyValueExpression() throws SyntaxException
    {
        Token start = t;
        Expression key = Expression();
        match(COLON);
        Expression value = Expression();

        return new KeyValueExpression(start, key, value);
    }

    protected MapListExpression KeyValueList(Token start) throws SyntaxException
    {
        List<KeyValueExpression> mapList = new ArrayList<KeyValueExpression>();

        // Check FIRST(KeyValueList)
        if(isKind(FIRST_KEY_VALUE_LIST))
        {
            mapList.add(KeyValueExpression());
            while(isKind(COMMA))
            {
                match(COMMA);
                mapList.add(KeyValueExpression());
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

        return new MapListExpression(start, mapList);
    }

    protected MapListExpression MapList(Token start) throws SyntaxException
    {
        match(LSQUARE);
        MapListExpression mapList = KeyValueList(start);
        match(RSQUARE);
        return mapList;
    }

    protected RangeExpression RangeExpr() throws SyntaxException
    {
        Token start = t;
        Expression lower = Expression();
        match(RANGE);
        Expression upper = Expression();
        return new RangeExpression(start, lower, upper);
    }

    protected Expression Expression() throws SyntaxException
    {
        Token start = t;
        Expression e0 = null;
        Expression e1 = null;

        e0 = Term();
        while(isKind(REL_OPS))
        {
            Token op = RelOp();
            e1 = Term();
            e0 = new BinaryExpression(start, e0, op, e1);
        }

        return e0;
    }

    protected Expression Term() throws SyntaxException
    {
        Token start = t;
        Expression e0 = null;
        Expression e1 = null;

        e0 = Elem();
        while(isKind(WEAK_OPS))
        {
            Token op = WeakOp();
            e1 = Elem();
            e0 = new BinaryExpression(start, e0, op, e1);
        }
        return e0;
    }

    protected Expression Elem() throws SyntaxException
    {
        Token start = t;
        Expression e0 = null;
        Expression e1 = null;

        e0 = Thing();
        while(isKind(STRONG_OPS))
        {
            Token op = StrongOp();
            e1 = Thing();
            e0 = new BinaryExpression(start, e0, op, e1);
        }
        return e0;
    }

    protected Expression Thing() throws SyntaxException
    {
        Token start = t;
        Expression e0 = null;
        Expression e1 = null;

        e0 = Factor();
        while(isKind(VERY_STRONG_OPS))
        {
            Token op = VeryStrongOp();
            e1 = Factor();
            e0 = new BinaryExpression(start, e0, op, e1);
        }
        return e0;
    }

    protected Expression Factor() throws SyntaxException
    {
        Token start = t;

        if(isKind(IDENT))
        {
            if(aheadIs(1, LPAREN)) return ClosureEvalExpression();
            else if (aheadIs(1, LSQUARE))
            {
                Token ident = match(IDENT);
                match(LSQUARE);
                Expression expression = Expression();
                match(RSQUARE);
                return new ListOrMapElemExpression(start, ident, expression);
            }
            else return new IdentExpression(start, match(IDENT));
        }
        else if(isKind(FACTOR_FACTOR))
        {
            Token op = match(FACTOR_FACTOR);
            Expression expression = Factor();
            return new UnaryExpression(start, op, expression);
        }
        else if(isKind(LCURLY)) return new ClosureExpression(start, Closure());
        else if(isKind(AT))
        {
            match(AT);
            if(isKind(AT))
            {
                match(AT);
                return MapList(start);
            }
            else return new ListExpression(start, List());
        }
        else if(isKind(FACTOR_EXPRESSION) || isKind(LPAREN))
        {
            Token kw = null;
            if(isKind(FACTOR_EXPRESSION)) kw = match(FACTOR_EXPRESSION);
            match(LPAREN);
            Expression expression = Expression();
            match(RPAREN);

            if(kw == null) return expression;
            else
            {
                switch(kw.kind)
                {
                    case KW_SIZE: return new SizeExpression(start, expression);
                    case KW_KEY: return new KeyExpression(start, expression);
                    case KW_VALUE: return new ValueExpression(start, expression);
                }
            }
        }
        else
        {
            Token next = match(FACTOR_FIRST);
            switch(next.kind)
            {
                case INT_LIT: return new IntLitExpression(start, next.getIntVal());
                case BL_TRUE: return new BooleanLitExpression(start, next.getBooleanVal());
                case BL_FALSE: return new BooleanLitExpression(start, next.getBooleanVal());
                case STRING_LIT: return new StringLitExpression(start, next.getText());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected token " + t + " found. Expected one of:");
        for (Kind kinds : FACTOR_FIRST) { sb.append(kinds).append(" "); }
        for (Kind kinds : FACTOR_FACTOR) { sb.append(kinds).append(" "); }
        for (Kind kinds : FACTOR_EXPRESSION) { sb.append(kinds).append(" "); }
        sb.append(LCURLY).append(" ");
        sb.append(LPAREN).append(" ");
        sb.append(AT).append(" ");
        throw new SyntaxException(t, sb.toString());
    }

    protected Token RelOp() throws SyntaxException { return match(REL_OPS); }

    protected Token WeakOp() throws SyntaxException { return match(WEAK_OPS); }

    protected Token StrongOp() throws SyntaxException { return match(STRONG_OPS); }

    protected Token VeryStrongOp() throws SyntaxException { return match(VERY_STRONG_OPS); }
}
