package powercraft.core;

import java.util.HashSet;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PCco_ItemActivator extends PC_Item {
	
	public PCco_ItemActivator(int id){
		super(id);
		setMaxDamage(100);
		setMaxStackSize(1);
		setIconIndex(2);
		setItemName("PCcoActivatorItem");
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {

		List<PC_IActivatorListener>listeners = PC_ActivatorListener.getListeners();
		
		for (PC_IActivatorListener listener : listeners) {

			if (listener.onActivatorUsedOnBlock(itemstack, entityplayer, world, new PC_CoordI(i, j, k))) {
				return true;
			}

		}
		return false;
	}
	
	@Override
	public String getTextureFile() {
		return mod_PowerCraftCore.getInstance().getTerrainFile();
	}
	
	@Override
	public String[] getDefaultNames() {
		return new String[]{getItemName(), "Activator Crystal"};
	}

	public String getCraftingToolModule() {
		return mod_PowerCraftCore.getInstance().getNameWithoutPowerCraft();
	}
	
}
