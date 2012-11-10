package powercraft.light;

import java.util.Random;

import net.minecraft.src.NBTTagCompound;
import powercraft.core.PC_Color;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PCli_TileEntityLight extends PC_TileEntity {
	private PC_Color color = null;
	/** flag that this light is lamp, and not indicator */
	private boolean isStable;
	/** flag that this light huge */
	private boolean isHuge;
	
	private boolean isActive;
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if(color==null)
			color = new PC_Color();
		PC_Utils.loadFromNBT(nbttagcompound, "color", color);
		isStable = nbttagcompound.getBoolean("stable");
		isHuge = nbttagcompound.getBoolean("huge");
		isActive = nbttagcompound.getBoolean("active");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		if(color!=null)
			PC_Utils.saveToNBT(nbttagcompound, "color", color);
		nbttagcompound.setBoolean("stable", isStable);
		nbttagcompound.setBoolean("huge", isHuge);
		nbttagcompound.setBoolean("active", isActive);
	}

	/**
	 * Set the light's color index
	 * 
	 * @param c color index
	 */
	public void setColor(PC_Color c) {
		color = c;
		worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * Get the light color index
	 * 
	 * @return color index
	 */
	public PC_Color getColor() {
		return color;
	}

	public void setStable(boolean stable) {
		PC_PacketHandler.setTileEntity(this, "isStable", stable);
		isStable = stable;
	}
	
	public boolean isStable(){
		return isStable;
	}
	
	public void setHuge(boolean huge) {
		PC_PacketHandler.setTileEntity(this, "isHuge", huge);
		isHuge = huge;
	}
	
	public boolean isHuge(){
		return isHuge;
	}
	
	public void setActive(boolean active) {
		PC_PacketHandler.setTileEntity(this, "isActive", active);
		isActive = active;
		worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
	}
	
	/**
	 * @return true if light is glowing
	 */
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("color")){
				color = (PC_Color)o[p++];
			}else if(var.equals("isStable")){
				isStable = (Boolean)o[p++];
				PCli_BlockLight bLight = (PCli_BlockLight)getBlockType();
				if(isStable)
					bLight.onPoweredBlockChange(worldObj, xCoord, yCoord, zCoord, true);
				else
					bLight.updateTick(worldObj, xCoord, yCoord, zCoord, new Random());
			}else if(var.equals("isHuge")){
				isHuge = (Boolean)o[p++];
			}else if(var.equals("isActive")){
				isActive = (Boolean)o[p++];
			}
		}
		worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Object[] getData() {
		return new Object[]{
			"color", color,
			"isStable", isStable,
			"isHuge", isHuge,
			"isActive", isActive
		};
	}
}
