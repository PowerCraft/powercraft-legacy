package powercraft.management;

import java.util.EnumSet;
import java.util.List;

import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_TickHandler implements ITickHandler {

	private int countDown = 20;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if(GameInfo.mcs()!=null && GameInfo.mcs().isServerRunning()){
			if(countDown<=0){
				List<PC_IMSG> objs = ModuleInfo.getMSGObjects();
				for (PC_IMSG obj : objs){
		        	obj.msg(PC_Utils.MSG_TICK_EVENT);
		        }
			}else{
				countDown--;
			}
		}else{
			countDown=20;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		if(GameInfo.isClient())
			return EnumSet.of(TickType.CLIENT);
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "PC_TickHandler";
	}

}
