package powercraft.core.block;


import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import powercraft.api.gres.PC_GresComponent;
import powercraft.api.gres.PC_GresGuiHandler;
import powercraft.api.gres.PC_GresLabel;
import powercraft.api.gres.PC_GresLayoutVertical;
import powercraft.api.gres.PC_GresWindow;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_GresTickEvent;
import powercraft.api.gres.events.PC_IGresEventListener;


public class PC_GuiPuffer implements PC_IGresClient, PC_IGresEventListener {

	private PC_GresLabel label;
	private PC_TileEntityPuffer tileEntityPuffer;


	public PC_GuiPuffer(PC_TileEntityPuffer tileEntityPuffer, EntityPlayer player) {

		this.tileEntityPuffer = tileEntityPuffer;
	}


	@Override
	public void initGui(PC_GresGuiHandler gui) {

		gui.setLayout(new PC_GresLayoutVertical());
		PC_GresWindow window = new PC_GresWindow("Puffer");
		window.setLayout(new PC_GresLayoutVertical());
		window.add(label = new PC_GresLabel("Energy:" + (int) (tileEntityPuffer.getEnergyLevel() + 0.5) + "/10000"));
		gui.add(window);
		gui.addEventListener(this);
	}


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
		} else if (event instanceof PC_GresTickEvent) {
			PC_GresTickEvent tickEvent = (PC_GresTickEvent) event;
			if (component == guiHandler && tickEvent.getEventType() == PC_GresTickEvent.EventType.PRE) {
				label.setText("Energy:" + (int) (tileEntityPuffer.getEnergyLevel() + 0.5) + "/10000");
			}
		}
	}

}
