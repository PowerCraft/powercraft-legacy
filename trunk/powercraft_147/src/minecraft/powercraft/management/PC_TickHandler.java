package powercraft.management;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.world.World;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.MSG;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_TickHandler implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		MSG.callAllMSG(PC_Utils.MSG_TICK_EVENT);
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
