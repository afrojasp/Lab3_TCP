package UDP;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadServidor extends Thread
{
	private DatagramSocket sktCliente;
	private String archivo;
	private InetAddress IPAddress;
	private int port;
	private int id;
	
	public ThreadServidor(DatagramSocket socket, int pId, String archivo, InetAddress IPAddressPar, int portPar) 
	{
		this.sktCliente = socket;
		this.id = pId;
		this.archivo = archivo;
		this.IPAddress = IPAddressPar;
		this.port = portPar;
	}
	
	public void run() 
	{
		System.out.println("THREADSERVIDOR: " + this.id);
		
		try 
		{
			protocoloServidor.procesoServidor(sktCliente, archivo, IPAddress, port);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
