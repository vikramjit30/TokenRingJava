
public class ClientApp {
    public static void main(String[] args) {
        System.out.println("It works");
        Thread serverThread = new Thread(new Server());
        Thread clientThread = new Thread(new Client());
        serverThread.start();
        clientThread.start();


    }
}
