
public class TokenRing 
{
	private static TokenRing instance=new TokenRing(); //just for synchronization locking
	private static boolean tokenRingExistance = false;
	private static boolean haveToken = false;
	private static boolean wantToken = false;
	
	protected static void initiateTokenRing() 
	{
		if(!tokenRingExistance)
		{
			tokenRingExistance = true;
			haveToken = false;
			wantToken = false;
		}
	}
	protected static void startTokenRingAlgorithm()
	{
		System.out.println("### Token Ring running ###");
		tokenRingExistance = true;
		haveToken = false;
		wantToken = false;
		sendToken();
	}
	protected static void  stopTokenRingAlgorithm()
	{
		System.out.println("### Token Ring stopped ###");
		tokenRingExistance = false;
		haveToken = true;
		wantToken = false;
	}
	protected static void waitForToken()
	{
		System.out.println("### Waiting for receiving token ###");
		if(!tokenRingExistance)
		{
			System.out.println("### Token Ring is not running ###");
			return;
		}
		else
		{
			wantToken = true;
			boolean flag = false;
			while(!flag)
			  synchronized(instance)
				{
					flag = haveToken;
				}
		}
		
	}
	protected static void sendToken() 
	{
		if(tokenRingExistance)
		{
			haveToken = false;
			wantToken = false;
			new TokenSender(Client.nextHostOnRing());
		}		
	}
	public int takeTheToken(int ack)
	{
		tokenRingExistance = true;
		synchronized(instance)
		{
			haveToken = true;
		}	
		if(!wantToken)
			sendToken();
		return ack+1;
	}
	
	
	
	
	
}
