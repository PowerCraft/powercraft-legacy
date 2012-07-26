package net.minecraft.src;


import java.util.List;

import weasel.obj.WeaselVariableMap;


/**
 * Disk for weasel
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemWeaselDisk extends Item {
	
	public static final int EMPTY=0, TEXT=1, IMAGE=2, NUMBERLIST=3, STRINGLIST=4, VARMAP=5;
	/** bg texture */
	public int textureBg;
	/** fg (label) texture */
	public int textureFg;

	/**
	 * @param i ID
	 */
	public PClo_ItemWeaselDisk(int i) {
		super(i);
		setMaxDamage(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Label", PC_Lang.tr("pc.weasel.disk.new_label"));
		tag.setInteger("Type", EMPTY);
		itemstack.setTagCompound(tag);
	}

	
	@Override
	public void addInformation(ItemStack itemstack, List list) {
		if(!itemstack.hasTagCompound()) {
			list.add(PC_Lang.tr("pc.weasel.disk.empty"));
			return;
		}
		switch(itemstack.getTagCompound().getInteger("Type")) {
			case EMPTY:
				list.add(PC_Lang.tr("pc.weasel.disk.empty"));
				break;
			case TEXT:
				list.add(PC_Lang.tr("pc.weasel.disk.text"));
				break;
			case IMAGE:
				list.add(PC_Lang.tr("pc.weasel.disk.image"));
				break;
			case NUMBERLIST:
				list.add(PC_Lang.tr("pc.weasel.disk.numberlist"));
				break;
			case STRINGLIST:
				list.add(PC_Lang.tr("pc.weasel.disk.stringlist"));
				break;
			case VARMAP:
				list.add(PC_Lang.tr("pc.weasel.disk.varmap"));
				break;
		}
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		if(!itemstack.hasTagCompound()) return PC_Lang.tr("pc.weasel.disk.new_label");
		return itemstack.getTagCompound().getString("Label");
	}
	
	/**
	 * Erase all disk data, only preserve label, set type to EMPTY
	 * @param itemstack
	 */
	public static void eraseDisk(ItemStack itemstack) {
		String label = getLabel(itemstack);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Label", label);
		tag.setInteger("Type", EMPTY);
		itemstack.setTagCompound(tag);
	}
	
	/**
	 * Format disk to given type.
	 * 
	 * @param itemstack
	 * @param type type, constants from PClo_ItemWeaselDisk
	 */
	public static void formatDisk(ItemStack itemstack, int type) {
		eraseDisk(itemstack);
		NBTTagCompound tag = itemstack.getTagCompound();
		switch(type) {
			case EMPTY:
				return;
				
			case TEXT:
				tag.setString("Text", "");
				return;
				
			case IMAGE:
				tag.setInteger("Width", 8);
				tag.setInteger("Height", 8);
				
				NBTTagCompound data = new NBTTagCompound();
				for(int x=0; x<8; x++)
					for(int y=0; y<8; y++)
						data.setInteger("p"+x+"_"+y, -1);
				
				tag.setCompoundTag("Data", data);				
				return;
				
			case STRINGLIST:
			case NUMBERLIST:				
				tag.setString("ListData", "");
				tag.setString("ListDelimiter", ",");				
				return;
				
			case VARMAP:				
				WeaselVariableMap map = new WeaselVariableMap();
				tag.setCompoundTag("Map",map.writeToNBT(new NBTTagCompound()));
				return;
		}
	}
	
	/**
	 * Get text
	 * @param itemstack
	 * @return text
	 */
	public static String getText(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != TEXT) return null;
		return itemstack.getTagCompound().getString("Text");
	}
	
	/**
	 * Set text
	 * @param itemstack
	 * @param text text
	 */
	public static void setText(ItemStack itemstack, String text) {
		if(itemstack.getTagCompound().getInteger("Type") != TEXT) return;
		itemstack.getTagCompound().setString("Text",text);
	}
	
	
	
	// list manipulation
	
	/**
	 * Get list - array of strings. You must trim thyem and try to convert to correct data type
	 * @param itemstack stack
	 * @return strings
	 */
	public static String[] getListEntires(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return null;
		return itemstack.getTagCompound().getString("ListData").split(itemstack.getTagCompound().getString("ListDelimiter"));
	}
	
	/**
	 * Get list raw string with delimiter-separated entries
	 * @param itemstack stack
	 * @return raw list
	 */
	public static String getList(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return null;
		return itemstack.getTagCompound().getString("ListData");
	}
	
	/**
	 * Get list delimiter, usually ","
	 * @param itemstack stack
	 * @return delimiter
	 */
	public static String getListDelimiter(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return null;
		return itemstack.getTagCompound().getString("ListDelimiter");
	}
	
	/**
	 * Set list - string with delimiter-separated entries
	 * @param itemstack
	 * @param listtext "entry,entry,entry"
	 * @param delimiter ","
	 */
	public static void setList(ItemStack itemstack, String listtext, String delimiter) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return;
		itemstack.getTagCompound().setString("ListData",listtext);
		itemstack.getTagCompound().setString("ListDelimiter",delimiter);
	}
	
	
	
	// set drive label
	
	/**
	 * Get drive label
	 * @param itemstack
	 * @return the label
	 */
	public static String getLabel(ItemStack itemstack) {
		return itemstack.getTagCompound().getString("Label");
	}
	
	/**
	 * Set drive label
	 * @param itemstack
	 * @param text the label
	 */
	public static void setLabel(ItemStack itemstack, String text) {
		itemstack.getTagCompound().setString("Label",text);
	}
	
	
	
	// manipulate the itemstack color
	
	/**
	 * Get color - internally stored as 0xFFF = 0xF0F0F0
	 * @param itemstack
	 * @return color hex
	 */
	public static int getColor(ItemStack itemstack) {
		return makeHexFromDamage(itemstack.getItemDamage());
	}
	
	/**
	 * Set color
	 * @param itemstack
	 * @param hexColor color hex
	 */
	public static void setColor(ItemStack itemstack, int hexColor) {
		itemstack.setItemDamage(makeDamageFromHex(hexColor));
	}
	
	
	
	/**
	 * Get texture from damage and pass
	 * @param dmg damage
	 * @param pass pass 0-1
	 */
    @Override
	public int func_46057_a(int dmg, int pass)
    {
        return pass==0?textureBg:textureFg;
    }
    
    /**
     * render double pass
     */
    @Override
	public boolean func_46058_c()
    {
        return true;
    }
    
    /**
     * make true hex color from damage
     * @param dmg damage
     * @return hex
     */
    public static int makeHexFromDamage(int dmg) {
    	int color = dmg&0xFFF;
    	int r=color>>8 & 0xF;
    	int g=color>>4 & 0xF;
    	int b=color & 0xF;
    	return r<<20 | g<<12 |b<<4*1;  
    }
    
    /**
     * Convert full hex to damage (0xF0F0F0 -> 0xFFF)
     * @param hex hex color
     * @return damage
     */
    public static int makeDamageFromHex(int hex) {
    	int color = hex&0xF0F0F0;
    	int r=color>>20 & 0xF;
    	int g=color>>12 & 0xF;
    	int b=color & 0xF;
    	return r<<8 | g<<4 |b;  
    }
   
    
    @Override
    public int getColorFromDamage(int dmg, int pass) {
    	if(pass==0) return 0xffffff;
    	return makeHexFromDamage(dmg);
    }
}
