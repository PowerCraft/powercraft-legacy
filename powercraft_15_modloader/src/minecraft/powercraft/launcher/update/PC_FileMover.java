package powercraft.launcher.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PC_FileMover {

	public static boolean moveAndDelete(File file, File to){
		if(move(file, to)){
			delete(file);
			return true;
		}
		return false;
	}
	
	private static void delete(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0; i<files.length; i++){
				delete(files[i]);
			}
		}
		file.delete();
	}
	
	private static boolean move(File file, File to){
		if(file.isDirectory()){
			return moveDirectory(file, to);
		}else if(file.isFile()){
			return moveFile(file, to);
		}
		return false;
	}
	
	private static boolean moveDirectory(File file, File to){
		File[] files = file.listFiles();
		to.mkdirs();
		boolean ok=true;
		for(int i=0; i<files.length; i++){
			ok &= move(files[i], new File(to, files[i].getName()));
		}
		return ok;
	}
	
	private static boolean moveFile(File file, File to){
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(to);
			byte[] buffer = new byte[1024];
			int length;
			while((length=fis.read(buffer))!=-1){
				fos.write(buffer, 0, length);
			}
			fis.close();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
