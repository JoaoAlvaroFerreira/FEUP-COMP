import java.util.ArrayList;

import javafx.util.Pair;

public class SymbolTableEntry{
    
	Pair<String, Object> pair;
	String returnDescriptor = "";
	ArrayList<Node> nodelist;
	
	public SymbolTableEntry(SimpleNode Node) {
		
		
		pair = new Pair<>(Node.symbol, Node.value); //key, value
		
		for(int i = 0; i < Node.jjtGetNumChildren(); i++ ) {
		nodelist.add(Node.jjtGetChild(i));
		}		
		
	}
	
	int getValue() {
		return (int) pair.getValue();
	}
	

}