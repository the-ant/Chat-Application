package client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class DownloadFileRunnable implements Runnable {

	private Socket socket;
	private String requestSendFile;
	private File file;
	private String fileName;

	public DownloadFileRunnable(String requestSendFile, Socket socket, File file, String fileName) {
		this.requestSendFile = requestSendFile;
		this.socket = socket;
		this.file = file;
		System.out.println("DownloadFileRunnable: " + file);
		this.fileName = fileName;
	}

	@Override
	public void run() {
		try {
			DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
			dataOut.writeUTF(requestSendFile);

			InputStream fileIS = socket.getInputStream();
			while (fileIS.available() == 0) {
			}

			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileIS);
			FileOutputStream fos = new FileOutputStream(file + "\\" + fileName);

			byte[] buffer = new byte[16 * 1024];
			int count;
			while ((count = bufferedInputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			fos.close();
			bufferedInputStream.close();
			socket.close();

			File dlFile = new File(file + "\\" + fileName);
			Runtime.getRuntime()
					.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", dlFile.getAbsolutePath() });
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
