import java.util.ArrayList;


public class SymbolTable{
    static String[] variables = {"int", "int[]", "bool"};
    ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

    
    int getValueEntry(int i) {
    	return entries.get(i).getValue();
    }
};
