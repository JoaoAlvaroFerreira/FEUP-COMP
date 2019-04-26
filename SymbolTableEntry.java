import java.util.ArrayList;

public class SymbolTableEntry {

	SymbolType returnDescriptor;
	ArrayList<SimpleNode> nodelist;
	ArrayList<SymbolType> params;
	ArrayList<SimpleNode> aux;
	ArrayList<SymbolType> vars;
	String name;

	public SymbolTableEntry(SimpleNode Node) {
		nodelist = new ArrayList<>();
		aux = new ArrayList<>();
		params = new ArrayList<>();
		vars = new ArrayList<>();

		if (Node.getId() == NewJava.JJTMAIN)
			name = "main";
		else
			name = Node.getSymbol();

		// nos da funcao
		for (int i = 0; i < Node.jjtGetNumChildren(); i++) {
			nodelist.add((SimpleNode) Node.jjtGetChild(i));
		}

		// return
		if (Node.getId() == NewJava.JJTMAIN)
			returnDescriptor = new SymbolType("void");
		else
			returnDescriptor = new SymbolType(nodelist.get(0).getSymbol());
		
		if (Node.getId() != NewJava.JJTMAIN){
			// construir params
			if (nodelist.get(1).getId() == NewJava.JJTARGS){
				for (int j = 0; j < nodelist.get(1).jjtGetNumChildren(); j++) {
					aux.add((SimpleNode) nodelist.get(1).jjtGetChild(j));
					params.add(new SymbolType(aux.get(j).getSymbol(), ((SimpleNode) aux.get(j).jjtGetChild(0)).getSymbol()));
				}
			}
		}

		// variaveis locais
		for (int i = 0; i < nodelist.size(); i++) {
			// se nao for variavel, adicionar à lista
			if (nodelist.get(i).getId() == NewJava.JJTVAR)
				if(((SimpleNode) nodelist.get(i).jjtGetChild(0)).getId() == NewJava.JJTTYPE) {
					vars.add(new SymbolType(nodelist.get(i).getSymbol(),
						((SimpleNode) nodelist.get(i).jjtGetChild(0)).getSymbol()));
				}
		}

	}

	public void dump() {
		System.out.println("\nFunction: " + name);
		System.out.println("Return: " + returnDescriptor.toString());
		System.out.println("Arguments:");
		for (SymbolType arg : params) {
			System.out.println("	" + arg);
		}
		System.out.println("Locals:");
		for (SymbolType var : vars) {
			System.out.println("	" + var);
		}
		System.out.println("");
	}

}
