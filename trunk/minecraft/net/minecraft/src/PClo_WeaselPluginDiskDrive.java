package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * @author MightyPork
 */
@SuppressWarnings("static-access")
public class PClo_WeaselPluginDiskDrive extends PClo_WeaselPlugin implements PC_IInventoryWrapper {

	private IInventory slots = new PClo_WeaselDiskDriveInventory("Drive", 8);
	/** plag taht needs save */
	public boolean isChanged;

	/**
	 * A disk drive
	 * 
	 * @param tew
	 */
	public PClo_WeaselPluginDiskDrive(PClo_TileEntityWeasel tew) {
		super(tew);
	}


	@Override
	public boolean onClick(EntityPlayer player) {
		PC_Utils.openGres(player, new PClo_GuiWeaselDiskDrive(this));
		return true;
	}

	@Override
	public boolean doesProvideFunction(String functionName) {
		return getProvidedFunctionNames().contains(functionName);
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String name, WeaselObject[] args) {

		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (WDT.getType(disk) == WDT.NUMBERLIST || WDT.getType(disk) == WDT.STRINGLIST) {

				if (name.equals(getName() + "." + i + ".get") || name.equals("list." + WDT.getLabel(disk) + ".get")) {
					int index = Calc.toInteger(args[0]);
					return WDT.getListEntry(disk, index);
				}

				continue;
			}
		}

		return null;
	}

	@Override
	public WeaselObject getVariable(String name) {
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (WDT.getType(disk) == WDT.TEXT) {
				if (name.equals(getName() + "." + (i + 1))) return new WeaselString(WDT.getText(disk));
				if (name.equals("text." + WDT.getLabel(disk))) return new WeaselString(WDT.getText(disk));
				continue;
			}

			if (WDT.getType(disk) == WDT.NUMBERLIST || WDT.getType(disk) == WDT.STRINGLIST) {
				if (name.equals(getName() + "." + (i + 1) + ".size")) return new WeaselInteger(WDT.getListLength(disk));
				if (name.equals(getName() + "." + (i + 1) + ".length")) return new WeaselInteger(WDT.getListLength(disk));
				if (name.equals("list." + WDT.getLabel(disk) + ".size")) return new WeaselInteger(WDT.getListLength(disk));
				if (name.equals("list." + WDT.getLabel(disk) + ".length")) return new WeaselInteger(WDT.getListLength(disk));
				continue;
			}
		}
		return null;
	}

	@Override
	public void setVariable(String name, Object object) {
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (WDT.getType(disk) == WDT.TEXT) {
				if (name.equals(getName() + "." + (i + 1)) || name.equals("text." + WDT.getLabel(disk))) {
					WDT.setText(disk, Calc.toString(object));
				}
				continue;
			}
		}
	}

	@Override
	public List<String> getProvidedFunctionNames() {
		List<String> list = new ArrayList<String>(8);

		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (WDT.getType(disk) == WDT.NUMBERLIST || WDT.getType(disk) == WDT.STRINGLIST) {
				list.add(getName() + "." + (i + 1) + ".get");
				list.add("list." + WDT.getLabel(disk) + ".get");
				continue;
			}
		}

		return list;
	}


	@Override
	public List<String> getProvidedVariableNames() {
		List<String> list = new ArrayList<String>(1);

		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (WDT.getType(disk) == WDT.TEXT) {
				list.add(getName() + "." + (i + 1));
				list.add("text." + WDT.getLabel(disk));
				continue;
			}

			if (WDT.getType(disk) == WDT.NUMBERLIST || WDT.getType(disk) == WDT.STRINGLIST) {
				list.add(getName() + "." + (i + 1) + ".size");
				list.add(getName() + "." + (i + 1) + ".length");
				list.add("list." + WDT.getLabel(disk) + ".size");
				list.add("list." + WDT.getLabel(disk) + ".length");
				continue;
			}
		}

		return list;
	}

	@Override
	public int getType() {
		return PClo_WeaselType.DISK_DRIVE;
	}

	@Override
	protected boolean updateTick() {
		if (isChanged) {
			isChanged = false;
			return true;
		}
		return false;
	}

	@Override
	public void onRedstoneSignalChanged() {}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public WeaselEngine getWeaselEngine() {
		return null;
	}

	@Override
	public boolean isMaster() {
		return false;
	}

	@Override
	protected void onNetworkChanged() {}

	@Override
	protected void onDeviceDestroyed() {
		PC_InvUtils.dropInventoryContents(slots, world(), coord());
	}

	@Override
	public Object callFunctionExternalDelegated(String function, Object... args) {
		return null;
	}

	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		PC_InvUtils.loadInventoryFromNBT(tag, "Inv", slots);
		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		PC_InvUtils.saveInventoryToNBT(tag, "Inv", slots);
		return tag;
	}

	@Override
	public void restartDevice() {}

	@Override
	public void onBlockPlaced(EntityLiving entityliving) {}


	@Override
	public void onRandomDisplayTick(Random random) {}

	private ItemStack getDisk(int slot) {
		return slots.getStackInSlot(slot);
	}

	private List<ItemStack> getDisks() {
		List<ItemStack> disks = new ArrayList<ItemStack>();
		for (int i = 0; i < slots.getSizeInventory(); i++) {
			disks.add(getDisk(i));
		}
		return disks;
	}

	/**
	 * Get image disk, if present
	 * 
	 * @param name disk name
	 * @return the disk or null
	 */
	public ItemStack getImageDisk(String name) {
		for (int i = 0; i < slots.getSizeInventory(); i++) {
			if (getDisk(i) == null) continue;
			if (getDiskType(i) == WDT.IMAGE) {
				if (getDiskName(i).equals(name)) return getDisk(i);
				if (name.equals(getName() + "." + (i + 1))) return getDisk(i);
			}
		}
		return null;
	}

	private String getDiskName(int slot) {
		return WDT.getLabel(getDisk(slot));
	}

	private int getDiskType(int slot) {
		return WDT.getType(getDisk(slot));
	}

	private static PClo_ItemWeaselDisk WDT = mod_PClogic.weaselDisk;


	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 1 - 2 * 0.0625F, 1 };
	}


	@Override
	public IInventory getInventory() {
		return slots;
	}
}
