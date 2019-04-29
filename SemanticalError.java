import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SemanticalError {
  ErrorType currentError;
  String filePath;
  int lineNum, colNum;

  enum ErrorType {
    UNKNOWN_SYMBOL {
      @Override
      public String toString() {
        return "UNKNOWN_SYMBOL";
      }
    },
    MULTIPLE_DECLARATION {
      @Override
      public String toString() {
        return "MULTIPLE_DECLARATION";
      }
    },
    NO_INITIALIZATION {
      @Override
      public String toString() {
        return "NO_INITIALIZATION";
      }
    },
    BAD_OPERAND_TYPES {
      @Override
      public String toString() {
        return "BAD_OPERAND_TYPES";
      }
    },
    INCOMPATIBLE_TYPES {
      @Override
      public String toString() {
        return "INCOMPATIBLE_TYPES";
      }
    },
    WRONG_METHOD_ARGS {
      @Override
      public String toString() {
        return "WRONG_METHOD_ARGS";
      }
    },
    OUT_OF_BOUNDS {
      @Override
      public String toString() {
        return "OUT_OF_BOUNDS";
      }
    }
  };

  public static ErrorType getErrorType(String error) {
    switch (error) {
    case "UNKNOWN_SYMBOL":
      return ErrorType.UNKNOWN_SYMBOL;
    case "MULTIPLE_DECLARATION":
      return ErrorType.MULTIPLE_DECLARATION;
    case "NO_INITIALIZATION":
      return ErrorType.NO_INITIALIZATION;
    case "BAD_OPERAND_TYPES":
      return ErrorType.BAD_OPERAND_TYPES;
    case "INCOMPATIBLE_TYPES":
      return ErrorType.INCOMPATIBLE_TYPES;
    case "WRONG_METHOD_ARGS":
      return ErrorType.WRONG_METHOD_ARGS;
      case "OUT_OF_BOUND":
      return ErrorType.OUT_OF_BOUNDS;
    default:
      return ErrorType.UNKNOWN_SYMBOL;
    }
  }

  SemanticalError(String error, String filePath, int lineNum, int colNum) {
    this.filePath = filePath;
    this.lineNum = lineNum;
    this.colNum = colNum;
    this.currentError = getErrorType(error);
  }

  void printError(int index, int length) {
    System.out.println("\n" + filePath + ":" + lineNum + ": error: Index " + index + " is out of bounds for length " + length);
    printLine();
  }

  void printError(String symbol) {
    if (this.currentError.toString().equals("BAD_OPERAND_TYPES")) {
      System.out.println("\n" + filePath + ":" + lineNum + ": error: bad operand types for binary operator '" + symbol + "'");
    }
    if (this.currentError.toString().equals("NO_INITIALIZATION")) {
      System.out.println("\n" + filePath + ":" + lineNum + ": error: variable " + symbol + " might not have been initialized");
    }
    printLine();
  }

  void printError(String className, String symbolType, String symbol) {
    System.out.println("\n" + filePath + ":" + lineNum + ": error: cannot find symbol");

    printLine();

    System.out.println("    symbol:   " + symbolType + " " + symbol);
    System.out.println("    location: class " + className + "\n");
  }

  void printError(String expressionType, String identifierType) {
    System.out.println("\n" + filePath + ":" + lineNum + ": error: incompatible types: " + expressionType
        + " cannot be converted to " + identifierType);

    printLine();
  }

  void printError(String[] responseArr, String symbol) {
    String args;
    args = "(";

    int index;
    while ((index = responseArr[2].indexOf("->")) != -1) {
      if (args.length() > 1) {
        args += ", ";
      }

      int finalIndex = responseArr[2].indexOf(",");
      if (finalIndex == -1) {
        finalIndex = responseArr[2].indexOf("]");
      }

      args += responseArr[2].substring(index + 3, finalIndex);
      responseArr[2] = responseArr[2].substring(finalIndex + 1);
    }
    args += ")";

    System.out.println("\n" + filePath + ":" + lineNum + ": error: variable " + symbol
        + " is already defined in method " + responseArr[1] + args);

    printLine();
  }

  void printError(String method, String className, ArrayList<SymbolType> params, SimpleNode node, SymbolTable data,
      int functionNum) {
    System.out.println("\n" + filePath + ":" + lineNum + ":  error: method " + method + " in class " + className
        + " cannot be applied to given types;");
    printLine();
    System.out.print("    required: ");
    for (int i = 0; i < params.size(); i++) {
      System.out.print(params.get(i).type);
      if (i < params.size() - 1) {
        System.out.print(",");
      }
    }
    System.out.print("\n    found: ");
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      String typeArg = (String) ((SimpleNode) node.jjtGetChild(i)).visit(data, functionNum);
      System.out.print(typeArg);
      if (i < node.jjtGetNumChildren() - 1) {
        System.out.print(",");
      }
    }
    System.out.println("\n    reason: actual and formal argument lists differ in length");

  }

  void printLine() {
    try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
      System.out.println(lines.skip(lineNum - 1).findFirst().get());
      while (colNum > 1) {
        System.out.print(" ");
        colNum--;
      }
      System.out.print("^");
      System.out.println();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}