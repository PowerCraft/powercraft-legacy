package net.minecraft.src.PCnt;


import java.util.List;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.PC_CoordI;
import net.minecraft.src.PC_GresBase;
import net.minecraft.src.PC_GresButton;
import net.minecraft.src.PC_GresColorMap;
import net.minecraft.src.PC_GresLayoutV;
import net.minecraft.src.PC_GresWidget;
import net.minecraft.src.PC_IGresGui;
import net.minecraft.src.PC_Lang;
import net.minecraft.src.PC_Utils;
import net.minecraft.src.Slot;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;
import net.minecraft.src.PCnt.PCnt_NetManager.WeaselNetwork;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselString;


/**
 * Gui for touchscreen with the touch area - not the settings.
 * 
 * @author MightyPork
 */
public class PCnt_GuiWeaselTouchscreenTouch extends PC_GresBase {

	private PCnt_WeaselPluginTouchscreen touchscreen;
	private PC_GresColorMap colorMap;

	/**
	 * GUI for port.
	 * 
	 * @param touchscreen plugin instance
	 */
	public PCnt_GuiWeaselTouchscreenTouch(PCnt_WeaselPluginTouchscreen touchscreen) {
		this.touchscreen = touchscreen;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWidget mvg = new PC_GresLayoutV();
		mvg.setAlignH(PC_GresAlign.CENTER);

		colorMap = new PC_GresColorMap(touchscreen.screen).setScale(4);
		mvg.add(colorMap);
		mvg.add(new PC_GresButton(PC_Lang.tr("pc.gui.close")).setButtonPadding(4, 4).setId(0));

		gui.add(mvg);
		gui.setBackground(0x90101010, 0x90101010);

		gui.setFocus(colorMap);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}
	
	int timer = -1;

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget == colorMap) {
			if(timer == -1) {
				timer = 0;
				return;
			}
			String event = colorMap.getLastEvent();
			if(event.length()==0) return;
			PC_CoordI mouse = colorMap.getLastMousePos();
			int mouseKey = colorMap.getLastMouseKey();
			char key = colorMap.getLastKey();

			WeaselNetwork network = touchscreen.getNetwork();
			if (network != null)
				((PCnt_WeaselPlugin) network.getMember("CORE")).callFunctionOnEngine("touchEvent", new WeaselString(touchscreen.getName()),
						new WeaselString(event), new WeaselInteger(mouse.x), new WeaselInteger(mouse.y), new WeaselInteger(mouseKey),
						new WeaselString("" + key));
			
			colorMap.setLastEvent("");
		}

		if (widget.getId() == 0) gui.close();

	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public List<Slot> getAllSlots(Container c) {
		return null;
	}

	@Override
	public boolean canShiftTransfer() {
		return false;
	}

}
