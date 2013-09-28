package weasel.interpreter.io;

import java.io.DataInputStream;
import java.io.IOException;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;
import weasel.interpreter.io.WeaselClassFile.WeaselAnnotationEntry;
import weasel.interpreter.io.WeaselClassFile.WeaselByteCode;
import weasel.interpreter.io.WeaselClassFile.WeaselClass;
import weasel.interpreter.io.WeaselClassFile.WeaselField;
import weasel.interpreter.io.WeaselClassFile.WeaselMethod;

public class  WeaselVersion1ClassFileLoader extends WeaselVersionClassFileLoader {

	private String[] constDict;
	
	@Override
	protected WeaselClassFile load(DataInputStream dataInputStream) throws IOException {
		loadConstDict(dataInputStream);
		return loadClass(dataInputStream);
	}

	private WeaselClassFile loadClass(DataInputStream dataInputStream) throws IOException{
		WeaselClassFile wcf = new WeaselClassFile();
		wcf.wClass = readClass(dataInputStream);
		wcf.superClasses = readSuperClasses(dataInputStream);
		wcf.fields = readFields(dataInputStream);
		wcf.methods = readMethods(dataInputStream);
		wcf.innerClasses = readClasses(dataInputStream);
		return wcf;
	}

	private WeaselClassFile[] readClasses(DataInputStream dataInputStream) throws IOException {
		WeaselClassFile[] classes = new WeaselClassFile[dataInputStream.readInt()];
		for(int i=0; i<classes.length; i++){
			classes[i] = loadClass(dataInputStream);
		}
		return classes;
	}

	private void loadConstDict(DataInputStream dataInputStream) throws IOException{
		int constCount = dataInputStream.readInt();
		constDict = new String[constCount];
		for(int i=0; i<constCount; i++){
			constDict[i] = dataInputStream.readUTF();
		}
	}
	
	private WeaselMethod[] readMethods(DataInputStream dataInputStream) throws IOException {
		WeaselMethod[] methods = new WeaselMethod[dataInputStream.readInt()];
		for(int i=0; i<methods.length; i++){
			methods[i] = readMethod(dataInputStream);
		}
		return methods;
	}
	
	private WeaselMethod readMethod(DataInputStream dataInputStream) throws IOException {
		WeaselMethod method = new WeaselMethod();
		method.name = readString(dataInputStream);
		method.returnType = readClass(dataInputStream);
		method.params = readFields(dataInputStream);
		method.throwClasses = readSuperClasses(dataInputStream);
		method.byteCodes = readByteCodes(dataInputStream);
		return method;
	}

	private WeaselByteCode[] readByteCodes(DataInputStream dataInputStream) throws IOException {
		WeaselByteCode[] byteCodes = new WeaselByteCode[dataInputStream.readInt()];
		for(int i=0; i<byteCodes.length; i++){
			byteCodes[i] = readByteCode(dataInputStream);
		}
		return byteCodes;
	}

	private WeaselByteCode readByteCode(DataInputStream dataInputStream) throws IOException {
		WeaselByteCode byteCode = new WeaselByteCode();
		return byteCode;
	}
	
	private WeaselField[] readFields(DataInputStream dataInputStream) throws IOException{
		WeaselField[] fields = new WeaselField[dataInputStream.readInt()];
		for(int i=0; i<fields.length; i++){
			fields[i] = readField(dataInputStream);
		}
		return fields;
	}
	
	private WeaselField readField(DataInputStream dataInputStream) throws IOException{
		WeaselField field = new WeaselField();
		field.name = readString(dataInputStream);
		field.type = readClass(dataInputStream);
		return field;
	}
	
	private WeaselClass[] readSuperClasses(DataInputStream dataInputStream) throws IOException{
		WeaselClass[] superClasses = new WeaselClass[dataInputStream.readInt()];
		for(int i=0; i<superClasses.length; i++){
			superClasses[i] = readClassEasy(dataInputStream);
		}
		return superClasses;
	}
	
	private WeaselAnnotation[] readAnnotations(DataInputStream dataInputStream) throws IOException{
		WeaselAnnotation[] annotations = new WeaselAnnotation[dataInputStream.readInt()];
		for(int i=0; i<annotations.length; i++){
			annotations[i] = readAnnotation(dataInputStream);
		}
		return annotations;
	}
	
	private WeaselAnnotation readAnnotation(DataInputStream dataInputStream) throws IOException{
		WeaselAnnotation annotation = new WeaselAnnotation();
		annotation.className = readString(dataInputStream);
		annotation.entries = readAnnotationEntries(dataInputStream);
		return annotation;
	}
	
	private WeaselAnnotationEntry[] readAnnotationEntries(DataInputStream dataInputStream) throws IOException{
		WeaselAnnotationEntry[] annotationEntries = new WeaselAnnotationEntry[dataInputStream.readInt()];
		for(int i=0; i<annotationEntries.length; i++){
			annotationEntries[i] = readAnnotationEntry(dataInputStream);
		}
		return annotationEntries;
	}
	
	private WeaselAnnotationEntry readAnnotationEntry(DataInputStream dataInputStream) throws IOException{
		WeaselAnnotationEntry annotationEntry = new WeaselAnnotationEntry();
		annotationEntry.name = readString(dataInputStream);
		annotationEntry.type = dataInputStream.readInt();
		annotationEntry.value = readAnnotationEntryValue(dataInputStream, annotationEntry.type);
		return annotationEntry;
	}
	
	private Object readAnnotationEntryValue(DataInputStream dataInputStream, int type) throws IOException{
		switch(type){
		case 0:
			return dataInputStream.readInt();
		case 1:
			return dataInputStream.readLong();
		case 2:
			return dataInputStream.readFloat();
		case 3:
			return dataInputStream.readDouble();
		case 4:
			return readClassEasy(dataInputStream);
		case 5:
			return readAnnotation(dataInputStream);
		case 6:
			return readString(dataInputStream);
		case 7:
			return dataInputStream.readInt();
		case 8:
			int[] intArray = new int[dataInputStream.readInt()];
			for(int i=0; i<intArray.length; i++){
				intArray[i] = dataInputStream.readInt();
			}
			return intArray;
		case 9:
			long[] longArray = new long[dataInputStream.readInt()];
			for(int i=0; i<longArray.length; i++){
				longArray[i] = dataInputStream.readLong();
			}
			return longArray;
		case 10:
			float[] floatArray = new float[dataInputStream.readInt()];
			for(int i=0; i<floatArray.length; i++){
				floatArray[i] = dataInputStream.readFloat();
			}
			return floatArray;
		case 11:
			double[] doubleArray = new double[dataInputStream.readInt()];
			for(int i=0; i<doubleArray.length; i++){
				doubleArray[i] = dataInputStream.readDouble();
			}
			return doubleArray;
		case 12:
			return readSuperClasses(dataInputStream);
		case 13:
			return readAnnotations(dataInputStream);
		case 14:
			String[] stringArray = new String[dataInputStream.readInt()];
			for(int i=0; i<stringArray.length; i++){
				stringArray[i] = readString(dataInputStream);
			}
			return stringArray;
		case 15:
			int[] enumArray = new int[dataInputStream.readInt()];
			for(int i=0; i<enumArray.length; i++){
				enumArray[i] = dataInputStream.readInt();
			}
			return enumArray;
		}
		return null;
	}
	
	private String readString(DataInputStream dataInputStream) throws IOException{
		return constDict[dataInputStream.readInt()];
	}
	
	private WeaselClass[] readTypeParams(DataInputStream dataInputStream) throws IOException{
		WeaselClass[] typeParams = new WeaselClass[dataInputStream.readInt()];
		for(int i=0; i<typeParams.length; i++){
			typeParams[i] = readClass(dataInputStream);
		}
		return typeParams;
	}
	
	private WeaselClass readClass(DataInputStream dataInputStream) throws IOException{
		WeaselClass wClass = new WeaselClass();
		wClass.name = readString(dataInputStream);
		wClass.typeParams = readTypeParams(dataInputStream);
		wClass.modifier = dataInputStream.readInt();
		wClass.annotations = readAnnotations(dataInputStream);
		return wClass;
	}
	
	private WeaselClass readClassEasy(DataInputStream dataInputStream) throws IOException{
		WeaselClass wClass = new WeaselClass();
		wClass.name = readString(dataInputStream);
		wClass.typeParams = readTypeParams(dataInputStream);
		return wClass;
	}
	
}
