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
        Client client = new Client();
        System.out.println(client.StringtoURL("192.168.55.32"));
    }
}
