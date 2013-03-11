package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Rect2i
{
    private int field_94164_a;
    private int field_94162_b;
    private int field_94163_c;
    private int field_94161_d;

    public Rect2i(int par1, int par2, int par3, int par4)
    {
        this.field_94164_a = par1;
        this.field_94162_b = par2;
        this.field_94163_c = par3;
        this.field_94161_d = par4;
    }

    public Rect2i func_94156_a(Rect2i par1Rect2i)
    {
        int i = this.field_94164_a;
        int j = this.field_94162_b;
        int k = this.field_94164_a + this.field_94163_c;
        int l = this.field_94162_b + this.field_94161_d;
        int i1 = par1Rect2i.func_94158_a();
        int j1 = par1Rect2i.func_94160_b();
        int k1 = i1 + par1Rect2i.func_94159_c();
        int l1 = j1 + par1Rect2i.func_94157_d();
        this.field_94164_a = Math.max(i, i1);
        this.field_94162_b = Math.max(j, j1);
        this.field_94163_c = Math.max(0, Math.min(k, k1) - this.field_94164_a);
        this.field_94161_d = Math.max(0, Math.min(l, l1) - this.field_94162_b);
        return this;
    }

    public int func_94158_a()
    {
        return this.field_94164_a;
    }

    public int func_94160_b()
    {
        return this.field_94162_b;
    }

    public int func_94159_c()
    {
        return this.field_94163_c;
    }

    public int func_94157_d()
    {
        return this.field_94161_d;
    }
}
