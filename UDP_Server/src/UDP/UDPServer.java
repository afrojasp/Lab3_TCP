package UDP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Random;

public class UDPServer 
{
	public static final int PUERTO = 9876;
	public static final int BUFFER = 1000;
	
	public static void main(String args[]) throws Exception
    {
        byte[] receiveData = new byte[BUFFER];
        byte[] sendData = new byte[BUFFER];
        String myLine = "";
        String yourLine = "";
        DatagramPacket sendPacket;
        DatagramPacket receivePacket;
        System.out.println("YOKAS: PRENDIDO");

    	DatagramSocket serverSocket = new DatagramSocket(PUERTO);
        System.out.println("YOKAS: CONECTANDO SOCKET");
        
    	receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        yourLine = new String(receivePacket.getData()); 
        
        int port = receivePacket.getPort();
        InetAddress IPAddress = receivePacket.getAddress();
        
        while (!yourLine.trim().equals("LISTO")) 
        { 
        	receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    serverSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    
            System.out.println("RESP: "+yourLine);
        } 
        
        myLine = "READY";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);

        myLine = "-"+String.valueOf(System.currentTimeMillis())+"-";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);
	    
	    myLine = "TIEMPO";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);
	    
		String archivo = args[0];
		
		myLine = ";"+archivo+";";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);
	    
	    myLine = "NOMBRE";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);
		
		FileInputStream fr = new FileInputStream("./src/dataSend/"+archivo);
		FileInputStream inFile = new FileInputStream("./src/dataSend/"+archivo);
		File tamano = new File("./src/dataSend/"+archivo);
		
		myLine = "/"+String.valueOf(tamano.length())+"/";	    
 	    sendData = myLine.getBytes();
 	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
 	    serverSocket.send(sendPacket);

		myLine = "SIZE";	    
 	    sendData = myLine.getBytes();
 	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
 	    serverSocket.send(sendPacket);
 	    
		long id = new Random().nextLong(); 
		
		myLine = "CLIENTE: "+id;	    
 	    sendData = myLine.getBytes();
 	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
 	    serverSocket.send(sendPacket);
		
		sendData = new byte[BUFFER];
		int paketes = 0;
		
		System.out.println((tamano.length()/BUFFER)+1);
		
		for (int i = 0; i<(tamano.length()/BUFFER)+1;i++)
		{
			paketes++;
			fr.read(sendData,0,BUFFER);
			
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
	    
	    myLine = "="+String.valueOf(paketes)+"=";	    
	    sendData = myLine.getBytes();
	    sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    serverSocket.send(sendPacket);
	    
	    while (!yourLine.trim().equals("CLOSE")) 
        { 
	    	receivePacket = new DatagramPacket(receiveData, receiveData.length);
		    serverSocket.receive(receivePacket);	    
		    yourLine = new String(receivePacket.getData());
		    yourLine = yourLine.substring(0,5);
		    
            System.out.println("RESP: "+yourLine);
        } 
	    
	    serverSocket.close();  
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
