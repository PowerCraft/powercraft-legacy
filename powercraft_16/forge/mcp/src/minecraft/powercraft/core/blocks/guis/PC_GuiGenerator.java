package powercraft.core.blocks.guis;


import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import powercraft.api.PC_RectI;
import powercraft.api.gres.*;
import powercraft.api.gres.events.PC_GresEvent;
import powercraft.api.gres.events.PC_GresKeyEvent;
import powercraft.api.gres.events.PC_IGresEventListener;
import powercraft.core.blocks.tileentities.PC_TileEntityGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PC_GuiGenerator extends PC_ContainerGenerator implements PC_IGresGui, PC_IGresEventListener {

	private PC_GresLabel label;
	private PC_GresProgressImage progress;
	private PC_GresInventory inv;


	public PC_GuiGenerator(PC_TileEntityGenerator generator, EntityPlayer player) {

		super(generator, player);
	}


	@Override
	public void initGui(PC_GresGuiHandler gui) {

		PC_GresWindow window = new PC_GresWindow("Generator");
		window.addSideTab(new PC_GresWindowSideTab());
		window.addSideTab(new PC_GresWindowSideTab());
		window.setLayout(new PC_GresLayoutVertical());
		window.add(label = new PC_GresLabel("Heat:0"));
		window.add(progress = new PC_GresProgressImage("Fire"));
		window.add(inv = new PC_GresInventory(1, 1));
		PC_GresScrollArea sa = new PC_GresScrollArea();
		window.add(sa);
		PC_GresContainer cont = sa.getContainer();
		cont.setLayout(new PC_GresLayoutVertical());
		PC_GresRadioButton rb;
		PC_GresComponent c;
		cont.add(c = new PC_GresCheckBox("T"));
		c.setPadding(new PC_RectI(0, 0, 0, 4));
		cont.add(c = rb = new PC_GresRadioButton("Test1"));
		c.setPadding(new PC_RectI(0, 0, 0, 4));
		cont.add(c = new PC_GresRadioButton("Test2", rb));
		c.setPadding(new PC_RectI(0, 0, 0, 4));
		cont.add(c = new PC_GresRadioButton("Test3", rb));
		c.setPadding(new PC_RectI(0, 0, 0, 4));
		cont.add(c = new PC_GresTextEdit("Test3", 7));
		c.setPadding(new PC_RectI(0, 0, 0, 4));
		cont.add(new PC_GresComboBox(Arrays.asList(new String[]{"Red", "Green", "Blue"}), 1));
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
