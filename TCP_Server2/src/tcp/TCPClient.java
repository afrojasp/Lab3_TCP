package tcp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPClient 
{		
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception 
	{				
		Socket socket = new Socket("localhost", TCPServer.PUERTO);
		
		System.out.println("YOKAS: SOCKET: CONECTADO");
		
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		 
		System.out.println("YOKAS: INPUT: CONECTADO");
		
		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);		
		String line = "NULL";
		
		System.out.println("YOKAS: OUTPUT: CONECTADO");
				
		byte []b = new byte[2002];
		
		writer.println("LISTO");
		System.out.println("YOKAS:LISTO");
		
		while (!line.equals("READY")) 
        { 
            line = reader.readLine(); 
            System.out.println("SERVER: "+line);
        }
		while (line.equals("READY")) 
        { 
            line = reader.readLine(); 
            System.out.println("SERVER: "+line);
        }
		
		long time = Long.parseLong(line);
		
		InputStream is = socket.getInputStream();
		FileOutputStream fr = new FileOutputStream("./src/dataReceived/archivoRecibido.bin");
		is.read(b, 0, b.length);
		fr.write(b, 0 , b.length);
		
		long temp = System.currentTimeMillis();
		System.out.println("YOKAS: "+temp);
		String tiempo = "RECIBIDO EN:"+(temp-time)/1000+" SEGUNDOS";
		
		System.out.println("YOKAS: "+tiempo);
		
		FileInputStream outputFile = new FileInputStream("./src/dataReceived/archivoRecibido.bin");
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);
		
		while (line.equals("READY")) 
        { 
            line = reader.readLine(); 
            System.out.println("RESP: "+line);
        } 
		
		String estado = "ESTADO DE RECEPCION: ";
		
		if (checkSum.equals(line))
		{
			estado = estado+"CORRECTO";
			writer.println("CORRECTO");
			System.out.println("YOKAS: CORRECTO");
		}
		else
		{
			estado = estado+"INCORRECTO";
			writer.println("INCORRECTO");
			System.out.println("YOKAS: INCORRECTO");
		}
		
		writer.println("CLOSE");
		System.out.println("YOKAS: CLOSE");
		
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		FileOutputStream log = new FileOutputStream("./src/logs/"+date+".txt");
		log.write(b);;
		
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
