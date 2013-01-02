package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_Utils.Gres;
import weasel.WeaselEngine;
import weasel.obj.WeaselObject;

public class PCws_WeaselPluginDiskDrive extends PCws_WeaselPlugin implements PCws_IWeaselInventory {

	private ItemStack inv[] = new ItemStack[8];
	
	@Override
	protected List<String> getProvidedPluginFunctionNames() {
		List<String> list = new ArrayList<String>(8);
		List<String> listBM = new PCws_WeaselBitmapUtils.WeaselBitmapAdapter(null).getProvidedFunctionNames();
		
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.NUMBERLIST || PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.STRINGLIST) {
				list.add((i + 1) + ".get");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".get");
				list.add((i + 1) + ".set");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".set");
				list.add((i + 1) + ".remove");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".remove");
				list.add((i + 1) + ".clear");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".clear");
				list.add((i + 1) + ".add");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".add");
				continue;
			}
			
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.VARMAP) {
				list.add("." + (i + 1) + ".get");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".get");
				list.add("." + (i + 1) + ".set");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".set");
				list.add("." + (i + 1) + ".has");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".has");
				list.add("." + (i + 1) + ".unset");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".unset");
				list.add("." + (i + 1) + ".remove");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".remove");
				list.add("." + (i + 1) + ".clear");
				list.add("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".clear");
				continue;
			}
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.IMAGE) {
				for(String s:listBM){
					list.add("img." + PCws_ItemWeaselDisk.getLabel(disk) + "." + s);
					list.add((i-1) + "." + s);
				}
				continue;
			}
		}

		return list;
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> getProvidedPluginVariableNames() {
		List<String> list = new ArrayList<String>(1);

		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.TEXT) {
				list.add(""+(i + 1));
				list.add("text." + PCws_ItemWeaselDisk.getLabel(disk));
				continue;
			}
			
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.IMAGE) {				
				list.add((i + 1)+".w");
				list.add((i + 1)+".W");
				list.add((i + 1)+".width");
				list.add((i + 1)+".WIDTH");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".w");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".W");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".width");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".WIDTH");	
				list.add((i + 1)+".h");
				list.add((i + 1)+".H");
				list.add((i + 1)+".height");
				list.add((i + 1)+".HEIGHT");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".h");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".H");
				list.add("img" + PCws_ItemWeaselDisk.getLabel(disk)+".height");
				list.add("img." + PCws_ItemWeaselDisk.getLabel(disk)+".HEIGHT");
				continue;
			}

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.NUMBERLIST || PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.STRINGLIST) {
				list.add((i + 1) + ".size");
				list.add((i + 1) + ".length");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".size");
				list.add("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".length");
				continue;
			}
		}

		return list;
	}

	@Override
	protected void setPluginVariable(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ItemStack> getDisks() {
		List<ItemStack> disks = new ArrayList<ItemStack>();
		for (int i = 0; i < inv.length; i++) {
			disks.add(getDisk(i));
		}
		return disks;
	}
	
	private ItemStack getDisk(int slot) {
		return inv[slot];
	}
	
	@Override
	public void update() {}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselDiskDrive", player, getPos().x, getPos().y, getPos().z);
	}

	@Override
	public void restart() {}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv[var1] = var2;
	}

}
