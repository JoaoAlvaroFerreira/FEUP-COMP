public class Main {
    public static SymbolTable symbolTable;
    
    public Main() {}

    public static void main(String[] args) throws ParseException {
        NewJava parser;

        for (int i = 0; i < args.length; i++) {
            parser = new NewJava(symbolTable, args[i]);
            symbolTable = parser.start();
            
        }
    }
}