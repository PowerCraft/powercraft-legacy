package powercraft.management;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;

import powercraft.management.PC_Utils.MSG;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_ClientTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		if(mc.getIntegratedServer()!=null){
			if(type.contains(TickType.SERVER)){
				MSG.callAllMSG(PC_Utils.MSG_TICK_EVENT);
			}
		}else if(PC_ClientUtils.mc().theWorld!=null){
			MSG.callAllMSG(PC_Utils.MSG_TICK_EVENT);
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "PC_TickHandler";
	}

}
