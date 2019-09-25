package tcp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;

public class TCPServer 
{
	public static final int PUERTO = 8000;
	
	public static void main(String[] args) throws Exception 
	{
		ServerSocket s = new ServerSocket(PUERTO);
		Socket sr = s.accept(); 
		FileInputStream fr = new FileInputStream("./src/dataSend/prueba.bin");
		byte b[] = new byte[2002];
		fr.read(b, 0, b.length);
		OutputStream os = sr.getOutputStream();
		os.write(b, 0, b.length);
		
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), fr);
		
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
