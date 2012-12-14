package powercraft.core;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;

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
            Gres.openGres("CraftingTool", entityplayer);
        }

        return itemstack;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Crafting Tool", null));
            return names;
		}
		return null;
	}
}
