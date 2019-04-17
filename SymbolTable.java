import java.util.ArrayList;


public class SymbolTable{

    ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

    //fazer construtor symbol table
    public SymbolTable(SimpleNode root){

      SimpleNode classe = (SimpleNode)root.jjtGetChild(0);

      //para cada filho da classe extrair apenas funcoes e main
      for(int i = 0; i < classe.jjtGetNumChildren(); i++ ) {
        if((classe.jjtGetChild(i).getId() == NewJava.JJTFUNCTION) ||
        (classe.jjtGetChild(i).getId() == NewJava.JJTMAIN)){
          entries.add(new SymbolTableEntry((SimpleNode)classe.jjtGetChild(i)));
        }
      }

    }





    //fazer prints symbol table
    public void dump(){
      for(SymbolTableEntry tableEntry : entries){
        tableEntry.dump();
      }
    }


    //public int getValueEntry(int i) {
    	//return (SimpleNode)entries.get(i).getValue();
    //}
};
