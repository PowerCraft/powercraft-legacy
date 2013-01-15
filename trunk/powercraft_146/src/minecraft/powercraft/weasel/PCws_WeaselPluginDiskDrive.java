package powercraft.weasel;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	@Override
	protected List<String> getProvidedPluginFunctionNames() {
		List<String> list = new ArrayList<String>(8);
		List<String> listBM = new PCws_WeaselBitmapUtils.WeaselBitmapAdapter(null).getProvidedFunctionNames();
		
		list.add("typeOf");
		
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
					list.add((i+1) + "." + s);
				}
				continue;
			}
		}
		return list;
	}

	@Override
	protected WeaselObject callProvidedPluginFunction(WeaselEngine engine, String functionName, WeaselObject[] args) {
		
		List<ItemStack> disks = getDisks();
		
		if(functionName.equals("typeOf")){
			ItemStack disk=null;
			if(args[0] instanceof WeaselDouble){
				int num = Calc.toInteger(args[0]);
				disk = disks.get(num-1);
			}else{
				String name = Calc.toString(args[0]);
				for(ItemStack is:disks){
					if(is!=null){
						if(PCws_ItemWeaselDisk.getLabel(is).equals(name)){
							disk = is;
							break;
						}
					}
				}
			}
			return new WeaselString(PCws_ItemWeaselDisk.getTypeString(disk));
		}
		
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;
			

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.IMAGE) {
				String[] funcSplitName = functionName.split("\\.", 2);
				if(funcSplitName.length==2){
					if(funcSplitName[0].equals("img")){
						funcSplitName = funcSplitName[1].split("\\.", 2);
					}
					if(funcSplitName.length==2){
						if(funcSplitName[0].equals(""+(i+1)) || funcSplitName[0].equals(PCws_ItemWeaselDisk.getLabel(disk))){
						
							try {
								WeaselObject obj = new PCws_WeaselBitmapUtils.WeaselBitmapAdapter(new ImageEditor(disk)).callProvidedFunction(engine, funcSplitName[1], args);
								return obj;
							}catch(IllegalAccessError e){
								try {
									WeaselObject obj = new PCws_WeaselBitmapUtils.WeaselBitmapAdapter(new ImageEditor(disk)).callProvidedFunction(engine, funcSplitName[1], args);
									return obj;
								}catch(IllegalAccessError e2){
									continue;
								}
							}
						}
					}
				}
					
			}
			
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.VARMAP) {

				if (functionName.equals((i+1) + ".get") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".get")) {
					WeaselObject obj = PCws_ItemWeaselDisk.getMapVariable(disk, Calc.toString(args[0]));
					if(obj == null) throw new WeaselRuntimeException(PCws_ItemWeaselDisk.getLabel(disk)+" does not contain variable "+args[0]);
					return obj;
				}

				if (functionName.equals((i+1) + ".clear") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".clear")) {
					PCws_ItemWeaselDisk.eraseVarMap(disk);
					return null;
				}
				
				if (functionName.equals((i+1) + ".set") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".set")) {
					PCws_ItemWeaselDisk.setMapVariable(disk, Calc.toString(args[0]), args[1]);
					return null;
				}
				
				if (functionName.equals((i+1) + ".has") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".has")) {
					return new WeaselBoolean(PCws_ItemWeaselDisk.hasMapVariable(disk, Calc.toString(args[0])));
				}
				
				if (functionName.equals((i+1) + ".unset") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".unset")||functionName.equals(i + ".remove") || functionName.equals("map." + PCws_ItemWeaselDisk.getLabel(disk) + ".remove")) {
					PCws_ItemWeaselDisk.removeMapVariable(disk, Calc.toString(args[0]));
					return null;
				}
				
			}

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.NUMBERLIST || PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.STRINGLIST) {

				if (functionName.equals((i+1) + ".get") || functionName.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".get")) {
					int index = Calc.toInteger(args[0]);
					return PCws_ItemWeaselDisk.getListEntry(disk, index);
				}
				
				if (functionName.equals((i+1) + ".add") || functionName.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".add")) {
					String delim = PCws_ItemWeaselDisk.getListDelimiter(disk);
					
					String[] pieces = PCws_ItemWeaselDisk.getListEntries(disk);
				
					if(PCws_ItemWeaselDisk.getListText(disk).length()>10000) {
						throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": disk is full (10kB).");
					}
					
					String s = PCws_ItemWeaselDisk.getListText(disk);
					
					if(s.length()>0) s +=delim;

					if(pieces.length!=0 && pieces.length%10==0 && !delim.equals("\\n")) {
						s+="\n";
					}
					
					s += Calc.toString(args[0]);
					if(s.length()>10000) {
						throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": can't add, disk is full (10kB).");
					}
					PCws_ItemWeaselDisk.setListText(disk, s, delim);
					
					engine.requestPause();
					return null;
				}
				
				// set remove clear
				
				if (functionName.equals((i+1) + ".set") || functionName.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".set")) {
					
					int pos = Calc.toInteger(args[0]);
					String obj = Calc.toString(args[1]);	
					if(PCws_ItemWeaselDisk.getType(disk)==PCws_ItemWeaselDisk.NUMBERLIST) {						
						obj = ""+Calc.toDouble(args[1]);
					}
					String delim = PCws_ItemWeaselDisk.getListDelimiter(disk);
					
					String[] pieces = PCws_ItemWeaselDisk.getListEntries(disk);
					
					if(pos < 0||pos > pieces.length) {
						throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": index "+pos+" out of bounds.");
					}
					if(pos == pieces.length) {
						if(PCws_ItemWeaselDisk.getListText(disk).length()>10000) {
							throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": disk is full (10kB).");
						}
						
						String s = PCws_ItemWeaselDisk.getListText(disk);

						if(s.length()>0) s +=delim;

						if(pieces.length!=0 && pieces.length%10==0 && !delim.equals("\\n")) {
							s+="\n";
						}
						
						s += Calc.toString(args[0]);
						if(s.length()>10000) {
							throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": can't add, disk is full (10kB).");
						}
						PCws_ItemWeaselDisk.setListText(disk, s, delim);
					}
					
					
					pieces[pos] = obj;

					String all = "";
					int n=-1;
					for(String piece:pieces) {
						n++;
						if(all.length()>0) {
							all+=delim;
							if(n!=0 && n%10==0 && !delim.equals("\\n")) {
								all+="\n";
							}
						}
						all += piece.trim();
					}
					
					PCws_ItemWeaselDisk.setListText(disk, all, delim);

					engine.requestPause();
					return null;
				}

				
				if (functionName.equals((i+1) + ".remove") || functionName.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".remove")) {
					
					int pos = Calc.toInteger(args[0]);			
					String delim = PCws_ItemWeaselDisk.getListDelimiter(disk);
					
					String[] pieces = PCws_ItemWeaselDisk.getListEntries(disk);
					
					if(pos < 0||pos >= pieces.length) {
						throw new WeaselRuntimeException("List "+PCws_ItemWeaselDisk.getLabel(disk)+": index "+pos+" out of bounds.");
					}
					
					String all = "";
					int n=-1;
					for(String piece:pieces) {
						n++;
						if(n == pos) continue;
						if(all.length()>0) {
							all+=delim;
						}
						all += piece.trim();
						if(n!=0&&n%10==0&&!delim.equals("\n")) {
							all+="\n";
						}
					}
					
					PCws_ItemWeaselDisk.setListText(disk, all, delim);
					engine.requestPause();
					return null;
				}
				
				if (functionName.equals((i+1) + ".clear") || functionName.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".clear")) {					
					String delim = PCws_ItemWeaselDisk.getListDelimiter(disk);					
					PCws_ItemWeaselDisk.setListText(disk, "", delim);
					engine.requestPause();
					return null;
				}
				

				continue;
			}
		}

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
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.TEXT) {
				if (name.equals(""+(i + 1)) || name.equals("text." + PCws_ItemWeaselDisk.getLabel(disk))) {
					PCws_ItemWeaselDisk.setText(disk, Calc.toString(value));
				}
				continue;
			}
			
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.IMAGE) {
				if (name.equals(""+(i + 1)) || name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk))) {
					PC_VecI size = PCws_ItemWeaselDisk.getImageSize(disk);
					PCws_WeaselBitmapUtils.rect(new ImageEditor(disk), 0, 0, size.x-1, size.y-1, PC_Color.getHexColorForName(Calc.toString(value)));
				}
				continue;
			}
		}
	}

	@Override
	protected WeaselObject getPluginVariable(String name) {
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.TEXT) {
				if (name.equals((i + 1))) return new WeaselString(PCws_ItemWeaselDisk.getText(disk));
				if (name.equals("text." + PCws_ItemWeaselDisk.getLabel(disk))) return new WeaselString(PCws_ItemWeaselDisk.getText(disk));
				continue;
			}
			
			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.IMAGE) {
				PC_VecI size = PCws_ItemWeaselDisk.getImageSize(disk);
				// its faster this way for size
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".w")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".W")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".width")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".WIDTH")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".h")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".H")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".height")) return new WeaselDouble(size.x);
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk) + ".HEIGHT")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".w")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".W")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".width")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".WIDTH")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".h")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".H")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".height")) return new WeaselDouble(size.x);
				if (name.equals((i + 1)+ ".HEIGHT")) return new WeaselDouble(size.x);
				
				if (name.equals((i + 1))) return new WeaselNull();
				if (name.equals("img." + PCws_ItemWeaselDisk.getLabel(disk))) return new WeaselNull();
				continue;
			}

			if (PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.NUMBERLIST || PCws_ItemWeaselDisk.getType(disk) == PCws_ItemWeaselDisk.STRINGLIST) {
				if (name.equals((i + 1) + ".size")) return new WeaselDouble(PCws_ItemWeaselDisk.getListLength(disk));
				if (name.equals((i + 1) + ".length")) return new WeaselDouble(PCws_ItemWeaselDisk.getListLength(disk));
				if (name.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".size")) return new WeaselDouble(PCws_ItemWeaselDisk.getListLength(disk));
				if (name.equals("list." + PCws_ItemWeaselDisk.getLabel(disk) + ".length")) return new WeaselDouble(PCws_ItemWeaselDisk.getListLength(disk));
				continue;
			}
		}
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
