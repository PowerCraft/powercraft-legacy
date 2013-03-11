package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

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
        int l = 0;
        Iterator iterator = par1World.getEntitiesWithinAABB(EntityItem.class, this.func_94352_a(par2, par3, par4)).iterator();

        while (iterator.hasNext())
        {
            EntityItem entityitem = (EntityItem)iterator.next();
            l += entityitem.getEntityItem().stackSize;

            if (l >= this.field_94357_a)
            {
                break;
            }
        }

        if (l <= 0)
        {
            return 0;
        }
        else
        {
            float f = (float)Math.min(this.field_94357_a, l) / (float)this.field_94357_a;
            return MathHelper.ceiling_float_int(f * 15.0F);
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
