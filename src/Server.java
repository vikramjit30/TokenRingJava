import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.io.IOException;


public class Server implements Runnable {

	//TODO 
	private int port;

	public Server(int port)
	{

		this.port = port;
	}
	//END CHANGE
    @Override
    public void run() {
        WebServer webServer = new WebServer(this.port); //TODO

        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

        PropertyHandlerMapping phm = new PropertyHandlerMapping();

        try {
            phm.addHandler("Calendar", Calendar.class);
            phm.addHandler("Node", Node.class);
            phm.addHandler("TokenRing", TokenRing.class);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

        xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl serverConfig =
                (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        try {
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
