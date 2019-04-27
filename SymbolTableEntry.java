import java.util.ArrayList;

public class SymbolTableEntry {

	String returnDescriptor;
	ArrayList<SimpleNode> nodelist;
	ArrayList<SymbolType> params;
	ArrayList<SimpleNode> aux;
	ArrayList<SymbolType> vars;
	String returnType;
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
			if (Node.jjtGetChild(i).getId() == NewJava.JJTRETURN) {
				for (int j = 0; j < Node.jjtGetChild(i).jjtGetNumChildren(); j++) {
					if (Node.jjtGetChild(i).jjtGetChild(j).getId() == NewJava.JJTVAL) {
						returnType = "int";
					}

					if (Node.jjtGetChild(i).jjtGetChild(j).getId() == NewJava.JJTTRUE || Node.jjtGetChild(i).jjtGetChild(j).getId() == NewJava.JJTFALSE) {
						returnType = "boolean";
					}
				}
			}
		}

		// return
		if (Node.getId() == NewJava.JJTMAIN)
			returnDescriptor = new SymbolType("void").type;
		else{
			returnDescriptor = nodelist.get(0).getSymbol();

			// construir params
			if (nodelist.get(1).getId() == NewJava.JJTARGS)
				for (int j = 0; j < nodelist.get(1).jjtGetNumChildren(); j++) {
					aux.add((SimpleNode) nodelist.get(1).jjtGetChild(j));
					params.add(
							new SymbolType(aux.get(j).getSymbol(), ((SimpleNode) aux.get(j).jjtGetChild(0)).getSymbol()));
				}
		}

		// variaveis locais
		for (int i = 0; i < nodelist.size(); i++) {
			// se nao for variavel, adicionar à lista
			if ((nodelist.get(i).getId() == NewJava.JJTVAR) && (((SimpleNode) nodelist.get(i).jjtGetChild(0)).getId() == NewJava.JJTTYPE)) {
				vars.add(new SymbolType(nodelist.get(i).getSymbol(),((SimpleNode) nodelist.get(i).jjtGetChild(0)).getSymbol()));
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
