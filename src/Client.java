import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable {

    public static ArrayList<HostIPnPort> activeNodes; //HAS CHANGED
    
    private static HostIPnPort thisMachineIpnPort; //TODO ADDED
    private static HostIPnPort firstActiveNode;     //TODO THE TYPE HAS CHANGED . IP-address of the first online node
    
    private boolean isOnline = false;
    public int result;
    public Client(int port) {
        activeNodes = new ArrayList<HostIPnPort>();
        initialize(port);
    }
    
        
    //Token Ring section
    public static HostIPnPort nextHostOnRing()
    {
    	int i = activeNodes.indexOf(thisMachineIpnPort);
    	//make circularity in the ring
    	if(i==-1)
    		return null;
    	else if (i == activeNodes.size()-1) //if the i refers to the last one we must return the first one
    		return activeNodes.get(0);
    	else
    		return activeNodes.get(i+1);	//else we must return next one
    }
    //End Token Ring Section
    @Override
    public void run() {
    	
    	   	
        System.out.println("Own IP Adress: " + thisMachineIpnPort.getIp());
        System.out.println("Do you want to change it? (y\\n)");
        @SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        String newOwnIpAddress = null;
        if (answer.equals("y")){
            System.out.println("Input correct IP: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                newOwnIpAddress = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
        }
        //TODO SOME CHANGE HERE
        if(newOwnIpAddress != null)
        	thisMachineIpnPort.setIp(newOwnIpAddress);
        //
        activeNodes.add(thisMachineIpnPort); //TODO ADDED
       while (true) {
           System.out.println("-----------------------");
           System.out.println("Own IP Address = " + thisMachineIpnPort.getIPnPort()); //TODO
           if (isOnline) {
               System.out.println("Node's status: online");
           } else {
               System.out.println("Node's status: offline");
           }

           if (isOnline) {
        	   System.out.println("List of all online nodes:");
               for (int i = 0; i < activeNodes.size(); i++) {
                   System.out.println(activeNodes.get(i).toString());
               }
           }
           System.out.println("-----------------------");
            System.out.println("Your choice:\n 1 - Addition \n 2 - Subtraction \n" +
                    " 3 - Multiplication \n 4 - Division \n 5 - Show the sequence" +
                    "\n 6 - Join the network \n 7 - Signout \n 8 - Show all the active nodes \n \n 0 - exit ");
           
            
            int choice = sc.nextInt();
            
           
            switch(choice) {
                case 1:
                    System.out.println("===Addition===");
				try {
					additionEntry();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    break; //add
                case 2:
                    System.out.println("===Subtraction===");
				try {
					subtractionEntry();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    break; //add
                case 3:
                    System.out.println("===Multiplication===");
				try {
					multiplicationEntry();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    break; //add
                case 4:
                    System.out.println("===Division===");
				try {
					divisionEntry();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                    break; //add
                case 5:
                    System.out.println("===Getting sequence of all events===");
                    getListOfEvents(false);
                    break; //list
                
                case 6:
                    System.out.println("===Joining the network===");
                    join();
                    break; // join to the network
                case 7:
                    System.out.println("===Signing off===");
                    signOff();
                    break; // sign off the network
                case 8:
                    System.out.println("=== Show all active nodes===");
                    listNodes();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Wrong input!");
            }
        }
    }



    

    private void listNodes() //TODO changed
    { 
        if (isOnline) 
        {
            for (int i = 0; i < activeNodes.size(); i++) 
            {
                System.out.println(activeNodes.get(i).toString());//changed
            }
        } else {
            System.out.println("Node is offline.");
        }
    }

    public void initialize(int ownPort) //TODO SOME CHANGES
    {
    	//TODO SOME CHANGES
        String ownIpAddress = "127.0.0.1";
        //End added section
    	//just set the local ip address & make a new text file if needed
        try {
            ownIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        thisMachineIpnPort = new HostIPnPort(ownIpAddress, ownPort);
        if (new File(System.getProperty("user.dir") + "Calendar.txt").isFile()) {
            //do nothing
        }   else {
            //String filename = "Calendar.txt";
            try {
                FileWriter fw = new FileWriter("Calendar.txt", true);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("resource")
	public void additionEntry() throws Exception{
    	 if (isOnline) {
             
    		 
    		 TokenRing.waitForToken(); //TODO //Wait for having the token
             getListOfEvents(false);
             
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             int b=0;
             String date = null;
                 try {
                 System.out.println("Input the number");
                 date = br.readLine();
                 b = Integer.parseInt(date);
                } catch (IOException e) {
                 System.out.println("IO error!");
                 System.exit(1);
             }
                 
                             
                 BufferedReader reader1 = new BufferedReader(new FileReader("Calendar.txt"));
                 String lastLine = "",sCurrentLine;

                 while ((sCurrentLine = reader1.readLine()) != null) 
                 {
                     lastLine = sCurrentLine;
                 }
                 result = Integer.parseInt(lastLine);
                                  
            
             calculation_entry entry = new calculation_entry(result,b);
             entry.writeToFile_addition(); //TODO here it is added to this machine
         
             for(HostIPnPort h : activeNodes)
             {
             	if(!h.equals(thisMachineIpnPort))
             	{
             		addOverRPC(h.getFullUrl(), entry.makeString());
             	}
             }
             
         TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
         }   else {
             System.out.println("Node is offline! Operation is not allowed");
             }
       }
    public void subtractionEntry() throws Exception{
   	 if (isOnline) {
            
   		 
   		 TokenRing.waitForToken(); //TODO //Wait for having the token
            getListOfEvents(false);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int b=0;
            String date = null;
                try {
                System.out.println("Input the number");
                date = br.readLine();
                b = Integer.parseInt(date);
               } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
                
                            
                @SuppressWarnings("resource")
				BufferedReader reader1 = new BufferedReader(new FileReader("Calendar.txt"));
                String lastLine = "",sCurrentLine;

                while ((sCurrentLine = reader1.readLine()) != null) 
                {
                    lastLine = sCurrentLine;
                }
                result = Integer.parseInt(lastLine);
                                 
           
            calculation_entry entry = new calculation_entry(result,b);
            entry.writeToFile_subtraction(); //TODO here it is added to this machine
        
            for(HostIPnPort h : activeNodes)
            {
            	if(!h.equals(thisMachineIpnPort))
            	{
            		addOverRPC(h.getFullUrl(), entry.makeString());
            	}
            }
            
        TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
        }   else {
            System.out.println("Node is offline! Operation is not allowed");
            }
      }
    
    public void multiplicationEntry() throws Exception{
      	 if (isOnline) {
               
      		 
      		 TokenRing.waitForToken(); //TODO //Wait for having the token
               getListOfEvents(false);
               
               BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
               int b=0;
               String date = null;
                   try {
                   System.out.println("Input the number");
                   date = br.readLine();
                   b = Integer.parseInt(date);
                  } catch (IOException e) {
                   System.out.println("IO error!");
                   System.exit(1);
               }
                   
                               
                   @SuppressWarnings("resource")
				BufferedReader reader1 = new BufferedReader(new FileReader("Calendar.txt"));
                   String lastLine = "",sCurrentLine;

                   while ((sCurrentLine = reader1.readLine()) != null) 
                   {
                       lastLine = sCurrentLine;
                   }
                   result = Integer.parseInt(lastLine);
                                    
              
               calculation_entry entry = new calculation_entry(result,b);
               entry.writeToFile_multiplication(); //TODO here it is added to this machine
           
               for(HostIPnPort h : activeNodes)
               {
               	if(!h.equals(thisMachineIpnPort))
               	{
               		addOverRPC(h.getFullUrl(), entry.makeString());
               	}
               }
               
           TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
           }   else {
               System.out.println("Node is offline! Operation is not allowed");
               }
         }
       
    public void divisionEntry() throws Exception{
     	 if (isOnline) {
              
     		 
     		 TokenRing.waitForToken(); //TODO //Wait for having the token
              getListOfEvents(false);
              
              BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
              int b=0;
              String date = null;
                  try {
                  System.out.println("Input the number");
                  date = br.readLine();
                  b = Integer.parseInt(date);
                 } catch (IOException e) {
                  System.out.println("IO error!");
                  System.exit(1);
              }
                  
                              
                  @SuppressWarnings("resource")
				  BufferedReader reader1 = new BufferedReader(new FileReader("Calendar.txt"));
                  String lastLine = "",sCurrentLine;

                  while ((sCurrentLine = reader1.readLine()) != null) 
                  {
                      lastLine = sCurrentLine;
                  }
                  result = Integer.parseInt(lastLine);
                                   
             
              calculation_entry entry = new calculation_entry(result,b);
              entry.writeToFile_division(); //TODO here it is added to this machine
          
              for(HostIPnPort h : activeNodes)
              {
              	if(!h.equals(thisMachineIpnPort))
              	{
              		addOverRPC(h.getFullUrl(), entry.makeString());
              	}
              }
              
          TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
          }   else {
              System.out.println("Node is offline! Operation is not allowed");
              }
        }
      
    /*
    public void addEntry() {
        if (isOnline) {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String date = null;
            String time = null;
            String duration = null;
            String header = null;
            String comment = null;
            try {
                System.out.println("Input date");
                date = br.readLine();
                System.out.println("Input time");
                time = br.readLine();
                System.out.println("Input duration");
                duration = br.readLine();
                System.out.println("Input header");
                header = br.readLine();
                System.out.println("Input comment");
                comment = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            CalendarEntry entry = new CalendarEntry(date, time, duration, header, comment);
            
            TokenRing.waitForToken(); //TODO //Wait for having the token 
            
            entry.writeToFile(); //TODO here it is added to this machine
           
            for(HostIPnPort h : activeNodes)
            {
            	if(!h.equals(thisMachineIpnPort))
            	{
            		addOverRPC(h.getFullUrl(), entry.makeString());
            	}
            }
            
            TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
        }   else {
            System.out.println("Node is offline! Operation is not allowed");
            }
    }
*/
    public void getListOfEvents(boolean getFromAnotherNode) 
    {
        if (getFromAnotherNode) {   //only when join the network
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            try {
                config.setServerURL(new URL(firstActiveNode.getFullUrl()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[] {};

            try {
                Object[] list =
                        (Object[]) client.execute("Calendar.getList", params);
                PrintWriter out = null;
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(new FileWriter("Calendar.txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer.print("");
                writer.close();

                for (int i = 0; i < list.length; i++) {
                    try {
                        out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
                        out.println(list[i].toString());
                    }  catch (IOException e) {
                        System.err.println(e);
                    }  finally  {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }

        } else {
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
                    System.out.println(line);
               
            	}
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
/*
     
    private void modifyEntry() {
        if (isOnline){
        	TokenRing.waitForToken(); //TODO //Wait for having the token 
        	getListOfEvents(false);
            System.out.println("Choose the number of the event:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String numberOfLine = null;
            try {
                numberOfLine = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }

            int numLine = Integer.parseInt(numberOfLine);

            File file = new File("Calendar.txt");
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String lineToRemove = null;
            CalendarEntry entry = null;
            try {
                while ((numLine--) != 0) {
                    lineToRemove = br.readLine();
                }

                System.out.println("Line to modify:" + lineToRemove);
                br.close();
                
                
                
                br = new BufferedReader(new InputStreamReader(System.in));
                String date = null;
                String time = null;
                String duration = null;
                String header = null;
                String comment = null;
                try {
                    System.out.println("Input date");
                    date = br.readLine();
                    System.out.println("Input time");
                    time = br.readLine();
                    System.out.println("Input duration");
                    duration = br.readLine();
                    System.out.println("Input header");
                    header = br.readLine();
                    System.out.println("Input comment");
                    comment = br.readLine();
                } catch (IOException e) {
                    System.out.println("IO error!");
                    System.exit(1);
                }
                entry = new CalendarEntry(date, time, duration, header, comment);
                
                
                
                String currentLine = null;
                ArrayList<String> lines = new ArrayList<String>();
                br = new BufferedReader(new FileReader(file));
                while ((currentLine = br.readLine()) != null) {
                    if (currentLine.equals(lineToRemove))
                    	lines.add(entry.makeString());//TODO
                    else
                    	lines.add(currentLine);
                }

                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
                
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
                for (int i = 0; i < lines.size(); i++) {
                    out.println(lines.get(i));
                }
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            for(HostIPnPort h : activeNodes)
            {
            	if(!h.equals(thisMachineIpnPort))
            	{
            		modifyOverRPC(h.getFullUrl(), lineToRemove, entry.makeString());
            	}
            }
            TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
        } else {
            System.out.println("Node is offline! Operation is not allowed");
        }
    }

	public void deleteEntry() {
        if (isOnline) {
        	TokenRing.waitForToken(); //TODO //Wait for having the token
            getListOfEvents(false);
            System.out.println("Choose the number of the event:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String numberOfLine = null;
            try {
                numberOfLine = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }

            int numLine = Integer.parseInt(numberOfLine);

            File file = new File("Calendar.txt");
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String lineToRemove = null;
            try {
                while ((numLine--) != 0) {
                    lineToRemove = br.readLine();
                }

                //System.out.println("Line to remove:" + lineToRemove);
                br.close();
                String currentLine = null;
                ArrayList<String> lines = new ArrayList<String>();
                br = new BufferedReader(new FileReader(file));
                while ((currentLine = br.readLine()) != null) {
                    if (currentLine.equals(lineToRemove))continue;
                    lines.add(currentLine);
                }

                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Calendar.txt", true)));
                for (int i = 0; i < lines.size(); i++) {
                    out.println(lines.get(i));
                }
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
           
            for(HostIPnPort h : activeNodes)
            {
            	if(!h.equals(thisMachineIpnPort))
            	{
            		deleteOverRPC(h.getFullUrl(), lineToRemove);
            	}
            }
           
            TokenRing.sendToken(); //TODO //after critical section we will send the token to next one
        } else {
            System.out.println("Node is offline! Operation is not allowed");
        }
    }*/
    @SuppressWarnings("resource")
	public void join() {
        if (!isOnline) {
            isOnline = true;
            System.out.println("Input IP-address of active node");
            System.out.println("<If it is the first online node input 127.0.0.1>");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String ipAddress = null;
            try {
                ipAddress = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            if (!ipAddress.equals("127.0.0.1")) 
            {
            	//TODO set port
            	System.out.println("Enter the port for this machine : ");
            	Scanner input = new Scanner(System.in);
            	String str = input.nextLine();
            	int port = Integer.parseInt(str);
                //
            	firstActiveNode = new HostIPnPort(ipAddress, port);
            	
                getListOfEvents(true); //TODO COMMENTED
                XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                try 
                {
                    //config.setServerURL(new URL(stringToURL(firstActiveNode)));//TODO
                	config.setServerURL(new URL(firstActiveNode.getFullUrl())); //TODO ADDED
                } 
                catch (MalformedURLException e) 
                {
                    e.printStackTrace();
                }
                XmlRpcClient client = new XmlRpcClient();
                client.setConfig(config);

                Object[] params = new Object[] {};
                try {

                    Object[] listOfNodes = (Object[]) client.execute("Node.getListOfActiveNode", params);

                    for (int i = 0; i < listOfNodes.length; i++) 
                    {
                        //activeNodes.add(listOfNodes[i].toString()); //TODO COMMENTED
                    	//TODO CHANGED
                    	HostIPnPort newObj = new HostIPnPort(listOfNodes[i].toString());
                    	int k=0;
                    	while( k<activeNodes.size() && activeNodes.get(k).compare(newObj)<0) k++;                    	
                    	activeNodes.add(k,newObj); 
                    	//eND OF THE CHANGE SECTION
                    }
                } catch (XmlRpcException e) {
                    e.printStackTrace();
                }
                /*//TODO COMMENTED
                for (String s:activeNodes) {
                    joinOverRPC(s);
                }*/
                //TODO HAS ADDED
                boolean tokenRingStarter = false;
                if(activeNodes.size()==2)
                	tokenRingStarter = true;
                for (HostIPnPort h:activeNodes) 
                {
                	if(!h.equals(thisMachineIpnPort))
                		joinOverRPC(h.getFullUrl());
                }
                if(tokenRingStarter)
                	TokenRing.startTokenRingAlgorithm(); //for the time that you are starter of the token ring
                if(activeNodes.size()>2)
                	TokenRing.initiateTokenRing(); //for the time that token does not rotating in the Network Ring and it need to be monitored by this client
            } else {
            	
            	// Clean the file and put the starting number
            	           	
            	isOnline = true;
            }
        } else  {
            System.out.println("Node is already online.");
        }
    }

    public void signOff() {
        if (isOnline) {
            isOnline = false;
            /*//TODO COMMENTED
            for (String s:activeNodes) {
                signOffOverRPC(s);
            }
            */
            //TODO HAS ADDED
            for (HostIPnPort h:activeNodes) 
            {
            	if(!h.equals(thisMachineIpnPort))
            		signOffOverRPC(h.getFullUrl());
            }
            TokenRing.stopTokenRingAlgorithm(); //TODO //Token Ring Stopped here
            //
            activeNodes.clear();
            activeNodes.add(thisMachineIpnPort);//TODO ADDED // WE NEED THIS BECAUSE WE MUST BE IN LIST FOR REJOINING
        }  else {
            System.out.println("Node is already offline.");
        }
    }

    public void signOffOverRPC(String fullUrl) { // TODO has changed 
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(fullUrl));// TODO has changed 
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            //Object[] params = new Object[]{ownIpAddress}; // TODO commented 
            Object[] params = new Object[]{thisMachineIpnPort.getIPnPort()}; //TODO added
            client.execute("Node.delete", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addOverRPC(String fullUrl, String message) { //TODO HAS CHANGED THE NAME
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(fullUrl)); //TODO HAS CHANGED
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{message};
            client.execute("Calendar.result_update", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void joinOverRPC(String fullURL) 
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try 
        {
            //config.setServerURL(new URL(stringToURL(ipAddress)));//TODO COMMENTED
        	config.setServerURL(new URL(fullURL));//TODO ADDED
        	
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            //Object[] params = new Object[]{ownIpAddress};//TODO COMMENTED
            Object[] params = new Object[]{thisMachineIpnPort.getIPnPort()};//TODO added
            client.execute("Node.add", params);
        } catch (XmlRpcException e) 
        {
            System.out.println("XML RPC threw an exception."); //TODO changed
        } catch (MalformedURLException e) {
        	System.out.println("The url was wrong."); //TODO changed
        }
    }
    public void deleteOverRPC(String fullUrl, String message) //TODO HAS CHANGED
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {

            config.setServerURL(new URL(fullUrl)); //TODO HAS CHANGED   

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[]{message};

            client.execute("Calendar.deleteEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    /*
    private void modifyOverRPC(String fullUrl, String lineToRemove, String newData) {
    	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {

            config.setServerURL(new URL(fullUrl)); //TODO HAS CHANGED   

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[]{lineToRemove, newData};

            client.execute("Calendar.modifyEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
		
	}
*/
}
