package cpw.mods.fml.common;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public interface ICraftingHandler
{
    void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix);

    void onSmelting(EntityPlayer player, ItemStack item);
}
