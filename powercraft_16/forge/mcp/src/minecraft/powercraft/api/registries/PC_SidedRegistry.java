package powercraft.api.registries;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import powercraft.api.blocks.PC_IBlock;
import powercraft.api.items.PC_Item;

@SuppressWarnings("unused")
class PC_SidedRegistry {
	
	protected PC_SidedRegistry(){}

	protected void registerLanguage(String key, String value){}
	
	protected void registerIcons(PC_IBlock block, IconRegister iconRegistry) {}

	protected void registerIcons(PC_Item item, IconRegister iconRegistry) {}

	protected Icon registerIcon(String icon, String objectName) {
		return null;
	}
	
	
	
}
