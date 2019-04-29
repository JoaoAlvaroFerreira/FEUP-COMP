import java.util.Map;
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
  private SymbolTableEntry methodSymbols;

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

    //default constructor
    output+= "; default constructor\n.method public <init>()V\naload_0\ninvokespecial java/lang/Object/<init>()V\nreturn\n.end method\n\n";
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
    this.methodSymbols = new SymbolTableEntry(method);
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
      tmp+=this.generateStatement(curStatement);
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

  public String generateStatement(SimpleNode curStatement){
    String ret = "";
    switch(curStatement.getId()){
      case NewJava.JJTASSIGN:
        ret+=this.generateAssign(curStatement);
        break;

      //caso de retorno, verificar se existe valor a retornar
      case NewJava.JJTRETURN:
        if(curStatement.jjtGetNumChildren() > 0){
          SimpleNode returnVal = (SimpleNode)curStatement.jjtGetChild(0);
          ret+=this.generateStatement(returnVal);
          ret+="ireturn\n";
          this.stackSize--;
        }else{
          ret+="return\n";
        }
        break;

      //carregar valor da variável para a stack
      case NewJava.JJTTEXT:

        //verifica se é local ou global
        if(localVarList.indexOf(curStatement.getSymbol()) != -1){
          //verifica tipo da variavel
          SymbolType curVar = this.methodSymbols.getLocal(curStatement.getSymbol());

          if(curVar.type.equals("int")){
              ret += "iload ";
            }else{
              ret += "aload ";
            }

          ret += Integer.toString(localVarList.indexOf(curStatement.getSymbol())) + "\n";
          this.incrementStackSize();

        }else if(this.symbolTable.getGlobal(curStatement.getSymbol())!=null){
          ret += "getfield ";
          if(this.supername != null)
            ret += this.supername + "/";
          ret+= this.classname + "/" + curStatement.getSymbol() + " " + this.getJasminType(this.symbolTable.getGlobal(curStatement.getSymbol()))+"\n";
          this.incrementStackSize();
        //classe externa
        }else{
          //ret += "getstatic " + curStatement.getSymbol() + "\n";
        }

        break;

      //guardar valor em variavel
      case NewJava.JJTVAR:
      //apenas se não for uma declaraçao (com type como filho)
      if(curStatement.jjtGetNumChildren()==0){
        //verifica se é local
        if(localVarList.indexOf(curStatement.getSymbol()) != -1){
          //encontrar tipo da var na symbol table
          SymbolType curVar = this.methodSymbols.getLocal(curStatement.getSymbol());

          if(curVar.type.equals("int")){
              ret += "istore ";
            }else{
              ret += "astore ";
            }

          ret += Integer.toString(localVarList.indexOf(curStatement.getSymbol())) + "\n";
          this.stackSize--;
        //global (atributo da classe)
        }else if(this.symbolTable.getGlobal(curStatement.getSymbol())!=null){
          ret += "putfield ";
          if(this.supername != null)
            ret += this.supername + "/";
          ret+= this.classname + "/" + curStatement.getSymbol() + " " + this.getJasminType(this.symbolTable.getGlobal(curStatement.getSymbol()))+"\n";
          this.stackSize--;
        //classe externa
        }else{
          //ret += "getstatic " + curStatement.getSymbol();
        }
      }
        break;

      //carregar valor duietemnte do codigo
      case NewJava.JJTVAL:
        ret+="bipush "+curStatement.getSymbol()+"\n";
        this.incrementStackSize();
        break;

      //booleanas
      case NewJava.JJTFALSE:
        ret+="bipush 0\n";
        this.incrementStackSize();
        break;

      case NewJava.JJTTRUE:
        ret+="bipush 1\n";
        this.incrementStackSize();
        break;
      case NewJava.JJTOP2:
      case NewJava.JJTOP3:
      case NewJava.JJTOP4:
      case NewJava.JJTOP5:
        ret+= this.generateOp(curStatement);
        break;

      case NewJava.JJTNEW:
        ret += "new " + curStatement.getSymbol() + "\n";
        this.incrementStackSize();
        ret += "dup\n";
        this.incrementStackSize();
        ret += "invokespecial " + curStatement.getSymbol() + "/<init>()V\n";
        this.stackSize--;
        break;

      //chamda de funcoes de uma classe
      case NewJava.JJTFULLSTOP:
        String methodTypes = "(";
        SimpleNode classe = (SimpleNode)curStatement.jjtGetChild(0);
        SimpleNode parameter = (SimpleNode)curStatement.jjtGetChild(1);

        if(classe.symbol.equals("io")){
          for(int i=0;i<parameter.jjtGetNumChildren();i++){
            ret+=this.generateStatement((SimpleNode)parameter.jjtGetChild(i))+"\n";
            //methodTypes += this.getJasminType((SimpleNode)parameter.jjtGetChild(i).) + ";";
          }

          ret+="invokestatic " + classe.symbol + "/" + parameter.symbol + "(I)V\n";
          this.stackSize--;

        }else{

          ret+=this.generateStatement(classe);

          for(int i=0;i<parameter.jjtGetNumChildren();i++){
            ret+=this.generateStatement((SimpleNode)parameter.jjtGetChild(i))+"\n";
            //methodTypes += this.getJasminType((SimpleNode)parameter.jjtGetChild(i).) + ";";
          }

          methodTypes = this.getMethodSignature(curStatement);

          //COLOCAR NOME DA CLASSE CERTA
          ret +="invokevirtual " + this.classname + "/" + parameter.getSymbol() + methodTypes + "\n";
          this.stackSize--;
        }


        break;
      default:
        break;
    }

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

    /*
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
    */

    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(1));
    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(0));


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
      return "L"+this.classname+";";
    }
  }

  public String getMethodSignature(SimpleNode classCall){
    String retSignature = "";
    SimpleNode classe = (SimpleNode)classCall.jjtGetChild(0);
    SimpleNode parameter = (SimpleNode)classCall.jjtGetChild(1);
    ArrayList<String> argTypes = new ArrayList<>();

    //obter tipo dos parametros
    for(int i=0;i<parameter.jjtGetNumChildren();i++){
      SimpleNode curArg = (SimpleNode) parameter.jjtGetChild(i);

      switch(curArg.getId()){
        case NewJava.JJTVAL:
        case NewJava.JJTOP2:
        case NewJava.JJTOP3:
        case NewJava.JJTOP4:
        case NewJava.JJTOP5:
          argTypes.add("int");
          break;
        case NewJava.JJTFULLSTOP:
          argTypes.add(this.getNormalType(new SymbolType(JasminParser.extractRet(this.getMethodSignature(curArg)))));
          break;
        case NewJava.JJTFALSE:
        case NewJava.JJTTRUE:
          argTypes.add("boolean");
        case NewJava.JJTTEXT:
        case NewJava.JJTVAR:
          argTypes.add(this.getVarType(curArg));
        break;

        default:
          break;

      }
    }


    for(String arg : argTypes){
      System.out.println(" "+arg);
    }

    //obter tipo retorno da funcao
    SymbolTable classTable = Main.tables.get(this.getVarType(classe));
    retSignature = classTable.getReturn(parameter.symbol,argTypes);

    //gerar method signature
    String methodTypes ="(";
    for(String argType : argTypes){
      methodTypes +=this.getJasminType(new SymbolType(argType))+";";
    }
    //remover ultimo ponto e vrigula
    if (methodTypes != null && methodTypes.length() > 0 && methodTypes.charAt(methodTypes.length() - 1) == ';') {
      methodTypes = methodTypes.substring(0, methodTypes.length() - 1);
    }
    System.out.println("Return: " + retSignature + " " + classe.symbol + "."+parameter.symbol);
    methodTypes += ")"+this.getJasminType(new SymbolType(retSignature));



    return methodTypes;
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

    ret+=this.generateStatement((SimpleNode)op.jjtGetChild(0));
    ret+=this.generateStatement((SimpleNode)op.jjtGetChild(1));

    /*
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

    */

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

  public String getVarType(SimpleNode curVariable){
    //verifica se é local
    if(localVarList.indexOf(curVariable.getSymbol()) != -1){
      //encontrar tipo da var na symbol table
      SymbolType curVar = this.methodSymbols.getLocal(curVariable.getSymbol());
      return curVar.type;

    //global (atributo da classe)
    }else if(this.symbolTable.getGlobal(curVariable.getSymbol())!=null){
      return this.symbolTable.getGlobal(curVariable.getSymbol()).type;
    }

    return null;
  }


  //extrai tipo de retorno de uma signature de uma funcao
  public static String extractRet(String signature){
    String[] splitted = signature.split("\\)");
    return splitted[1];
  }

  public String getNormalType(SymbolType varType){
    switch(varType.type){
      case "I":
      return "int";
      case "[I":
      return "int[]";
      case "Z":
      return "boolean";
      case "V":
      return "void";
      default:
      return varType.type.substring(1,varType.type.length()-1);
    }
  }

}
