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
	
	/**
	 * Add a widget
	 * @param widget widget to add
	 * @return the child layout
	 */
	public abstract PC_GresWidget add(PC_GresWidget widget);
	
	/**
	 * @param b does pause game?
	 */
	public abstract void setPausesGame(boolean b);
	
	/**
	 * Close the gui and set in-game focus.
	 */
	public abstract void close();
	
}
