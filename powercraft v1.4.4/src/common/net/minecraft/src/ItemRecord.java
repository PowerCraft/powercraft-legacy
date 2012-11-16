package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRecord extends Item
{
    private static final Map field_90044_b = new HashMap();

    /** The name of the record. */
    public final String recordName;

    protected ItemRecord(int par1, String par2Str)
    {
        super(par1);
        this.recordName = par2Str;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        field_90044_b.put(par2Str, this);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.getBlockId(par4, par5, par6) == Block.jukebox.blockID && par3World.getBlockMetadata(par4, par5, par6) == 0)
        {
            if (par3World.isRemote)
            {
                return true;
            }
            else
            {
                ((BlockJukeBox)Block.jukebox).func_85106_a(par3World, par4, par5, par6, par1ItemStack);
                par3World.playAuxSFXAtEntity((EntityPlayer)null, 1005, par4, par5, par6, this.shiftedIndex);
                --par1ItemStack.stackSize;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(this.func_90043_g());
    }

    @SideOnly(Side.CLIENT)
    public String func_90043_g()
    {
        return "C418 - " + this.recordName;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.rare;
    }

    @SideOnly(Side.CLIENT)
    public static ItemRecord func_90042_d(String par0Str)
    {
        return (ItemRecord)field_90044_b.get(par0Str);
    }
}
