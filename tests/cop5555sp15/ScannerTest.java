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
     * Tests the isEOF method in Scanner
     * @throws Exception
     */
    @Test
    public void testIsEOF() throws Exception
    {
        Scanner scanner = makeScanner("");
        assertEquals("isEOF should return true when code is an empty array", true, scanner.isEOF());

        scanner = makeScanner("123");
        assertEquals("isEOF should return false when cur is at the beginning of a non-empty code", false, scanner.isEOF());

        scanner.cur = 2;
        assertEquals("isEOF should return false when cur is at the last character of code", false, scanner.isEOF());

        scanner.cur = 3;
        assertEquals("isEOF should return true when cur has passed the last character of code", true, scanner.isEOF());

        scanner.cur = 50;
        assertEquals("isEOF should return true when cur has surpassed the last character", true, scanner.isEOF());
    }

    @Test
    public void testTokenize() throws Exception {

    }
}