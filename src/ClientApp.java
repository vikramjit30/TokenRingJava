
public class ClientApp {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server());
        System.out.println(args[0]);
        Thread clientThread = new Thread(new Client(args[0]));
        serverThread.start();
        clientThread.start();


    }
}
