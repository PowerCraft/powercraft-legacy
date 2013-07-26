package powercraft.api.items;


import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_CreativeTab;
import powercraft.api.PC_Module;
import powercraft.api.registries.PC_ModuleRegistry;
import powercraft.api.registries.PC_TextureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SuppressWarnings("unused")
public abstract class PC_Item extends Item {

	public final PC_Module module;
	public final PC_ItemInfo itemInfo;


	public PC_Item(int id) {

		super(id);
		itemInfo = getClass().getAnnotation(PC_ItemInfo.class);
		module = PC_ModuleRegistry.getActiveModule();
	}


	public PC_Module getModule() {

		return module;
	}


	public PC_ItemInfo getItemInfo() {

		return itemInfo;
	}


	public abstract void registerRecipes();


	public abstract void loadIcons();


	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {

		PC_TextureRegistry.registerIcons(this, iconRegistry);
	}


	public int getBurnTime(ItemStack fuel) {

		return 0;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		if(getCreativeTab()==null)
			return new CreativeTabs[]{};
		return new CreativeTabs[]{ getCreativeTab(), PC_CreativeTab.getCrativeTab()};
	}
	
}
