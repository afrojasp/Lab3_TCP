package tcp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.MessageDigest;

public class TCPClient 
{
	public static void main(String[] args) throws Exception 
	{		
		byte []b = new byte[2002];
		Socket sr = new Socket("localhost", TCPServer.PUERTO);
		InputStream is = sr.getInputStream();
		FileOutputStream fr = new FileOutputStream("./src/dataReceived/archivoRecibido.bin");
		is.read(b, 0, b.length);
		fr.write(b, 0 , b.length);
		
		FileInputStream output = new FileInputStream("./src/dataReceived/archivoRecibido.bin");
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), output);
		
		System.out.println("HASH: "+ checkSum);
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
