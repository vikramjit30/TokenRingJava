import java.io.*;
import java.util.Vector;

public class Node {
   public Vector GetListOfActiveNode(){
        //1. Read  from file Nodes.txt              done
        //2. Create an array of Strings from lines      done
        //3. Create an array of Objects and copy        done
        //4. Return an array of Objects                 done
       Vector result = new Vector();
       File file = new File("Nodes.txt");
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
       return result;
   }

    public boolean Add(String IpAddress){
        //1. Add a new line to Nodes.txt
       // Client.
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("Nodes.txt", true)));
            out.println(IpAddress);
        }catch (IOException e) {
            System.err.println(e);
        }finally{
            if(out != null){
                out.close();
            }
        }
        return true;
    }

    public boolean Delete (String IpAddress){
        //1. Search in lines
        //2. Delete line with IpAddress
        return true;
    }
}
