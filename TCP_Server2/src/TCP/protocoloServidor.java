package TCP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;

public class protocoloServidor 
{	
	public static final int BUFFER = 1000;
		
	public static void procesoServidor(OutputStream os, PrintWriter writerComm, BufferedReader readerComm, String archivo) throws Exception 
	{
		System.out.println("PROTOCOLO");
		FileInputStream fr = new FileInputStream("./src/dataSend/"+archivo);
		FileInputStream inFile = new FileInputStream("./src/dataSend/"+archivo);
		File tamano = new File("./src/dataSend/"+archivo);
		
		String line = "NULL";
		
		byte b[] = new byte[BUFFER];
		int paketes = 0;
		
		for (int i = 0; i<(tamano.length()/BUFFER)+1;i++)
		{
			paketes++;
			fr.read(b,0,BUFFER);
			os.write(b,0,BUFFER);
		}
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), inFile);
		
		writerComm.println("TERMINATED");
		writerComm.println(checkSum);
		
		writerComm.println("PAKETES");
		writerComm.println(paketes);
		
		while (!line.equals("CLOSE")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("RESP: "+line);
        }  
		
		os.close();
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
