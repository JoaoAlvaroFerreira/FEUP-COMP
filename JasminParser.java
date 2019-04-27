import java.lang.String;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class JasminParser{


  private String source;
  private SimpleNode root;
  private SimpleNode fileClass;
  private String accessspec;
  private String classname;
  private String supername;
  private ArrayList<FieldDefinition> definitionlist;
  private int vars;
  private int stacklim;
  private ArrayList<String> localVarList;
  private SymbolTable symbolTable;
  private int stackSize;
  private int maxStackSize;

  public JasminParser(String file_path,SimpleNode root,SymbolTable symbolTable){
    this.source = file_path;
    this.root = root;
    this.fileClass = (SimpleNode) root.jjtGetChild(0);
    this.localVarList = new ArrayList<>();
    this.symbolTable = symbolTable;

    if(fileClass.getId() == NewJava.JJTCLASS){
      this.accessspec = "public";
      this.classname = fileClass.getSymbol();
      if(fileClass.jjtGetChild(0).getId() == NewJava.JJTEXTENDS){
        supername = fileClass.jjtGetChild(0).getSymbol();
      }
    }
  }

  public void generate(){
    String[] sourceInfo = source.split("\\.");
    String sourceClass = sourceInfo[0];
    String sourceFileExtension = sourceInfo[1];

    //initial directives
    String output = "";
    output += ".source "+sourceClass+"."+sourceFileExtension+"\n";
    output += ".class " + accessspec + " " + classname + "\n";
    output += ".super java/lang/Object";

    if(supername!= null)
      output +="/"+supername;

    output += "\n\n";

    //globals
    output += "\n" + this.generateGlobals() + "\n";

    //methods
    for(int i=0;i<fileClass.jjtGetNumChildren();i++){
      if((fileClass.jjtGetChild(i).getId() == NewJava.JJTFUNCTION) ||
         (fileClass.jjtGetChild(i).getId() == NewJava.JJTMAIN)){
        output += this.generateMethod((SimpleNode)(fileClass.jjtGetChild(i)));
      }
    }

    //File Output
    File file = new File(sourceClass+".j");


    try{
      if (!file.exists()) {
        //file.getParentFile().mkdirs();
        file.createNewFile();
      }
    }
    catch(IOException ex){
      System.out.println("Caught:" + ex);
      ex.printStackTrace();
    }

    try{
      PrintWriter out = new PrintWriter(file);

      out.println(output);
      out.close();
    }
    catch(FileNotFoundException ex){
      System.out.println("Caught:" + ex);
      ex.printStackTrace();
    }

  }

  public String generateMethod(SimpleNode method){
    String ret = "";
    String tmp = "";
    localVarList.clear();
    localVarList.add("this");
    SymbolTableEntry methodSymbols = new SymbolTableEntry(method);
    this.stackSize = 0;
    this.maxStackSize = 0;

    //se for main, method e sempre igual
    if(method.getId() == NewJava.JJTMAIN){
      ret += ".method public static main([Ljava/lang/String;)V\n";
      method.symbol = "main";
      localVarList.add("args");
    }else{
      ret += ".method public " + method.getSymbol() + "(";

      //argumentos funcao
      for(int i=0;i<methodSymbols.params.size();i++){
        ret+=this.getJasminType(methodSymbols.params.get(i)) + ";";
        localVarList.add(methodSymbols.params.get(i).symbol);
      }
      //remove ultimo ponto e virgula
      if (ret != null && ret.length() > 0 && ret.charAt(ret.length() - 1) == ';') {
        ret = ret.substring(0, ret.length() - 1);
      }

      //tipo de retorno
      ret+= ")" + this.getJasminType(new SymbolType(methodSymbols.returnDescriptor)) + "\n";
    }


    //local variables directive
    tmp += this.generateLocalVariables(method);

    tmp +="\n";
    //label init
    tmp += method.getSymbol() + "_init:\n";

    //inserir statements
    for(int i=0;i<method.jjtGetNumChildren();i++){
      SimpleNode curStatement = (SimpleNode) method.jjtGetChild(i);
      switch(curStatement.getId()){
        case NewJava.JJTASSIGN:
          tmp+=this.generateAssign(curStatement);
          break;
        case NewJava.JJTRETURN:
          if(curStatement.jjtGetNumChildren() > 0){
            SimpleNode returnVal = (SimpleNode)curStatement.jjtGetChild(0);
            if(returnVal.getId() == NewJava.JJTTRUE){
              tmp+="bipush 1\n";
              this.incrementStackSize();
            }else if(returnVal.getId() == NewJava.JJTFALSE){
              tmp+="bipush 0\n";
              this.incrementStackSize();
            }else{
              tmp+="bipush "+returnVal.getSymbol()+"\n";
              this.incrementStackSize();
            }

            tmp+="ireturn\n";
            this.stackSize--;
          }else{
            tmp+="return\n";
          }

          break;
        default:
          break;
      }
    }

    if(methodSymbols.returnDescriptor.equals("void")){
      tmp+="return\n";
    }

    //label end
    tmp += method.getSymbol() + "_end:\n";

    tmp +="\n";

    tmp += ".end method\n\n";

    ret += ".limit stack " + Integer.toString(this.maxStackSize) + "\n";
    ret += tmp;

    return ret;
  }

  public String generateLocalVariables(SimpleNode method){
    String ret = "";
    String localVar = "";
    int varIndex = localVarList.size();
    SymbolType varType;

    for(int i=0; i<method.jjtGetNumChildren();i++){
      if((method.jjtGetChild(i).getId() == NewJava.JJTVAR)
          && (method.jjtGetChild(i).jjtGetChild(0).getId() == NewJava.JJTTYPE)){
        varType = new SymbolType(method.jjtGetChild(i).getSymbol(), method.jjtGetChild(i).jjtGetChild(0).getSymbol());

        localVarList.add(varType.symbol);

        //declaracao variaveis locais
        localVar += ".var " + Integer.toString(varIndex) + " is "
            + varType.symbol + " "
            + this.getJasminType(varType)
            + " from " + method.getSymbol() + "_init to " + method.getSymbol() + "_end\n";

        varIndex++;
      }
    }

    ret += ".limit locals " + Integer.toString(varIndex) + "\n\n";
    ret += localVar;

    return ret;
  }

  public String generateAssign(SimpleNode statement){
    String ret = "";

    //caso seja atrib. de operacoes
    if((statement.jjtGetChild(1).getId() == NewJava.JJTOP2) || (statement.jjtGetChild(1).getId() == NewJava.JJTOP3) || (statement.jjtGetChild(1).getId() == NewJava.JJTOP4) || (statement.jjtGetChild(1).getId() == NewJava.JJTOP5)){
      ret += " ; " + statement.jjtGetChild(0).getSymbol() + " = " + statement.jjtGetChild(1).getSymbol() + "\n"; //FIX THE SIGN ONLY COMMENT
      ret+= this.generateOp((SimpleNode)statement.jjtGetChild(1));
    //atribuicao de booleanas
    }else if(statement.jjtGetChild(1).getId() == NewJava.JJTFALSE){
      ret += " ; " + statement.jjtGetChild(0).getSymbol() + " = false\n";
      ret += "bipush 0\n";
      this.incrementStackSize();
    }else if(statement.jjtGetChild(1).getId() == NewJava.JJTTRUE){
      ret += " ; " + statement.jjtGetChild(0).getSymbol() + " = true\n";
      ret += "bipush 1\n";
      this.incrementStackSize();
    //inteiros
    }else{
      ret += " ; " + statement.jjtGetChild(0).getSymbol() + " = " + statement.jjtGetChild(1).getSymbol() + "\n";
      ret += "bipush " + statement.jjtGetChild(1).getSymbol() + "\n";
      this.incrementStackSize();
    }
    //verifica se é local ou global
    if(localVarList.indexOf(statement.jjtGetChild(0).getSymbol()) != -1){
      ret += "istore " + Integer.toString(localVarList.indexOf(statement.jjtGetChild(0).getSymbol())) + "\n";
      this.stackSize--;
    }else{
      ret += "putfield ";
      if(this.supername != null)
        ret += this.supername + "/";
      ret+= this.classname + "/" + statement.jjtGetChild(0).getSymbol() + " " + this.getJasminType(this.symbolTable.getGlobal(statement.jjtGetChild(0).getSymbol()))+"\n";
      this.stackSize--;
    }

    return ret;
  }

  public String getJasminType(SymbolType varType){
    switch(varType.type){
      case "int":
      return "I";
      case "int[]":
      return "[I";
      case "boolean":
      return "Z";
      case "void":
      return "V";
      default:
      return "";
    }
  }

  public String generateGlobals(){
    String ret = "";

    for(int i=0;i<this.symbolTable.globals.size();i++){
      ret += ".field public " + this.symbolTable.globals.get(i).symbol + " " + this.getJasminType(this.symbolTable.globals.get(i)) + "\n";
    }

    return ret;
  }
  public String generateOp(SimpleNode op){
    String ret = "";

    if(op.jjtGetChild(0).getId() == NewJava.JJTVAL){
      ret += "bipush " + op.jjtGetChild(0).getSymbol() + "\n";
      this.incrementStackSize();
    }else{
      ret += this.generateOp((SimpleNode)op.jjtGetChild(0));
    }

    if(op.jjtGetChild(1).getId() == NewJava.JJTVAL){
      ret += "bipush " + op.jjtGetChild(1).getSymbol() + "\n";
      this.incrementStackSize();
    }else{
      ret += this.generateOp((SimpleNode)op.jjtGetChild(1));
    }

    switch(op.symbol){
      case "+":
        ret += "iadd\n";
        this.stackSize--;
      break;
      case "-":
        ret += "isub\n";
        this.stackSize--;
      break;
      case "*":
        ret += "imul\n";
        this.stackSize--;
      break;
      case "/":
        ret += "idiv\n";
        this.stackSize--;
      break;
    }

    return ret;
  }

  public void incrementStackSize(){
    this.stackSize++;
    if(this.stackSize > this.maxStackSize)
      this.maxStackSize = this.stackSize;
  }

}
