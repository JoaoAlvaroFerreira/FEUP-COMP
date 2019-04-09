import java.util.ArrayList;



public class SymbolTableEntry{
    
	SymbolType symboltype;
	SymbolType returnDescriptor;
	ArrayList<SimpleNode> nodelist;
	ArrayList<SymbolType> params;
	ArrayList<SimpleNode> aux;
	
	
	public SymbolTableEntry(SimpleNode Node) {
		
		
		SymbolType symboltype = new SymbolType(Node.symbol, (String)Node.value); //key, value
		
		for(int i = 0; i < Node.jjtGetNumChildren(); i++ ) {
		nodelist.add((SimpleNode)Node.jjtGetChild(i));
		}		
		
		returnDescriptor = new SymbolType(nodelist.get(0).getSymbol(), (String)nodelist.get(0).getValue());
		
		
		for(int j = 0; j < nodelist.get(1).jjtGetNumChildren(); j++ ) {
			aux.add((SimpleNode)nodelist.get(1).jjtGetChild(j));
			params.add(new SymbolType(aux.get(j).getSymbol(), (String)aux.get(j).getValue()));
			}		
		
	}
	

}