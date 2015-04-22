package cop5555sp15;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;

import cop5555sp15.ast.ASTNode;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.symbolTable.SymbolTable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class CodeletBuilder {

    public static class DynamicClassLoader extends ClassLoader
    {
        public DynamicClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> define(String className, byte[] bytecode) {
            return super.defineClass(className, bytecode, 0, bytecode.length);
        }
    }

    private static ASTNode parseSource(String source)
    {
        TokenStream stream = new TokenStream(source);
        Scanner scanner = new Scanner(stream);
        scanner.scan();
        Parser parser = new Parser(stream);
        System.out.println();
        ASTNode ast = parser.parse();
        if (ast == null) System.out.println("Syntax errors found.");
        assertNotNull(ast);
        return ast;
    }

    private static ASTNode typeCheckAST(ASTNode ast) throws Exception
    {
        SymbolTable symbolTable = new SymbolTable();
        TypeCheckVisitor v = new TypeCheckVisitor(symbolTable);
        try
        {
            ast.visit(v, null);
        } catch (TypeCheckVisitor.TypeCheckException e)
        {
            System.out.println(e.getMessage());
            fail("Type errors found");
        }
        return ast;
    }

    private static void dumpBytecode(byte[] bytecode)
    {
        int flags = ClassReader.SKIP_DEBUG;
        ClassReader cr;
        cr = new ClassReader(bytecode);
        cr.accept(new TraceClassVisitor(new PrintWriter(System.out)), flags);
    }

    private static byte[] generateByteCode(ASTNode ast) throws Exception {
        CodeGenVisitor v = new CodeGenVisitor();
        byte[] bytecode = (byte[]) ast.visit(v, null);
        dumpBytecode(bytecode);
        return bytecode;
    }

    public static Codelet newInstance(String source) throws Exception
    {
        Program program = (Program) parseSource(source);
        if(program == null) throw new Exception("Error occurred when parsing source.");
        typeCheckAST(program);
        byte[] bytecode = generateByteCode(program);
        if(bytecode == null) throw new Exception("Error occurred when generating code.");

        DynamicClassLoader loader = new DynamicClassLoader(Thread
                .currentThread().getContextClassLoader());
        Class<?> testClass = loader.define(program.JVMName, bytecode);
        Codelet codelet = (Codelet) testClass.newInstance();

        return codelet;
    }

    public static Codelet newInstance(File file) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder source = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
        {
            source.append(line);
        }
        return CodeletBuilder.newInstance(source.toString());
    }

    @SuppressWarnings("rawtypes")
    public static List getList(Codelet codelet, String name) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        List l = (List) field.get(codelet);
        return l;
    }

    public static int getInt(Codelet codelet, String name) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        int i = (Integer) field.get(codelet);
        return i;
    }

    public static void setInt(Codelet codelet, String name, int value) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        field.set(codelet, value);
    }

    public static String getString(Codelet codelet, String name) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        String s = (String) field.get(codelet);
        return s;
    }

    public static void setString(Codelet codelet, String name, String value) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        field.set(codelet, value);
    }

    public static boolean getBoolean(Codelet codelet, String name) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        boolean b = (Boolean) field.get(codelet);
        return b;
    }

    public static void setBoolean(Codelet codelet, String name, boolean value) throws Exception
    {
        Class<? extends Codelet> codeletClass = codelet.getClass();
        Field field = codeletClass.getDeclaredField(name);
        field.set(codelet, value);
    }
}