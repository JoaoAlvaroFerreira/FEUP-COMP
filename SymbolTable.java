import java.util.ArrayList;

public class SymbolTable {
  ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

  // fazer construtor symbol table
  public SymbolTable(SimpleNode root) {

    SimpleNode classe = (SimpleNode) root.jjtGetChild(0);

    // para cada filho da classe extrair apenas funcoes e main
    for (int i = 0; i < classe.jjtGetNumChildren(); i++) {
      if ((classe.jjtGetChild(i).getId() == NewJava.JJTFUNCTION)
          || (classe.jjtGetChild(i).getId() == NewJava.JJTMAIN)) {
        entries.add(new SymbolTableEntry((SimpleNode) classe.jjtGetChild(i)));
        System.out.println("semantic analysis: " + root.jjtGetChild(i).visit(this));
        for (int j = 0; j < classe.jjtGetChild(i).jjtGetNumChildren(); j++) {
          System.out.println("semantic analysis: " + classe.jjtGetChild(i).jjtGetChild(j).visit(this));
        }
      }
    }
  }

  // fazer prints symbol table
  public void dump() {
    for (SymbolTableEntry tableEntry : entries) {
      tableEntry.dump();
    }
  }

  public SymbolType.Type checkIfExists(String entryName) {
    for (int i = 0; i < entries.size(); i++) {
      if (entries.get(i).name == entryName) {
        return entries.get(i).returnDescriptor.getType(entries.get(i).returnDescriptor.symbol);
      }
    }
    return SymbolType.Type.ERROR;
  }

  // public int getValueEntry(int i) {
  // return (SimpleNode)entries.get(i).getValue();
  // }
};
