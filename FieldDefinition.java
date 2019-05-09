public class FieldDefinition{

    public String access, field, signature;
    public int value;
    
    public FieldDefinition(String access, String field, String signature, int value){
        this.access = access;
        this.field = field;
        this.signature = signature;
        this.value = value;
    }
}