package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

class SlotArmor extends Slot
{
    final int armorType;

    final ContainerPlayer parent;

    SlotArmor(ContainerPlayer par1ContainerPlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6)
    {
        super(par2IInventory, par3, par4, par5);
        this.parent = par1ContainerPlayer;
        this.armorType = par6;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return par1ItemStack == null ? false : (par1ItemStack.getItem() instanceof ItemArmor ? ((ItemArmor)par1ItemStack.getItem()).armorType == this.armorType : (par1ItemStack.getItem().shiftedIndex != Block.pumpkin.blockID && par1ItemStack.getItem().shiftedIndex != Item.skull.shiftedIndex ? false : this.armorType == 0));
    }

    @SideOnly(Side.CLIENT)

    public int getBackgroundIconIndex()
    {
        return 15 + this.armorType * 16;
    }
}
