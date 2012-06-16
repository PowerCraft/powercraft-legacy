package net.minecraft.src;

public interface PC_IGresBase {

	public abstract void initGui(PC_IGresGui gui);
	public abstract void onGuiClosed(PC_IGresGui gui);
	public abstract void actionPerformed(PC_GresWidget widget, PC_IGresGui gui);
	public abstract void onEscapePressed(PC_IGresGui gui);
	public abstract void onReturnPressed(PC_IGresGui gui);
	
}
