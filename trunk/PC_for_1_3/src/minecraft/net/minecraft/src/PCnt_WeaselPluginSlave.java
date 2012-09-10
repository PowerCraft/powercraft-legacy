package net.minecraft.src;


/**
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PCnt_WeaselPluginSlave extends PCnt_WeaselPluginCore {

	/**
	 * SLAVE plugin
	 * 
	 * @param tew weasel tile entity
	 */
	public PCnt_WeaselPluginSlave(PCnt_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.SLAVE;
	}
	
	@Override
	protected PCnt_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		super.readPluginFromNBT(tag);
		sleepTimer -= 3;
		return this;
	}
	
	@Override
	public boolean isMaster() {
		return false;
	}

}
