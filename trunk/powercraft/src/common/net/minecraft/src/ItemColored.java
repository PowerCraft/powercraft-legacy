package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemColored extends ItemBlock
{
    private final Block blockRef;
    private String[] blockNames;

    public ItemColored(int par1, boolean par2)
    {
        super(par1);
        this.blockRef = Block.blocksList[this.getBlockID()];

        if (par2)
        {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return this.blockRef.getRenderColor(par1ItemStack.getItemDamage());
    }

    public int getMetadata(int par1)
    {
        return par1;
    }

    public ItemColored setBlockNames(String[] par1ArrayOfStr)
    {
        this.blockNames = par1ArrayOfStr;
        return this;
    }

    @SideOnly(Side.CLIENT)

    public int getIconFromDamage(int par1)
    {
        return this.blockRef.getBlockTextureFromSideAndMetadata(0, par1);
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        if (this.blockNames == null)
        {
            return super.getItemNameIS(par1ItemStack);
        }
        else
        {
            int var2 = par1ItemStack.getItemDamage();
            return var2 >= 0 && var2 < this.blockNames.length ? super.getItemNameIS(par1ItemStack) + "." + this.blockNames[var2] : super.getItemNameIS(par1ItemStack);
        }
    }
}
