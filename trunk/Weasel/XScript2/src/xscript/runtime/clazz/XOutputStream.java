package xscript.runtime.clazz;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import xscript.runtime.XRuntimeException;

public class XOutputStream extends OutputStream implements DataOutput {

	private ByteArrayOutputStream outputStream;
	private List<String> constDict = new ArrayList<String>();
	
	public XOutputStream(){
		outputStream = new ByteArrayOutputStream();
	}
	
	@Override
	public void writeBoolean(boolean v) throws IOException {
		outputStream.write(v?-1:0);
	}

	@Override
	public void writeByte(int v) throws IOException {
		outputStream.write(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		outputStream.write(s.getBytes());
	}

	@Override
	public void writeChar(int v) throws IOException {
		outputStream.write(v>>>8);
		outputStream.write(v);
	}

	@Override
	public void writeChars(String s) throws IOException {
		writeUTF(s);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	@Override
	public void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	@Override
	public void writeInt(int v) throws IOException {
		outputStream.write(v>>>24);
		outputStream.write(v>>>16);
		outputStream.write(v>>>8);
		outputStream.write(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		outputStream.write((int) (v>>>56));
		outputStream.write((int) (v>>>48));
		outputStream.write((int) (v>>>40));
		outputStream.write((int) (v>>>32));
		outputStream.write((int) (v>>>24));
		outputStream.write((int) (v>>>16));
		outputStream.write((int) (v>>>8));
		outputStream.write((int) v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		outputStream.write(v>>>8);
		outputStream.write(v);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		int i = constDict.indexOf(s);
		if(i==-1){
			i = constDict.size();
			constDict.add(s);
		}
		if(i>=1<<Short.SIZE)
			throw new IOException("To many strings, only 65535 are allowed");
		writeShort(i);
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	@Override
	public void close() throws IOException {}

	@Override
	public void flush() throws IOException {}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputStream.write(b, off, len);
	}

	@Override
	public void write(byte[] b) throws IOException {
		outputStream.write(b);
	}

	public void writeToOutputStream(OutputStream outputStream) throws IOException{
		DataOutputStream dos = new DataOutputStream(outputStream);
		outputStream.write(constDict.size()>>>8);
		outputStream.write(constDict.size());
		for(String s:constDict){
			dos.writeUTF(s);
		}
		outputStream.write(this.outputStream.toByteArray());
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			writeToOutputStream(outputStream);
		} catch (IOException e) {
			throw new XRuntimeException(e, "Unexpected IOException");
		}
		return outputStream.toByteArray();
	}

}
