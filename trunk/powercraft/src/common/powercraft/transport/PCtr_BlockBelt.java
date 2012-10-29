package powercraft.transport;

import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import powercraft.core.PC_Block;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_ISwapTerrain;

public class PCtr_BlockBelt extends PC_Block implements PC_ISwapTerrain,
		PC_ICraftingToolDisplayer {

	public PCtr_BlockBelt(int id){
		super(id, Material.iron);
		setBlockName("PCtrBlockBelt");
		setCreativeTab(CreativeTabs.tabTransport);
	}
	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftTransport.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public String getTerrainFile() {
		return mod_PowerCraftTransport.getInstance().getTerrainFile();
	}

	@Override
	public String getDefaultName() {
		return "belt";
	}

}
