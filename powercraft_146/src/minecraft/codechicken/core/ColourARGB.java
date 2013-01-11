package codechicken.core;

public class ColourARGB
{
	public ColourARGB(int colourARGB)
	{
		a = (byte) (colourARGB >> 24);
		r = (byte) (colourARGB >> 16);
		g = (byte) (colourARGB >> 8);
		b = (byte) (colourARGB);
	}
	
	public ColourARGB(byte a, byte r, byte g, byte b)
	{
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;		
	}
	
	public ColourARGB(ColourARGB colour)
	{
		a = colour.a;
		r = colour.r;
		g = colour.g;
		b = colour.b;
	}
	
	public ColourARGB copy()
	{
		return new ColourARGB(this);
	}
	
	public ColourARGB add(ColourARGB colour2)
	{
		a += colour2.a;
		r += colour2.r;
		g += colour2.g;
		b += colour2.b;
		return this;
	}
	
	public ColourARGB sub(ColourARGB colour2)
	{
		int ia = (a & 0xFF) - (colour2.a & 0xFF);
		int ir = (r & 0xFF) - (colour2.r & 0xFF);
		int ig = (g & 0xFF) - (colour2.g & 0xFF);
		int ib = (b & 0xFF) - (colour2.b & 0xFF);
		a = (byte) (ia < 0 ? 0 : ia);
		r = (byte) (ir < 0 ? 0 : ir);
		g = (byte) (ig < 0 ? 0 : ig);
		b = (byte) (ib < 0 ? 0 : ib);
		return this;
	}
	
	public ColourARGB invert()
	{
		a = (byte)(0xFF - (a & 0xFF));
		r = (byte)(0xFF - (r & 0xFF));
		g = (byte)(0xFF - (g & 0xFF));
		b = (byte)(0xFF - (b & 0xFF));
		return this;
	}
	
	public ColourARGB multiply(ColourARGB colour2)
	{
		a = (byte) ((a&0xFF)*((colour2.a&0xFF)/255D));
		r = (byte) ((r&0xFF)*((colour2.r&0xFF)/255D));
		g = (byte) ((g&0xFF)*((colour2.g&0xFF)/255D));
		b = (byte) ((b&0xFF)*((colour2.b&0xFF)/255D));
		return this;
		//return this.sub(colour2.copy().invert());
	}
	
	public ColourARGB scale(double d)
	{
		a = (byte) ((a & 0xFF) * d);
		r = (byte) ((r & 0xFF) * d);
		g = (byte) ((g & 0xFF) * d);
		b = (byte) ((b & 0xFF) * d);
		return this;
	}
	
	public ColourARGB interpolate(ColourARGB colour2, double d)
	{
		return this.add(colour2.copy().sub(this).scale(d));
	}
	
	public int toInt()
	{
		return ((a & 0xFF) << 24) + ((r & 0xFF) << 16) + ((g & 0xFF) << 8) + (b & 0xFF);
	}
	
	@Override
	public String toString()
	{
		return Integer.toHexString(toInt());
	}
	
	public byte a;
	public byte r;
	public byte g;
	public byte b;
}
