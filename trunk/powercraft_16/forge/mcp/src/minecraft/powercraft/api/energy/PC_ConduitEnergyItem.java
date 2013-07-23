package powercraft.api.energy;


import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import powercraft.api.items.PC_ItemInfo;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.conduits.PC_ConduitItem;
import powercraft.api.registries.PC_TextureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@PC_ItemInfo(name = "Energy Conduit", itemid = "energyconduit", defaultid = 17000)
public class PC_ConduitEnergyItem extends PC_ConduitItem {

	private static Data[] data = new Data[16];
	public static PC_ConduitEnergyItem item;


	public PC_ConduitEnergyItem(int id) {

		super(id);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabDecorations);
		data[0] = new Data("Nanotube");
		item = this;
	}


	@Override
	public Class<? extends PC_MultiblockTileEntity> getTileEntityClass() {

		return PC_ConduitEnergyTileEntity.class;
	}


	@Override
	public PC_MultiblockTileEntity getTileEntity(ItemStack itemstack) {

		return new PC_ConduitEnergyTileEntity(itemstack.getItemDamage());
	}


	@Override
	public void registerRecipes() {

	}


	@Override
	public void loadIcons() {

		for (int i = 0; i < data.length; i++) {
			Data d = data[i];
			if (d != null) {
				d.iconItem = PC_TextureRegistry.registerIcon(d.iconName + "_Normal");
			}
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemID, CreativeTabs creativeTabs, List list) {

		list.add(new ItemStack(itemID, 1, 0));
	}


	@Override
	public Icon getIconFromDamage(int metadata) {

		return data[metadata].iconItem;
	}

	public static class Data {

		public final String iconName;
		public Icon iconCorner;
		public Icon iconNormal;
		public Icon iconConnection[] = new Icon[4];
		public Icon iconItem;


		public Data(String iconName) {

			this.iconName = iconName;
		}

	}


	@Override
	@SideOnly(Side.CLIENT)
	public void loadMultiblockItem() {

		for (int i = 0; i < data.length; i++) {
			Data d = data[i];
			if (d != null) {
				d.iconCorner = PC_TextureRegistry.registerIcon(d.iconName + "_Corner", itemInfo.itemid());
				d.iconNormal = PC_TextureRegistry.registerIcon(d.iconName + "_Normal", itemInfo.itemid());
				d.iconConnection[0] = PC_TextureRegistry.registerIcon(d.iconName + "_Connection_None", itemInfo.itemid());
				d.iconConnection[1] = PC_TextureRegistry.registerIcon(d.iconName + "_Connection_Input", itemInfo.itemid());
				d.iconConnection[2] = PC_TextureRegistry.registerIcon(d.iconName + "_Connection_Output", itemInfo.itemid());
				d.iconConnection[3] = PC_TextureRegistry.registerIcon(d.iconName + "_Connection", itemInfo.itemid());
			}
		}
	}


	public static Data getData(int type) {

		return data[type];
	}

}
