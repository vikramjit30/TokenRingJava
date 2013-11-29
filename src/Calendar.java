//auxiliary class for implementing of XMLRPC-methods

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Calendar {

    public int addEntry(String s){
        System.out.println("Adding entry...Done!");
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
            out.println(s);
        }catch (IOException e) {
            System.err.println(e);
        }finally{
            if(out != null){
                out.close();
            }
        }
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
