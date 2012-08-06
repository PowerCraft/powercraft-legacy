package net.minecraft.src;

public class DestroyBlockProgress
{
    private final int field_73115_a;
    private final int field_73113_b;
    private final int field_73114_c;
    private final int field_73111_d;
    private int field_73112_e;

    public DestroyBlockProgress(int par1, int par2, int par3, int par4)
    {
        this.field_73115_a = par1;
        this.field_73113_b = par2;
        this.field_73114_c = par3;
        this.field_73111_d = par4;
    }

    public int func_73110_b()
    {
        return this.field_73113_b;
    }

    public int func_73109_c()
    {
        return this.field_73114_c;
    }

    public int func_73108_d()
    {
        return this.field_73111_d;
    }

    public void func_73107_a(int par1)
    {
        if (par1 > 10)
        {
            par1 = 10;
        }

        this.field_73112_e = par1;
    }

    public int func_73106_e()
    {
        return this.field_73112_e;
    }
}
