package net.minecraft.src;


/**
 * The weasel CPU
 * 
 * @author MightyPork
 */
public class PCnt_WeaselPluginSlave_UNUSED extends PCnt_WeaselPluginCore_UNUSED {

	/**
	 * SLAVE plugin
	 * 
	 * @param tew weasel tile entity
	 */
	public PCnt_WeaselPluginSlave_UNUSED(PCnt_TileEntityWeasel_UNUSED tew) {
		super(tew);
	}

	@Override
	public int getType() {
		return PCnt_WeaselType.SLAVE;
	}
	
	@Override
	protected PCnt_WeaselPlugin_UNUSED readPluginFromNBT(NBTTagCompound tag) {
		super.readPluginFromNBT(tag);
		sleepTimer -= 3;
		return this;
	}
	
	@Override
	public boolean isMaster() {
		return false;
	}

}
