package powercraft.api.items;


import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_ClientRegistry;
import powercraft.api.PC_Module;
import powercraft.api.PC_Registry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SuppressWarnings("unused")
public abstract class PC_Item extends Item {

	public final PC_Module module;
	public final PC_ItemInfo itemInfo;


	public PC_Item(int id) {

		super(id);
		itemInfo = getClass().getAnnotation(PC_ItemInfo.class);
		module = PC_Registry.getActiveModule();
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

		PC_ClientRegistry.registerIcons(this, iconRegistry);
	}


	public int getBurnTime(ItemStack fuel) {

		return 0;
	}

}
