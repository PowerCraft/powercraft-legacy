package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap extends ItemMapBase
{
    protected ItemEmptyMap(int par1)
    {
        super(par1);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ItemStack itemstack1 = new ItemStack(Item.map, 1, par2World.getUniqueDataId("map"));
        String s = "map_" + itemstack1.getItemDamage();
        MapData mapdata = new MapData(s);
        par2World.setItemData(s, mapdata);
        mapdata.scale = 0;
        int i = 128 * (1 << mapdata.scale);
        mapdata.xCenter = (int)(Math.round(par3EntityPlayer.posX / (double)i) * (long)i);
        mapdata.zCenter = (int)(Math.round(par3EntityPlayer.posZ / (double)i) * (long)i);
        mapdata.dimension = (byte)par2World.provider.dimensionId;
        mapdata.markDirty();
        --par1ItemStack.stackSize;

        if (par1ItemStack.stackSize <= 0)
        {
            return itemstack1;
        }
        else
        {
            if (!par3EntityPlayer.inventory.addItemStackToInventory(itemstack1.copy()))
            {
                par3EntityPlayer.dropPlayerItem(itemstack1);
            }

            return par1ItemStack;
        }
    }
}
