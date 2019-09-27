package tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Random;

public class TCPServer 
{
	public static final int PUERTO = 8000;
	
	public static void main(String[] args) throws Exception 
	{
		ServerSocket s = new ServerSocket(PUERTO);
		
		System.out.println("ESPERANDO");
		
		Socket socket = s.accept();
		
		System.out.println("SOCKET: CONECTADO");
		
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		 
		System.out.println("INPUT: CONECTADO");
		
		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);		
		String line = "NULL";
		
		System.out.println("OUTPUT: CONECTADO");
				
		while (!line.equals("LISTO")) 
        { 
            line = reader.readLine(); 
            System.out.println("RESP: "+line);
        } 
		
		writer.println("READY");
		writer.println(System.currentTimeMillis());
		writer.println("TIEMPO");
		String archivo = args[0];
		writer.println(archivo);
		writer.println("NOMBRE");
		
		FileInputStream fr = new FileInputStream("./src/dataSend/"+archivo);
		FileInputStream inFile = new FileInputStream("./src/dataSend/"+archivo);
		File tamano = new File("./src/dataSend/"+archivo);
		
		writer.println(tamano.length());
		writer.println("SIZE");
		long id = new Random().nextLong(); 
		writer.println("CLIENTE: "+id);
		
		byte b[] = new byte[(int) tamano.length()];
		fr.read(b, 0, b.length);
		OutputStream os = socket.getOutputStream();
		os.write(b, 0, b.length);
		
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), inFile);

		writer.println("TERMINATED");
		writer.println(checkSum);
		
		while (!line.equals("CLOSE")) 
        { 
            line = reader.readLine(); 
            System.out.println("RESP: "+line);
        } 
		
		socket.close();
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
