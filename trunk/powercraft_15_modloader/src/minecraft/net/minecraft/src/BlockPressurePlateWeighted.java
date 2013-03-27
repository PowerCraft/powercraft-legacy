package net.minecraft.src;

import java.util.Iterator;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    private final int field_94357_a;

    protected BlockPressurePlateWeighted(int par1, String par2Str, Material par3Material, int par4)
    {
        super(par1, par2Str, par3Material);
        this.field_94357_a = par4;
    }

    protected int func_94351_d(World par1World, int par2, int par3, int par4)
    {
        int var5 = 0;
        Iterator var6 = par1World.getEntitiesWithinAABB(EntityItem.class, this.func_94352_a(par2, par3, par4)).iterator();

        while (var6.hasNext())
        {
            EntityItem var7 = (EntityItem)var6.next();
            var5 += var7.getEntityItem().stackSize;

            if (var5 >= this.field_94357_a)
            {
                break;
            }
        }

        if (var5 <= 0)
        {
            return 0;
        }
        else
        {
            float var8 = (float)Math.min(this.field_94357_a, var5) / (float)this.field_94357_a;
            return MathHelper.ceiling_float_int(var8 * 15.0F);
        }
    }

    protected int func_94350_c(int par1)
    {
        return par1;
    }

    protected int func_94355_d(int par1)
    {
        return par1;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 10;
    }
}
