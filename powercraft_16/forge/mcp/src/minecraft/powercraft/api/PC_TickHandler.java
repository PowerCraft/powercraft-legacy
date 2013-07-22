package powercraft.api;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;


@SuppressWarnings("unchecked")
public class PC_TickHandler implements ITickHandler {

	private static List<PC_ITickHandler>[] tickHandlers;
	private static List<PC_ITickHandler>[] tickHandlersToAdd;
	private static List<PC_ITickHandler>[] tickHandlersToRemove;
	private static boolean[] iterating;

	static {
		tickHandlers = new List[TickType.values().length];
		for (int i = 0; i < tickHandlers.length; i++) {
			tickHandlers[i] = new ArrayList<PC_ITickHandler>();
		}
		tickHandlersToAdd = new List[TickType.values().length];
		for (int i = 0; i < tickHandlersToAdd.length; i++) {
			tickHandlersToAdd[i] = new ArrayList<PC_ITickHandler>();
		}
		tickHandlersToRemove = new List[TickType.values().length];
		for (int i = 0; i < tickHandlersToRemove.length; i++) {
			tickHandlersToRemove[i] = new ArrayList<PC_ITickHandler>();
		}
		iterating = new boolean[TickType.values().length];
	}


	public static void addTickHandler(TickType tickType, PC_ITickHandler tickHandler) {

		if (iterating[tickType.ordinal()]) {
			tickHandlersToAdd[tickType.ordinal()].add(tickHandler);
		} else {
			tickHandlers[tickType.ordinal()].add(tickHandler);
		}
	}


	public static void removeTickHandler(TickType tickType, PC_ITickHandler tickHandler) {

		if (iterating[tickType.ordinal()]) {
			tickHandlersToRemove[tickType.ordinal()].add(tickHandler);
		} else {
			tickHandlers[tickType.ordinal()].remove(tickHandler);
		}
	}


	private static void makeStartTick(int type, Object... tickData) {

		List<PC_ITickHandler> list = tickHandlers[type];
		iterating[type] = true;
		for (PC_ITickHandler tickHandler : list) {
			tickHandler.tickStart(tickData);
		}
		iterating[type] = false;
		list.addAll(tickHandlersToAdd[type]);
		tickHandlersToAdd[type].clear();
		list.removeAll(tickHandlersToRemove[type]);
		tickHandlersToRemove[type].clear();
	}


	private static void makeEndTick(int type, Object... tickData) {

		List<PC_ITickHandler> list = tickHandlers[type];
		iterating[type] = true;
		for (PC_ITickHandler tickHandler : list) {
			tickHandler.tickEnd(tickData);
		}
		iterating[type] = false;
		list.addAll(tickHandlersToAdd[type]);
		tickHandlersToAdd[type].clear();
		list.removeAll(tickHandlersToRemove[type]);
		tickHandlersToRemove[type].clear();
	}


	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

		for (TickType t : type) {
			makeStartTick(t.ordinal(), tickData);
		}
	}


	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {

		for (TickType t : type) {
			makeEndTick(t.ordinal(), tickData);
		}
	}


	@Override
	public EnumSet<TickType> ticks() {

		return EnumSet.allOf(TickType.class);
	}


	@Override
	public String getLabel() {

		return "PowerCraft Tick Handler";
	}

}
