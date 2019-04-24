import java.util.ArrayList;

public class SymbolTable {
  ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();
  ArrayList<SymbolType> globals = new ArrayList<SymbolType>();
  String className;

  // fazer construtor symbol table
  public SymbolTable(SimpleNode root) {
    int numSemanticErrors = 0;
    SimpleNode classe = (SimpleNode) root.jjtGetChild(0);

    className = classe.symbol;

    // para cada filho da classe extrair apenas funcoes e main
    for (int i = 0; i < classe.jjtGetNumChildren(); i++) {
      if ((classe.jjtGetChild(i).getId() == NewJava.JJTFUNCTION) || (classe.jjtGetChild(i).getId() == NewJava.JJTMAIN)) {
        entries.add(new SymbolTableEntry((SimpleNode) classe.jjtGetChild(i)));

        /*
        if (classe.jjtGetChild(i).visit(this, i).toString().equals("error")) {
          numSemanticErrors++;
        }

        for (int j = 0; j < classe.jjtGetChild(i).jjtGetNumChildren(); j++) {
          if (classe.jjtGetChild(i).jjtGetChild(j).visit(this, i).toString().equals("error")) {
            numSemanticErrors++;
          }
        }
        */
      }else  if ((classe.jjtGetChild(i).getId() == NewJava.JJTVAR)
          && (((SimpleNode) classe.jjtGetChild(i).jjtGetChild(0)).getId() == NewJava.JJTTYPE)) {
        globals.add(new SymbolType(classe.jjtGetChild(i).getSymbol(),
            ((SimpleNode) classe.jjtGetChild(i).jjtGetChild(0)).getSymbol()));
      }
    }


    if (numSemanticErrors > 1) {
      System.out.println(numSemanticErrors + " errors");
    } else if (numSemanticErrors == 1) {
      System.out.println("1 error");
    } else {
      this.dump();
    }
  }

  // fazer prints symbol table
  public void dump() {
    System.out.println("Globals:");
    for (SymbolType globalVar : globals) {
      System.out.println(" " + globalVar);
    }
    for (SymbolTableEntry tableEntry : entries) {
      tableEntry.dump();
    }
  }

  public String searchIdentType(String entryName, int functionNum) {
    String type = "";
    for (int i = 0; i < entries.get(functionNum).vars.size(); i++) {

      if (entryName.equals(entries.get(functionNum).vars.get(i).symbol)) {
        type = entries.get(functionNum).vars.get(i).type.toString();
        break;
      }
    }

    return type;
  }

  public String checkIfExists(String entryName, int functionNum) {
    String method = "";
    String ident = SymbolType.Type.ERROR.toString();

    for (int i = 0; i < entries.get(functionNum).nodelist.size(); i++) {
      if (entries.get(functionNum).nodelist.get(i).symbol != null) {
        if (entries.get(functionNum).nodelist.get(i).symbol.equals(entryName)) {
          // duplicate identifiers
          if (method.isEmpty()) {
            ident = searchIdentType(entries.get(functionNum).nodelist.get(i).symbol, functionNum);
            method = entries.get(functionNum).name + "/" + entries.get(functionNum).params;
          } else {
            return entries.get(functionNum).nodelist.get(i).getLineNumber() + "/" + method;
          }
        }
      }
    }

    return ident;
  }

  public String searchParam(String symbol, int functionNum) {
    for (int i = 0; i < entries.get(functionNum).params.size(); i++) {
      String param = entries.get(functionNum).params.get(i).toString();

      int index = param.indexOf("->");
      String name = param.substring(0, index - 1);

      if(name.equals(symbol)) {
        return param.substring(index + 3);
      }
    }
    return "error";
  }
};
