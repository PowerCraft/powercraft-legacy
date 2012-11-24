package net.minecraftforge.common;

import net.minecraft.src.IInventory;

public interface ISidedInventory extends IInventory
{
    int getStartInventorySide(ForgeDirection side);

    int getSizeInventorySide(ForgeDirection side);
}
