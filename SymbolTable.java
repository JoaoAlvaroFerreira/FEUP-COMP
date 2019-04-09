import java.util.ArrayList;


public class SymbolTable{
    static String[] variables = {"int", "int[]", "bool"};
    ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

    //fazer construtor symbol table

    //fazer prints symbol table

    int getValueEntry(int i) {
    	return entries.get(i).getValue();
    }
};
