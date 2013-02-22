package powercraft.management;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import powercraft.management.registry.PC_MSGRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_ClientTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		if(mc.getIntegratedServer()!=null){
			if(type.contains(TickType.SERVER)){
				PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_TICK_EVENT);
			}
		}else if(PC_ClientUtils.mc().theWorld!=null){
			PC_MSGRegistry.callAllMSG(PC_MSGRegistry.MSG_TICK_EVENT);
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
