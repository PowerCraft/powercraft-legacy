package net.minecraft.src;

public interface PC_IGresGui {
	
	public abstract PC_GresWidget add(PC_GresWidget widget);
	public abstract void setPauseGame(boolean b);
	public abstract void close();
	
}
