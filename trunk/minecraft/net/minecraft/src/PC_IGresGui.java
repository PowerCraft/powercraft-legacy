package net.minecraft.src;

/**
 * GRES GUI interface.<br>
 * This is used by the GUI wrapper class {@link PC_GresGui}, and passed to widgets.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public interface PC_IGresGui {
	
	public abstract PC_GresWidget add(PC_GresWidget widget);
	public abstract void setPausesGame(boolean b);
	public abstract void close();
	
}
