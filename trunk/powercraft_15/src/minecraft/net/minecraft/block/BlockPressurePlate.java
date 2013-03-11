package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    /** The mob type that can trigger this pressure plate. */
    private EnumMobType triggerMobType;

    protected BlockPressurePlate(int par1, String par2Str, Material par3Material, EnumMobType par4EnumMobType)
    {
        super(par1, par2Str, par3Material);
        this.triggerMobType = par4EnumMobType;
    }

    protected int func_94355_d(int par1)
    {
        return par1 > 0 ? 1 : 0;
    }

    protected int func_94350_c(int par1)
    {
        return par1 == 1 ? 15 : 0;
    }

    protected int func_94351_d(World par1World, int par2, int par3, int par4)
    {
        List list = null;

        if (this.triggerMobType == EnumMobType.everything)
        {
            list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, this.func_94352_a(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.mobs)
        {
            list = par1World.getEntitiesWithinAABB(EntityLiving.class, this.func_94352_a(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.players)
        {
            list = par1World.getEntitiesWithinAABB(EntityPlayer.class, this.func_94352_a(par2, par3, par4));
        }

        if (!list.isEmpty())
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();

                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    return 15;
                }
            }
        }

        return 0;
    }
}
