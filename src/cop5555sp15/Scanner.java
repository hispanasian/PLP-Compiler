package cop5555sp15;

import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;

import java.util.ArrayList;

public class Scanner
{
	protected TokenStream stream;
	protected char[] code;
	protected int cur;
	protected int line;

	/**
	 * The constructor for Scanner.
	 * @param stream	The TokenStream that will be tokenized by this Scanner.
	 */
	public Scanner(TokenStream stream)
	{
		this.stream = stream;
		this.code = stream.inputChars;
		this.cur = 0;
		this.line = 0;
	}

	/**
	 * Fills in the stream.tokens list with recognized tokens
	 * from the input
	 */
	public void scan()
	{
		Kind kind = null;
		int start = cur;

		while(!eof())
		{
			if(newLine()) {/* do nothing on new line, newLine() increments line and cur automatically */}
			else if(whitespace()) {/* do nothing, whitespace() increments cur automatically */}
			else
			{
				start = cur;
				kind = this.identity();
				if(kind == null) kind = this.stringLiteral();
				if(kind == null) kind = this.intLiteral();
				if(kind == null) kind = this.operator();
				if(kind == null) kind = this.separator();
				if(kind == null) kind = this.comment();
				/* Check if cur == start because comment can still return null but not be an illegal char */
				if(kind == null && cur == start)
				{
					kind = Kind.ILLEGAL_CHAR;
					cur++;
				}

				/* Again, a terminated comment does not create a token */
				if(kind != null) stream.tokens.add(stream.new Token(kind, start, cur, line+1)); // increment line by 1 so it does not start at 0
			}
		}
		/* Add EOF token */
		stream.tokens.add(stream.new Token(Kind.EOF, cur, cur, line+1));
	}

	/**
	 * Returns if scanner has reached the end of the file. The end of file is reached when the current index surpasses
	 * the last element in code.
	 * @return	Whether or not scanner has reached the end of file.
	 */
	protected boolean eof() { return cur >= code.length; }

	/**
	 * Returns if cur is currently pointing at whitespace. If the whitespace is also a new line, it will increment the
	 * line counter.
	 * @return	True if cur is pointing at whitespace.
	 */
	protected boolean whitespace()
	{
		boolean whitespace = false;
		if(newLine()) whitespace = true;
		else if(Character.isWhitespace(code[cur]))
		{
			whitespace = true;
			cur++;
		}
		return whitespace;
	}

	/**
	 * Checks if cur is a new line. If it is, returns true and increments the line counter and increments cur
	 * accordingly.
	 * @return	Whether or scanner is at a new line
	 */
	protected boolean newLine()
	{
		boolean newline = false;
		if(code[cur] == '\r')
		{
			cur++;
			line++;
			newline = true;
			if(!eof() && code[cur] == '\n') cur++;
		}
		else if(code[cur] == '\n')
		{
			cur++;
			line ++;
			newline = true;
		}
		return newline;
	}

	/**
	 * Checks if cur is at an operator. If it is, returns the Kind of the operator and increments cur accordingly.
	 * @return	The Kind of operator at cur.
	 */
	protected Kind operator()
	{
		Kind kind = null;
		switch(code[cur])
		{
			/* This is a tricky case. We must check for the following operators: = and == */
			case '=': kind = Kind.ASSIGN;
				cur++;
				if(!eof() && code[cur] == '=') kind = Kind.EQUAL;
				else cur--;
				break;
			case '|': kind = Kind.BAR;
				break;
			case '&': kind = Kind.AND;
				break;
			/* This is a tricky case. We must check for the following operators: ! and != */
			case '!': kind = Kind.NOT;
				cur++;
				if(!eof() && code[cur] == '=') kind = Kind.NOTEQUAL;
				else cur--;
				break;
			/* This is a tricky case. We must check for the following operators: < and <= and << */
			case '<': kind = Kind.LT;
				cur++;
				if(!eof() && code[cur] == '=') kind = Kind.LE;
				else if(!eof() && code[cur] == '<') kind = Kind.LSHIFT;
				else cur--;
				break;
			/* This is a tricky case. We must check for the following operators: > and >= and >> */
			case '>': kind = Kind.GT;
				cur++;
				if(!eof() && code[cur] == '=') kind = Kind.GE;
				else if(!eof() && code[cur] == '>') kind = Kind.RSHIFT;
				else cur--;
				break;
			case '+': kind = Kind.PLUS;
				break;
			/* This is a tricky case. We must check for the following operators: - and -> */
			case '-': kind = Kind.MINUS;
				cur++;
				if(!eof() && code[cur] == '>') kind = Kind.ARROW;
				else cur--;
				break;
			case '*': kind = Kind.TIMES;
				break;
			/* This is a tricky case. We must check for / and ensure there is not a leading * */
			case '/': cur++;
				if(eof() || !(!eof() && code[cur] == '*')) kind = Kind.DIV;
				cur--;
				break;
			case '%': kind = Kind.MOD;
				break;
			case '@': kind = Kind.AT;
				break;
		}
		/* Always increment cur when an operator is found */
		if(kind != null) cur++;
		return kind;
	}

	/**
	 * Checks if cur is a separator. If it is, returns the Kind of the separator and increments cur accordingly.
	 * @return	The Kind of the separator at cur.
	 */
	protected Kind separator()
	{
		Kind kind = null;
		switch(code[cur])
		{
			/* This is a tricky case. We must check for the following separators: . and .. */
			case '.': kind = Kind.DOT;
				cur++;
				if(!eof() && code[cur] == '.') kind = Kind.RANGE;
				else cur--;
				break;
			case ';': kind = Kind.SEMICOLON;
				break;
			case ',': kind = Kind.COMMA;
				break;
			case '(': kind = Kind.LPAREN;
				break;
			case ')': kind = Kind.RPAREN;
				break;
			case '[': kind = Kind.LSQUARE;
				break;
			case ']': kind = Kind.RSQUARE;
				break;
			case '{': kind = Kind.LCURLY;
				break;
			case '}': kind = Kind.RCURLY;
				break;
			case ':': kind = Kind.COLON;
				break;
			case '?': kind = Kind.QUESTION;
				break;
		}
		/* Always increment cur when a separator is found */
		if(kind != null) cur++;
		return kind;
	}

	/**
	 * Checks if cur is at a int literal. If it is, returns Kind.INT_LIT and increments cur accordingly.
	 * @return	Kind.INT_LIT if cur is at a int literal and null if it is not
	 */
	protected Kind intLiteral()
	{
		Kind kind = null;
		if(code[cur] == '0')
		{
			kind = Kind.INT_LIT;
			cur++;
		}
		else if(Character.isDigit(code[cur]))
		{
			kind = Kind.INT_LIT;
			while(!eof() && Character.isDigit(code[cur]))
			{
				cur++;
			}
		}
		return kind;
	}

	/**
	 * Checks if cur is at a string literal. If it is, returns Kind.STR_LIT and increments cur accordingly.
	 * @return	Kind.STR_LIT if cur is at a string literal and null if it is not
	 */
	protected Kind stringLiteral()
	{
		Kind kind = null;
		if(code[cur] == '\"')
		{
			kind = Kind.UNTERMINATED_STRING;
			cur++;
			boolean terminated = false;
            boolean escapePrev = false;
			while(!eof() && !terminated)
			{
                if(code[cur] == '\\' && !escapePrev) escapePrev = true;
				else if(code[cur] == '\"' && !escapePrev) terminated = true;
				else if(newLine()) { /* new line found */ }
                else escapePrev = false;
				cur++;
			}
			if(terminated) kind = Kind.STRING_LIT;
		}
		return kind;
	}

	/**
	 * Checks if cur is at a comment. If it is, returns null (unless it is an UNTERMINATED_COMMENT) and increments cur
	 * and line accordingly.
	 * @return	null unless the comment is unterminated.
	 */
	protected Kind comment()
	{
		Kind kind = Kind.UNTERMINATED_COMMENT;
		int start = cur;
		boolean terminated = false;
		if(code[cur] == '/')
		{
			cur++;
			if(code[cur] == '*')
			{/* We are inside a comment */
				cur++;
				while(!eof() && !terminated)
				{
					/* check for termination */
					if(code[cur] == '*')
					{
						cur++;
						if(!eof() && code[cur] == '/')
						{
							terminated = true;
							cur++;
						}
						else if(!eof() && newLine()) { /* take care of any new lines */ }
					}
					else if(newLine()) { /* take care of any new lines */ }
					else cur++;
				}
			}
			else cur--; // Fix the increment to cur
		}
		if(terminated || (cur - start == 0)) kind = null;
		return kind;
	}

	/**
	 * Checks if cur is at an identity. If it is, returns the Kind of identity at cur and increments cur accordingly.
	 * @return	The Kind of identity at cur or null.
	 */
	protected Kind identity()
	{
		Kind kind = null;
		if(!eof() && Character.isJavaIdentifierStart(code[cur]))
		{
			int start = cur;
			kind = Kind.IDENT;
			cur++;

			/* Now to see if the following characters are also part of the Identity */
			while(!eof() && Character.isJavaIdentifierPart(code[cur]))
			{
				cur++;
			}

			/* Now check to see if the identity is a keyword or reserved literal */
			String ident = String.copyValueOf(code, start, (cur - start));
			Kind temp = Scanner.reservedLiteral(ident);
			if(temp != null) kind = temp;
		}
		return kind;
	}

	/**
	 * Returns the Kind of keyword string is or null if string is not a keyword
	 * @param string	The string being checked
	 * @return			The Kind of keyword or null
	 */
	static protected Kind keyword(String string)
	{
		if(string.equals("int")) return Kind.KW_INT;
		else if(string.equals("string")) return Kind.KW_STRING;
		else if(string.equals("boolean")) return Kind.KW_BOOLEAN;
		else if(string.equals("import")) return Kind.KW_IMPORT;
		else if(string.equals("class")) return Kind.KW_CLASS;
		else if(string.equals("def")) return Kind.KW_DEF;
		else if(string.equals("while")) return Kind.KW_WHILE;
		else if(string.equals("if")) return Kind.KW_IF;
		else if(string.equals("else")) return Kind.KW_ELSE;
		else if(string.equals("return")) return Kind.KW_RETURN;
		else if(string.equals("print")) return Kind.KW_PRINT;
		else return null;
	}

	/**
	 * Returns the Kind of booleanLiteral that string is or null if string is not a boolean literal
	 * @param string	The string being checked
	 * @return			The Kind of boolean literal or null
	 */
	static protected Kind booleanLiteral(String string)
	{
		if(string.equals("true")) return Kind.BL_TRUE;
		if(string.equals("false")) return Kind.BL_FALSE;
		else return null;
	}

	/**
	 * Returns Kind.NL_NULL if string is a null literal or null if string is not a null literal
	 * @param string	The string being checked
	 * @return			Kind.NL_NULL if string is a null literal or null
	 */
	static protected Kind nullLiteral(String string)
	{
		if(string.equals("null")) return Kind.NL_NULL;
		else return null;
	}

	/**
	 * Returns the Kind of string if string is a reserved keyword or literal. If string is not a reserved keyword or
	 * literal, this method returns null.
	 * @param string	The string being checked
	 * @return			The Kind of string
	 */
	static protected Kind reservedLiteral(String string)
	{
		Kind kind = keyword(string);
		if(kind == null) kind = booleanLiteral(string);
		if(kind == null) kind = nullLiteral(string);
		return kind;
	}
}

