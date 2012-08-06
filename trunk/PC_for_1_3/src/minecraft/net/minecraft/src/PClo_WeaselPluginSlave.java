package net.minecraft.src;

/**
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PClo_WeaselPluginSlave extends PClo_WeaselPluginCore {

	/**
	 * SLAVE plugin
	 * 
	 * @param tew weasel tile entity
	 */
	public PClo_WeaselPluginSlave(PClo_TileEntityWeasel tew) {
		super(tew);
	}

	@Override
	public int getType() {
		return PClo_WeaselType.SLAVE;
	}
	
	@Override
	protected PClo_WeaselPlugin readPluginFromNBT(NBTTagCompound tag) {
		super.readPluginFromNBT(tag);
		sleepTimer -= 3;
		return this;
	}
	
	@Override
	public boolean isMaster() {
		return false;
	}

}
