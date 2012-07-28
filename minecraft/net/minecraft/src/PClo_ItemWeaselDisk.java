package net.minecraft.src;


import java.util.List;
import java.util.regex.Pattern;

import weasel.Calc;
import weasel.exception.WeaselRuntimeException;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;


/**
 * Disk for weasel
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemWeaselDisk extends Item {

	@SuppressWarnings("javadoc")
	public static final int EMPTY = 0, TEXT = 1, IMAGE = 2, NUMBERLIST = 3, STRINGLIST = 4;

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
		checkTag(itemstack);
	}

	/**
	 * Get type label for display, localized.
	 * 
	 * @param itemstack disk
	 * @return label
	 */
	public static String getTypeVerbose(ItemStack itemstack) {
		checkTag(itemstack);
		switch (itemstack.getTagCompound().getInteger("Type")) {
			case EMPTY:
				return PC_Lang.tr("pc.weasel.disk.empty");
			case TEXT:
				return PC_Lang.tr("pc.weasel.disk.text");
			case IMAGE:
				return PC_Lang.tr("pc.weasel.disk.image");
			case NUMBERLIST:
				return PC_Lang.tr("pc.weasel.disk.numberlist");
			case STRINGLIST:
				return PC_Lang.tr("pc.weasel.disk.stringlist");
		}
		return "FAILED DISK";
	}


	@Override
	public void addInformation(ItemStack itemstack, List list) {
		checkTag(itemstack);
		list.add(itemstack.getTagCompound().getString("Label"));
	}

	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return getTypeVerbose(itemstack);
	}

	/**
	 * Erase all disk data, only preserve label, set type to EMPTY
	 * 
	 * @param itemstack
	 */
	public static void eraseDisk(ItemStack itemstack) {
		checkTag(itemstack);
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
		checkTag(itemstack);
		eraseDisk(itemstack);
		NBTTagCompound tag = itemstack.getTagCompound();
		setType(itemstack, type);
		switch (type) {
			case EMPTY:
				return;

			case TEXT:
				tag.setString("Text", "");
				return;

			case IMAGE:
				tag.setCompoundTag("Size", new PC_CoordI(8, 8).writeToNBT(new NBTTagCompound()));

				NBTTagCompound data = new NBTTagCompound();
				for (int x = 0; x < 8; x++)
					for (int y = 0; y < 8; y++)
						data.setInteger("p" + x + "_" + y, -1);

				tag.setCompoundTag("Data", data);
				return;

			case STRINGLIST:
			case NUMBERLIST:
				tag.setString("ListData", "");
				tag.setString("ListDelimiter", ",");
				return;
		}
	}

	/**
	 * Get image disk size
	 * 
	 * @param itemstack image disk
	 * @return size
	 */
	public static PC_CoordI getImageSize(ItemStack itemstack) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");
		return new PC_CoordI().readFromNBT(itemstack.getTagCompound().getCompoundTag("Size"));
	}

	/**
	 * Set image disk size
	 * 
	 * @param itemstack image disk
	 * @param size size to set
	 */
	public static void setImageSize(ItemStack itemstack, PC_CoordI size) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");

		itemstack.getTagCompound().setCompoundTag("Size", new PC_CoordI(size.x, size.y).writeToNBT(new NBTTagCompound()));
	}

	/**
	 * Get image color at coord
	 * 
	 * @param itemstack image disk
	 * @param pos coord
	 * @return color hex rgb
	 */
	public static int getImageColorAt(ItemStack itemstack, PC_CoordI pos) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");

		NBTTagCompound data = itemstack.getTagCompound().getCompoundTag("Data");

		PC_CoordI size = getImageSize(itemstack);
		if (pos.x < 0 || pos.y < 0 || pos.x > size.x || pos.y > size.y) throw new WeaselRuntimeException("Image Disk: coordinate out of range");

		return data.getInteger("p" + pos.x + "_" + pos.y);
	}

	/**
	 * Set color of pixel at coord
	 * 
	 * @param itemstack image disk
	 * @param pos coord
	 * @param color color to set
	 */
	public static void setImageColorAt(ItemStack itemstack, PC_CoordI pos, int color) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");

		NBTTagCompound data = itemstack.getTagCompound().getCompoundTag("Data");

		PC_CoordI size = getImageSize(itemstack);
		if (pos.x < 0 || pos.y < 0 || pos.x > size.x || pos.y > size.y) throw new WeaselRuntimeException("Image Disk: coordinate out of range");

		data.setInteger("p" + pos.x + "_" + pos.y, color);
	}

	/**
	 * Set image array
	 * 
	 * @param itemstack image disk
	 * @param data data to set - array of rgb ints or -1
	 */
	public static void setImageData(ItemStack itemstack, int[][] data) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");

		NBTTagCompound tag = itemstack.getTagCompound();

		tag.setCompoundTag("Size", new PC_CoordI(data.length, data[0].length).writeToNBT(new NBTTagCompound()));

		NBTTagCompound dataTag = new NBTTagCompound();
		for (int x = 0; x < data.length; x++)
			for (int y = 0; y < data[0].length; y++)
				dataTag.setInteger("p" + x + "_" + y, data[x][y]);

		tag.setCompoundTag("Data", dataTag);
	}

	/**
	 * Get image array
	 * 
	 * @param itemstack image disk
	 * @return array of rgb ints or -1
	 */
	public static int[][] getImageData(ItemStack itemstack) {
		checkTag(itemstack);
		if (getType(itemstack) != IMAGE) throw new WeaselRuntimeException("Image function called on " + getTypeVerbose(itemstack) + " disk.");

		NBTTagCompound tag = itemstack.getTagCompound();

		PC_CoordI size = getImageSize(itemstack);

		int[][] data = new int[size.x][size.y];

		NBTTagCompound dataTag = tag.getCompoundTag("Data");
		for (int x = 0; x < data.length; x++)
			for (int y = 0; y < data[0].length; y++)
				data[x][y] = dataTag.getInteger("p" + x + "_" + y);

		return data;
	}



	/**
	 * Get text
	 * 
	 * @param itemstack
	 * @return text
	 */
	public static String getText(ItemStack itemstack) {
		checkTag(itemstack);
		if (itemstack.getTagCompound().getInteger("Type") != TEXT)
			throw new WeaselRuntimeException("Text function called on " + getTypeVerbose(itemstack) + " disk.");
		return itemstack.getTagCompound().getString("Text");
	}

	/**
	 * Set text
	 * 
	 * @param itemstack
	 * @param text text
	 */
	public static void setText(ItemStack itemstack, String text) {
		checkTag(itemstack);
		if (getType(itemstack) != TEXT) throw new WeaselRuntimeException("Text function called on " + getTypeVerbose(itemstack) + " disk.");
		itemstack.getTagCompound().setString("Text", text);
	}



	// list manipulation

	/**
	 * Get list - array of strings. You must trim thyem and try to convert to
	 * correct data type
	 * 
	 * @param itemstack stack
	 * @return strings
	 */
	private static String[] getListEntries(ItemStack itemstack) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");
		return itemstack.getTagCompound().getString("ListData").split(Pattern.quote(itemstack.getTagCompound().getString("ListDelimiter")));
	}

	/**
	 * Get length of a list on disk
	 * 
	 * @param itemstack list disk
	 * @return length
	 */
	public static int getListLength(ItemStack itemstack) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");
		return getListEntries(itemstack).length;
	}

	/**
	 * Get list entry at index 0 based
	 * 
	 * @param itemstack list disk
	 * @param entry entry index
	 * @return entry value converted to string or integer
	 */
	public static WeaselObject getListEntry(ItemStack itemstack, int entry) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");

		int size = getListLength(itemstack);
		if (entry < 0 || entry >= size)
			throw new WeaselRuntimeException("Disk: getListEntry called with invalid index " + entry + " (length " + size + ").");

		String str = getListEntries(itemstack)[entry];
		if (getType(itemstack) == NUMBERLIST) {
			return new WeaselInteger(Calc.toInteger(str));
		}
		if (getType(itemstack) == STRINGLIST) {
			return new WeaselInteger(Calc.toString(str));
		}

		return null;
	}

	/**
	 * Get list raw string with delimiter-separated entries
	 * 
	 * @param itemstack stack
	 * @return raw list
	 */
	public static String getListText(ItemStack itemstack) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");
		return itemstack.getTagCompound().getString("ListData");
	}

	/**
	 * Get list delimiter, usually ","
	 * 
	 * @param itemstack stack
	 * @return delimiter
	 */
	public static String getListDelimiter(ItemStack itemstack) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");
		return itemstack.getTagCompound().getString("ListDelimiter");
	}

	/**
	 * Set list - string with delimiter-separated entries
	 * 
	 * @param itemstack
	 * @param listtext "entry,entry,entry"
	 * @param delimiter ","
	 */
	public static void setListText(ItemStack itemstack, String listtext, String delimiter) {
		checkTag(itemstack);
		int type = getType(itemstack);
		if (type != NUMBERLIST && type != STRINGLIST)
			throw new WeaselRuntimeException("List function called on " + getTypeVerbose(itemstack) + " disk.");
		itemstack.getTagCompound().setString("ListData", listtext);
		itemstack.getTagCompound().setString("ListDelimiter", delimiter);
	}

	/**
	 * Get disk type
	 * 
	 * @param itemstack
	 * @return type index
	 */
	public static int getType(ItemStack itemstack) {
		checkTag(itemstack);
		return itemstack.getTagCompound().getInteger("Type");
	}

	/**
	 * Set disk type (does not format)
	 * 
	 * @param itemstack disk
	 * @param type type index
	 */
	public static void setType(ItemStack itemstack, int type) {
		checkTag(itemstack);
		itemstack.getTagCompound().setInteger("Type", type);
	}

	/**
	 * Check tag, create if missing
	 * 
	 * @param stack disk stack
	 */
	public static void checkTag(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if (!stack.getTagCompound().hasKey("Type")) {
				stack.getTagCompound().setInteger("Type", EMPTY);
			}
			if (!stack.getTagCompound().hasKey("Label")) {
				stack.getTagCompound().setString("Label", PC_Lang.tr("pc.weasel.disk.new_label"));
			}
			return;
		} else {
			stack.setTagCompound(new NBTTagCompound());
			checkTag(stack);
		}
	}



	// set drive label

	/**
	 * Get drive label
	 * 
	 * @param itemstack
	 * @return the label
	 */
	public static String getLabel(ItemStack itemstack) {
		checkTag(itemstack);
		return itemstack.getTagCompound().getString("Label");
	}

	/**
	 * Set drive label
	 * 
	 * @param itemstack
	 * @param text the label
	 */
	public static void setLabel(ItemStack itemstack, String text) {
		checkTag(itemstack);
		itemstack.getTagCompound().setString("Label", text);
	}



	// manipulate the itemstack color

	/**
	 * Get color - internally stored as 0xFFF = 0xF0F0F0
	 * 
	 * @param itemstack
	 * @return color hex
	 */
	public static int getColor(ItemStack itemstack) {
		return makeHexFromDamage(itemstack.getItemDamage());
	}

	/**
	 * Set color
	 * 
	 * @param itemstack
	 * @param hexColor color hex
	 */
	public static void setColor(ItemStack itemstack, int hexColor) {
		itemstack.setItemDamage(makeDamageFromHex(hexColor));
	}



	/**
	 * Get texture from damage and pass
	 * 
	 * @param dmg damage
	 * @param pass pass 0-1
	 */
	@Override
	public int func_46057_a(int dmg, int pass) {
		return pass == 0 ? textureBg : textureFg;
	}

	/**
	 * render double pass
	 */
	@Override
	public boolean func_46058_c() {
		return true;
	}

	/**
	 * make true hex color from damage
	 * 
	 * @param dmg damage
	 * @return hex
	 */
	public static int makeHexFromDamage(int dmg) {
		int color = dmg & 0xFFF;
		int r = color >> 8 & 0xF;
		int g = color >> 4 & 0xF;
		int b = color & 0xF;
		return r << 20 | g << 12 | b << 4 * 1;
	}

	/**
	 * Convert full hex to damage (0xF0F0F0 -> 0xFFF)
	 * 
	 * @param hex hex color
	 * @return damage
	 */
	public static int makeDamageFromHex(int hex) {
		int color = hex & 0xF0F0F0;
		int r = color >> 20 & 0xF;
		int g = color >> 12 & 0xF;
		int b = color >> 4 & 0xF;
		return r << 8 | g << 4 | b;
	}


	@Override
	public int getColorFromDamage(int dmg, int pass) {
		if (pass == 0) return 0xffffff;
		return makeHexFromDamage(dmg);
	}
}
