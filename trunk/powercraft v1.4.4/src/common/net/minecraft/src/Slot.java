package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class Slot
{
    private final int slotIndex;

    public final IInventory inventory;

    public int slotNumber;

    public int xDisplayPosition;

    public int yDisplayPosition;

    protected int backgroundIconIndex = -1;

    protected String texture = "/gui/items.png";

    public Slot(IInventory par1IInventory, int par2, int par3, int par4)
    {
        this.inventory = par1IInventory;
        this.slotIndex = par2;
        this.xDisplayPosition = par3;
        this.yDisplayPosition = par4;
    }

    public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        if (par1ItemStack != null && par2ItemStack != null)
        {
            if (par1ItemStack.itemID == par2ItemStack.itemID)
            {
                int var3 = par2ItemStack.stackSize - par1ItemStack.stackSize;

                if (var3 > 0)
                {
                    this.onCrafting(par1ItemStack, var3);
                }
            }
        }
    }

    protected void onCrafting(ItemStack par1ItemStack, int par2) {}

    protected void onCrafting(ItemStack par1ItemStack) {}

    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        this.onSlotChanged();
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return true;
    }

    public ItemStack getStack()
    {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean getHasStack()
    {
        return this.getStack() != null;
    }

    public void putStack(ItemStack par1ItemStack)
    {
        this.inventory.setInventorySlotContents(this.slotIndex, par1ItemStack);
        this.onSlotChanged();
    }

    public void onSlotChanged()
    {
        this.inventory.onInventoryChanged();
    }

    public int getSlotStackLimit()
    {
        return this.inventory.getInventoryStackLimit();
    }

    public ItemStack decrStackSize(int par1)
    {
        return this.inventory.decrStackSize(this.slotIndex, par1);
    }

    public boolean isSlotInInventory(IInventory par1IInventory, int par2)
    {
        return par1IInventory == this.inventory && par2 == this.slotIndex;
    }

    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    public int getBackgroundIconIndex()
    {
        return backgroundIconIndex;
    }

    public String getBackgroundIconTexture()
    {
        return (texture == null ? "/gui/items.png" : texture);
    }

    public void setBackgroundIconIndex(int iconIndex)
    {
        backgroundIconIndex = iconIndex;
    }

    public void setBackgroundIconTexture(String textureFilename)
    {
        texture = textureFilename;
    }

    public int getSlotIndex()
    {
        return slotIndex;
    }
}
