
public class SymbolType{

  enum Type{
    INT,
    INT_ARR,
    BOOLEAN
  }
  String symbol;
  Type type;


  SymbolType(String symbol,String value){
    this.symbol = symbol;
    this.type = value;
  }


public int getValue() {
	return type;
}


}
