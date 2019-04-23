import java.lang.String;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class JasminParser{

   
    private String source;
    private String accessspec;
    private String classname;
    private String supername;
    private ArrayList<FieldDefinition> definitionlist;
    private int vars;
    private int stacklim;

    public JasminParser(String file_path){

    }

    public void generate(){
        String output = "";
        output += ".source "+source+"\n";
        output += ".class" + accessspec + " " + classname + "\n";
        output += ".super" + supername + "\n";

        File file = new File(source+".j");
        
        
       try{
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } 
    }
    catch(IOException ex){
        System.out.println("Caught:" + ex);
        ex.printStackTrace();
    }
        try{
        PrintWriter out = new PrintWriter(source+".j");

        out.println(output);
        out.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Caught:" + ex);
        ex.printStackTrace();
        }

    } 
}