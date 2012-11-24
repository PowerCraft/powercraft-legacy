package powercraft.core;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public interface PC_IActivatorListener
{
    public abstract boolean onActivatorUsedOnBlock(ItemStack stack, EntityPlayer player, World world, PC_CoordI pos);
}
