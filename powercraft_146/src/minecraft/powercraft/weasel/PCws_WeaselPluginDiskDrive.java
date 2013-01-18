package powercraft.weasel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import powercraft.management.PC_Color;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;
import powercraft.weasel.PCws_WeaselBitmapUtils.WeaselBitmapProvider;
import weasel.Calc;
import weasel.InstructionList;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.lang.InstructionFunction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselDouble;
import weasel.obj.WeaselNull;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginDiskDrive extends PCws_WeaselPlugin implements PCws_IWeaselInventory {

	private ItemStack inv[] = new ItemStack[8];
	
	private class ImageEditor implements WeaselBitmapProvider {
		private ItemStack stack;
		
		public ImageEditor(ItemStack imageDisk) {
			stack = imageDisk;
		}
		
		@Override
		public PC_VecI getBitmapSize() {
			return PCws_ItemWeaselDisk.getImageSize(stack);
		}

		@Override
		public int getBitmapPixel(int x, int y) {
			return PCws_ItemWeaselDisk.getImageColorAt(stack, new PC_VecI(x, y));
		}

		@Override
		public void setBitmapPixel(int x, int y, int color) {
			PCws_ItemWeaselDisk.setImageColorAt(stack, new PC_VecI(x, y), color);
		}

		@Override
		public void resize(int w, int h) {
			PCws_ItemWeaselDisk.setImageSize(stack, new PC_VecI(w, h));
		}

		@Override
		public void notifyChanges() {}
		
		public WeaselBitmapProvider getImageForName(String name){
			ItemStack disk = null;
			if(getNetwork()!=null){
				for (PCws_WeaselPlugin member : getNetwork()) {
					if (member instanceof PCws_WeaselPluginDiskDrive) {
						disk = ((PCws_WeaselPluginDiskDrive) member).getImageDisk(name);
						if (disk != null) break;
					}
				}
			}
			if(disk == null) return null;
			
			return new ImageEditor(disk);
		}
		
	}
	
	@Override
	public WeaselFunctionManager makePluginProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected PCws_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		NBTTagList nbtList = tag.getTagList("inv");
		for (int i = 0; i < nbtList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtList.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;

            if (j >= 0 && j < inv.length)
            {
            	inv[i] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

		return this;
	}

	@Override
	protected NBTTagCompound writePluginToNBT(NBTTagCompound tag) {
		NBTTagList nbtList = new NBTTagList();
		for (int i = 0; i < inv.length; i++)
        {
            if (inv[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                inv[i].writeToNBT(nbttagcompound1);
                nbtList.appendTag(nbttagcompound1);
            }
        }
		tag.setTag("inv", nbtList);
		return tag;
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
	
	/**
	 * Get image disk, if present
	 * 
	 * @param name disk name
	 * @return the disk or null
	 */
	private ItemStack getImageDisk(String name) {
		for (int i = 0; i < inv.length; i++) {
			if (getDisk(i) == null) continue;
			if (getDiskType(i) == PCws_ItemWeaselDisk.IMAGE) {
				if (getDiskName(i).equals(name)) return getDisk(i);
				if (name.equals(""+(i + 1))) return getDisk(i);
			}
		}
		return null;
	}
	
	private String getDiskName(int slot) {
		return PCws_ItemWeaselDisk.getLabel(getDisk(slot));
	}

	private int getDiskType(int slot) {
		return PCws_ItemWeaselDisk.getType(getDisk(slot));
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
		if(var2!=null){
			if(getNetwork()!=null){
				WeaselString diskName = new WeaselString(PCws_ItemWeaselDisk.getLabel(var2));
				String diskType = PCws_ItemWeaselDisk.getTypeString(var2);
				if(!getNetwork().callFunctionOnEngine("newDisk."+getName()+"."+diskType, diskName)){
					WeaselString wdiskType = new WeaselString(diskType);
					if(!getNetwork().callFunctionOnEngine("newDisk."+getName(), diskName, wdiskType)){
						WeaselString name = new WeaselString(getName());
						getNetwork().callFunctionOnEngine("newDisk", name, diskName, wdiskType);
					}
				}
			}
		}
	}

	public List<Instruction> getLibaryInstructions(String name) {
		for(int i=0; i<8; i++){
			if(getDisk(i)!=null){
				if(getDiskType(i)==PCws_ItemWeaselDisk.LIBRARY){
					if(name.equals(getDiskName(i))){
						return PCws_ItemWeaselDisk.getLibraryInstructions(getDisk(i));
					}
				}
			}
		}
		return null;
	}

	public List<String> getAllLibaryFunctions() {
		List<String> list = new ArrayList<String>();
		for(int i=0; i<8; i++){
			if(getDisk(i)!=null){
				if(getDiskType(i)==PCws_ItemWeaselDisk.LIBRARY){
					String libName = getDiskName(i);
					List<Instruction> ilist = PCws_ItemWeaselDisk.getLibraryInstructions(getDisk(i));
					for(Instruction in : ilist) {
						if(in instanceof InstructionFunction) {
							list.add(libName+"."+((InstructionFunction) in).getFunctionName());
						}
					}
				}
			}
		}
		return list;
	}
	
}
