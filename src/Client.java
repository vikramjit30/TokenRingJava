import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable{

    private String firstActiveNode;     //IP-address of the first online node
    private ArrayList<String> activeNodes;   //we should use the separate class for that!
    private boolean isOnline = false;

    public Client(String firstActiveNode) {
        this.firstActiveNode = firstActiveNode;
        activeNodes = new ArrayList<String>();
        activeNodes.add(firstActiveNode);
        isOnline = true;
    }

    public Client() {
        activeNodes = new ArrayList<String>();
        //some code
    }

    @Override
    public void run() {

        System.out.println("Client starts");
        initialize();

        test();

        while(true){
            System.out.println("Your choice:\n 1 - add an event \n 2 - list of all events \n" +
                    " 3 - modify an event \n 4 - delete an event \n 5 - join the network \n 6 - sign off" +
                    "\n 0 - exit ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Input is 1"); addEntry(); break; //add
                case 2:
                    System.out.println("Input is 2"); getListOfEvents(); break; //list
                case 3:
                    System.out.println("Input is 3"); deleteEntry(); break; //remove entry
                case 4:
                    System.out.println("Input is 4"); modifyEntry(); break; // modify entry
                case 5:
                    System.out.println("Input is 5");  join(); break;// join to the network
                case 6:
                    System.out.println("Input is 6"); signOff(); break; // sign off the network
                case 0:
                    System.exit(0);
                default:
                    System.out.println("No input!");
            }
        }
    }



    public void test(){
        try{

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(StringtoURL(firstActiveNode)));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[]{new Integer(33), new Integer(100)};
            System.out.println("Test: get result from the server(Calculator.add)");
            Integer result = (Integer) client.execute("Calculator.add", params);
            System.out.println(result);
//          test for sending String to the server
//          params[0] = new Integer(42);
//          params[1] = new String("test");
//
//          a = (String) client.execute("CalendarWrite.Test", params);
//          System.out.println("Response: " + a);
//          test for sending CalendarEntry
////        params[0] = new CalendarEntry(123, "name");
//          params[1] = new CalendarEntry(1234, "name2");
//          a = (String) client.execute("Calendar.join", params);
//          failed. Only standard types
//
//          testing reading an entry from File
//            Object[] params2 = new Object[]{"example"};
//            int a = (Integer) client.execute("Calendar.addEntry", params2);
//            System.out.println("if the result equals to 42 it works right: " + a);
            }
        catch (Exception exception) {
            System.err.println("Client: " + exception.toString());
        }
    }
    public void initialize(){
        //To do:
        // 1. parse IP address as argument  ... done!
        // 2. create file...             ....done!
        // 3. call function get_all events from the IP-address
        // 4. put all events in file
        // 5. get the list of all online nodes from IP_address
        // 6. sout (Ready to use!)
        // 7. show a menu
        if (new File(System.getProperty("user.dir") + "Calendar.txt").isFile()){
            //do nothing
        }   else {
            String filename= "Calendar.txt";
            try {
                FileWriter fw = new FileWriter(filename,true);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public String StringtoURL(String s){
        return ("http://" + s + ":8764/xmlrpc");
    }

    public void addEntry(){
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
        System.out.println("Input header");
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
        CalendarEntry entry = new CalendarEntry(date, time, duration, header, comment);
        entry.writeToFile();
        //1. code for reading an entry from standard input and   ... done!
        //2. creating an object CalendarEntry    ....done!
        //3. Write to file an entry.              ...done!
        //4. send a message to all active nodes about adding Entry       ...in process!
        activeNodes.add(firstActiveNode);
        for (String s:activeNodes){
           addOverRPC(s, entry.makeString());
        }
    }

    public void getListOfEvents(){
        if(isOnline){
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            try {
                config.setServerURL(new URL(StringtoURL(firstActiveNode)));
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] params = new Object[] {};
            try {
               String s = (String) client.execute("Calendar.getList", params);
                } catch (XmlRpcException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            File file = new File("Calendar.txt");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //1. reading from file line by line
        //2. output each line
    }
    public void deleteEntry(){
        //1. code for reading an entry from standard input
        //2. find in our text file
        //3. send a message to all active nodes about deleting Entry
    }
    public void join(){
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
        getListOfEvents();
    }
    public void signOff(){
        //some code
        isOnline = false;
    }
    public void modifyEntry() {
        //some code
    }

    public void addOverRPC(String IpAddress, String message){
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(StringtoURL(IpAddress)));
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Object[] params = new Object[]{message};
        try {
            client.execute("Calendar.addEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
