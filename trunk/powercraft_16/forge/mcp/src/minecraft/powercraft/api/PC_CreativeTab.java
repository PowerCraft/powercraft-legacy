package powercraft.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * 
 * PowerCrafts creative tab, shown in the creative menue
 * 
 * @author XOR
 *
 */
public class PC_CreativeTab extends CreativeTabs {

	/**
	 * instance of the tab
	 */
	private static PC_CreativeTab creativeTab;
	
	/**
	 * get the instance, if needed create a new instance
	 * @return the instance
	 */
	public static PC_CreativeTab getCrativeTab(){
		if(creativeTab==null)
			creativeTab = new PC_CreativeTab();
		return creativeTab;
	}
	
	/**
	 * only should be called from {@link PC_CreativeTab}.getCrativeTab()
	 */
	private PC_CreativeTab() {
		super("PowerCraft");
	}

	/**
	 * get the item to display on the tab
	 * @return the item
	 */
	@Override
	public Item getTabIconItem() {
		//TODO choose a good item to display
		return super.getTabIconItem();
	}
	
}
