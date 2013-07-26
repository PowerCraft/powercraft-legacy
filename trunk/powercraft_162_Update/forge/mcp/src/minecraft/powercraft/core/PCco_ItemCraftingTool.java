package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCco_ItemCraftingTool extends PC_Item{
    public PCco_ItemCraftingTool(int id){
    	super(id, "craftingtool");
        setMaxDamage(0);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer){
        if (!world.isRemote){
            PC_GresRegistry.openGres("CraftingTool", entityplayer, null);
        }

        return itemstack;
    }

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Crafting Tool"));
		return names;
	}
}
