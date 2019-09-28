package TCP;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Tcp_server {
	
	//public static final String RUTA_IMAGEN = "./src/dataSend/monsterHunter.jpg";
	
	public static final String RUTA_IMAGEN = "./src/dataSend/joker.jpg";
	
	public static final String RUTA_TXT = "./src/dataSend/pruebaPequena.txt";
	
	public static final int PUERTO = 8000;
	
	public static final int BYTES = 11987337;
	
	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(PUERTO);
		
		int numeroThreads = 0;
		boolean continuar = true;
		ArrayList<ThreadServidor> listaThreads = new ArrayList<ThreadServidor>();
		
		System.out.println("Ingrese 1 si quiere enviar el jpg \nIngrese 2 si quiere enviar el txt");
		
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		int opcion;
		opcion = Integer.parseInt(stdIn.readLine());
		
		System.out.println("Ingrese cuantos clientes desea aceptar: ");
		int numClientesMax;
		numClientesMax = Integer.parseInt(stdIn.readLine());
		
		while (continuar) {
			System.out.println("ESPERANDO");
			numeroThreads++;
			Socket sr = s.accept();
			System.out.println("SOCKET " + numeroThreads + ": CONECTADO");
			
			PrintWriter escritor = new PrintWriter(sr.getOutputStream(), true);
			System.out.println("OUTPUT SOCKET " + numeroThreads + ": CONECTADO");
			
			BufferedReader lector = new BufferedReader(new InputStreamReader(sr.getInputStream()));
			System.out.println("INPUT SOCKET " + numeroThreads + ": CONECTADO");
			
			String line ="NULL";
			
			line = lector.readLine();
			System.out.println("RESP SOCKET " + numeroThreads +": " + line);
			
			escritor.println("READY");
			escritor.println(System.currentTimeMillis());
			escritor.println("TIEMPO");
			File archivo;
			if (opcion == protocoloServidor.IMAGEN){
				archivo = new File(Tcp_server.RUTA_IMAGEN);
				escritor.println("joker.jpg");
			}
			else {
				archivo = new File(Tcp_server.RUTA_TXT);
				escritor.println("pruebaPequena.txt");
			}
			escritor.println("NOMBRE");
			
			escritor.println(archivo.length());
			escritor.println("SIZE");
			escritor.println("CLIENTE: " + numeroThreads);
			
			escritor.println(opcion);
			ThreadServidor thread = new ThreadServidor(sr, numeroThreads, opcion, escritor, lector);
			listaThreads.add(thread);
			if (numeroThreads == numClientesMax) {
				continuar = false;
			}
		}
		
		for (int i = 0 ; i<listaThreads.size();  i++) {
			listaThreads.get(i).start();
		}
		
		for (int i = 0 ; i<listaThreads.size();  i++) {
			listaThreads.get(i).join();
		}
		
		
		/**
		Socket sr = s.accept();
		OutputStream os = sr.getOutputStream();
		protocoloServidor.procesoServidor(os, protocoloServidor.IMAGEN);
		*/
		
		s.close();
		
		
	}
	

}
