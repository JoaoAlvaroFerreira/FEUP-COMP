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

  public JasminParser(String file_path,SimpleNode root){
    this.source = file_path;
    this.root = root;
    this.fileClass = (SimpleNode) root.jjtGetChild(0);

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
    if(supername!= null)
      output += ".super " + supername + "\n\n";


    //methods
    output += this.generateMethod((SimpleNode)(fileClass.jjtGetChild(1)));


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

    ret += ".method public " + method.getSymbol() + "(";

    SymbolTableEntry methodSymbols = new SymbolTableEntry(method);

    for(int i=0;i<methodSymbols.params.size();i++){
      ret+=this.getJasminType(methodSymbols.params.get(i)) + ",";
    }
    //remove ultima virgula
    if (ret != null && ret.length() > 0 && ret.charAt(ret.length() - 1) == ',') {
      ret = ret.substring(0, ret.length() - 1);
    }

    ret+= ")" + this.getJasminType(methodSymbols.returnDescriptor) + "\n";

    //inserir statements

    ret += ".end method\n";


    return ret;
  }

  public String getJasminType(SymbolType varType){
    switch(varType.type){
      case INT:
        return "I";
      case INT_ARR:
        return "[I";
      case BOOLEAN:
        return "Z";
      case VOID:
        return "V";
      default:
        return "";
    }
  }
}
