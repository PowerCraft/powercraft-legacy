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
	 * Get hex color
	 * 
	 * @param on is glowing
	 * @return hex color
	 */
	public int getHexColor(boolean on) {
		return getHexColor(color, on);
	}
	
	private int lastcolor = 0;

	/**
	 * Get light's color in hex format.
	 * 
	 * @param color_index color index
	 * @param on is light glowing
	 * @return hex color
	 */
	public int getHexColor(int color_index, boolean on) {
		int rc;
		
		try {
			rc = PC_Color.light_colors[color_index];
		} catch (ArrayIndexOutOfBoundsException e) {
			rc = 0xf0f0f0;
		}
		
		if(rc == 0xf0f0f0) {
			//try to get color from driver - weasel port.
			
			PC_CoordI drc = getCoord().copy();
			
			int meta = drc.getMeta(worldObj);
			switch(meta) {
				case 0: drc = drc.offset(0,-1,0); break;
				case 1: drc = drc.offset(0,0,1); break;
				case 2: drc = drc.offset(0,0,-1); break;
				case 3: drc = drc.offset(1,0,0); break;
				case 4: drc = drc.offset(-1,0,0); break;
				case 5: drc = drc.offset(0,1,0); break;
			}
			TileEntity te = drc.getTileEntity(worldObj);
			if(te != null && te instanceof PClo_TileEntityWeasel) {
				PClo_WeaselPlugin plugin = ((PClo_TileEntityWeasel) te).getPlugin();
				if(plugin != null && plugin instanceof PClo_WeaselPluginPort) {
					rc = ((PClo_WeaselPluginPort)plugin).rgbcolor;
					if(rc != lastcolor) {
						//worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
						worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
						//worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
						worldObj.notifyBlockChange(xCoord, yCoord, zCoord, mod_PClogic.lightOn.blockID);
					}
					lastcolor = rc;
				}
			}
			
		}
		

		int r = (rc & 0xff0000) >> 16;
		int g = (rc & 0x00ff00) >> 8;
		int b = (rc & 0x0000ff);

		if (on) {

		} else {
			// darker
			r *= 0.3D;
			g *= 0.3D;
			b *= 0.3D;
		}

		return (r << 16) + (g << 8) + b;
	}

	/**
	 * @return if forge updates this TE
	 */
	@Override
	public boolean canUpdate() {
		return false;
	}
}
