package net.minecraft.src;

public class FlatLayerInfo
{
    private int field_82664_a;
    private int field_82662_b;
    private int field_82663_c;
    private int field_82661_d;

    public FlatLayerInfo(int par1, int par2)
    {
        this.field_82664_a = 1;
        this.field_82662_b = 0;
        this.field_82663_c = 0;
        this.field_82661_d = 0;
        this.field_82664_a = par1;
        this.field_82662_b = par2;
    }

    public FlatLayerInfo(int par1, int par2, int par3)
    {
        this(par1, par2);
        this.field_82663_c = par3;
    }

    public int func_82657_a()
    {
        return this.field_82664_a;
    }

    public int func_82659_b()
    {
        return this.field_82662_b;
    }

    public int func_82658_c()
    {
        return this.field_82663_c;
    }

    public int func_82656_d()
    {
        return this.field_82661_d;
    }

    public void func_82660_d(int par1)
    {
        this.field_82661_d = par1;
    }

    public String toString()
    {
        String var1 = Integer.toString(this.field_82662_b);

        if (this.field_82664_a > 1)
        {
            var1 = this.field_82664_a + "x" + var1;
        }

        if (this.field_82663_c > 0)
        {
            var1 = var1 + ":" + this.field_82663_c;
        }

        return var1;
    }
}
