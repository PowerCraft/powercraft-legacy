package net.minecraft.src;


/**
 * Light block tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityLight extends PC_TileEntity {
	private int color = 1;
	/** flag that this light is lamp, and not indicator */
	public boolean isStable;
	/** flag that this light huge */
	public boolean isHuge;

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		color = nbttagcompound.getInteger("color");
		isStable = nbttagcompound.getBoolean("stable");
		isHuge = nbttagcompound.getBoolean("huge");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("color", color);
		nbttagcompound.setBoolean("stable", isStable);
		nbttagcompound.setBoolean("huge", isHuge);
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
	 * @return true if light is glowing
	 */
	public boolean isActive() {
		return getCoord().getId(worldObj) == mod_PClogic.lightOn.blockID;
	}

	/**
	 * Get hex color
	 * 
	 * @param on is glowing
	 * @return hex color
	 */
	public PC_Color getFullColor(boolean on) {
		int rc;

		rc = PC_Color.light_colors[color];


		if (rc == 0xf0f0f0) {
			//try to get color from driver - weasel port.
			PC_CoordI drc = getCoord().copy();

			int meta = drc.getMeta(worldObj);
			switch (meta) {
				case 0:
					drc = drc.offset(0, -1, 0);
					break;
				case 1:
					drc = drc.offset(0, 0, 1);
					break;
				case 2:
					drc = drc.offset(0, 0, -1);
					break;
				case 3:
					drc = drc.offset(1, 0, 0);
					break;
				case 4:
					drc = drc.offset(-1, 0, 0);
					break;
				case 5:
					drc = drc.offset(0, 1, 0);
					break;
			}
			TileEntity te = drc.getTileEntity(worldObj);
			if (te != null && te instanceof PClo_TileEntityWeasel) {
				PClo_WeaselPlugin plugin = ((PClo_TileEntityWeasel) te).getPlugin();
				if (plugin != null && plugin instanceof PClo_WeaselPluginPort) {
					rc = ((PClo_WeaselPluginPort) plugin).rgbcolor;
				}
			}
		}

		if (!on) {
			PC_Color color = PC_Color.fromHex(rc);
			// darker
			return new PC_Color(color.r * 0.3D, color.g * 0.3D, color.b * 0.3D);
		} else {
			return PC_Color.fromHex(rc);
		}
	}

	/**
	 * @return if forge updates this TE
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("color"))
				color = (Integer)o[p++];
			else if(var.equals("isStable"))
				isStable = (Boolean)o[p++];
			else if(var.equals("isHuge"))
				isHuge = (Boolean)o[p++];
		}
	}

	@Override
	public Object[] get() {
		Object[] o = new Object[6];
		o[0] = "color";
		o[1] = color;
		o[2] = "isStable";
		o[3] = isStable;
		o[4] = "isHuge";
		o[5] = isHuge;
		return o;
	}
}
