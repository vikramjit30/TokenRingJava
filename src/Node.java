import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class Node {
   public Vector getListOfActiveNode() {
        //1. Read  from file Nodes.txt              done
        //2. Create an array of Strings from lines      done
        //3. Create an array of Objects and copy        done
        //4. Return an array of Objects                 done
       Vector result = new Vector();
       for (int i = 0; i < Client.activeNodes.size(); i++) {
            result.add(Client.activeNodes.get(i));
       }
       try {
           result.add(InetAddress.getLocalHost().getHostAddress());
       } catch (UnknownHostException e) {
           e.printStackTrace();
       }

//       File file = new File("Nodes.txt");
//       BufferedReader br = null;
//       try {
//           br = new BufferedReader(new FileReader(file));
//       } catch (FileNotFoundException e) {
//           e.printStackTrace();
//       }
//       String line;
//       try {
//           while ((line = br.readLine()) != null) {
//               result.add(line);
//           }
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//       try {
//           br.close();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
       return result;
   }

    public boolean add(String ipAddress) {
        Client.activeNodes.add(ipAddress);
        return true;
    }

    public boolean delete(String ipAddress) {
        Client.activeNodes.remove(ipAddress);
        return true;
    }
}
