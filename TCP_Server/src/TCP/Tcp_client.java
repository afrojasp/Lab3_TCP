package TCP;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class Tcp_client {
	
	public static void main(String[] args) throws Exception {
		
		byte []b = new byte[2002];
		Socket sr = new Socket("localhost", Tcp_server.PUERTO);
		InputStream is = sr.getInputStream();
		FileOutputStream fr = new FileOutputStream("./src/dataReceived/archivoRecibido.bin");
		is.read(b, 0, b.length);
		fr.write(b, 0 , b.length);
		
		
	}

}
