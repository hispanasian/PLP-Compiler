package cop5555sp15;

import com.sun.xml.internal.fastinfoset.util.CharArray;
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
          //TODO: IMPLEMENT THIS
	}

	/**
	 * Returns if scanner has reached the end of the file. The end of file is reached when the current index surpasses
	 * the last element in code.
	 * @return	Whether or not scanner has reached the end of file.
	 */
	protected boolean eof() { return cur >= code.length; }

	/**
	 * Checks if cur is a new line. If it is, returns true and increments the line counter.
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
	 * Returns the Kind of keyword string is or null if string is not a keyword
	 * @param string	The string being checked
	 * @return			The Kind of keyword or null
	 */
	static protected Kind keyword(String string)
	{
		if(string == "int") return Kind.KW_INT;
		else if(string == "string") return Kind.KW_STRING;
		else if(string == "boolean") return Kind.KW_BOOLEAN;
		else if(string == "import") return Kind.KW_IMPORT;
		else if(string == "class") return Kind.KW_CLASS;
		else if(string == "def") return Kind.KW_DEF;
		else if(string == "while") return Kind.KW_WHILE;
		else if(string == "if") return Kind.KW_IF;
		else if(string == "else") return Kind.KW_ELSE;
		else if(string == "return") return Kind.KW_RETURN;
		else if(string == "print") return Kind.KW_PRINT;
		else return null;
	}

	/**
	 * Returns the Kind of booleanLiteral that string is or null if string is not a boolean literal
	 * @param string	The string being checked
	 * @return			The Kind of boolean literal or null
	 */
	static protected Kind booleanLiteral(String string)
	{
		if(string == "true") return Kind.BL_TRUE;
		if(string == "false") return Kind.BL_FALSE;
		else return null;
	}

	/**
	 * Returns Kind.NL_NULL if string is a null literal or null if string is not a null literal
	 * @param string	The string being checked
	 * @return			Kind.NL_NULL if string is a null literal or null
	 */
	static protected Kind nullLiteral(String string)
	{
		if(string == "null") return Kind.NL_NULL;
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

	/**
	 * Takes in the code character array and tokenizes code.
	 * @param code	The character array being tokenized
	 * @return		A ArrayList<Token> representation of code.
	 */
	protected ArrayList<Token> tokenize(char[] code)
	{
		ArrayList<Token> tokens = new ArrayList<Token>();
		return tokens;
	}
}

