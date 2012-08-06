package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RConOutputStream
{
    private ByteArrayOutputStream field_72674_a;
    private DataOutputStream field_72673_b;

    public RConOutputStream(int par1)
    {
        this.field_72674_a = new ByteArrayOutputStream(par1);
        this.field_72673_b = new DataOutputStream(this.field_72674_a);
    }

    public void func_72670_a(byte[] par1ArrayOfByte) throws IOException
    {
        this.field_72673_b.write(par1ArrayOfByte, 0, par1ArrayOfByte.length);
    }

    public void func_72671_a(String par1Str) throws IOException
    {
        this.field_72673_b.writeBytes(par1Str);
        this.field_72673_b.write(0);
    }

    public void func_72667_a(int par1) throws IOException
    {
        this.field_72673_b.write(par1);
    }

    public void func_72668_a(short par1) throws IOException
    {
        this.field_72673_b.writeShort(Short.reverseBytes(par1));
    }

    public byte[] func_72672_a()
    {
        return this.field_72674_a.toByteArray();
    }

    public void func_72669_b()
    {
        this.field_72674_a.reset();
    }
}
