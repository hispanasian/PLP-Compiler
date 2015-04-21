package cop5555sp15;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.List;
import cop5555sp15.ast.CodeGenVisitor;
import cop5555sp15.ast.Program;
import cop5555sp15.ast.TypeCheckVisitor;
import cop5555sp15.symbolTable.SymbolTable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CodeletBuilder {
    public static Codelet newInstance(String source) throws Exception
    {
        throw new NotImplementedException();
    }

    public static Codelet newInstance(File file) throws Exception
    {
        throw new NotImplementedException();
    }

    @SuppressWarnings("rawtypes")
    public static List getList(Codelet codelet, String name) throws Exception
    {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    public static void setString(Codelet codelet, String name, String value) throws Exception
    {
        throw new NotImplementedException();
    }

    public static boolean getBoolean(Codelet codelet, String name) throws Exception
    {
        throw new NotImplementedException();
    }

    public static void setBoolean(Codelet codelet, String name, boolean value) throws Exception
    {
        throw new NotImplementedException();
    }
}