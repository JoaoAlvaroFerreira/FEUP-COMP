import java.util.Map;
import java.lang.String;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.*;

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
  private int whileCounter;  //identifies the scope number of the current while
  private int compCounter;  //identifies the number of the current comparation
  private int ifCounter;  //identifies the scope number of the current if
  private int elseCounter;  //identifies the scope number of the current else
  private int arrayCounter;

  public JasminParser(String file_path,SimpleNode root,SymbolTable symbolTable){
    this.source = file_path;
    this.root = root;
    this.fileClass = (SimpleNode) root.jjtGetChild(0);
    this.localVarList = new ArrayList<>();
    this.symbolTable = symbolTable;
    this.arrayCounter = 0;
    this.whileCounter = 0;
    this.compCounter = 0;
    this.ifCounter = 0;


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
    } else {
      ret += ".method public " + method.getSymbol() + "(";

      //argumentos funcao
      for(int i=0;i<methodSymbols.params.size();i++){
        ret+=this.getJasminType(methodSymbols.params.get(i)) /*+ ";"*/;
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
      tmp += "\n";
    }

    if(methodSymbols.returnDescriptor.equals("int[]")) {
      tmp+="a";
    } else if (methodSymbols.returnDescriptor.equals("int") || methodSymbols.returnDescriptor.equals("boolean")) {
      tmp+="i";
    }

    tmp+="return\n";

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
    Boolean arrayflag = false;

    switch(curStatement.getId()){
      case NewJava.JJTASSIGN:
      ret+=this.generateAssign(curStatement);
      break;

      //caso de retorno, verificar se existe valor a retornar
      case NewJava.JJTRETURN:
      if(curStatement.jjtGetNumChildren() > 0){
        SimpleNode returnVal = (SimpleNode)curStatement.jjtGetChild(0);
        ret+=this.generateStatement(returnVal);
        this.stackSize--;
      }
      break;

      //carregar valor da variável para a stack
      case NewJava.JJTTEXT:
      //apenas se não for uma declaraçao (com type como filho)
      if(curStatement.jjtGetNumChildren()==0){
        //verifica se é local ou global
        if(localVarList.indexOf(curStatement.getSymbol()) != -1){
          //verifica tipo da variavel
          SymbolType curVar = this.methodSymbols.getLocal(curStatement.getSymbol());

          if(curVar.type.equals("int") || methodSymbols.returnDescriptor.equals("boolean") || curVar.type.equals("boolean")){
            ret += "iload ";
          }else{
            ret += "aload ";
          }

          ret += Integer.toString(localVarList.indexOf(curStatement.getSymbol())) + "\n";
          this.incrementStackSize();

        } else if(this.symbolTable.getGlobal(curStatement.getSymbol()) != null){
          ret+="aload 0\n";
          this.incrementStackSize();
          ret += "getfield ";
          if(this.supername != null)
          ret += this.supername + "/";
          ret+= this.classname + "/" + curStatement.getSymbol() + " " + this.getJasminType(this.symbolTable.getGlobal(curStatement.getSymbol()))+"\n";
          this.incrementStackSize();
          //classe externa

        } else {
          //ret += "getstatic " + curStatement.getSymbol() + "\n";
        }
      } else if(curStatement.jjtGetChild(0).getId() == NewJava.JJTARRINDEX){
        ret+=this.generateArray(curStatement);
        ret+="iaload\n";
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

          if(curVar.type.equals("int") || curVar.type.equals("boolean")){
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
      } else if(curStatement.jjtGetChild(0).getId() == NewJava.JJTARRINDEX)
      ret+=this.generateArray(curStatement);


      break;

      //carregar valor duietemnte do codigo
      case NewJava.JJTVAL:
      ret+="sipush "+curStatement.getSymbol()+"\n";
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
      //cria um novo array
      if(curStatement.getSymbol().equals("int[]")){
        //extrai o comprimento do array
        String arraySize = curStatement.jjtGetChild(0).getSymbol();
        ret+="bipush " + arraySize + "\n";
        this.incrementStackSize();
        ret += "newarray int\n";
      }else{
        ret += "new " + curStatement.getSymbol() + "\n";
        this.incrementStackSize();
        ret += "dup\n";
        this.incrementStackSize();
        ret += "invokespecial " + curStatement.getSymbol() + "/<init>()V\n";
        this.stackSize--;
      }
      break;

      //chamda de funcoes de uma classe
      case NewJava.JJTFULLSTOP:
      String methodTypes = "(";
      SimpleNode classe = (SimpleNode)curStatement.jjtGetChild(0);
      SimpleNode parameter = (SimpleNode)curStatement.jjtGetChild(1);


      if(parameter.symbol.equals("length")){
        ret+= "aload ";
        ret += Integer.toString(localVarList.indexOf(classe.getSymbol())) + "\n"; //mudar para globals
        ret += "arraylength\n";
        //se a classe for uma variavel local
      }else{    //classe externa
        //if((localVarList.indexOf(classe.getSymbol()) == -1)&&(this.symbolTable.getGlobal(classe.getSymbol())==null)){
        String type = "";
        ArrayList<String> argTypes = new ArrayList<String>();

        //generate loads for nested functions
        SimpleNode tmpParameter = parameter;
        SimpleNode tmpClasse = classe;
        for(int i=0;i<tmpParameter.jjtGetNumChildren();i++){
          if((curStatement.jjtGetParent().getId() != NewJava.JJTPAREMETER) &&
          (tmpParameter.jjtGetChild(i).getId() == NewJava.JJTFULLSTOP)){
            ret += "aload ";
            tmpClasse = (SimpleNode)tmpParameter.jjtGetChild(i).jjtGetChild(0);
            tmpParameter = (SimpleNode)tmpParameter.jjtGetChild(i).jjtGetChild(1);

            int varInd = localVarList.indexOf(tmpClasse.getSymbol());

            if(varInd<0){
              ret+="0\n";
            }else{
              ret+=varInd+"\n";
            }

            this.incrementStackSize();
            i=-1;
          }
        }


        for(int i=0;i<parameter.jjtGetNumChildren();i++){
          ret+=this.generateStatement((SimpleNode)parameter.jjtGetChild(i))+"";
          argTypes.add(this.getType((SimpleNode)parameter.jjtGetChild(i)).type);
          type+=this.getJasminType(this.getType((SimpleNode)parameter.jjtGetChild(i))) + "";
          //methodTypes += this.getJasminType((SimpleNode)parameter.jjtGetChild(i).) + ";";

          //se o parametro for outra funcao
          if(parameter.jjtGetChild(i).getId()==NewJava.JJTFULLSTOP){
            this.generateStatement((SimpleNode)parameter.jjtGetChild(i));
          }
        }
        //remover ultimo espaco
        if (type != null && type.length() > 0 && type.charAt(type.length() - 1) == ' ') {
          type = type.substring(0, type.length() - 1);
        }

        SimpleNode parent = (SimpleNode) curStatement.jjtGetParent();

        String retType = "V";

        if (parent.getId() == NewJava.JJTASSIGN){
          retType =this.getJasminType(this.getType((SimpleNode)parent.jjtGetChild(0)));
        }


        //this.function
        if (classe.getId() == NewJava.JJTTHIS){
          classe.symbol = this.classname;
          //load this to stack if its not parameter of another function
          if(curStatement.jjtGetParent().getId()!=NewJava.JJTPAREMETER){
            ret += "aload 0\n";
            this.incrementStackSize();
          }

          ret += "invokevirtual ";

          retType = this.getJasminType(new SymbolType(this.symbolTable.getReturn(parameter.symbol,argTypes)));
          ret+=classe.symbol + "/" + parameter.symbol + "("+type+")"+retType+"\n";
        }else if(classe.getId() == NewJava.JJTNEW){
          ret+=this.generateStatement(classe);
          for(int i=0;i<parameter.jjtGetNumChildren();i++){
            ret+=this.generateStatement((SimpleNode)parameter.jjtGetChild(i))+"\n";
            //methodTypes += this.getJasminType((SimpleNode)parameter.jjtGetChild(i).) + ";";
          }

          //System.out.println("curState: " + curStatement);

          methodTypes = this.getMethodSignature(curStatement);

          ret +="invokevirtual ";
          ret+=classe.symbol + "/" + parameter.symbol + "("+type+")"+retType+"\n";
        }else //classe externa ou interna
          if((localVarList.indexOf(classe.getSymbol()) == -1)&&(this.symbolTable.getGlobal(classe.getSymbol())==null)){
          ret += "invokestatic ";
          this.incrementStackSize();
          ret+=classe.symbol + "/" + parameter.symbol + "("+type+")"+retType+"\n";
        }else{
          retType = this.getJasminType(new SymbolType(this.symbolTable.getReturn(parameter.symbol,argTypes)));
          //load this to stack if its not parameter of another function
          if(curStatement.jjtGetParent().getId()!=NewJava.JJTPAREMETER){
            ret += "aload ";
            ret += Integer.toString(localVarList.indexOf(classe.getSymbol())) + "\n";
            this.incrementStackSize();
          }
          ret +="invokevirtual ";
          ret+=this.getType(classe).type + "/" + parameter.symbol + "("+type+")"+retType+"\n";
        }
        //this.stackSize--;
        //.lenght
      }




      /*else {
      ret+=this.generateStatement(classe);

      for(int i=0;i<parameter.jjtGetNumChildren();i++){
      ret+=this.generateStatement((SimpleNode)parameter.jjtGetChild(i))+"\n";
      //methodTypes += this.getJasminType((SimpleNode)parameter.jjtGetChild(i).) + ";";
    }

    System.out.println("curState: " + curStatement);

    methodTypes = this.getMethodSignature(curStatement);

    //COLOCAR NOME DA CLASSE
    ret +="invokevirtual " + this.getVarType(classe) + "/" + parameter.getSymbol() + methodTypes + "\n";
    //this.stackSize--;
  }*/
  break;
  case NewJava.JJTWHILE:
  ret+=this.generateWhile(curStatement);
  break;
  case NewJava.JJTIF:
  System.out.println("entrei no if");
  ret+=this.generateCondition(curStatement, "if");
  break;
  case NewJava.JJTELSE:
  System.out.println("entrei no else");
  ret+=this.generateCondition(curStatement, "else");
  break;
  // case NewJava.JJTARRINDEX:
  // ret+=this.generateArray(curStatement);

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

  //se for global e for um putfield, fazer push do this
  if((this.symbolTable.getGlobal(statement.jjtGetChild(0).getSymbol())!=null) &&
  (statement.jjtGetChild(0).getId()==NewJava.JJTVAR) &&
  !((statement.jjtGetChild(0).jjtGetNumChildren()>0)&&(statement.jjtGetChild(0).jjtGetChild(0).getId()==NewJava.JJTARRINDEX))){
    ret+="aload 0\n";
    this.incrementStackSize();
  }

  if((this.symbolTable.getGlobal(statement.jjtGetChild(1).getSymbol())!=null) &&
  (statement.jjtGetChild(1).getId()==NewJava.JJTVAR) &&
  !((statement.jjtGetChild(0).jjtGetNumChildren()>0)&&(statement.jjtGetChild(1).jjtGetChild(0).getId()==NewJava.JJTARRINDEX))){
    ret+="aload 0\n";
    this.incrementStackSize();
  }

  if ((statement.jjtGetChild(0).jjtGetNumChildren() > 0) && (statement.jjtGetChild(0).jjtGetChild(0).getId() == NewJava.JJTARRINDEX)){
    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(0));
    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(1));
    ret+="iastore\n";
  }else{
    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(1));
    ret+=this.generateStatement((SimpleNode)statement.jjtGetChild(0));
  }

  return ret;
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

  //obter tipo retorno da funcao

  String varType = this.getVarType(classe);
  if(varType == null){
    varType = "void";
    retSignature = "void";
  }else{
    SymbolTable classTable = Main.tables.get(varType);
    retSignature = classTable.getReturn(parameter.symbol,argTypes);
  }

  //gerar method signature
  String methodTypes ="(";
  for(String argType : argTypes){
    methodTypes +=this.getJasminType(new SymbolType(argType))/*+";"*/;
  }
  //remover ultimo ponto e vrigula
  if (methodTypes != null && methodTypes.length() > 0 && methodTypes.charAt(methodTypes.length() - 1) == ';') {
    methodTypes = methodTypes.substring(0, methodTypes.length() - 1);
  }
  //System.out.println("Return: " + retSignature + " " + classe.symbol + "."+parameter.symbol);
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
    //push 0 -> false ou 1 -> true
    case "<":
    ret += "if_icmplt true"+this.compCounter+"\n";
    ret += "bipush 0\n";
    ret += "goto endComp"+this.compCounter+"\n";
    ret += "true"+this.compCounter+":\n";
    ret += "bipush 1\n";
    ret += "endComp"+this.compCounter+":\n";
    this.compCounter++;
    break;
  }

  return ret;
}

public String generateWhile(SimpleNode loop){
  String ret = "";
  int whileNum = this.whileCounter++;

  if(loop.jjtGetNumChildren()>0){

    SimpleNode cond = (SimpleNode) loop.jjtGetChild(0);

    //label while
    ret+="\nwhile"+whileNum+": \n";


    //se for uma AND
    if(cond.getId() == NewJava.JJTOP2){
      //se algum for falso, sair do while
      ret+=this.generateStatement((SimpleNode)cond.jjtGetChild(0));
      ret +="ifeq endWhile"+whileNum+"\n\n";
      this.stackSize--;
      ret+=this.generateStatement((SimpleNode)cond.jjtGetChild(1));
      ret +="ifeq endWhile"+whileNum+"\n\n";
      this.stackSize--;
      //caso contrario 0-> false  tudo o resto -> true
    }else{
      ret+=this.generateStatement((SimpleNode)cond);
      ret +="ifeq endWhile"+whileNum+"\n\n";
      //this.stackSize--;
    }

    //instruction inside while
    for(int i=1;i<loop.jjtGetNumChildren();i++){

      ret+=this.generateStatement((SimpleNode)loop.jjtGetChild(i));
    }


    ret +="goto while"+whileNum+"\n";
    //end label
    ret +="endWhile"+whileNum+":\n";


  }
  return ret;
}


public String generateArray(SimpleNode array){
  String ret = "";


  if(array.jjtGetNumChildren()>0){

    //IF IT'S ASSIGNING VALUES WITH THE ARRAY
    //if(array.jjtGetChild(0).jjtGetChild(0).getId() == NewJava.JJTVAL){

    //STACK: ->array reference, index, value



    //verifica se é local ou global
    if(localVarList.indexOf(array.getSymbol()) != -1){
      ret += "aload ";
      ret += Integer.toString(localVarList.indexOf(array.getSymbol())) + "\n";
      this.incrementStackSize();
    }else{
      ret+="aload 0\n";
      this.incrementStackSize();
      ret += "getfield ";
      if(this.supername != null)
      ret += this.supername + "/";
      ret+= this.classname + "/" + array.getSymbol() + " " + this.getJasminType(this.symbolTable.getGlobal(array.getSymbol()))+"\n";
    }

    ret+=this.generateStatement((SimpleNode)array.jjtGetChild(0).jjtGetChild(0));
    //}

  }
  return ret;
}

public String generateCondition(SimpleNode cond, String type) {
  String ret = "";
  int num;
  SimpleNode condition = cond;
  if(cond.jjtGetNumChildren()>0){
    condition = (SimpleNode)cond.jjtGetChild(0);
  }
    if (type.equals("if")) {
      num = this.ifCounter++;
      //label if
      ret+="\nif"+num+": \n";
      //se for uma AND
      if(cond.jjtGetNumChildren()>0){
      System.out.println(condition.getId());

      if(condition.getId() == NewJava.JJTOP2){

        System.out.println("minor operator");

        //se algum for falso, sair do if
        ret += this.generateStatement((SimpleNode)condition.jjtGetChild(0));
        ret += "ifeq endIf"+num+"\n\n";
        this.stackSize--;
        ret += this.generateStatement((SimpleNode)condition.jjtGetChild(1));
        ret += "ifeq endIf"+num+"\n\n";
        this.stackSize--;
        //caso contrario 0-> false  tudo o resto -> true
      } else{
        //se for boolenana, fezer push de 0 ou 1
      //  if(((SimpleNode)condition).getId()==NewJava.JJTBOOLEAN){

        //}

        ret += this.generateStatement((SimpleNode)condition);
        if(cond.jjtGetParent().jjtGetNumChildren() > 2){
          ret +="ifeq else"+num+"\n\n";
        }else{
          ret +="ifeq endIf"+num+"\n\n";
        }

        //this.stackSize--;
      }
    }
    } else if (type.equals("else")) {
      num = this.elseCounter++;
      ret+="\nelse"+num+": \n";
      if(cond.jjtGetNumChildren()>0){
        ret += this.generateStatement((SimpleNode)condition);
      }
    } else {
      return "";
    }


    //statement inside if
    for(int i = 1 ; i < cond.jjtGetNumChildren();i++){
      ret += this.generateStatement((SimpleNode)cond.jjtGetChild(i));
      ret += "goto endIf"+num+"\n";

    }

    //end label
    if (type.equals("else")) {
      ret += "endElse"+num+":\n";
      ret += "endIf"+num+":\n";
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
  } else if(this.symbolTable.getGlobal(curVariable.getSymbol())!=null){
    return this.symbolTable.getGlobal(curVariable.getSymbol()).type;
  }

  return null;
}


//extrai tipo de retorno de uma signature de uma funcao
public static String extractRet(String signature){
  String[] splitted = signature.split("\\)");
  return splitted[1];
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

public SymbolType getType(SimpleNode variable){
  SymbolType type = new SymbolType("int");

  //se for o elemento de um array, o tipo e int
  if((variable.jjtGetNumChildren() > 0) && (((SimpleNode)variable.jjtGetChild(0)).getId() == NewJava.JJTARRINDEX)){
    return type;
  }

  //tipo de uma funcao
  if(variable.getId()==NewJava.JJTFULLSTOP){
    ArrayList<String> argTypes = new ArrayList<String>();
    for(int i=0;i<variable.jjtGetChild(1).jjtGetNumChildren();i++){
      argTypes.add(this.getType((SimpleNode)variable.jjtGetChild(1).jjtGetChild(i)).type);
    }
    type = new SymbolType(this.symbolTable.getReturn(variable.jjtGetChild(1).getSymbol(),argTypes));
  }

  if(this.methodSymbols.getLocal(variable.getSymbol())!= null){
    type = this.methodSymbols.getLocal(variable.getSymbol());
  }else if(this.symbolTable.getGlobal(variable.getSymbol()) != null){
    type = this.symbolTable.getGlobal(variable.getSymbol());
  }

  return type;
}

}
