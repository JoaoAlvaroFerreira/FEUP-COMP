import java.util.ArrayList;

public class SymbolTable {
  ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

  // fazer construtor symbol table
  public SymbolTable(SimpleNode root) {
    int numSemanticErrors = 0;
    SimpleNode classe = (SimpleNode) root.jjtGetChild(0);
    root.jjtGetChild(0).visit(this);

    // para cada filho da classe extrair apenas funcoes e main
    for (int i = 0; i < classe.jjtGetNumChildren(); i++) {
      if ((classe.jjtGetChild(i).getId() == NewJava.JJTFUNCTION) || (classe.jjtGetChild(i).getId() == NewJava.JJTMAIN)) {
        entries.add(new SymbolTableEntry((SimpleNode) classe.jjtGetChild(i)));
        
        if (classe.jjtGetChild(i).visit(this).toString().equals("error")) {
          numSemanticErrors++; 
        }

        for (int j = 0; j < classe.jjtGetChild(i).jjtGetNumChildren(); j++) {
            if (classe.jjtGetChild(i).jjtGetChild(j).visit(this).toString().equals("error")) {
              numSemanticErrors++; 
            }
        }
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
    for (SymbolTableEntry tableEntry : entries) {
        tableEntry.dump();
    }
  }

  public Object searchIdentType(String entryName) {
    String type = "";
    for (int i = 0; i < entries.get(0).vars.size(); i++) {
      if (entryName.equals(entries.get(0).vars.get(i).symbol)) {
        type = entries.get(0).vars.get(i).type.toString();

      }
    }

    return type;
  }

  public Object checkIfExists(String entryName) {
    for (int i = 0; i < entries.get(0).nodelist.size(); i++) {
      if (entries.get(0).nodelist.get(i).symbol != null) {
        if (entries.get(0).nodelist.get(i).symbol.equals(entryName)) {
          return searchIdentType(entries.get(0).nodelist.get(i).symbol);
        }
      }
    }

    return SymbolType.Type.ERROR;
  }

  public Object checkReturnValue() {
    return entries.get(1).returnDescriptor;
  }
};
