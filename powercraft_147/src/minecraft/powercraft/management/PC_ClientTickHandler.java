package powercraft.management;

import java.util.EnumSet;
import java.util.List;

import powercraft.management.PC_Utils.ModuleInfo;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_ClientTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(PC_ClientUtils.mc().theWorld!=null){
			List<PC_IMSG> objs = ModuleInfo.getMSGObjects();
			for (PC_IMSG obj : objs){
				obj.msg(PC_Utils.MSG_TICK_EVENT);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "PC_TickHandler";
	}

}
