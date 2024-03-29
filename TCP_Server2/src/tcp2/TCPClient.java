package tcp2;

import java.io.BufferedReader;
import java.io.File;
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
	public static void main(String[] args) throws Exception 
	{				
		Socket socket = new Socket("localhost", TCPServer.PUERTO);
		Socket comm = new Socket("localhost", TCPServer.COMM);
		
		System.out.println("YOKAS: SOCKET: CONECTADO");
		
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		InputStream inputComm = comm.getInputStream();
		BufferedReader readerComm = new BufferedReader(new InputStreamReader(inputComm));
		 
		System.out.println("YOKAS: INPUT: CONECTADO");
		
		OutputStream output = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);		
		
		OutputStream outputComm = comm.getOutputStream();
		PrintWriter writerComm = new PrintWriter(outputComm, true);	
		
		String line = "NULL";
		
		System.out.println("YOKAS: OUTPUT: CONECTADO");
				
		writerComm.println("LISTO");
		System.out.println("YOKAS:LISTO");
		
		while (!line.equals("READY")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		while (line.equals("READY")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		
		long time = Long.parseLong(line);
		
		while (!line.equals("TIEMPO")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		while (line.equals("TIEMPO")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		
	    String archivo = line; 
	    String archivoLog = "ARCHIVO: "+line; 
	    
	    while (!line.equals("NOMBRE")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		while (line.equals("NOMBRE")) 
        { 
            line = readerComm.readLine(); 
            System.out.println("SERVER: "+line);
        }
		
		String tamano = line;
		String tamanoMB = "TAMANO: "+String.valueOf(Long.parseLong(line)/1000)+" KB";
		
		while (!line.equals("SIZE")) 
	    { 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		while (line.equals("SIZE"))
		{ 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		
		String cliente = line;
		
		InputStream is = socket.getInputStream();
		FileOutputStream fr = new FileOutputStream("./src/dataReceived/"+archivo);
		byte []b = new byte[TCPServer.BUFFER];
		int paketes = 0;
		
		for (int i = 0; i<(Integer.parseInt(tamano)/TCPServer.BUFFER); i++)
		{
			paketes++;
			System.out.println("sigo vivo 1");
			is.read(b, 0, TCPServer.BUFFER);
			System.out.println("sigo vivo 2");
			fr.write(b, 0, TCPServer.BUFFER);
			System.out.println("sigo vivo 3");
			System.out.println(i);
		}	
		
		paketes++;
		int extra = Integer.parseInt(tamano) % TCPServer.BUFFER;
		System.out.println("EL EXTRA ES: "+extra);
		is.read(b, 0, extra);
		fr.write(b, 0, extra);	
		
		fr.close();
		
		long temp = System.currentTimeMillis();
		System.out.println("YOKAS: "+temp);
		String tiempo = "RECIBIDO EN:"+(temp-time)/1000+" SEGUNDOS";
		
		System.out.println("YOKAS: "+tiempo);
		FileInputStream outputFile = new FileInputStream("./src/dataReceived/"+archivo);
		File archRec = new File("./src/dataReceived/"+archivo);
		String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);

		while (!line.equals("TERMINATED"))
		{ 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		while (line.equals("TERMINATED"))
		{ 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		
		String estado = "ESTADO DE RECEPCION: ";
		System.out.println("CHECKSUM HASH RECIBIDO: "+checkSum);
		System.out.println("CHECKSUM HASH ENVIADO: "+line);
		
		if (checkSum.equals(line))
		{			
			estado = estado+"CORRECTO";
			writerComm.println("CORRECTO");
			System.out.println("YOKAS: CORRECTO");
		}
		else
		{
			estado = estado+"INCORRECTO";
			writerComm.println("INCORRECTO");
			System.out.println("YOKAS: INCORRECTO");
		}
		
		while (!line.equals("PAKETES"))
		{ 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		while (line.equals("PAKETES"))
		{ 
			line = readerComm.readLine(); 
	        System.out.println("SERVER: "+line);
	    }
		
		String paks = "SE ENVIARON: "+ line +" PAQUETES";
		
		writerComm.println("CLOSE");
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
