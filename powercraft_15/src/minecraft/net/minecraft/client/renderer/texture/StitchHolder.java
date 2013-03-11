package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StitchHolder implements Comparable
{
    private final Texture field_98151_a;
    private final int field_94204_c;
    private final int field_94201_d;
    private boolean field_94202_e;
    private float field_94205_a = 1.0F;

    public StitchHolder(Texture par1Texture)
    {
        this.field_98151_a = par1Texture;
        this.field_94204_c = par1Texture.func_94275_d();
        this.field_94201_d = par1Texture.func_94276_e();
        this.field_94202_e = this.func_94193_b(this.field_94201_d) > this.func_94193_b(this.field_94204_c);
    }

    public Texture func_98150_a()
    {
        return this.field_98151_a;
    }

    public int func_94197_a()
    {
        return this.field_94202_e ? this.func_94193_b((int)((float)this.field_94201_d * this.field_94205_a)) : this.func_94193_b((int)((float)this.field_94204_c * this.field_94205_a));
    }

    public int func_94199_b()
    {
        return this.field_94202_e ? this.func_94193_b((int)((float)this.field_94204_c * this.field_94205_a)) : this.func_94193_b((int)((float)this.field_94201_d * this.field_94205_a));
    }

    public void func_94194_d()
    {
        this.field_94202_e = !this.field_94202_e;
    }

    public boolean func_94195_e()
    {
        return this.field_94202_e;
    }

    private int func_94193_b(int par1)
    {
        return (par1 >> 0) + ((par1 & 0) == 0 ? 0 : 1) << 0;
    }

    public void func_94196_a(int par1)
    {
        if (this.field_94204_c > par1 && this.field_94201_d > par1)
        {
            this.field_94205_a = (float)par1 / (float)Math.min(this.field_94204_c, this.field_94201_d);
        }
    }

    public String toString()
    {
        return "TextureHolder{width=" + this.field_94204_c + ", height=" + this.field_94201_d + '}';
    }

    public int func_94198_a(StitchHolder par1StitchHolder)
    {
        int i;

        if (this.func_94199_b() == par1StitchHolder.func_94199_b())
        {
            if (this.func_94197_a() == par1StitchHolder.func_94197_a())
            {
                if (this.field_98151_a.func_94280_f() == null)
                {
                    return par1StitchHolder.field_98151_a.func_94280_f() == null ? 0 : -1;
                }

                return this.field_98151_a.func_94280_f().compareTo(par1StitchHolder.field_98151_a.func_94280_f());
            }

            i = this.func_94197_a() < par1StitchHolder.func_94197_a() ? 1 : -1;
        }
        else
        {
            i = this.func_94199_b() < par1StitchHolder.func_94199_b() ? 1 : -1;
        }

        return i;
    }

    public int compareTo(Object par1Obj)
    {
        return this.func_94198_a((StitchHolder)par1Obj);
    }
}
