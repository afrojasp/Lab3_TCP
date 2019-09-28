package TCP;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServidor extends Thread {
	
	private Socket sktCliente;
	private int archivo;
	private int id;
	private PrintWriter escritor;
	private BufferedReader lector;
	
	public ThreadServidor(Socket socket, int pId, int archivo, PrintWriter escritor, BufferedReader lector) {
		this.sktCliente = socket;
		this.id = pId;
		this.archivo = archivo;
		this.escritor = escritor;
		this.lector = lector;
	}
	
	public void run() {
		System.out.println("Inicia un nuevo ThreadServidor: " + this.id);
		
		try {
			OutputStream os = sktCliente.getOutputStream();
			if (archivo == 1) {
				//sktCliente.setSoTimeout(800000000);
				protocoloServidor.procesoServidor(os, protocoloServidor.IMAGEN, escritor, lector);
				sktCliente.close();
			}
			else {
				protocoloServidor.procesoServidor(os, protocoloServidor.TEXTO, escritor, lector);
				sktCliente.close();
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
