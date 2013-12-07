package xscript.runtime.clazz;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XInputStream extends InputStream implements DataInput {

	private String fileName;
	private InputStream inputStream;
	private String[] constDict;
	
	public XInputStream(InputStream inputStream, String fileName) throws IOException {
		this.fileName = fileName;
		this.inputStream = inputStream;
		readConstDict();
	}

	private void readConstDict() throws IOException{
		int size = readUnsignedShort();
		constDict = new String[size];
		for(int i=0; i<size; i++){
			constDict[i] = DataInputStream.readUTF(this);
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return inputStream.read()!=0;
	}

	@Override
	public byte readByte() throws IOException {
		return (byte)inputStream.read();
	}

	@Override
	public char readChar() throws IOException {
		return (char)inputStream.read();
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		inputStream.read(b);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		inputStream.read(b, off, len);
	}

	@Override
	public int readInt() throws IOException {
		return inputStream.read()<<24 | inputStream.read()<<16 | inputStream.read()<<8 | inputStream.read();
	}

	@Override
	public String readLine() throws IOException {
		return constDict[readUnsignedShort()];
	}

	@Override
	public long readLong() throws IOException {
		return ((long)inputStream.read())<<56 | ((long)inputStream.read())<<48 | ((long)inputStream.read())<<40 | ((long)inputStream.read())<<32 | ((long)inputStream.read())<<24 | ((long)inputStream.read())<<16 | ((long)inputStream.read())<<8 | ((long)inputStream.read());
	}

	@Override
	public short readShort() throws IOException {
		return (short) (inputStream.read()<<8 | inputStream.read());
	}

	@Override
	public String readUTF() throws IOException {
		return constDict[readUnsignedShort()];
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return inputStream.read();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return inputStream.read()<<8 | inputStream.read();
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return (int) inputStream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return inputStream.available();
	}

	@Override
	public synchronized void mark(int readlimit) {
		inputStream.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return inputStream.markSupported();
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return inputStream.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return inputStream.read(b);
	}

	@Override
	public synchronized void reset() throws IOException {
		inputStream.reset();
	}

	@Override
	public long skip(long n) throws IOException {
		return inputStream.skip(n);
	}

	
	
}
