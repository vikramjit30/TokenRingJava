import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.Scanner;

public class Client implements Runnable{

    private String activeNode;     //IP-address of the first online node

    public Client(String activeNode) {
        this.activeNode = StringtoURL(activeNode);
    }

    public Client() {
    }

    @Override
    public void run() {

        System.out.println("Client starts");
        initialize();

        test();
        System.out.println("Your choice:\n 1 - add an event \n 2 - list of all events \n" +
                " 3 - modify an event \n 4 - delete an event \n 0 - exit");
        while(true){
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Input is 1");addEntry(); break; //add
                case 2:
                    System.out.println("Input is 2"); getListOfEvents(); break; //list
                case 3:
                    System.out.println("Input is 3"); break; //remove entry
                case 4:
                    System.out.println("Input is 4"); break; // modify entry
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
            config.setServerURL(new URL(activeNode));
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
            Object[] params2 = new Object[]{"example"};
            int a = (Integer) client.execute("Calendar.addEntry", params2);
            System.out.println("if the result equals to 42 it works right: " + a);
            }
        catch (Exception exception) {
            System.err.println("Client: " + exception.toString());
        }
    }
    public void initialize(){
        //To do:
        // 1. parse IP address as argument  ... done!
        // 2. create file...
        // 3. call function get_all events from the IP-address
        // 4. put all events in file
        // 5. get the list of all online nodes from IP_address
        // 6. sout (Ready to use!)
        // 7. show a menu
    }

    public String StringtoURL(String s){
        return ("http://" + s + ":8764/xmlrpc");
    }

    public String addEntry(){
        //1. code for reading an entry from standard input and
        //2. creating an object CalendarEntry
        //3. CalendarEntry.
        return null;
    }

    public void getListOfEvents(){
        //1. reading from file line by line
        //2. output each line
    }

}
