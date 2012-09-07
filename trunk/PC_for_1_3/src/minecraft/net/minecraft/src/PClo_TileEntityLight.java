package net.minecraft.src;

import java.util.Random;


/**
 * Light block tile entity
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_TileEntityLight extends PC_TileEntity {
	private PC_Color color = null;
	/** flag that this light is lamp, and not indicator */
	public boolean isStable;
	/** flag that this light huge */
	public boolean isHuge;

	public boolean send = true;
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if(color==null)
			color = new PC_Color();
		PC_Utils.loadFromNBT(nbttagcompound, "color", color);
		isStable = nbttagcompound.getBoolean("stable");
		isHuge = nbttagcompound.getBoolean("huge");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if(color!=null)
			PC_Utils.saveToNBT(nbttagcompound, "color", color);
		nbttagcompound.setBoolean("stable", isStable);
		nbttagcompound.setBoolean("huge", isHuge);
	}

	/**
	 * Set the light's color index
	 * 
	 * @param c color index
	 */
	public void setColor(PC_Color c) {
		color = c;
	}

	/**
	 * Get the light color index
	 * 
	 * @return color index
	 */
	public PC_Color getColor() {
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
		//int rc;

		return color;
		
		/*if(color==null)
			return null;
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
		}*/
	}

	@Override
	public void updateEntity() {
		if(color!=null&&send){
			PC_Utils.setTileEntity(PC_Utils.mc().thePlayer, this, "color", color.r, color.g, color.b, "isStable", isStable, "isHuge", isHuge);
			send=false;
		}
	}
	
	/**
	 * @return if forge updates this TE
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("color")){
				if(color==null)
					color = new PC_Color();
				color.r = (Double)o[p++];
				color.g = (Double)o[p++];
				color.b = (Double)o[p++];
			}else if(var.equals("isStable")){
				isStable = (Boolean)o[p++];
				PClo_BlockLight bLight = (PClo_BlockLight)getBlockType();
				if(isStable)
					bLight.onPoweredBlockChange(worldObj, xCoord, yCoord, zCoord, true);
				else
					bLight.updateTick(worldObj, xCoord, yCoord, zCoord, new Random());
			}else if(var.equals("isHuge"))
				isHuge = (Boolean)o[p++];
		}
	}

	@Override
	public Object[] get() {
		Object[] o = new Object[8];
		o[0] = "color";
		if(color!=null){
			o[1] = color.r;
			o[2] = color.g;
			o[3] = color.b;
		}else{
			o[1] = 1.0d;
			o[2] = 1.0d;
			o[3] = 1.0d;
		}
		o[4] = "isStable";
		o[5] = isStable;
		o[6] = "isHuge";
		o[7] = isHuge;
		return o;
	}
}
