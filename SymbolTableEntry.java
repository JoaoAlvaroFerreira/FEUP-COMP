import java.util.ArrayList;



public class SymbolTableEntry{
    
	SymbolType symboltype;
	SymbolType returnDescriptor;
	ArrayList<Node> nodelist;
	
	
	public SymbolTableEntry(SimpleNode Node) {
		
		
		SymbolType symboltype = new SymbolType(Node.symbol, (String)Node.value); //key, value
		
		for(int i = 0; i < Node.jjtGetNumChildren(); i++ ) {
		nodelist.add(Node.jjtGetChild(i));
		}		
		
		returnDescriptor = new SymbolType(nodelist.get(0).)
		
	}
	
int getValue() {
		return  symboltype.getValue();
	}
	

}