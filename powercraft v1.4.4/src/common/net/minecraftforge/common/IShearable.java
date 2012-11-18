package net.minecraftforge.common;

import java.util.ArrayList;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public interface IShearable
{
    public boolean isShearable(ItemStack item, World world, int x, int y, int z);

    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune);
}
