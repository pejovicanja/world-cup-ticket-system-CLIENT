package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FileReceiver {

    private DataInputStream dataInputStream;
    
	public FileReceiver(Socket socket) throws IOException {
		this.dataInputStream = new DataInputStream(socket.getInputStream());
		
	}
	
	public void receiveFile(String fileName) throws IOException {
		int bytes = 0;
        FileOutputStream fileOutputStream
            = new FileOutputStream(fileName);
 
        long size
            = dataInputStream.readLong();  //citam ono sto je na serveru
        byte[] buffer = new byte[4 * 1024];
        while (size > 0
               && (bytes = dataInputStream.read(
                       buffer, 0,
                       (int)Math.min(buffer.length, size)))
                      != -1) {
            fileOutputStream.write(buffer, 0, bytes); //uzimam bajtove i pravim fajl od njih
            size -= bytes; 
        }
        fileOutputStream.close();
	}
}
