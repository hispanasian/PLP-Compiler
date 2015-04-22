package cop5555sp15;

/**
 * Example2 will calculate the n!
 * Expected output:
 * 0! =
 * 1
 * 1! =
 * 1
 * 5! =
 * 120
 * 10! =
 * 3628800
 * Created by Carlos on 4/21/2015.
 */
public class Example2
{
    public static void main(String[] args) throws Exception
    {
        String source = "class Factorial {\n"
                    + "def n: int; \n"
                    + "def result: int; \n"
                    + "def i : int; \n"
                    + "result = 1; \n"
                    + "i = 1; \n"
                    + "while(i <= n) {\n"
                        + "result = result * i; \n"
                        + "i = i + 1; \n"
                    + "}; \n"
                    + "print result;"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);

        CodeletBuilder.setInt(codelet, "n", 0);
        System.out.println("0! = "); // shoiuld be 1
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 1);
        System.out.println("1! = "); // should be 1
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 5);
        System.out.println("5! = "); // should be 120
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 10);
        System.out.println("10! = "); // should be 3628800
        codelet.execute();
    }
}
