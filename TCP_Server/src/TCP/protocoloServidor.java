package TCP;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

public class protocoloServidor {
	
	public static final int IMAGEN = 1;
	public static final int TEXTO  = 2;
		
	public static void procesoServidor(OutputStream os, int archivo,PrintWriter escritor,BufferedReader lector ) throws Exception {
		if ( archivo == IMAGEN) {
			File f = new File(Tcp_server.RUTA_IMAGEN);
			BufferedImage image = ImageIO.read(f);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteArray);
			
			byte[] b = ByteBuffer.allocate(4).putInt(byteArray.size()).array();
			os.write(b);
			os.write(byteArray.toByteArray());
			os.flush();
			
			FileInputStream outputFile = new FileInputStream(Tcp_server.RUTA_IMAGEN);
			String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), outputFile);
			
			escritor.println("TERMINATED");
			escritor.println(checkSum);
			String line = "";
			while(!line.equals("CLOSE")) {
				line = lector.readLine();
				System.out.println("RESP: " + line);
				
			}
			
			os.close();
		}
		
		else if (archivo == TEXTO) {
			
			File f = new File(Tcp_server.RUTA_TXT);
			FileInputStream fr = new FileInputStream(f);
			byte[] b = new byte[(int) f.length()];
			fr.read(b,0, b.length);
			os.write(b, 0, b.length);
			String checkSum = getFileChecksum(MessageDigest.getInstance("SHA"), fr);
			
			escritor.println("TERMINATED");
			escritor.println(checkSum);
			String line = "";
			while(!line.equals("CLOSE")) {
				line = lector.readLine();
				System.out.println("RESP: " + line);
				
			}
			os.close();
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
