package net.minecraft.client.renderer.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.StitcherException;

@SideOnly(Side.CLIENT)
public class Stitcher
{
    private final Set field_94319_a;
    private final List field_94317_b;
    private int field_94318_c;
    private int field_94315_d;
    private final int field_94316_e;
    private final int field_94313_f;
    private final boolean field_94314_g;
    private final int field_94323_h;
    private Texture field_94322_k;
    private final String field_94320_l;

    public Stitcher(String par1Str, int par2, int par3, boolean par4)
    {
        this(par1Str, par2, par3, par4, 0);
    }

    public Stitcher(String par1, int par2, int par3, boolean par4, int par5)
    {
        this.field_94319_a = new HashSet(256);
        this.field_94317_b = new ArrayList(256);
        this.field_94318_c = 0;
        this.field_94315_d = 0;
        this.field_94320_l = par1;
        this.field_94316_e = par2;
        this.field_94313_f = par3;
        this.field_94314_g = par4;
        this.field_94323_h = par5;
    }

    public void func_94312_a(StitchHolder par1StitchHolder)
    {
        if (this.field_94323_h > 0)
        {
            par1StitchHolder.func_94196_a(this.field_94323_h);
        }

        this.field_94319_a.add(par1StitchHolder);
    }

    public Texture func_94306_e()
    {
        if (this.field_94314_g)
        {
            this.field_94318_c = this.func_94308_a(this.field_94318_c);
            this.field_94315_d = this.func_94308_a(this.field_94315_d);
        }

        this.field_94322_k = TextureManager.func_94267_b().func_98145_a(this.field_94320_l, 1, this.field_94318_c, this.field_94315_d, 6408);
        this.field_94322_k.func_94272_a(this.field_94322_k.func_94274_a(), -65536);
        List list = this.func_94309_g();

        for (int i = 0; i < list.size(); ++i)
        {
            StitchSlot stitchslot = (StitchSlot)list.get(i);
            StitchHolder stitchholder = stitchslot.func_94183_a();
            this.field_94322_k.func_94281_a(stitchslot.func_94186_b(), stitchslot.func_94185_c(), stitchholder.func_98150_a(), stitchholder.func_94195_e());
        }

        TextureManager.func_94267_b().func_94264_a(this.field_94320_l, this.field_94322_k);
        return this.field_94322_k;
    }

    public void func_94305_f()
    {
        StitchHolder[] astitchholder = (StitchHolder[])this.field_94319_a.toArray(new StitchHolder[this.field_94319_a.size()]);
        Arrays.sort(astitchholder);
        this.field_94322_k = null;

        for (int i = 0; i < astitchholder.length; ++i)
        {
            StitchHolder stitchholder = astitchholder[i];

            if (!this.func_94310_b(stitchholder))
            {
                throw new StitcherException(stitchholder);
            }
        }
    }

    public List func_94309_g()
    {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.field_94317_b.iterator();

        while (iterator.hasNext())
        {
            StitchSlot stitchslot = (StitchSlot)iterator.next();
            stitchslot.func_94184_a(arraylist);
        }

        return arraylist;
    }

    private int func_94308_a(int par1)
    {
        int j = par1 - 1;
        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    private boolean func_94310_b(StitchHolder par1StitchHolder)
    {
        for (int i = 0; i < this.field_94317_b.size(); ++i)
        {
            if (((StitchSlot)this.field_94317_b.get(i)).func_94182_a(par1StitchHolder))
            {
                return true;
            }

            par1StitchHolder.func_94194_d();

            if (((StitchSlot)this.field_94317_b.get(i)).func_94182_a(par1StitchHolder))
            {
                return true;
            }

            par1StitchHolder.func_94194_d();
        }

        return this.func_94311_c(par1StitchHolder);
    }

    private boolean func_94311_c(StitchHolder par1StitchHolder)
    {
        int i = Math.min(par1StitchHolder.func_94199_b(), par1StitchHolder.func_94197_a());
        boolean flag = this.field_94318_c == 0 && this.field_94315_d == 0;
        boolean flag1;

        if (this.field_94314_g)
        {
            int j = this.func_94308_a(this.field_94318_c);
            int k = this.func_94308_a(this.field_94315_d);
            int l = this.func_94308_a(this.field_94318_c + i);
            int i1 = this.func_94308_a(this.field_94315_d + i);
            boolean flag2 = l <= this.field_94316_e;
            boolean flag3 = i1 <= this.field_94313_f;

            if (!flag2 && !flag3)
            {
                return false;
            }

            int j1 = Math.max(par1StitchHolder.func_94199_b(), par1StitchHolder.func_94197_a());

            if (flag && !flag2 && this.func_94308_a(this.field_94315_d + j1) > this.field_94313_f)
            {
                return false;
            }

            boolean flag4 = j != l;
            boolean flag5 = k != i1;

            if (flag4 ^ flag5)
            {
                flag1 = flag4 && flag2;
            }
            else
            {
                flag1 = flag2 && j <= k;
            }
        }
        else
        {
            boolean flag6 = this.field_94318_c + i <= this.field_94316_e;
            boolean flag7 = this.field_94315_d + i <= this.field_94313_f;

            if (!flag6 && !flag7)
            {
                return false;
            }

            flag1 = (flag || this.field_94318_c <= this.field_94315_d) && flag6;
        }

        StitchSlot stitchslot;

        if (flag1)
        {
            if (par1StitchHolder.func_94197_a() > par1StitchHolder.func_94199_b())
            {
                par1StitchHolder.func_94194_d();
            }

            if (this.field_94315_d == 0)
            {
                this.field_94315_d = par1StitchHolder.func_94199_b();
            }

            stitchslot = new StitchSlot(this.field_94318_c, 0, par1StitchHolder.func_94197_a(), this.field_94315_d);
            this.field_94318_c += par1StitchHolder.func_94197_a();
        }
        else
        {
            stitchslot = new StitchSlot(0, this.field_94315_d, this.field_94318_c, par1StitchHolder.func_94199_b());
            this.field_94315_d += par1StitchHolder.func_94199_b();
        }

        stitchslot.func_94182_a(par1StitchHolder);
        this.field_94317_b.add(stitchslot);
        return true;
    }
}
