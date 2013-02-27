package powercraft.core;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.item.PC_Item;
import powercraft.management.registry.PC_GresRegistry;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;

public class PCco_ItemCraftingTool extends PC_Item
{
    public PCco_ItemCraftingTool(int id)
    {
    	super(id);
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
        setIconIndex(0);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!world.isRemote)
        {
            PC_GresRegistry.openGres("CraftingTool", entityplayer, null);
        }

        return itemstack;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Crafting Tool"));
            return names;
		}
		return null;
	}
}
