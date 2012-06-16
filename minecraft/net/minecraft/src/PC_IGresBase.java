package net.minecraft.src;

public interface PC_IGresBase {

	public abstract void initGui(PC_IGresGui gui);
	public abstract void onGuiClosed(PC_IGresGui gui);
	public abstract void actionPerformed(PC_GresWidget widget, PC_IGresGui gui);
	public abstract boolean isKeyValid(char c, int i, PC_GresWidget widget, PC_IGresGui gui);
	
}
