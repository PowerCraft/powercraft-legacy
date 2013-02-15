package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_VecI;
import powercraft.weasel.PCws_WeaselBitmapUtils.WeaselBitmapProvider;
import weasel.Calc;
import weasel.WeaselEngine;
import weasel.WeaselFunctionManager;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.lang.InstructionFunction;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;

public class PCws_WeaselPluginDiskDrive extends PCws_WeaselPlugin implements PCws_IWeaselInventory {

	private ItemStack inv[] = new ItemStack[8];
	
	@Override
	public WeaselFunctionManager makePluginProvider() {
		return new DiskDrivePluginProvider();
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
	public ItemStack getImageDisk(String name) {
		for (int i = 0; i < inv.length; i++) {
			if (getDisk(i) == null) continue;
			if (getDiskType(i) == PCws_ItemWeaselDisk.IMAGE) {
				if (getDiskName(i).equals(name)) return getDisk(i);
				if (name.equals(""+(i + 1))) return getDisk(i);
			}
		}
		return null;
	}
	
	@Override
	public void update() {}

	@Override
	public void syncWithClient(PCws_TileEntityWeasel tileEntityWeasel) {}

	@Override
	protected void openPluginGui(EntityPlayer player) {
		Gres.openGres("WeaselDiskDrive", player, getTE());
	}

	@Override
	public void restart() {}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if(inv[var1]!=null){
			WeaselString diskName = new WeaselString(PCws_ItemWeaselDisk.getLabel(inv[var1]));
			String diskType = PCws_ItemWeaselDisk.getTypeString(var2);
			if(!getNetwork().callFunctionOnEngine("freeDisk."+getName()+"."+diskType, diskName)){
				WeaselString wdiskType = new WeaselString(diskType);
				if(!getNetwork().callFunctionOnEngine("freeDisk."+getName(), diskName, wdiskType)){
					WeaselString name = new WeaselString(getName());
					getNetwork().callFunctionOnEngine("freeDisk", name, diskName, wdiskType);
				}
			}
			if(PCws_ItemWeaselDisk.getType(inv[var1])==PCws_ItemWeaselDisk.LIBRARY){
				String lib = PCws_ItemWeaselDisk.getLabel(inv[var1]);
				for(PCws_IWeaselNetworkDevice plugin:getNetwork()){
					if(plugin instanceof PCws_IWeaselEngine){
						WeaselEngine engine = ((PCws_IWeaselEngine) plugin).getEngine();
						if(engine.libs.containsKey(lib)){
							engine.libs.remove(lib);
						}
					}
				}
			}
		}
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
	
	public int getDiskSlot(String disk){
		for(int i=0; i<inv.length; i++){
			if(disk.equals(PCws_ItemWeaselDisk.getLabel(inv[i]))){
				return i;
			}
		}
		return -1;
	}
	
	public int getDiskType(String disk){
		int slot = getDiskSlot(disk);
		if(slot==-1)
			return PCws_ItemWeaselDisk.EMPTY;
		return getDiskType(slot);
	}
	
	public int getDiskType(int slot){
		return PCws_ItemWeaselDisk.getType(getDisk(slot));
	}
	
	public String getDiskName(int slot){
		return PCws_ItemWeaselDisk.getLabel(getDisk(slot));
	}
	
	public class DiskDrivePluginProvider extends WeaselFunctionManager{
		
		@Override
		public WeaselObject call(WeaselEngine engine, String name, boolean var, WeaselObject... args) throws WeaselRuntimeException {
			if(var){
				return null;
			}else{
				Object ret = null;
				if(name.equals("restart") || name.equals("reset")){
					restartDevice();
				}else if(name.equals("getDiskSlot")){
					ret = getDiskSlot(Calc.toString(args[0]));
				}else if(name.equals("hasDisk")){
					ret = getDiskSlot(Calc.toString(args[0]))!=-1;
				}else{
					int slot = 0;
					if(args[0] instanceof WeaselString){
						slot = getDiskSlot(Calc.toString(args[0]));
						if(slot==-1)
							return null;
					}else{
						slot = Calc.toInteger(args[0]);
					}
					ItemStack disk = getDisk(slot);
					WeaselObject[] newArgs = new WeaselObject[args.length-1];
					for(int i=0; i<newArgs.length; i++){
						newArgs[i] = args[i+1];
					}
					ret = PCws_ItemWeaselDisk.handleFunction(engine, PCws_WeaselPluginDiskDrive.this, disk, name, newArgs);
				}
				return WeaselObject.getWrapperForValue(ret);
			}
		}

		@Override
		public boolean doesProvideFunction(String name) {
			return getProvidedFunctionNames().contains(name);
		}
		
		@Override
		public List<String> getProvidedFunctionNames() {
			List<String> list = new ArrayList<String>();
			list.add("restart");
			list.add("reset");
			list.add("getDiskSlot");
			list.add("hasDisk");
			list.addAll(PCws_ItemWeaselDisk.getAllFunctionNames());
			return list;
		}
		
		@Override
		public List<String> getProvidedVariableNames() {
			return new ArrayList<String>();
		}
		
	}
	
}
