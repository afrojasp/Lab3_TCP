package UDP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class UDPServer 
{
	public static final int PUERTO = 9876;
	public static final int BUFFER = 1000;

	public static void main(String args[]) throws Exception 
	{
		int numeroThreads = 0;
		boolean continuar = true;
		ArrayList<ThreadServidor> listaThreads = new ArrayList<ThreadServidor>();

		System.out.println("Digite el archov a enviar");

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String archivo = stdIn.readLine();

		System.out.println("Ingrese cuantos clientes desea aceptar: ");
		int numClientesMax;
		numClientesMax = Integer.parseInt(stdIn.readLine());
		
		DatagramSocket serverSocket = new DatagramSocket(PUERTO);
		System.out.println("YOKAS: CONECTANDO SOCKET");
		
		while(continuar)
		{				
			byte[] receiveData = new byte[BUFFER];
			byte[] sendData = new byte[BUFFER];
			String myLine = "";
			String yourLine = "";
			DatagramPacket sendPacket;
			DatagramPacket receivePacket;
			System.out.println("YOKAS: PRENDIDO");

			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			yourLine = new String(receivePacket.getData());

			int port = receivePacket.getPort();
			InetAddress IPAddress = receivePacket.getAddress();
			numeroThreads++;

			while (!yourLine.trim().equals("LISTO")) 
			{
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				yourLine = new String(receivePacket.getData());

				System.out.println("RESP: " + yourLine);
			}

			myLine = "READY";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = "-" + String.valueOf(System.currentTimeMillis()) + "-";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = "TIEMPO";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = ";" + archivo + ";";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = "NOMBRE";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			FileInputStream fr = new FileInputStream("./src/dataSend/" + archivo);
			FileInputStream inFile = new FileInputStream("./src/dataSend/" + archivo);
			File tamano = new File("./src/dataSend/" + archivo);

			myLine = "/" + String.valueOf(tamano.length()) + "/";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = "SIZE";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);

			myLine = ">CLIENTE: " + numeroThreads + ">";
			sendData = myLine.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			
			ThreadServidor thread = new ThreadServidor(serverSocket, numeroThreads, archivo, IPAddress, port);
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

		serverSocket.close();
	}
}
