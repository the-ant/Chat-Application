package client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendingFileRunnable implements Runnable{
	
	private Socket socket;
	private File file;
	private String requestSendFile;

	public SendingFileRunnable(String requestSendFile, Socket socket, File file, int groupId) {
		this.requestSendFile = requestSendFile;
		this.socket = socket;
		this.file = file;
	}

	@Override
	public void run() {
		if (file != null) {
			try {
				DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
				dataOut.writeUTF(requestSendFile);
				
				InputStream is = new FileInputStream(file);
				OutputStream os = socket.getOutputStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
				
				byte[] buffer = new byte[16 * 1024];
				int count;
				while((count = bufferedInputStream.read(buffer)) > 0) {
					os.write(buffer, 0, count);
				}
				os.flush();
				os.close();
				bufferedInputStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
