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

    @Test
    public void testTokenize() throws Exception {

    }
}