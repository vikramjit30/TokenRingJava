
public class ClientApp {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server());
        Thread clientThread = new Thread(new Client());
        serverThread.start();
        clientThread.start();


    }
}
