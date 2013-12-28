import java.util.Vector;

public class Node {
   public Vector getListOfActiveNode() {
       Vector result = new Vector();
       for (int i = 0; i < Client.activeNodes.size(); i++) {
            result.add(Client.activeNodes.get(i));
       }
       result.add(Client.ownIpAddress);
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
