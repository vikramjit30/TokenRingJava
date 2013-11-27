
public class ClientApp {
    public static void main(String[] args) {
        Thread serverThread = new Thread(new Server());
        serverThread.start();
        System.out.println(args[0]);
        if (args.length == 0){
            Thread clientThread = new Thread(new Client());
            clientThread.start();
        } else if(args.length == 1){
            Thread clientThread = new Thread(new Client(args[0]));
            clientThread.start();
        } else{
            System.out.println("Input error!");
            System.exit(0);
        }





    }
}
