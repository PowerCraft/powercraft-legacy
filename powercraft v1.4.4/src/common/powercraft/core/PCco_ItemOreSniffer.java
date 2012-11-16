package powercraft.core;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PCco_ItemOreSniffer extends PC_Item {

	public PCco_ItemOreSniffer(int id){
		super(id);
		setMaxStackSize(1);
		setMaxDamage(500);
		setIconIndex(1);
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		
		if(world.isRemote)
			PC_Utils.openGres("OreSnifferResultScreen", entityplayer, i, j, k, l);

		itemstack.damageItem(1, entityplayer);

		return false;
	}
	
	@Override
	public String[] getDefaultNames() {
		return new String[]{getItemName(), "Ore Sniffer"};
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b) {
		list.add(PC_Utils.tr("pc.sniffer.desc"));
	}
	
}
