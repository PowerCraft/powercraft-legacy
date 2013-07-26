package powercraft.api.blocks;


import powercraft.apiOld.PC_CreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;


public class PC_ItemBlock extends ItemBlock {

	public PC_ItemBlock(int id) {

		super(id);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		if(getCreativeTab()==null)
			return new CreativeTabs[]{};
		return new CreativeTabs[]{ getCreativeTab(), PC_CreativeTab.getCrativeTab()};
	}
	
}
