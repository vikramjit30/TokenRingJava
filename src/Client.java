import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.Scanner;

public class Client implements Runnable{
    @Override
    public void run() {

        System.out.println("Client starts");
        test();
        System.out.println("Your choice: 1 - add an event /n 2 - list of all events /n1");
        while(true){
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Input is 1");break; //add
                case 2:
                    System.out.println("Input is 2");break; //list
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
            config.setServerURL(new URL("http://127.0.0.1:8764/xmlrpc"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{new Integer(33), new Integer(20)};
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
//          Object[] params2 = new Object[]{"example"};
//          String b = (String) client.execute("CalendarReceive.read", params2);
//          System.out.println("if the result equals to \"test\" it works right: " + b);
            }
        catch (Exception exception) {
            System.err.println("Client: " + exception.toString());
        }
    }
    public void initialize(){
        // 1. parse IP address as argument
        // 1. create file
        //
    }

}
