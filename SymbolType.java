
public class SymbolType{

  enum Type{
    INT,
    INT_ARR,
    BOOLEAN,
    VOID;
  }

  public static Type getType(String varType){
    switch(varType){
      case "void":
        return Type.VOID;
      case "int":
        return Type.INT;
      case "boolean":
        return Type.BOOLEAN;
      default:
        return Type.VOID;
    }
  }

  String symbol;
  Type type;


  SymbolType(String symbol,String varType){
    this.symbol = symbol;
    this.type = getType(varType);
    }
}
