package net.minecraftforge.common;

import net.minecraft.src.ItemStack;

public interface IArmorTextureProvider
{
    public String getArmorTextureFile(ItemStack itemstack);
}
