package net.minecraft.src;

/**
 * Light block tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PClo_TileEntityLight extends TileEntity {
	private int color = 1;

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		color = nbttagcompound.getInteger("color");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("color", color);
	}

	/**
	 * Set the light's color index
	 * 
	 * @param c color index
	 */
	public void setColor(int c) {
		color = c;
	}

	/**
	 * Get the light color index
	 * 
	 * @return color index
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Get hex color
	 * 
	 * @param on is glowing
	 * @return hex color
	 */
	public int getHexColor(boolean on) {
		return getHexColor(color, on);
	}

	/**
	 * Get light's color in hex format.
	 * 
	 * @param color_index color index
	 * @param on is light glowing
	 * @return hex color
	 */
	public static int getHexColor(int color_index, boolean on) {
		int rc;
		try {
			rc = PC_Color.light_colors[color_index];
		} catch (ArrayIndexOutOfBoundsException e) {
			rc = 0xf0f0f0;
		}

		int r = (rc & 0xff0000) >> 16;
		int g = (rc & 0x00ff00) >> 8;
		int b = (rc & 0x0000ff);

		if (on) {

		} else {
			// darker
			r *= 0.6D;
			g *= 0.6D;
			b *= 0.6D;
		}

		return (r << 16) + (g << 8) + b;
	}

	/**
	 * @return if forge updates this TE
	 */
	public boolean canUpdate() {
		return false;
	}
}
