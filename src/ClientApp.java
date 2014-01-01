import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Input the port:");
        Client.setPORT(sc.nextInt());
        Thread clientThread = new Thread(new Client());
        clientThread.start();
        Thread serverThread = new Thread(new Server());
        serverThread.start();

    }
}
