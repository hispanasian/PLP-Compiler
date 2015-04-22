package cop5555sp15;

/**
 * Example1 will calculate the nth Fibonacci number
 * Expected output:
 * 0th Fibonacci number:
 * 0
 * 1st Fibonacci number:
 * 1
 * 2nd Fibonacci number:
 * 1
 * 5th Fibonacci number:
 * 5
 * 10th Fibonacci number:
 * 55
 */
public class Example1
{
    public static void main(String[] args) throws Exception
    {
        String source = "class Fibonacci {\n"
                    + "def n: int; \n"
                    + "def fib: @[int]; \n"
                    + "def i : int; \n"
                    + "def result : int;"
                    + "i = 2; \n"
                    + "fib = @[0,1];"
                    + "if(n == 0) { result = 0; };"
                    + "if(n == 1) { result = 1; };"
                    + "while(i <= n) {\n"
                        + "result = fib[0] + fib[1]; \n"
                        + "i = i + 1; \n"
                        + "fib[0] = fib[1];"
                        + "fib[1] = result;"
                    + "}; \n"
                    + "print result;"
                + "}";

        Codelet codelet = CodeletBuilder.newInstance(source);

        CodeletBuilder.setInt(codelet, "n", 0);
        System.out.println("0th Fibonacci number: ");
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 1);
        System.out.println("1st Fibonacci number: ");
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 2);
        System.out.println("2nd Fibonacci number: ");
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 5);
        System.out.println("5th Fibonacci number: ");
        codelet.execute();

        CodeletBuilder.setInt(codelet, "n", 10);
        System.out.println("10th Fibonacci number: ");
        codelet.execute();
    }
}
