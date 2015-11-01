import java.util.Vector;
//TODO SOME CHANGES CHECK YOURSELF
public class Node {
   public Vector<String> getListOfActiveNode() 
   {
       System.out.println("Returning list of active nodes...");
       Vector<String> result = new Vector<String>();
       for (int i = 0; i < Client.activeNodes.size(); i++) 
       {
            result.add(Client.activeNodes.get(i).getIPnPort());
       }
       //result.add(Client.getOwnIpAddress()); //MJZ MADE THIS AS COMMENT
       System.out.println("Done!");
       return result;
   }

    public boolean add(String IpnPort) //TODO CHANGED
    {
        System.out.println("New node has joined.");
        //Client.activeNodes.add(new HostIPnPort(IpnPort)); //TODO CHANGED
        HostIPnPort newObj = new HostIPnPort(IpnPort);
    	int k=0;
    	while( k<Client.activeNodes.size() && Client.activeNodes.get(k).compare(newObj)<0) k++;                    	
    	Client.activeNodes.add(k,newObj); 
    	//END OF CHANGE SECTION
        return true;
    }

    public boolean delete(String IpnPort) //TODO
    {//TODO IT HAS BEEN CHANGED
        System.out.println("New node has signed off");
        //
        int k=0;
    	while( k<Client.activeNodes.size() && !Client.activeNodes.get(k).getIPnPort().equals(IpnPort)) k++;
    	if(k<Client.activeNodes.size())
    		Client.activeNodes.remove(k);
    	if(Client.activeNodes.size() == 1)
    		TokenRing.stopTokenRingAlgorithm(); //If you are alone you must stop Token Ring
        return true;
    }
}
