
public class HostIPnPort 
{
	private String Ip;
	private int port;
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public HostIPnPort(String ip, int port) {
		super();
		Ip = ip;
		this.port = port;
	}
	public HostIPnPort(String IpnPort) {
		super();
		String[] obj = IpnPort.split(":");
		this.Ip = obj[0];
		this.port = Integer.parseInt(obj[1]);
	}
	public String getIPnPort()
	{
		return this.Ip+":"+this.port;
	}
	public String getFullUrl()
	{
		return "http://"+this.Ip+":"+this.port+"/xmlrpc";
	}
	@Override
	public String toString() {
		return "http://"+this.Ip+":"+this.port+"/";
	}
	public long getHostId()
	{
		String[] parts = this.Ip.split("\\.");
		String ID = "";
		for(int i=0;i<parts.length;i++)
			ID += parts[i];
		ID += port;
		return Long.parseLong(ID);
	}
	public int compare(HostIPnPort obj2)
	{
		if(this.getHostId()<obj2.getHostId())
			return -1;
		if(this.getHostId()==obj2.getHostId())
			return 0;
		else
			return 1;
	}
}
