package weasel.interpreter.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.WeaselModifier;
import weasel.interpreter.WeaselRuntimeException;

public class WeaselClassFile {

	private static List<WeaselVersionGetter> versions = new ArrayList<WeaselVersionGetter>();
	
	public static int MAGICNUMBER = (byte)('w')<<24 | (byte)('s')<<16 | (byte)('c')<<8 | (byte)('f');
	
	public WeaselClass wClass;
	public WeaselGenericInfo[] genericInfos;
	public WeaselClass[] superClasses;
	public WeaselField[] fields;
	public WeaselMethod[] methods;
	public WeaselClassFile[] innerClasses;
	
	static{
		addVersionGetter(new StandardVersionGetter());
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i=0; i<wClass.annotations.length; i++){
			s += wClass.annotations[i];
		}
		s += WeaselModifier.toSourceString(wClass.modifier);
		s += "class ";
		s += wClass.name;
		if(wClass.typeParams.length>0){
			s += "<";
			s += wClass.typeParams[0];
			for(int i=1; i<wClass.typeParams.length; i++){
				s += ", "+wClass.typeParams[i];
			}
			s += ">";
		}
		if(superClasses.length>0){
			s += ":"+superClasses[0];
			for(int i=1; i<superClasses.length; i++){
				s += ", "+superClasses[i];
			}
		}
		s += "{\n";
		for(int i=0; i<fields.length; i++){
			s += fields[i]+"\n";
		}
		for(int i=0; i<methods.length; i++){
			s += methods[i]+"\n";
		}
		for(int i=0; i<innerClasses.length; i++){
			s += innerClasses[i]+"\n";
		}
		s += "}";
		return s;
	}

	public static void addVersionGetter(WeaselVersionGetter versionGetter){
		versions.add(versionGetter);
	}
	
	public static WeaselClassFile load(byte[] byteArray){
		try {
			return load(new DataInputStream(new ByteArrayInputStream(byteArray)));
		} catch (IOException e) {
			throw new WeaselRuntimeException("Error while read class file");
		}
	}
	
	public static WeaselClassFile load(DataInputStream dataInputStream) throws IOException{
		int magicNumber = dataInputStream.readInt();
		if(magicNumber!=MAGICNUMBER)
			throw new WeaselRuntimeException("Magic number isn't the same");
		int version = dataInputStream.readInt();
		WeaselVersionClassFileLoader wvcfl = null;
		for(WeaselVersionGetter wvg:versions){
			wvcfl = wvg.getVersionClassFileLoader(version);
			if(wvcfl!=null)
				break;
		}
		if(wvcfl==null)
			throw new WeaselRuntimeException("Unkonw version %s", version);
		return wvcfl.load(dataInputStream);
	}
	
	public static byte[] save(WeaselClassFile weaselClassFile, int version){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			save(new DataOutputStream(baos), weaselClassFile, version);
		} catch (IOException e) {
			throw new WeaselRuntimeException("Error while write class file");
		}
		return baos.toByteArray();
	}
	
	public static void save(DataOutputStream dataOutputStream, WeaselClassFile weaselClassFile, int version) throws IOException{
		dataOutputStream.writeInt(MAGICNUMBER);
		dataOutputStream.writeInt(version);
		WeaselVersionClassFileSaver wvcfs = null;
		for(WeaselVersionGetter wvg:versions){
			wvcfs = wvg.getVersionClassFileSaver(version);
			if(wvcfs!=null)
				break;
		}
		if(wvcfs==null)
			throw new WeaselRuntimeException("Unkonw version %s", version);
		wvcfs.save(dataOutputStream, weaselClassFile);
	}
	
	private static class StandardVersionGetter implements WeaselVersionGetter{

		@Override
		public WeaselVersionClassFileLoader getVersionClassFileLoader(int version) {
			switch(version){
			case 1:
				return new WeaselVersion1ClassFileLoader();
			default:
				return null;
			}
		}

		@Override
		public WeaselVersionClassFileSaver getVersionClassFileSaver(int version) {
			switch(version){
			case 1:
				return new WeaselVersion1ClassFileSaver();
			default:
				return null;
			}
		}
		
	}
	
	public static class WeaselAnnotation{
		
		public String className;
		public WeaselAnnotationEntry[] entries;
		
		@Override
		public String toString(){
			String s = "@" + className;
			if(entries.length>0){
				s += "(";
				s += entries[0];
				for(int i=1; i<entries.length; i++){
					s += ", "+entries[i];
				}
				s += ")";
			}
			return s;
		}
		
	}
	
	public static class WeaselAnnotationEntry{
		
		public String name;
		public int type;
		public Object value;
		
		@Override
		public String toString(){
			String s = name + "=";
			switch(type){
			case 0:
			case 1:
			case 2:
			case 3:
				s += value;
				break;
			case 4:
				s += value + ".class";
				break;
			case 5:
				s += value;
				break;
			case 6:
				s += "\""+value+"\"";
				break;
			case 7:
				s += value;
				break;
			case 8:
				int[] intArray = (int[])value;
				s += "{";
				if(intArray.length>0){
					s += intArray[0];
					for(int i=1; i<intArray.length; i++){
						s += ", "+intArray[i];
					}
				}
				break;
			case 9:
				long[] longArray = (long[])value;
				s += "{";
				if(longArray.length>0){
					s += longArray[0];
					for(int i=1; i<longArray.length; i++){
						s += ", "+longArray[i];
					}
				}
				break;
			case 10:
				float[] floatArray = (float[])value;
				s += "{";
				if(floatArray.length>0){
					s += floatArray[0];
					for(int i=1; i<floatArray.length; i++){
						s += ", "+floatArray[i];
					}
				}
				break;
			case 11:
				double[] doubleArray = (double[])value;
				s += "{";
				if(doubleArray.length>0){
					s += doubleArray[0];
					for(int i=1; i<doubleArray.length; i++){
						s += ", "+doubleArray[i];
					}
				}
				break;
			case 12:
				WeaselClass[] classArray = (WeaselClass[])value;
				s += "{";
				if(classArray.length>0){
					s += classArray[0]+".class";
					for(int i=1; i<classArray.length; i++){
						s += ", "+classArray[i]+".class";
					}
				}
				break;
			case 13:
				WeaselAnnotation[] annotationArray = (WeaselAnnotation[])value;
				s += "{";
				if(annotationArray.length>0){
					s += annotationArray[0];
					for(int i=1; i<annotationArray.length; i++){
						s += ", "+annotationArray[i];
					}
				}
				break;
			case 14:
				String[] stringArray = (String[])value;
				s += "{";
				if(stringArray.length>0){
					s += "\""+stringArray[0]+"\"";
					for(int i=1; i<stringArray.length; i++){
						s += ", \""+stringArray[i]+"\"";
					}
				}
				break;
			case 15:
				int[] enumArray = (int[])value;
				s += "{";
				if(enumArray.length>0){
					s += enumArray[0];
					for(int i=1; i<enumArray.length; i++){
						s += ", "+enumArray[i];
					}
				}
				break;
			}
			return s;
		}
		
	}
	
	
	public static class WeaselClass{
		
		public String name;
		public WeaselClass[] typeParams;
		public int modifier;
		public WeaselAnnotation[] annotations;
		
		@Override
		public String toString(){
			String s = "";
			for(int i=0; i<annotations.length; i++){
				s += annotations[i];
			}
			s += WeaselModifier.toSourceString(modifier);
			s += name;
			if(typeParams.length>0){
				s += "<";
				s += typeParams[0];
				for(int i=1; i<typeParams.length; i++){
					s += ", "+typeParams[i];
				}
				s += ">";
			}
			return s;
		}
		
	}
	
	public static class WeaselField{

		public String name;
		public WeaselClass type;
		
		@Override
		public String toString(){
			return type +" "+name+";";
		}
		
	}
	
	public static class WeaselMethod{

		public String name;
		public WeaselClass returnType;
		public WeaselClass[] params;
		public WeaselClass[] throwClasses;
		public WeaselByteCode[] byteCodes;
		
		@Override
		public String toString(){
			String s = returnType + " " + name + "(";
			if(params.length>0){
				s += params[0];
				for(int i=1; i<params.length; i++){
					s += ", "+params[i];
				}
			}
			s += ")";
			if(throwClasses.length>0){
				s += " throws "+throwClasses[0];
				for(int i=1; i<throwClasses.length; i++){
					s += ", "+throwClasses[i];
				}
			}
			s += "{}";
			return s;
		}
		
	}
	
	public static class WeaselByteCode{
		
	}
	
	public static class WeaselGenericInfo{
		
		public String name;
		public WeaselClass classes[];
		
		@Override
		public String toString(){
			String s = name;
			if(classes.length>0){
				s += " extends "+classes[0];
				for(int i=1; i<classes.length; i++){
					s += ", "+classes[i];
				}
			}
			return s;
		}
		
	}
	
}
