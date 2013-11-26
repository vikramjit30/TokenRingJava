import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.util.Scanner;

public class Client implements Runnable{
    @Override
    public void run() {
        try{

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

            config.setServerURL(new URL("http://127.0.0.1:8764/xmlrpc"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            Object[] params = new Object[]{new Integer(33), new Integer(9)};
            Integer result = (Integer) client.execute("Calculator.add", params);
            System.out.println(result);
            String a = (String) client.execute("Calculator.toString", params);
            System.out.println(a + "\n It works if we have 51");


//            //test for sending String to the server
//            params[0] = new Integer(42);
//            params[1] = new String("test");
//
//            a = (String) client.execute("CalendarWrite.Test", params);
//            System.out.println("Response: " + a);
//
//            //test for sending CalendarEntry
////            params[0] = new CalendarEntry(123, "name");
//
////            params[1] = new CalendarEntry(1234, "name2");
////            a = (String) client.execute("Calendar.join", params);
//            //failed. Only standard types
//
//            //testing reading an entry from File
//            Object[] params2 = new Object[]{"example"};
//            String b = (String) client.execute("CalendarReceive.read", params2);
//            System.out.println("if the result equals to \"test\" it works right: " + b);

        }
        catch (Exception exception) {
            System.err.println("Client: " + exception.toString());
        }
        System.out.println("Client have done his work");
    }

}
