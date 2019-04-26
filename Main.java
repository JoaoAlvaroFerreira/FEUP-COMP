import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static Map<String, SymbolTable> tables = new TreeMap<String, SymbolTable>();
    public static int numSemanticErrors = 0;
    
    public Main() {}

    public static void main(String[] args) throws ParseException {
      NewJava parser;
      SymbolTable symbolTable = null;

      for (int i = 0; i < args.length; i++) {
          parser = new NewJava(symbolTable, args[i]);
          symbolTable = parser.start();
          tables.put(symbolTable.className, symbolTable);
      }

      for(Map.Entry<String, SymbolTable> entry : tables.entrySet()) {
        String className = entry.getKey();
        SymbolTable table = entry.getValue();


        for (int i = 0; i < table.entries.size(); i++) {
          for (int j = 0; j < table.entries.get(i).nodelist.size(); j++) {
            if(table.entries.get(i).nodelist.get(j).visit(table, i).toString().equals("error"))
              numSemanticErrors++;
          }
        }
      }
        
      //NOOBICE A++  DA MARIANA!!!!!!!    
      if (numSemanticErrors > 1) {
        System.out.println(numSemanticErrors + " errors");
      } else if (numSemanticErrors == 1) {
        System.out.println(numSemanticErrors + " error");
      } else {
        symbolTable.dump();
      }
    }
}