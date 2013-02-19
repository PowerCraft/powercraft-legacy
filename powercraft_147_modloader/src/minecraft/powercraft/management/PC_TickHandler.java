package powercraft.management;

import java.util.List;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.MSG;

public class PC_TickHandler {

	public void tick() {
		if(PC_ClientUtils.mc().theWorld!=null){
			MSG.callAllMSG(PC_Utils.MSG_TICK_EVENT);
		}
	}
	
}
