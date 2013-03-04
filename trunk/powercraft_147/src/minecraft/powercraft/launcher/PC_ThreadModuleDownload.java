package powercraft.launcher;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Scanner;

public class PC_ThreadModuleDownload extends Thread {
	
	private URL adflyLink;
	private File target;
	
	public PC_ThreadModuleDownload(URL adflyLink, File target){
		this.adflyLink = adflyLink;
		this.target = target;
		start();
	}
	
	public void downloadFile(URL source, File target) throws IOException {
		File output = new File(target.toURI());
		if (output.exists()) {
			Files.delete(target.toPath());
		}
		output.getParentFile().mkdirs();
		output.createNewFile();

		InputStream is = source.openStream();
		FileOutputStream fos = new FileOutputStream(target);
		ReadableByteChannel in = Channels.newChannel(is);
		FileChannel out = fos.getChannel();

		ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024);

		while (in.read(buffer) != -1 || buffer.position() > 0) {
			buffer.flip();
			out.write(buffer);
			buffer.compact();
		}
		in.close();
		out.close();
		is.close();
		fos.close();

	}

	public URL decryptAdfly(URL adflyLink) throws IOException, URISyntaxException {

		Desktop.getDesktop().browse(adflyLink.toURI());
		Boolean notBanner = false, easyUrl = false;
		Scanner reader = new Scanner(adflyLink.openStream());
		reader.findWithinHorizon("var at = ", 0);
		reader.nextLine();
		if (reader.nextLine().contains("countdown")) {
			notBanner = true;
			reader.findWithinHorizon("var easyUrl = ", 0);
			String easurl = reader.nextLine();
			easyUrl = Boolean.parseBoolean(easurl.substring(1,
					easurl.length() - 2));
		}

		reader.findWithinHorizon((notBanner ? "zzz" : "self.location")
				+ " = ", 0);
		String line = reader.nextLine();
		line = line.substring(1, line.length() - 2);
		reader.close();
		return new URL(line);
	}
	
	@Override
	public void run(){
		File tmpFile = new File(target.getParentFile(), "tmp_"+target.getName());
		File newFile = new File(target.getParentFile(), target.getName());
		if(target.exists()){
			target.renameTo(tmpFile);
		}
		try{
			URL source = decryptAdfly(adflyLink);
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			downloadFile(source, newFile);
		}catch(Exception e){
			e.printStackTrace();
			if(newFile.exists()){
				newFile.delete();
			}
			target.renameTo(newFile);
		}
		if(tmpFile.exists()){
			tmpFile.delete();
		}
	}
	
}
