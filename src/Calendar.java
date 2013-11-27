//auxiliary class for implementing of XMLRPC=methods

public class Calendar {

    public int addEntry(String s){
        System.out.println("Adding entry..."+ s);
        return 42;
    }

    public void deleteEntry(String s){
        System.out.println("Deleting entry..." + s);
    }

    public void modifyEntry(String s){
        System.out.println("Modifying entry..." + s);
    }

    public String[] getList(){
        String[] s = new String[1];
        return s;
    }
}
