import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class ClientApp {

    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, UnsupportedEncodingException {

    	System.out.println("The starting number is 30 ");
    	PrintWriter writer = new PrintWriter("Calendar.txt", "UTF-8");
    	writer.println("30");
    	
    	writer.close();
       	
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the port:");
        int port = sc.nextInt();
        Thread clientThread = new Thread(new Client(port));
        clientThread.start();
        Thread serverThread = new Thread(new Server(port));
        serverThread.start();

    }
}
