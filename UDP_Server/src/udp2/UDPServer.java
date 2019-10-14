package udp2;

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

public class UDPServer 
{
	public static final int PUERTO = 8000;
	public static final int COMM = 4000; 
	public static final int BUFFER = 1000; 
	
	public static void main(String[] args) throws Exception 
	{
		ServerSocket s = new ServerSocket(PUERTO);
		ServerSocket comm = new ServerSocket(COMM);
		
		System.out.println("ESPERANDO");
		
		Socket socket = s.accept();
		Socket comunic = comm.accept(); 
		
		System.out.println("SOCKET: CONECTADO");
		
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		InputStream inputComm = comunic.getInputStream();
		BufferedReader readerComm = new BufferedReader(new InputStreamReader(inputComm));
		 
		System.out.println("INPUT: CONECTADO");
		
		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);		
		
		OutputStream outputComm = comunic.getOutputStream();
		PrintWriter writerComm = new PrintWriter(outputComm, true);
		
		String line = "NULL";
		
		System.out.println("OUTPUT: CONECTADO");
				
		while (!line.equals("LISTO")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("RESP: "+line);
        } 
		
		writerComm.println("READY");
		writerComm.println(System.currentTimeMillis());
		writerComm.println("TIEMPO");
		String archivo = args[0];
		writerComm.println(archivo);
		writerComm.println("NOMBRE");
		
		FileInputStream fr = new FileInputStream("./src/dataSend/"+archivo);
		FileInputStream inFile = new FileInputStream("./src/dataSend/"+archivo);
		File tamano = new File("./src/dataSend/"+archivo);
		
		writerComm.println(tamano.length());
		writerComm.println("SIZE");
		long id = new Random().nextLong(); 
		writerComm.println("CLIENTE: "+id);
		
		byte b[] = new byte[BUFFER];
		OutputStream os = socket.getOutputStream();
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
		
		socket.close();
		comm.close();
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
