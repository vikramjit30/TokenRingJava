import java.util.Vector;

public class Node {
   public Vector getListOfActiveNode() {
       System.out.println("Returning list of active nodes...");
       Vector result = new Vector();
       for (int i = 0; i < Client.activeNodes.size(); i++) {
            result.add(Client.activeNodes.get(i));
       }
       result.add(Client.getOwnIpAddress());
       System.out.println("Done!");
       return result;
   }

    public boolean add(String ipAddress) {
        System.out.println("New node has joined.");
        Client.activeNodes.add(ipAddress);
        return true;
    }

    public boolean delete(String ipAddress) {
        System.out.println("New node has signed off");
        Client.activeNodes.remove(ipAddress);
        return true;
    }
}
