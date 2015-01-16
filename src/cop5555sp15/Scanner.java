package cop5555sp15;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import cop5555sp15.TokenStream.Kind;
import cop5555sp15.TokenStream.Token;

import java.util.ArrayList;

import static cop5555sp15.TokenStream.Kind.*;

public class Scanner
{
	protected TokenStream stream;
	protected char[] code;
	protected int cur;

	/**
	 * The constructor for Scanner.
	 * @param stream	The TokenStream that will be tokenized by this Scanner.
	 */
	public Scanner(TokenStream stream)
	{
		this.stream = stream;
		this.code = stream.inputChars;
		this.cur = 0;
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
	 * Returns the Kind of keyword string is or null if string is not a keyword
	 * @param string	The string being checked
	 * @return			The Kind of keyword or null
	 */
	static protected Kind keyword(String string)
	{
		return null;
	}

	/**
	 * Returns the Kind of booleanLiteral that string is or null if string is not a boolean literal
	 * @param string	The string being checked
	 * @return			The Kind of boolean literal or null
	 */
	static protected Kind booleanLiteral(String string)
	{
		return null;
	}

	/**
	 * Returns Kind.NL_NULL if string is a null literal or null if string is not a null literal
	 * @param string	The string being checked
	 * @return			Kind.NL_NULL if string is a null literal or null
	 */
	static protected Kind nullLiteral(String string)
	{
		return null;
	}

	/**
	 * Returns the Kind of string if string is a reserved keyword or literal. If string is not a reserved keyword or
	 * literal, this method returns null.
	 * @param string	The string being checked
	 * @return			The Kind of string
	 */
	static protected Kind reservedLiteral(String string)
	{
		Kind kind = null;

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

