package weasel.interpreter.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weasel.interpreter.io.WeaselClassFile.WeaselAnnotation;
import weasel.interpreter.io.WeaselClassFile.WeaselAnnotationEntry;
import weasel.interpreter.io.WeaselClassFile.WeaselByteCode;
import weasel.interpreter.io.WeaselClassFile.WeaselClass;
import weasel.interpreter.io.WeaselClassFile.WeaselField;
import weasel.interpreter.io.WeaselClassFile.WeaselGenericInfo;
import weasel.interpreter.io.WeaselClassFile.WeaselMethod;

public class  WeaselVersion1ClassFileSaver extends WeaselVersionClassFileSaver{

	private List<String> constDict = new ArrayList<String>();
	
	@Override
	public void save(DataOutputStream dataOutputStream, WeaselClassFile weaselClassFile) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		writeClass(new DataOutputStream(baos), weaselClassFile);
		writeConstDict(dataOutputStream);
		dataOutputStream.write(baos.toByteArray());
	}

	private WeaselClassFile writeClass(DataOutputStream dataOutputStream, WeaselClassFile wcf) throws IOException{
		writeClass(dataOutputStream, wcf.wClass);
		writeGenericInfos(dataOutputStream, wcf.genericInfos);
		writeSuperClasses(dataOutputStream, wcf.superClasses);
		writeFields(dataOutputStream, wcf.fields);
		writeMethods(dataOutputStream, wcf.methods);
		writeClasses(dataOutputStream, wcf.innerClasses);
		return wcf;
	}

	private void writeGenericInfos(DataOutputStream dataOutputStream, WeaselGenericInfo[] genericInfos) throws IOException{
		dataOutputStream.writeInt(genericInfos.length);
		for(int i=0; i<genericInfos.length; i++){
			writeGenericInfo(dataOutputStream, genericInfos[i]);
		}
	}
	
	private void writeGenericInfo(DataOutputStream dataOutputStream, WeaselGenericInfo genericInfo) throws IOException{
		writeString(dataOutputStream, genericInfo.name);
		writeSuperClasses(dataOutputStream, genericInfo.classes);
	}
	
	private void writeClasses(DataOutputStream dataOutputStream, WeaselClassFile[] classes) throws IOException {
		dataOutputStream.writeInt(classes.length);
		for(int i=0; i<classes.length; i++){
			writeClass(dataOutputStream, classes[i]);
		}
	}

	private void writeConstDict(DataOutputStream dataOutputStream) throws IOException{
		dataOutputStream.writeInt(constDict.size());
		for(String s:constDict){
			dataOutputStream.writeUTF(s);
		}
	}
	
	private void writeMethods(DataOutputStream dataOutputStream, WeaselMethod[] methods) throws IOException {
		dataOutputStream.writeInt(methods.length);
		for(int i=0; i<methods.length; i++){
			writeMethod(dataOutputStream, methods[i]);
		}
	}
	
	private void writeMethod(DataOutputStream dataOutputStream, WeaselMethod method) throws IOException {
		writeString(dataOutputStream, method.name);
		writeClass(dataOutputStream, method.returnType);
		writeFields(dataOutputStream, method.params);
		writeSuperClasses(dataOutputStream, method.throwClasses);
		writeByteCodes(dataOutputStream, method.byteCodes);
	}

	private void writeByteCodes(DataOutputStream dataOutputStream, WeaselByteCode[] byteCodes) throws IOException {
		dataOutputStream.writeInt(byteCodes.length);
		for(int i=0; i<byteCodes.length; i++){
			writeByteCode(dataOutputStream, byteCodes[i]);
		}
	}

	private void writeByteCode(DataOutputStream dataOutputStream, WeaselByteCode byteCode) throws IOException {
	}
	
	private void writeFields(DataOutputStream dataOutputStream, WeaselField[] fields) throws IOException{
		dataOutputStream.writeInt(fields.length);
		for(int i=0; i<fields.length; i++){
			writeField(dataOutputStream, fields[i]);
		}
	}
	
	private void writeField(DataOutputStream dataOutputStream, WeaselField field) throws IOException{
		writeString(dataOutputStream, field.name);
		writeClass(dataOutputStream, field.type);
	}
	
	private void writeSuperClasses(DataOutputStream dataOutputStream, WeaselClass[] superClasses) throws IOException{
		dataOutputStream.writeInt(superClasses.length);
		for(int i=0; i<superClasses.length; i++){
			writeClassEasy(dataOutputStream, superClasses[i]);
		}
	}
	
	private void writeAnnotations(DataOutputStream dataOutputStream, WeaselAnnotation[] annotations) throws IOException{
		dataOutputStream.writeInt(annotations.length);
		for(int i=0; i<annotations.length; i++){
			writeAnnotation(dataOutputStream, annotations[i]);
		}
	}
	
	private void writeAnnotation(DataOutputStream dataOutputStream, WeaselAnnotation annotation) throws IOException{
		writeString(dataOutputStream, annotation.className);
		writeAnnotationEntries(dataOutputStream, annotation.entries);
	}
	
	private void writeAnnotationEntries(DataOutputStream dataOutputStream, WeaselAnnotationEntry[] annotationEntries) throws IOException{
		dataOutputStream.writeInt(annotationEntries.length);
		for(int i=0; i<annotationEntries.length; i++){
			writeAnnotationEntry(dataOutputStream, annotationEntries[i]);
		}
	}
	
	private void writeAnnotationEntry(DataOutputStream dataOutputStream, WeaselAnnotationEntry annotationEntry) throws IOException{
		writeString(dataOutputStream, annotationEntry.name);
		dataOutputStream.writeInt(annotationEntry.type);
		writeAnnotationEntryValue(dataOutputStream, annotationEntry.type, annotationEntry.value);
	}
	
	private void writeAnnotationEntryValue(DataOutputStream dataOutputStream, int type, Object value) throws IOException{
		switch(type){
		case 0:
			dataOutputStream.writeInt((Integer)value);
			break;
		case 1:
			dataOutputStream.writeLong((Long)value);
			break;
		case 2:
			dataOutputStream.writeFloat((Float)value);
			break;
		case 3:
			dataOutputStream.writeDouble((Double)value);
			break;
		case 4:
			writeClassEasy(dataOutputStream, (WeaselClass)value);
			break;
		case 5:
			writeAnnotation(dataOutputStream, (WeaselAnnotation)value);
			break;
		case 6:
			writeString(dataOutputStream, (String)value);
			break;
		case 7:
			dataOutputStream.writeInt((Integer)value);
			break;
		case 8:
			int[] intArray = (int[])value;
			dataOutputStream.writeInt(intArray.length);
			for(int i=0; i<intArray.length; i++){
				dataOutputStream.writeInt(intArray[i]);
			}
			break;
		case 9:
			long[] longArray = (long[])value;
			dataOutputStream.writeInt(longArray.length);
			for(int i=0; i<longArray.length; i++){
				dataOutputStream.writeLong(longArray[i]);
			}
			break;
		case 10:
			float[] floatArray = (float[])value;
			dataOutputStream.writeInt(floatArray.length);
			for(int i=0; i<floatArray.length; i++){
				dataOutputStream.writeFloat(floatArray[i]);
			}
			break;
		case 11:
			double[] doubleArray = (double[])value;
			dataOutputStream.writeInt(doubleArray.length);
			for(int i=0; i<doubleArray.length; i++){
				dataOutputStream.writeDouble(doubleArray[i]);
			}
			break;
		case 12:
			writeSuperClasses(dataOutputStream, (WeaselClass[])value);
			break;
		case 13:
			writeAnnotations(dataOutputStream, (WeaselAnnotation[])value);
			break;
		case 14:
			String[] stringArray = (String[])value;
			dataOutputStream.writeInt(stringArray.length);
			for(int i=0; i<stringArray.length; i++){
				writeString(dataOutputStream, stringArray[i]);
			}
			break;
		case 15:
			int[] enumArray = (int[])value;
			dataOutputStream.writeInt(enumArray.length);
			for(int i=0; i<enumArray.length; i++){
				dataOutputStream.writeInt(enumArray[i]);
			}
			break;
		}
	}
	
	private void writeString(DataOutputStream dataOutputStream, String s) throws IOException{
		int i = constDict.indexOf(s);
		if(i==-1){
			i = constDict.size();
			constDict.add(s);
		}
		dataOutputStream.writeInt(i);
	}
	
	private void writeTypeParams(DataOutputStream dataOutputStream, WeaselClass[] typeParams) throws IOException{
		dataOutputStream.writeInt(typeParams.length);
		for(int i=0; i<typeParams.length; i++){
			writeClass(dataOutputStream, typeParams[i]);
		}
	}
	
	private void writeClass(DataOutputStream dataOutputStream, WeaselClass wClass) throws IOException{
		writeString(dataOutputStream, wClass.name);
		writeTypeParams(dataOutputStream, wClass.typeParams);
		dataOutputStream.writeInt(wClass.modifier);
		writeAnnotations(dataOutputStream, wClass.annotations);
	}
	
	private void writeClassEasy(DataOutputStream dataOutputStream, WeaselClass wClass) throws IOException{
		writeString(dataOutputStream, wClass.name);
		writeTypeParams(dataOutputStream, wClass.typeParams);
	}
	
}
