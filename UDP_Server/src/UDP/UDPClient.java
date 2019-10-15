package UDP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPClient 
{
	public static void main(String args[]) throws Exception
	{
		byte[] sendData = new byte[UDPServer.BUFFER];
	    byte[] receiveData = new byte[UDPServer.BUFFER];
	    String myLine = "";
	    String yourLine = "";
	    DatagramPacket receivePacket;
	    DatagramPacket sendPacket;
		System.out.println("CLIENTE: PRENDIDO");
		
	    DatagramSocket clientSocket = new DatagramSocket();
	    System.out.println("CLIENTE: CONECTANDO SOCKET");
	    InetAddress IPAddress = InetAddress.getByName("localhost");
	    System.out.println("CLIENTE: CONECTANDO");	    
	    
	    myLine = "LISTO";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDPServer.PUERTO);
	    clientSocket.send(sendPacket);
	    System.out.println("CLIENTE: "+myLine); 
	    
	    while (!yourLine.trim().equals("READY")) 
        { 
	    	receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    
            System.out.println("SERVER: "+yourLine);
        }
		while (yourLine.trim().equals("READY")) 
        { 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.split("-")[1];
		    
            System.out.println("SERVER: "+yourLine);
        }
		
		long time = Long.parseLong(yourLine.trim());
		
		while (!yourLine.trim().equals("TIEMPO")) 
        {
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,6);
		    
            System.out.println("SERVER: "+yourLine);
        }
		while (yourLine.trim().equals("TIEMPO")) 
        { 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.split(";")[1];
		    
            System.out.println("SERVER: "+yourLine);
        }
		
		String archivo = yourLine.trim(); 
		String archivoLog = "ARCHIVO: "+yourLine;
		
		while (!yourLine.trim().equals("NOMBRE")) 
        { 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,6);
		    
            System.out.println("SERVER: "+yourLine);
        }
		while (yourLine.trim().equals("NOMBRE")) 
        { 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.split("/")[1];
		    yourLine = yourLine.replaceAll("[^0-9]", "");
		    
            System.out.println("SERVER: "+yourLine);
        }
		
		String tamano = yourLine;
		String tamanoMB = "TAMANO: "+String.valueOf(Long.parseLong(yourLine)/1000)+" KB";
		
		while (!yourLine.trim().equals("SIZE")) 
	    { 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,4);
		    
            System.out.println("SERVER: "+yourLine);
	    }
		while (yourLine.trim().equals("SIZE"))
		{ 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.split(">")[1];
		    
            System.out.println("SERVER: "+yourLine);
	    }
		
		String cliente = yourLine;
		
		FileOutputStream fr = new FileOutputStream("./src/dataReceived/"+archivo);
		receiveData = new byte[UDPServer.BUFFER];
		int paketes = 0;
		
		for (int i = 0; i<(Long.parseLong(tamano.trim())/UDPServer.BUFFER) && !yourLine.equals("TERMINATED"); i++)
		{
			paketes++;
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,10);
			
			fr.write(receiveData, 0, UDPServer.BUFFER);
			System.out.println(i);
		}		
		
		if (!yourLine.equals("TERMINATED"))
		{
			paketes++;
			int extra = Integer.parseInt(tamano) % UDPServer.BUFFER;
			System.out.println("EL EXTRA ES: "+extra);
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);
			fr.write(receiveData, 0, extra);
		}		
		
		fr.close();
		
		myLine = "FINIQ";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDPServer.PUERTO);
	    clientSocket.send(sendPacket);
	    System.out.println("CLIENTE: "+myLine);
		
		long temp = System.currentTimeMillis();
		System.out.println("YOKAS: "+temp);
		String tiempo = "RECIBIDO EN: "+(temp-time)/1000+" SEGUNDOS";
		
		System.out.println("YOKAS: "+tiempo);
		FileInputStream outputFile = new FileInputStream("./src/dataReceived/"+archivo);
		File archRec = new File("./src/dataReceived/"+archivo);
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);
		
		while (!yourLine.trim().equals("TERMINATED"))
		{ 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,10);
		    
            System.out.println("SERVER: "+yourLine);
	    }
		while (yourLine.trim().equals("TERMINATED"))
		{ 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,40);
		    
            System.out.println("SERVER: "+yourLine);
	    }
		
		String estado = "ESTADO DE RECEPCION: ";
		System.out.println("CHECKSUM HASH RECIBIDO: "+checkSum);
		System.out.println("CHECKSUM HASH ENVIADO: "+yourLine);
		
		if (checkSum.equals(yourLine))
		{			
			estado = estado + "CORRECTO";
			
			myLine = "NICE";	    
		    sendData = myLine.getBytes();
		    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDPServer.PUERTO);
		    clientSocket.send(sendPacket);
			
			System.out.println("YOKAS: CORRECTO");
		}
		else
		{
			estado = estado + "INCORRECTO";
			
			myLine = "MAL";	    
		    sendData = myLine.getBytes();
		    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDPServer.PUERTO);
		    clientSocket.send(sendPacket);
			
			System.out.println("YOKAS: INCORRECTO");
		}
		
		while (!yourLine.trim().equals("PAKETES"))
		{ 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,7);
		    
            System.out.println("SERVER: "+yourLine);
	    }
		while (yourLine.trim().equals("PAKETES"))
		{ 
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    clientSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());	
		    yourLine = yourLine.split("=")[1];
		    
            System.out.println("SERVER: "+yourLine);
	    }
		
		String paks = "SE ENVIARON: "+ yourLine +" PAQUETES";
		
		myLine = "CLOSE";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDPServer.PUERTO);
	    clientSocket.send(sendPacket);
		
		System.out.println("YOKAS: CLOSE");
		
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String dateLog = "DATE: "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String enviado = "ENVIADO: "+tamano+" BYTES";
		String recibido = "RECIBIDO: "+String.valueOf(archRec.length())+" BYTES";
		String pakr = "SE RECIBIERON: " + paketes + " PAQUETES";
		
		System.out.println(dateLog);
		System.out.println(archivoLog);
		System.out.println(tamanoMB);
		System.out.println(cliente);
		System.out.println(tiempo);
		System.out.println(enviado);
		System.out.println(recibido);
		System.out.println(pakr);
		System.out.println(paks);
		System.out.println(estado);
		
		PrintWriter log = new PrintWriter("./src/logs/"+date+".txt", "UTF-8");
		
		log.println(dateLog);
		log.println(archivoLog);
		log.println(tamanoMB);
		log.println(cliente);
		log.println(tiempo);
		log.println(enviado);
		log.println(recibido);
		log.println(pakr);
		log.println(paks);
		log.println(estado);
		log.close();
		
	    clientSocket.close();
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
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }	     
	    
	    return sb.toString();
	}
}
