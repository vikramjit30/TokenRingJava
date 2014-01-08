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

    public static ArrayList<String> activeNodes;

    private static String ownIpAddress;

    private static  int PORT;
    private String firstActiveNode;     //IP-address of the first online node
    private boolean isOnline = false;

    public Client() {
        activeNodes = new ArrayList<String>();
    }

    @Override
    public void run() {

       initialize();

        System.out.println("Own IP Adress: " + ownIpAddress);
        System.out.println("Do you want to change it? (y\\n)");
        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();
        if (answer.equals("y")){
            System.out.println("Input correct IP: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            try {
                ownIpAddress = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
        }


       while (true) {
           System.out.println("-----------------------");
           System.out.println("Own IP Address = " + ownIpAddress + ":" + PORT);
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
            System.out.println("Your choice:\n 1 - add an event \n 2 - list of all events \n" +
                    " 3 - delete an event \n 4 - join the network \n 5 - sign off" +
                    "\n 6 - list of all active nodes \n 7 - modify entry \n 0 - exit ");

            int choice = sc.nextInt();
            switch(choice) {
                case 1:
                    System.out.println("===Adding a new event===");
                    addEntry();
                    break; //add
                case 2:
                    System.out.println("===Getting list of all events===");
                    getListOfEvents(false);
                    break; //list
                case 3:
                    System.out.println("===Deleting an event===");
                    deleteEntry();
                    break; //remove entry
                case 4:
                    System.out.println("===Joining the network===");
                    join();
                    break; // join to the network
                case 5:
                    System.out.println("===Signing off===");
                    signOff();
                    break; // sign off the network
                case 6:
                    System.out.println("===Getting list of all active nodes===");
                    listNodes();
                    break;
                case 7:
                    System.out.println("Test");
                    modifyEntry();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("No input!");
            }
        }
    }


    public static String getOwnIpAddress() {
        return ownIpAddress;
    }

    public static int getPort() {
        return PORT;
    }

    public static void setPORT(int port) {
        Client.PORT = port;
    }

    private void listNodes() {
        if (isOnline) {
            for (int i = 0; i < activeNodes.size(); i++) {
                System.out.println(activeNodes.get(i));
            }
        } else {
            System.out.println("Node is offline.");
        }
    }

    public void initialize() {
        try {
            ownIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

    public String stringToURL(String s) {
        return ("http://" + s + ":" + Integer.toString(PORT) + "/xmlrpc");
    }

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
            CalendarEntry entry =
                    new CalendarEntry(date, time, duration, header, comment);
            entry.writeToFile();
            //1. code for reading an entry from standard input and   ... done!
            //2. creating an object CalendarEntry    ....done!
            //3. Write to file an entry.              ...done!
            //4. send a message to all active nodes about adding Entry
            //activeNodes.add(firstActiveNode);
            for (String s:activeNodes) {
                addOverRPC(s, entry.makeString());
            }
        }   else {
            System.out.println("Node is offline! Operation is not allowed");
            }
    }

    public void getListOfEvents(boolean getFromAnotherNode) {
        if (getFromAnotherNode) {   //only when join the network
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            try {
                config.setServerURL(new URL(stringToURL(firstActiveNode)));
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

    private void modifyEntry() {
        if (isOnline){
            deleteEntry();
            addEntry();
        } else {
            System.out.println("Node is offline! Operation is not allowed");
        }
    }

    public void deleteEntry() {
        if (isOnline) {
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
            for (String s:activeNodes) {
                deleteOverRPC(s, lineToRemove);
            }
        } else {
            System.out.println("Node is offline! Operation is not allowed");
        }


    }
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
            if (!ipAddress.equals("127.0.0.1")) {
                firstActiveNode = ipAddress;
                getListOfEvents(true);
                XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                try {
                    config.setServerURL(new URL(stringToURL(firstActiveNode)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                XmlRpcClient client = new XmlRpcClient();
                client.setConfig(config);

                Object[] params = new Object[] {};
                try {

                    Object[] listOfNodes = (Object[]) client.execute("Node.getListOfActiveNode", params);

                    for (int i = 0; i < listOfNodes.length; i++) {
                        activeNodes.add(listOfNodes[i].toString());
                    }
                } catch (XmlRpcException e) {
                    e.printStackTrace();
                }
                for (String s:activeNodes) {
                    joinOverRPC(s);
                }
            } else {
                isOnline = true;
            }
        } else  {
            System.out.println("Node is already online.");
        }
    }

    public void signOff() {
        if (isOnline) {
            isOnline = false;
            for (String s:activeNodes) {
                signOffOverRPC(s);
            }
            activeNodes.clear();
        }  else {
            System.out.println("Node is already offline.");
        }
    }

    public void signOffOverRPC(String ipAddress) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(stringToURL(ipAddress)));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{ownIpAddress};
            client.execute("Node.delete", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addOverRPC(String ipAddress, String message) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(stringToURL(ipAddress)));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{message};
            client.execute("Calendar.addEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void joinOverRPC(String ipAddress) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(stringToURL(ipAddress)));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{ownIpAddress};
            client.execute("Node.add", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public void deleteOverRPC(String ipAddress, String message) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {

            config.setServerURL(new URL(stringToURL(ipAddress)));     //была только этастрочка в try

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

}
