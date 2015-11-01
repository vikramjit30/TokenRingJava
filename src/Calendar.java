//auxiliary class for implementing of XMLRPC-methods

import java.io.*;
import java.util.Vector;

public class Calendar {

	public int result_update(String result)
	{
	System.out.println(" Result is updating..");
	PrintWriter out = null;
    try {
        out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
        out.println(result);
    } catch (IOException e) {
        System.err.println(e);
    } finally {
        if (out != null) {
            out.close();
        }
    }
        System.out.println("Updation Done!");
	     return 42;
	}
    
	    public Vector<String> getList() {
        System.out.println("Returning list of all events...");
        Vector<String> result = new Vector<String>();
        File file = new File("Calendar.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("List Passed!");
        return result;
    }


}
