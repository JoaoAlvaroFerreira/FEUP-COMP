import java.util.ArrayList;


public class SymbolTable{

    ArrayList<SymbolTableEntry> entries = new ArrayList<SymbolTableEntry>();

    
    int getValueEntry(int i) {
    	return entries.get(i).getValue();
    }

    
};
