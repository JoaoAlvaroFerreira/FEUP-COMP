
public class SymbolType {

  String symbol;
  String type;

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    if (this.symbol == null) {
      return "  -> " + type.toString();
    } else {
      return symbol + " -> " + type.toString();
    }
  }

  SymbolType(String symbol, String varType) {
    this.symbol = symbol;
    this.type = varType;
  }

  SymbolType(String varType) {
    this.type = varType;
  }
}
