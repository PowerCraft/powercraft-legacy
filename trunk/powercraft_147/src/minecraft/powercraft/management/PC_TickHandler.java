package powercraft.management;

import java.util.EnumSet;

import powercraft.management.registry.PC_MSGRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_TickHandler implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_TICK_EVENT);
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "PC_TickHandler";
	}

}
