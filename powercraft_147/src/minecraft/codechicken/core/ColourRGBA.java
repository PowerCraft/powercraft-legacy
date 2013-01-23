package codechicken.core;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ColourRGBA
{
	public int r;
	public int g;
	public int b;
	public int a;
	
	public ColourRGBA(int colour)
	{
		this((colour>>24)&0xFF, (colour>>16)&0xFF, (colour>>8)&0xFF, colour&0xFF);
	}
	
	public ColourRGBA(int r, int g, int b, int a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public ColourRGBA(double r, double g, double b, double a)
	{
		this((int)(255*r), (int)(255*g), (int)(255*b), (int)(255*a));
	}
	
	public ColourRGBA(ColourRGBA colour)
	{
		r = colour.r;
		g = colour.g;
		b = colour.b;
		a = colour.a;
	}
	
	@SideOnly(Side.CLIENT)
	public void glColour()
	{
		GL11.glColor4ub((byte)r, (byte)g, (byte)b, (byte)a);
	}

    public void glColour(byte alpha)
    {
        GL11.glColor4ub((byte)r, (byte)g, (byte)b, (byte)alpha);
    }
	
	public int getRGBA()
	{
		return ((r&0xFF)<<24)|((g&0xFF)<<16)|((b&0xFF)<<8)|a;
	}
	
	@Override
	public String toString()
	{
		return "0x"+Integer.toHexString(getRGBA()).toUpperCase();
	}
	
	public ColourRGBA multiply(ColourRGBA colour)
	{
		return new ColourRGBA(
				Math.min(r*colour.r/255, 255), 
				Math.min(g*colour.g/255, 255), 
				Math.min(b*colour.b/255, 255), 
				Math.min(a*colour.a/255, 255));
	}
	
	public ColourRGBA multiply(double d)
	{
		return multiply(new ColourRGBA(d, d, d, 1));
	}

	public ColourRGBA interpolate(ColourRGBA end, double d)
	{
		return new ColourRGBA(
				(int)(r+(end.r-r)*d), 
				(int)(g+(end.g-g)*d), 
				(int)(b+(end.b-b)*d), 
				(int)(a+(end.a-a)*d));
	}
}
