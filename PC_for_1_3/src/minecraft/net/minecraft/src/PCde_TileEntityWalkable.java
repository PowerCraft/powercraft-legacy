package net.minecraft.src;


/**
 * Decorative block tile entity - because of the renderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_TileEntityWalkable extends PC_TileEntity {

	/** block type. */
	public int type = 0;
	private boolean send = true;

	@Override
	public void updateEntity() {
		if(send){
			PC_Utils.setTileEntity(PC_Utils.mc().thePlayer, this, "type", type);
			send = false;
		}
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return false
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = tag.getInteger("type");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("type", type);

	}

	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("type")){
				type = (Integer)o[p++];
			}
		}
	}

	@Override
	public Object[] get() {
		Object o[] = new Object[2];
		o[0] = "type";
		o[1] = type;
		return o;
	}
}
