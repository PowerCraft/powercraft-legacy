package powercraft.api.tick;

import powercraft.api.registry.PC_TickRegistry;
import powercraft.api.utils.PC_ClientUtils;

public class PC_TickHandler {
	
	public void tick() {
		if (PC_ClientUtils.mc().theWorld != null) {
			PC_TickRegistry.onTickEvent();
		}
	}
	
}
