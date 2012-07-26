package net.minecraft.src;


import java.util.List;


/**
 * Disk for weasel
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemWeaselDisk extends Item {
	
	public static final int EMPTY=0, TEXT=1, IMAGE=2, NUMBERLIST=3, STRINGLIST=4, VARMAP=5;
	public int textureBg;
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


	public static void setChannel(ItemStack itemstack, String channel) {

		NBTTagCompound tag = new NBTTagCompound();

		tag.setString("channel", channel == null ? "default" : channel);

		itemstack.setTagCompound(tag);
		PC_Utils.chatMsg(PC_Lang.tr("pc.radioRemote.connected", new String[] { channel }), true);
	}

	@Override
	public void addInformation(ItemStack itemstack, List list) {
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
		return itemstack.getTagCompound().getString("Label");
	}
	
	public static String getText(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != TEXT) return null;
		return itemstack.getTagCompound().getString("DataText");
	}
	
	public static void setText(ItemStack itemstack, String text) {
		if(itemstack.getTagCompound().getInteger("Type") != TEXT) return;
		itemstack.getTagCompound().setString("DataText",text);
	}
	
	public static String[] getList(ItemStack itemstack) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return null;
		return itemstack.getTagCompound().getString("ListData").split(itemstack.getTagCompound().getString("ListDelimiter"));
	}
	
	public static void setList(ItemStack itemstack, String list) {
		if(itemstack.getTagCompound().getInteger("Type") != NUMBERLIST && itemstack.getTagCompound().getInteger("Type") != STRINGLIST) return;
		itemstack.getTagCompound().setString("ListData",list);
	}
	
	public static String getLabel(ItemStack itemstack) {
		return itemstack.getTagCompound().getString("Label");
	}
	
	public static void setLabel(ItemStack itemstack, String text) {
		itemstack.getTagCompound().setString("Label",text);
	}
	
	public static int getColor(ItemStack itemstack) {
		return makeHexFromDamage(itemstack.getItemDamage());
	}
	
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
    public boolean func_46058_c()
    {
        return true;
    }
    
    public static int makeHexFromDamage(int dmg) {
    	int color = dmg&0xFFF;
    	int r=color>>8 & 0xF;
    	int g=color>>4 & 0xF;
    	int b=color & 0xF;
    	return r<<20 | g<<12 |b<<4*1;  
    }
    
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
