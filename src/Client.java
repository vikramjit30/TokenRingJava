import org.apache.xmlrpc.XmlRpcException;
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
    private String firstActiveNode;     //IP-address of the first online node

    private boolean isOnline = false;



    public Client() {
        activeNodes = new ArrayList<String>();
        //some code
    }

    @Override
    public void run() {

        System.out.println("Client starts");
        initialize();

        //test();

        while (true) {
            System.out.println("Your choice:\n 1 - add an event \n 2 - list of all events \n" +
                    " 3 - modify an event \n 4 - delete an event \n 5 - join the network \n 6 - sign off" +
                    "\n 0 - exit ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch(choice) {
                case 1:
                    System.out.println("Input is 1");
                    addEntry();
                    break; //add
                case 2:
                    System.out.println("Input is 2");
                    getListOfEvents(false);
                    break; //list
                case 3:
                    System.out.println("Input is 3");
                    deleteEntry();
                    break; //remove entry
                case 4:
                    System.out.println("Input is 4");
                    modifyEntry();
                    break; // modify entry
                case 5:
                    System.out.println("Input is 5");
                    join();
                    break; // join to the network
                case 6:
                    System.out.println("Input is 6");
                    signOff();
                    break; // sign off the network
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("No input!");
            }
        }
    }
//    public void test(){
//        try{
//
//            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//            config.setServerURL(new URL(stringToURL(firstActiveNode)));
//            XmlRpcClient client = new XmlRpcClient();
//            client.setConfig(config);
//
//            Object[] params = new Object[]{new Integer(33), new Integer(100)};
//            System.out.println("Test: get result from the server(Calculator.add)");
//            Integer result = (Integer) client.execute("Calculator.add", params);
//            System.out.println(result);
//         // test for sending String to the server
//          params[0] = new Integer(42);
//          params[1] = new String("test");
//
//          a = (String) client.execute("CalendarWrite.Test", params);
//          System.out.println("Response: " + a);
//      //  test for sending CalendarEntry
//           params[0] = new CalendarEntry(123, "name");
//          params[1] = new CalendarEntry(1234, "name2");
//          a = (String) client.execute("Calendar.join", params);
//         // failed. Only standard types
//
//          testing reading an entry from File
//            Object[] params2 = new Object[]{"example"};
//            int a = (Integer) client.execute("Calendar.addEntry", params2);
//            System.out.println("if the result equals to 42 it works right: " + a);
//            }
//        catch (Exception exception) {
//            System.err.println("Client: " + exception.toString());
//        }
//    }
    public void initialize() {
        //To do:
        // 1. parse IP address as argument  ... done!
        // 2. create file...             ....done!
        // 3. call function get_all events from the IP-address   ..done!
        // 4. put all events in file
        // 5. get the list of all online nodes from IP_address
        //
        if (new File(System.getProperty("user.dir") + "Calendar.txt").isFile()) {
            //do nothing
        }   else {
            String filename = "Calendar.txt";
            try {
                FileWriter fw = new FileWriter(filename, true);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String stringToURL(String s) {
        return ("http://" + s + ":8764/xmlrpc");
    }

    public void addEntry() {
        if (isOnline) {
            System.out.println("Input date");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String date = null;
            try {
                date = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            System.out.println("Input time");
            String time = null;
            try {
                time = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            System.out.println("Input duration");
            String duration = null;
            try {
                duration = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            System.out.println("Input header (must be unique)");
            String header = null;
            try {
                header = br.readLine();
            } catch (IOException e) {
                System.out.println("IO error!");
                System.exit(1);
            }
            System.out.println("Input comment");
            String comment = null;
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void deleteEntry() {
        getListOfEvents(false);
        System.out.println("Which entry do you want to delete?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String numberOfLine = null;
        try {
            numberOfLine = br.readLine();
        } catch (IOException e) {
            System.out.println("IO error!");
            System.exit(1);
        }


        //1. code for reading an entry from standard input ..done!
        //2. find in our text file   ..done
        //3. send a message to all active nodes about deleting Entry
    }
    public void join() {
        isOnline = true;
        System.out.println("Input IP-address of active node");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String ipAddress = null;
        try {
            ipAddress = br.readLine();
        } catch (IOException e) {
            System.out.println("IO error!");
            System.exit(1);
        }
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
            Object[] listOfNodes =
                    (Object[]) client.execute("Node.getListOfActiveNode", params);
            PrintWriter out = null;
            for (int i = 0; i < listOfNodes.length; i++) {
                activeNodes.add(listOfNodes[i].toString());
            }
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        for (String s:activeNodes) {
            joinOverRPC();
        }
    }
    public void signOff() {
        if (isOnline) {
            isOnline = false;
            //ArrayList <String> activeNodes
            //get ipadress        ..done!
            //get list of active nodes   ..done!
            //call function from all nodes Node.delete

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            try {
                config.setServerURL(new URL(stringToURL(firstActiveNode)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{getOwnIp()};
            try {
                client.execute("Node.delete", params);
            } catch (XmlRpcException e) {
                e.printStackTrace();
            }
        }  else {
            System.out.println("Node is offline! Operation is not allowed");
        }

    }
    public void modifyEntry() {
        //some code
    }
    public void addOverRPC(String ipAddress, String message) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(stringToURL(ipAddress)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Object[] params = new Object[]{message};
        try {
            client.execute("Calendar.addEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    public void joinOverRPC(){
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        Object[] params = new Object[]{getOwnIp()};
        try {
            client.execute("Node.add", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    public String getOwnIp(){
        String ownIpAddress = null;
        try {
            ownIpAddress = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ownIpAddress;
    }
}
