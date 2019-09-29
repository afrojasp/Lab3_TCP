package TCP;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Tcp_server 
{
	public static final int PUERTO = 8000;
	public static final int COMM = 4000; 
	public static final int BUFFER = 1000; 
	
	public static void main(String[] args) throws Exception 
	{
		ServerSocket s = new ServerSocket(PUERTO);
		ServerSocket comm = new ServerSocket(COMM);
		
		int numeroThreads = 0;
		boolean continuar = true;
		ArrayList<ThreadServidor> listaThreads = new ArrayList<ThreadServidor>();
		
		System.out.println("Digite el archov a enviar");
		
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String opcion;
		opcion = stdIn.readLine();
		
		System.out.println("Ingrese cuantos clientes desea aceptar: ");
		int numClientesMax;
		numClientesMax = Integer.parseInt(stdIn.readLine());
		
		while (continuar) 
		{
			System.out.println("ESPERANDO");
			
			numeroThreads++;
			Socket socket = s.accept();
			Socket comunic = comm.accept(); 
			
			System.out.println("SOCKET " + numeroThreads + ": CONECTADO");
			
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			InputStream inputComm = comunic.getInputStream();
			BufferedReader readerComm = new BufferedReader(new InputStreamReader(inputComm));			
			
			System.out.println("INPUT SOCKET " + numeroThreads + ": CONECTADO");
			
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);		
			
			OutputStream outputComm = comunic.getOutputStream();
			PrintWriter writerComm = new PrintWriter(outputComm, true);
			
			String line = "NULL";
			
			System.out.println("OUTPUT SOCKET " + numeroThreads + ": CONECTADO");	
			
			while (!line.equals("LISTO")) 
	        { 
	            line = readerComm.readLine(); 
	            System.out.println("RESP: "+line);
	        } 
			
			writerComm.println("READY");
			writerComm.println(System.currentTimeMillis());
			writerComm.println("TIEMPO");
			String archivo = opcion;
			writerComm.println(archivo);
			writerComm.println("NOMBRE");
			
			File tamano = new File("./src/dataSend/"+archivo);
			
			writerComm.println(tamano.length());
			writerComm.println("SIZE");
			writerComm.println("CLIENTE: "+numeroThreads);
			
			ThreadServidor thread = new ThreadServidor(socket, numeroThreads, opcion, writerComm, readerComm);
			listaThreads.add(thread);
			if (numeroThreads == numClientesMax) 
			{
				continuar = false;
			}
		}
		
		for (int i = 0 ; i<listaThreads.size();  i++) 
		{
			listaThreads.get(i).start();
		}
		
		for (int i = 0 ; i<listaThreads.size();  i++) 
		{
			listaThreads.get(i).join();
		}		
		
		/**
		Socket sr = s.accept();
		OutputStream os = sr.getOutputStream();
		protocoloServidor.procesoServidor(os, protocoloServidor.IMAGEN);
		*/
		
		s.close();		
		comm.close(); 
	}
}
