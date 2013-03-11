package powercraft.api.tick;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import powercraft.api.PC_ClientUtils;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_TickRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_ClientTickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		if(mc.getIntegratedServer()!=null){
			if(type.contains(TickType.SERVER)){
				PC_TickRegistry.onTickEvent();
			}
		}else if(PC_ClientUtils.mc().theWorld!=null){
			PC_TickRegistry.onTickEvent();
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
