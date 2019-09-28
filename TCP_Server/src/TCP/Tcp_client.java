package TCP;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class Tcp_client {
		
	public static void main(String[] args) throws Exception {
		Socket sr = new Socket("localhost", Tcp_server.PUERTO);
		System.out.println("CLIENTE SOCKET: CONECTADO");
		
		PrintWriter escritor = new PrintWriter(sr.getOutputStream(), true);
		System.out.println("CLIENTE OUTPUT: CONECTADO");
		
		BufferedReader lector = new BufferedReader(new InputStreamReader(sr.getInputStream()));
		System.out.println("CLIENTE INPUT: CONECTADO");
		
		escritor.println("LISTO");
		System.out.println("CLIENTE: LISTO");
		
		String line = "NULL";
		
		while (!line.equals("READY")) {
			line = lector.readLine();
			System.out.println("SERVER: " + line);
		}
		
		while(line.equals("READY")) {
			line = lector.readLine();
			System.out.println("SERVER: " + line);
		}
		
		//long time = Long.parseLong(line);
		
		while(!line.equals("TIEMPO")) {
			line = lector.readLine();
			System.out.println("SERVER TIME: " + line);
		}
		
		while(line.equals("TIEMPO")) {
			line = lector.readLine();
			System.out.println("SERVER ARCHIVO: " + line);
		}
		
		String archivo = line;
		String archivoLog = "ARCHIVO: " + line;
		
		while(!line.equals("NOMBRE")) {
			line = lector.readLine();
			System.out.println("SERVER: " + line);
		}
		
		while(line.equals("NOMBRE")) {
			line = lector.readLine();
			System.out.println("SERVER TAMAÑO ARCHIVO: " + line);
		}
		
		String tamano = line;
		String tamanoMB = "TAMANO: " + String.valueOf(Long.parseLong(line)/1000) + " KB";
		
		while(!line.equals("SIZE")) {
			line = lector.readLine();
			System.out.println("SERVER: " + line);
		}
		
		while(line.equals("SIZE")) {
			line = lector.readLine();
			System.out.println("SERVER: " + line);
		}
		
		String cliente = line;
		
		int opcionArchivo = Integer.parseInt(lector.readLine());
		
		long startTime = System.nanoTime();
		long realTime = 0L;
		String tiempo;
		String estado;
		
		if (opcionArchivo == 1) {
		//	sr.setSoTimeout(800000000);
			InputStream is = sr.getInputStream();
			byte[] sizeAr = new byte[4];
			is.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
			
			byte[] imageAr = new byte[size];
			is.read(imageAr);
			
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
			ImageIO.write(image, "jpg",new File("./src/dataReceived/archivoRecibido.jpg"));
			long endTime = System.nanoTime();
			
			realTime = (endTime - startTime)/1000000;
			tiempo = "RECIBIDO EN: " + realTime + " MILISEGUNDOS";
			System.out.println(tiempo);
			
			FileInputStream outputFile = new FileInputStream("./src/dataReceived/archivoRecibido.jpg");
			File archRec = new File("./src/dataReceived/archivoRecibido.jpg");
			String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);
			
			while (!line.equals("TERMINATED")) {
				line = lector.readLine();
				System.out.println("SERVER: " + line);
			}
			
			while(line.equals("TERMINATED")) {
				line = lector.readLine();
				System.out.println("SERVER: " + line);
			}
			
			estado = "ESTADO DE RECEPCION: ";
			System.out.println("CHECKSUM HASH RECIBIDO: " + checkSum);
			System.out.println("CHECKSUM HASH ENVIADO: " + line);
			
			if (checkSum.equals(line)) {
				estado = estado+"CORRECTO";
				escritor.println("CORRECTO");
				System.out.println("CLIENTE: CORRECTO");
			}
			else {
				estado = estado+"INCORRECTO";
				escritor.println("INCORRECTO");
				System.out.println("CLIENTE: INCORRECTO");
			}
			
			escritor.println("CLOSE");
			System.out.println("CLIENTE: CLOSE");
			
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String dateLog = "DATE: "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String enviado = "ENVIADO: "+tamano+" BYTES";
			String recibido = "RECIBIDO: "+String.valueOf(archRec.length())+" BYTES";
			
			System.out.println(dateLog);
			System.out.println(archivoLog);
			System.out.println(tamanoMB);
			System.out.println(cliente);
			System.out.println(tiempo);
			System.out.println(enviado);
			System.out.println(recibido);
			System.out.println(estado);
			
			PrintWriter log = new PrintWriter("./src/logs/"+date+".txt", "UTF-8");
			log.println(dateLog);
			log.println(archivoLog);
			log.println(tamanoMB);
			log.println(cliente);
			log.println(tiempo);
			log.println(enviado);
			log.println(recibido);
			log.println(estado);
			log.close();
			
			
			escritor.close();
			lector.close();
			sr.close();				
		}
		
		else if(opcionArchivo == 2) {
			InputStream is = sr.getInputStream();
			byte b[] = new byte[2002];
			FileOutputStream fr = new FileOutputStream("./src/dataReceived/archivoRecibido.txt");
			is.read(b, 0, b.length);
			fr.write(b, 0 , b.length);
			long endTime = System.nanoTime();
			realTime = (endTime - startTime)/1000000;
			
			tiempo = "RECIBIDO EN: " + realTime + " MILISEGUNDOS";
			System.out.println(tiempo);
			
			FileInputStream outputFile = new FileInputStream("./src/dataReceived/archivoRecibido.txt");
			File archRec = new File("./src/dataReceived/archivoRecibido.txt");
			String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);
			
			while (!line.equals("TERMINATED")) {
				line = lector.readLine();
				System.out.println("SERVER: " + line);
			}
			
			while(line.equals("TERMINATED")) {
				line = lector.readLine();
				System.out.println("SERVER: " + line);
			}
			
			estado = "ESTADO DE RECEPCION: ";
			System.out.println("CHECKSUM HASH RECIBIDO: " + checkSum);
			System.out.println("CHECKSUM HASH ENVIADO: " + line);
			
			if (checkSum.equals(line)) {
				estado = estado+"CORRECTO";
				escritor.println("CORRECTO");
				System.out.println("CLIENTE: CORRECTO");
			}
			else {
				estado = estado+"INCORRECTO";
				escritor.println("INCORRECTO");
				System.out.println("CLIENTE: INCORRECTO");
			}
			
			escritor.println("CLOSE");
			System.out.println("CLIENTE: CLOSE");
			
			String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String dateLog = "DATE: "+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String enviado = "ENVIADO: "+tamano+" BYTES";
			String recibido = "RECIBIDO: "+String.valueOf(archRec.length())+" BYTES";
			
			System.out.println(dateLog);
			System.out.println(archivoLog);
			System.out.println(tamanoMB);
			System.out.println(cliente);
			System.out.println(tiempo);
			System.out.println(enviado);
			System.out.println(recibido);
			System.out.println(estado);
			
			PrintWriter log = new PrintWriter("./src/logs/"+date+".txt", "UTF-8");
			log.println(dateLog);
			log.println(archivoLog);
			log.println(tamanoMB);
			log.println(cliente);
			log.println(tiempo);
			log.println(enviado);
			log.println(recibido);
			log.println(estado);
			log.close();
			
			escritor.close();
			lector.close();
			sr.close();
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
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }	     
	    
	    return sb.toString();
	}

}
