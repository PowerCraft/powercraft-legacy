package xscript.runtime.clazz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XClassLoader {

	private File rootFile;
	
	public XClassLoader(File rootFile) throws IOException{
		this.rootFile = rootFile;
		if(!rootFile.exists())
			throw new IOException("class root "+rootFile+" not found");
	}
	
	private XInputStream loadClassBytes(File file, String name){
		try{
			InputStream inputStream = new FileInputStream(file);
			XInputStream classInput = new XInputStream(inputStream, name);
			return classInput;
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}

	public XInputStream getInputStream(String name) {
		String[] s = name.split("\\.");
		File file = rootFile;
		File oldFile = rootFile;
		String n = "";
		for(int i=0; i<s.length; i++){
			oldFile = file;
			file = new File(file, s[i]);
			n += s[i];
			if(!(file.isDirectory() && file.exists())){
				oldFile = new File(oldFile, s[i]+".xcbc");
				if(oldFile.isFile() && oldFile.exists()){
					return loadClassBytes(oldFile, n);
				}
			}
			n += ".";
		}
		return null;
	}
	
}
