package UDP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;

public class protocoloServidor 
{
	public static final int BUFFER = 1000;
	
	public static void procesoServidor(DatagramSocket serverSocket, String archivo, InetAddress IPAddress, int port) throws Exception 
	{
		System.out.println("PROTOCOLO");
		FileInputStream fr = new FileInputStream("./src/dataSend/" + archivo);
		FileInputStream inFile = new FileInputStream("./src/dataSend/" + archivo);
		File tamano = new File("./src/dataSend/" + archivo);
		DatagramPacket sendPacket;
		DatagramPacket receivePacket;
		String myLine = "";
		String yourLine = ""; 
		
		byte[] receiveData = new byte[BUFFER];
		byte[] sendData = new byte[BUFFER];
		int paketes = 0;

		System.out.println((tamano.length() / BUFFER) + 1);

		for (int i = 0; i < (tamano.length() / BUFFER) + 1; i++) 
		{
			paketes++;
			fr.read(sendData, 0, BUFFER);

			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}

		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), inFile);

		myLine = "TERMINATED";
		sendData = myLine.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		myLine = checkSum;
		sendData = myLine.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		myLine = "PAKETES";
		sendData = myLine.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		myLine = "=" + String.valueOf(paketes) + "=";
		sendData = myLine.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		while (!yourLine.trim().equals("CLOSE")) 
		{
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			yourLine = new String(receivePacket.getData());
			yourLine = yourLine.substring(0, 5);

			System.out.println("RESP: " + yourLine);
		}
	}
	
	public static String getFileChecksum(MessageDigest digest, FileInputStream fis) throws IOException 
	{
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		while ((bytesCount = fis.read(byteArray)) != -1) 
		{
			digest.update(byteArray, 0, bytesCount);
		}
		fis.close();

		byte[] bytes = digest.digest();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) 
		{
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
