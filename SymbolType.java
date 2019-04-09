public class SymbolType{

  enum Type{
    INT,
    INT_ARR,
    BOOLEAN,
    VOID
  }
  String symbol;
  Type type;


  SymbolType(String symbol,Type type){
    this.symbol = symbol;
    this.type = type;
  }
}
