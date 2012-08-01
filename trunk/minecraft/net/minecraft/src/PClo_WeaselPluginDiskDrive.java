package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.PC_BitmapUtils.WeaselBitmapAdapter;
import net.minecraft.src.PC_BitmapUtils.WeaselBitmapProvider;
import net.minecraft.src.PClo_NetManager.NetworkMember;

import weasel.Calc;
import weasel.WeaselEngine;
import weasel.exception.WeaselRuntimeException;
import weasel.lang.Instruction;
import weasel.lang.InstructionFunction;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselNull;
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
	
	private class ImageEditor implements WeaselBitmapProvider {
		private ItemStack stack;
		
		public ImageEditor(ItemStack imageDisk) {
			stack = imageDisk;
		}

		@Override
		public int[][] getImageForName(String name) {
			ItemStack disk = null;
			for (NetworkMember member : getNetwork().getMembers().values()) {
				if (member instanceof PClo_WeaselPluginDiskDrive) {
					disk = ((PClo_WeaselPluginDiskDrive) member).getImageDisk(name);
					if (disk != null) break;
				}
			}
			if(disk == null) return null;
			
			return PClo_ItemWeaselDisk.getImageData(disk);
		}

		@Override
		public int[][] getScreen() {
			return WDT.getImageData(stack);
		}

		@Override
		public void screenChanged(int[][] newScreen) {
			WDT.setImageData(stack, newScreen);			
		}

		@Override
		public void setScreen(int[][] newdata) {
			WDT.setImageData(stack, newdata);			
		}
		
	}

	@Override
	public WeaselObject callProvidedFunction(WeaselEngine engine, String name, WeaselObject[] args) {

		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;
			

			if (WDT.getType(disk) == WDT.IMAGE) {
				try {
					WeaselObject obj = new PC_BitmapUtils.WeaselBitmapAdapter(new ImageEditor(disk), getName() + "." + (i+1)).callProvidedFunction(engine, name, args);
					return obj;
				}catch(IllegalAccessError e){
					try {
						WeaselObject obj = new PC_BitmapUtils.WeaselBitmapAdapter(new ImageEditor(disk), "img." + WDT.getLabel(disk)).callProvidedFunction(engine, name, args);
						return obj;
					}catch(IllegalAccessError e2){
						continue;
					}
				}
					
			}
			
			if (WDT.getType(disk) == WDT.VARMAP) {

				if (name.equals(getName() + "." + (i+1) + ".get") || name.equals("map." + WDT.getLabel(disk) + ".get")) {
					WeaselObject obj = WDT.getMapVariable(disk, Calc.toString(args[0]));
					if(obj == null) throw new WeaselRuntimeException(WDT.getLabel(disk)+" does not contain variable "+args[0]);
					return obj;
				}

				if (name.equals(getName() + "." + (i+1) + ".clear") || name.equals("map." + WDT.getLabel(disk) + ".clear")) {
					WDT.eraseVarMap(disk);
					return null;
				}
				
				if (name.equals(getName() + "." + (i+1) + ".set") || name.equals("map." + WDT.getLabel(disk) + ".set")) {
					WDT.setMapVariable(disk, Calc.toString(args[0]), args[1]);
					return null;
				}
				
				if (name.equals(getName() + "." + (i+1) + ".has") || name.equals("map." + WDT.getLabel(disk) + ".has")) {
					return new WeaselBoolean(WDT.hasMapVariable(disk, Calc.toString(args[0])));
				}
				
				if (name.equals(getName() + "." + (i+1) + ".unset") || name.equals("map." + WDT.getLabel(disk) + ".unset")||name.equals(getName() + "." + i + ".remove") || name.equals("map." + WDT.getLabel(disk) + ".remove")) {
					WDT.removeMapVariable(disk, Calc.toString(args[0]));
					return null;
				}
				
			}

			if (WDT.getType(disk) == WDT.NUMBERLIST || WDT.getType(disk) == WDT.STRINGLIST) {

				if (name.equals(getName() + "." + (i+1) + ".get") || name.equals("list." + WDT.getLabel(disk) + ".get")) {
					int index = Calc.toInteger(args[0]);
					return WDT.getListEntry(disk, index);
				}
				
				if (name.equals(getName() + "." + (i+1) + ".add") || name.equals("list." + WDT.getLabel(disk) + ".add")) {
					String delim = WDT.getListDelimiter(disk);
					
					String[] pieces = WDT.getListEntries(disk);
				
					if(WDT.getListText(disk).length()>10000) {
						throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": disk is full (10kB).");
					}
					
					String s = WDT.getListText(disk);
					
					if(s.length()>0) s +=delim;

					if(pieces.length!=0 && pieces.length%10==0 && !delim.equals("\\n")) {
						s+="\n";
					}
					
					s += Calc.toString(args[0].get());
					if(s.length()>10000) {
						throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": can't add, disk is full (10kB).");
					}
					WDT.setListText(disk, s, delim);
					
					engine.requestPause();
					return null;
				}
				
				// set remove clear
				
				if (name.equals(getName() + "." + (i+1) + ".set") || name.equals("list." + WDT.getLabel(disk) + ".set")) {
					
					int pos = Calc.toInteger(args[0]);
					String obj = Calc.toString(args[1]);	
					if(WDT.getType(disk)==WDT.NUMBERLIST) {						
						obj = ""+Calc.toInteger(args[1]);
					}
					String delim = WDT.getListDelimiter(disk);
					
					String[] pieces = WDT.getListEntries(disk);
					
					if(pos < 0||pos > pieces.length) {
						throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": index "+pos+" out of bounds.");
					}
					if(pos == pieces.length) {
						if(WDT.getListText(disk).length()>10000) {
							throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": disk is full (10kB).");
						}
						
						String s = WDT.getListText(disk);

						if(s.length()>0) s +=delim;

						if(pieces.length!=0 && pieces.length%10==0 && !delim.equals("\\n")) {
							s+="\n";
						}
						
						s += Calc.toString(args[0].get());
						if(s.length()>10000) {
							throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": can't add, disk is full (10kB).");
						}
						WDT.setListText(disk, s, delim);
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
					
					WDT.setListText(disk, all, delim);

					engine.requestPause();
					return null;
				}

				
				if (name.equals(getName() + "." + (i+1) + ".remove") || name.equals("list." + WDT.getLabel(disk) + ".remove")) {
					
					int pos = Calc.toInteger(args[0]);			
					String delim = WDT.getListDelimiter(disk);
					
					String[] pieces = WDT.getListEntries(disk);
					
					if(pos < 0||pos >= pieces.length) {
						throw new WeaselRuntimeException("List "+WDT.getLabel(disk)+": index "+pos+" out of bounds.");
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
					
					WDT.setListText(disk, all, delim);
					engine.requestPause();
					return null;
				}
				
				if (name.equals(getName() + "." + (i+1) + ".clear") || name.equals("list." + WDT.getLabel(disk) + ".clear")) {					
					String delim = WDT.getListDelimiter(disk);					
					WDT.setListText(disk, "", delim);
					engine.requestPause();
					return null;
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
			
			if (WDT.getType(disk) == WDT.IMAGE) {
				PC_CoordI size = WDT.getImageSize(disk);
				// its faster this way for size
				if (name.equals("img." + WDT.getLabel(disk) + ".w")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".W")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".width")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".WIDTH")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".h")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".H")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".height")) return new WeaselInteger(size.x);
				if (name.equals("img." + WDT.getLabel(disk) + ".HEIGHT")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".w")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".W")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".width")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".WIDTH")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".h")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".H")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".height")) return new WeaselInteger(size.x);
				if (name.equals(getName() + "." + (i + 1)+ ".HEIGHT")) return new WeaselInteger(size.x);
				
				if (name.equals(getName() + "." + (i + 1))) return new WeaselNull();
				if (name.equals("img." + WDT.getLabel(disk))) return new WeaselNull();
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
			
			if (WDT.getType(disk) == WDT.IMAGE) {
				if (name.equals(getName() + "." + (i + 1)) || name.equals("img." + WDT.getLabel(disk))) {
					PC_CoordI size = WDT.getImageSize(disk);
					int[][] data = WDT.getImageData(disk);
					PC_BitmapUtils.rect(data, 0, 0, size.x-1, size.y-1, PC_Color.getHexColorForName(object));
					WDT.setImageData(disk, data);
				}
				continue;
			}
		}
	}
	
	/**
	 * @return list of names provided in existing libraries
	 */
	public List<String> getLibraryFunctionNames(){
		List<String> list = new ArrayList<String>(8);
		List<ItemStack> disks = getDisks();
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;
			
			if (WDT.getType(disk) == WDT.LIBRARY) {
				
				List<Instruction> ilist = WDT.getLibraryInstructions(disk);
				
				for(Instruction in : ilist) {
					if(in instanceof InstructionFunction) {
						list.add(((InstructionFunction) in).getFunctionName());
					}
				}
				
				continue;
			}
		}
		return list;
	}
	

	/**
	 * @return list of all instructions in a library
	 */
	public List<Instruction> getAllLibraryInstructions(){
		List<ItemStack> disks = getDisks();
		List<Instruction> ilist = new ArrayList<Instruction>();
		
		for (int i = 0; i < disks.size(); i++) {
			ItemStack disk = disks.get(i);
			if (disk == null) continue;
			
			if (WDT.getType(disk) == WDT.LIBRARY) {				
				ilist.addAll(WDT.getLibraryInstructions(disk));				
				continue;
			}
		}
		return ilist;
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
				list.add(getName() + "." + (i + 1) + ".set");
				list.add("list." + WDT.getLabel(disk) + ".set");
				list.add(getName() + "." + (i + 1) + ".remove");
				list.add("list." + WDT.getLabel(disk) + ".remove");
				list.add(getName() + "." + (i + 1) + ".clear");
				list.add("list." + WDT.getLabel(disk) + ".clear");
				list.add(getName() + "." + (i + 1) + ".add");
				list.add("list." + WDT.getLabel(disk) + ".add");
				continue;
			}
			
			if (WDT.getType(disk) == WDT.VARMAP) {
				list.add(getName() + "." + (i + 1) + ".get");
				list.add("map." + WDT.getLabel(disk) + ".get");
				list.add(getName() + "." + (i + 1) + ".set");
				list.add("map." + WDT.getLabel(disk) + ".set");
				list.add(getName() + "." + (i + 1) + ".has");
				list.add("map." + WDT.getLabel(disk) + ".has");
				list.add(getName() + "." + (i + 1) + ".unset");
				list.add("map." + WDT.getLabel(disk) + ".unset");
				list.add(getName() + "." + (i + 1) + ".remove");
				list.add("map." + WDT.getLabel(disk) + ".remove");
				list.add(getName() + "." + (i + 1) + ".clear");
				list.add("map." + WDT.getLabel(disk) + ".clear");
				continue;
			}
			if (WDT.getType(disk) == WDT.IMAGE) {
				list.addAll(new PC_BitmapUtils.WeaselBitmapAdapter(null, "img." + WDT.getLabel(disk) ).getProvidedFunctionNames());
				list.addAll(new PC_BitmapUtils.WeaselBitmapAdapter(null, getName() + "." + (i + 1) ).getProvidedFunctionNames());
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
			
			if (WDT.getType(disk) == WDT.IMAGE) {				
				list.add(getName() + "." + (i + 1)+".w");
				list.add(getName() + "." + (i + 1)+".W");
				list.add(getName() + "." + (i + 1)+".width");
				list.add(getName() + "." + (i + 1)+".WIDTH");
				list.add("img." + WDT.getLabel(disk)+".w");
				list.add("img." + WDT.getLabel(disk)+".W");
				list.add("img." + WDT.getLabel(disk)+".width");
				list.add("img." + WDT.getLabel(disk)+".WIDTH");	
				list.add(getName() + "." + (i + 1)+".h");
				list.add(getName() + "." + (i + 1)+".H");
				list.add(getName() + "." + (i + 1)+".height");
				list.add(getName() + "." + (i + 1)+".HEIGHT");
				list.add("img." + WDT.getLabel(disk)+".h");
				list.add("img." + WDT.getLabel(disk)+".H");
				list.add("img" + WDT.getLabel(disk)+".height");
				list.add("img." + WDT.getLabel(disk)+".HEIGHT");
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
	public void callFunctionOnEngine(String function, Object... args) {}

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
