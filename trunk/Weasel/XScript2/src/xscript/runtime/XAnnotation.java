package xscript.runtime;

import java.io.IOException;

import xscript.runtime.clazz.XInputStream;
import xscript.runtime.clazz.XOutputStream;


public class XAnnotation {

	private String name;
	private XAnnotationEntry[] annotationEntries;
	
	public XAnnotation(XInputStream inputStream) throws IOException{
		name = inputStream.readUTF();
		annotationEntries = new XAnnotationEntry[inputStream.readUnsignedByte()];
		for(int i=0; i<annotationEntries.length; i++){
			annotationEntries[i] = new XAnnotationEntry(inputStream);
		}
	}

	public XAnnotation(String name, XAnnotationEntry[] annotationEntries) {
		this.name = name;
		this.annotationEntries = annotationEntries;
	}

	public String getName(){
		return name;
	}

	public void save(XOutputStream outputStream) throws IOException {
		outputStream.writeUTF(name);
		outputStream.writeByte(annotationEntries.length);
		for(int i=0; i<annotationEntries.length; i++){
			annotationEntries[i].save(outputStream);
		}
	}
	
}
