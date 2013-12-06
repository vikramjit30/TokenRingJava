import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 11/26/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("It works.");
        System.out.println(System.getProperty("user.dir"));
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int numLine = 4;
        File file = new File("Calendar.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            while ((numLine--) != 0) {
                line = br.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(line);
    }
}
