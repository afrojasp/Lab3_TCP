package TCP;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServidor extends Thread 
{	
	private Socket sktCliente;
	private String archivo;
	private int id;
	private PrintWriter escritor;
	private BufferedReader lector;
	
	public ThreadServidor(Socket socket, int pId, String archivo, PrintWriter escritor, BufferedReader lector) 
	{
		this.sktCliente = socket;
		this.id = pId;
		this.archivo = archivo;
		this.escritor = escritor;
		this.lector = lector;
	}
	
	public void run() 
	{
		System.out.println("THREADSERVIDOR: " + this.id);
		
		try 
		{
			OutputStream os = sktCliente.getOutputStream();
			protocoloServidor.procesoServidor(os, escritor, lector, archivo);
			sktCliente.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
}
