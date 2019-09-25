package TCP;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Tcp_server {
	
	public static final int PUERTO = 8000;
	
	public static void main(String[] args) throws Exception {
		ServerSocket s = new ServerSocket(PUERTO);
		Socket sr = s.accept(); 
		FileInputStream fr = new FileInputStream("./src/dataSend/prueba.bin");
		byte b[] = new byte[2002];
		fr.read(b, 0, b.length);
		OutputStream os = sr.getOutputStream();
		os.write(b, 0, b.length);
	}

}
