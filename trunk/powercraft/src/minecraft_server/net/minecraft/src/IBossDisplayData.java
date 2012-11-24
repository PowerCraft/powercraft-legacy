package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public interface IBossDisplayData
{
    int getMaxHealth();

    /**
     * Returns the health points of the dragon.
     */
    int getDragonHealth();

    /**
     * Gets the username of the entity.
     */
    String getEntityName();
}
