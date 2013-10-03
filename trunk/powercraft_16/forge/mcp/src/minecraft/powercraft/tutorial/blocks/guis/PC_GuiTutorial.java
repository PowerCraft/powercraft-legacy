/**
 * 
 */
package powercraft.tutorial.blocks.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresComponent;
import powercraft.api.gres.PC_GresGuiHandler;
import powercraft.api.gres.PC_GresLabel;
import powercraft.api.gres.PC_GresLayoutVertical;
import powercraft.api.gres.PC_GresTextEdit;
import powercraft.api.gres.PC_GresTextEdit.PC_GresInputType;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresFocusLostEvent;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_GresPrePostEvent.EventType;
import powercraft.api.gres.events.PC_GresTickEvent;
import powercraft.api.gres.events.PC_IGresEventListener;
import powercraft.tutorial.blocks.tileentities.PC_TileEntityTutorial;

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

	PC_TileEntityTutorial tileEntityTut;
	
	
	public PC_GuiTutorial(PC_TileEntityTutorial tileEntityTut, EntityPlayer player) {

		this.tileEntityTut = tileEntityTut;
	}
	
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
					keepInSync();
					guiHandler.close();
				}
			}
		}else if(event instanceof PC_GresTickEvent){
			PC_GresTickEvent ev = (PC_GresTickEvent) event;
			if(ev.getEventType()==EventType.PRE){
				label.setText("Speed: "+tileEntityTut.speed);
			}
		}
	}
	
	private void keepInSync(){
		NBTTagCompound nbt = new NBTTagCompound("guiChanges");
		nbt.setDouble("speed", Double.valueOf(text.getText()));
		tileEntityTut.sendMessage(nbt);
	}

	/* (non-Javadoc)
	 * @see powercraft.api.gres.PC_IGresClient#initGui(powercraft.api.gres.PC_GresGuiHandler)
	 */
	@Override
	public void initGui(PC_GresGuiHandler gui) {
		gui.setLayout(new PC_GresLayoutVertical());
		PC_GresWindow window = new PC_GresWindow("TutorialGui");
		window.setLayout(new PC_GresLayoutVertical());
		window.add(label = new PC_GresLabel("Speed: " + tileEntityTut.speed));
		(text = new PC_GresTextEdit("Speed:", 5, PC_GresInputType.SIGNED_FLOAT)).setText(""+tileEntityTut.speed);
		window.add(text);
		gui.add(window);
		gui.addEventListener(this);
		text.addEventListener(this);
	}

	PC_GresLabel label;
	PC_GresTextEdit text;
}
