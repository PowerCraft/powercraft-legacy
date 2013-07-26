package powercraft.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class PC_CreativeTab extends CreativeTabs {

	private static PC_CreativeTab creativeTab;
	
	public static PC_CreativeTab getCrativeTab(){
		if(creativeTab==null)
			creativeTab = new PC_CreativeTab();
		return creativeTab;
	}
	
	private PC_CreativeTab() {
		super("PowerCraft");
	}

	@Override
	public Item getTabIconItem() {
		return super.getTabIconItem();
	}
	
}
