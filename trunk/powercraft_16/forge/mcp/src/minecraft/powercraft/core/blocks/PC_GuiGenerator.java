package powercraft.core.blocks;


import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import powercraft.api.PC_RectI;
import powercraft.api.gres.*;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_IGresEventListener;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_GuiGenerator extends PC_ContainerGenerator implements PC_IGresClient, PC_IGresEventListener {

	private PC_GresLabel label;
	private PC_GresProgressImage progress;
	private PC_GresInventory inv;


	public PC_GuiGenerator(PC_TileEntityGenerator generator, EntityPlayer player) {

		super(generator, player);
	}


	@Override
	public void initGui(PC_GresGuiHandler gui) {

		gui.setLayout(new PC_GresLayoutVertical());
		PC_GresWindow window = new PC_GresWindow("Generator");
		window.setLayout(new PC_GresLayoutVertical());
		window.add(label = new PC_GresLabel("Heat:0"));
		window.add(progress = new PC_GresProgressImage("Fire"));
		window.add(inv = new PC_GresInventory(1, 1));
		window.add(new PC_GresButton("Test"));
		PC_GresButton b;
		window.add(b = new PC_GresButton("Test2"));
		b.setEnabled(false);
		inv.setSlot(0, 0, invSlots[0]);
		inv.setPadding(new PC_RectI(0, 0, 0, 4));
		gui.add(window);
		window.add(new PC_GresPlayerInventory(this));
		gui.addEventListener(this);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int key, int value) {

		if (key == 0) {
			progress.setProgress(value / 10.0f);
		} else if (key == 1) {
			label.setText("Heat:" + value);
		}
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
		}
	}

}
