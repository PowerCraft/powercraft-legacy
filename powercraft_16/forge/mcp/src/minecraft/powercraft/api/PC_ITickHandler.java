package powercraft.api;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;

public interface PC_ITickHandler {

	public void tickStart(Object[] tickData);

	public void tickEnd(Object[] tickData);
	
}
