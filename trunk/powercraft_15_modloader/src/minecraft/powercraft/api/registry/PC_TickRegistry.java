package powercraft.api.registry;

import java.util.ArrayList;
import java.util.List;

import powercraft.api.tick.PC_ITickHandler;

public class PC_TickRegistry {

	private static List<PC_ITickHandler> tickHandlers = new ArrayList<PC_ITickHandler>();
	
	public static void register(PC_ITickHandler tickHandler){
		if(!tickHandlers.contains(tickHandler)){
			tickHandlers.add(tickHandler);
		}
	}
	
	public static void onTickEvent(){
		for(PC_ITickHandler tickHandler:tickHandlers){
			tickHandler.tickEvent();
		}
	}
	
}
