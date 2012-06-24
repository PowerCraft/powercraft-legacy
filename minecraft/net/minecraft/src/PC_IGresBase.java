package net.minecraft.src;

/**
 * Gres Base interface, implemented by GRES individual GUIs.
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public interface PC_IGresBase {
	
	/**
	 * @return player instance who opened the gui
	 */
	public EntityPlayer getPlayer();


	/**
	 * Create {@link PC_GresWindow}, fill with widgets and add it to the outer GUI.
	 * 
	 * @param gui the outer gui
	 */
	public void initGui(PC_IGresGui gui);
	
	/**
	 * Hook called when the GuiScreen is closed
	 * @param gui
	 */
	public void onGuiClosed(PC_IGresGui gui);
	
	/**
	 * On action performed.<br>
	 * Action = mouse click, key press, button press etc., accepted by the corresponding widget.
	 * 
	 * @param widget widget generating the action
	 * @param gui outer gui
	 */
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui);
	
	/**
	 * On ESC key pressed  and no widget accepted it
	 * @param gui outer gui
	 */
	public void onEscapePressed(PC_IGresGui gui);
	
	/**
	 * On ENTER key pressed  and no widget accepted it
	 * @param gui outer gui
	 */
	public void onReturnPressed(PC_IGresGui gui);


	/**
	 * Called usually when somehting in ivnentory was changed.
	 * @param iinventory the inventory
	 */
	public void onCraftMatrixChanged(IInventory iinventory);
	
	/**
	 * Update tick, can be used to update some numbers, animations etc.
	 * @param gui
	 */
	public void updateTick(PC_IGresGui gui);
	
}
