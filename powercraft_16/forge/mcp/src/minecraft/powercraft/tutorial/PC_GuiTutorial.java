/**
 * 
 */
package powercraft.tutorial;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresComponent;
import powercraft.api.gres.PC_GresGuiHandler;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_IGresEventListener;

/**
 * if there were an Inventory we would extend PC_GresBaseWithInventory
 *but we don't have an inventory so we don't need that
 *implenting PC_IGresClient for the "{@link PC_IGresClient#initGui(PC_GresGuiHandler)}"-Method and
 *PC_IGresEventListener for being able to receive events that can be found in the package {@link gres.events}
 *
 * @author Aaron
 *
 */

public class PC_GuiTutorial implements PC_IGresClient, PC_IGresEventListener {

	/* (non-Javadoc)
	 * @see powercraft.api.gres.events.PC_IGresEventListener#onEvent(powercraft.api.gres.events.PC_GresEvent)
	 */
	@Override
	public void onEvent(PC_GresEvent event) {
		PC_GresComponent component = event.getComponent();
		PC_GresGuiHandler guiHandler = component.getGuiHandler();
		if (event instanceof PC_GresKeyEvent) {
			PC_GresKeyEvent keyEvent = (PC_GresKeyEvent) event;
			if (component == guiHandler) {
				if (keyEvent.getKeyCode() == Keyboard.KEY_ESCAPE || keyEvent.getKeyCode() == Keyboard.KEY_E) {
					guiHandler.close();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see powercraft.api.gres.PC_IGresClient#initGui(powercraft.api.gres.PC_GresGuiHandler)
	 */
	@Override
	public void initGui(PC_GresGuiHandler gui) {
		// TODO Auto-generated method stub
		
	}

}
