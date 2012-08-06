package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class Vec3Pool
{
    private final int field_72351_a;
    private final int field_72349_b;
    private final List field_72350_c = new ArrayList();
    private int field_72347_d = 0;
    private int field_72348_e = 0;
    private int field_72346_f = 0;

    public Vec3Pool(int par1, int par2)
    {
        this.field_72351_a = par1;
        this.field_72349_b = par2;
    }

    public Vec3 func_72345_a(double par1, double par3, double par5)
    {
        Vec3 var7;

        if (this.field_72347_d >= this.field_72350_c.size())
        {
            var7 = new Vec3(par1, par3, par5);
            this.field_72350_c.add(var7);
        }
        else
        {
            var7 = (Vec3)this.field_72350_c.get(this.field_72347_d);
            var7.setComponents(par1, par3, par5);
        }

        ++this.field_72347_d;
        return var7;
    }

    public void func_72343_a()
    {
        if (this.field_72347_d > this.field_72348_e)
        {
            this.field_72348_e = this.field_72347_d;
        }

        if (this.field_72346_f++ == this.field_72351_a)
        {
            int var1 = Math.max(this.field_72348_e, this.field_72350_c.size() - this.field_72349_b);

            while (this.field_72350_c.size() > var1)
            {
                this.field_72350_c.remove(var1);
            }

            this.field_72348_e = 0;
            this.field_72346_f = 0;
        }

        this.field_72347_d = 0;
    }
}
