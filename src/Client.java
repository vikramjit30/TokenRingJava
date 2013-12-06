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
                    " 3 - delete an event \n 4 - join the network \n 5 - sign off" +
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
                    join();
                    break; // join to the network
                case 5:
                    System.out.println("Input is 5");
                    signOff();
                    break; // sign off the network
                case 6:
                    listNodes();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    System.out.println("No input!");
            }
        }
    }

    private void listNodes() {
        for (int i = 0; i < activeNodes.size(); i++) {
            System.out.println(activeNodes.get(i));
        }
    }

    public void initialize() {
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
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(new FileWriter("Calendar.txt"));
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                writer.print("");
                writer.close();
                for (int i = 0; i < list.length; i++) {
                    try {

                        System.out.println("List length:" + list.length);
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
        System.out.println("Which entry do you want to delete? Input the number.");
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

            System.out.println(lineToRemove);
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
        if (!ipAddress.equals("127.0.0.1")){
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

    }
    public void signOff() {
        if (isOnline) {
            isOnline = false;
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

    public void joinOverRPC(String ipAddress) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(stringToURL(ipAddress)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        Object[] params = new Object[]{getOwnIp()};
        try {
            client.execute("Node.add", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }
    public void deleteOverRPC(String ipAddress, String message) {
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
            client.execute("Calendar.deleteEntry", params);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
    }

    public String getOwnIp() {
        String ownIpAddress = null;
        try {
            ownIpAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
            e.printStackTrace();
            }
        return ownIpAddress;
    }

}
