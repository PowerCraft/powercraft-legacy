package net.minecraft.src;



import java.util.List;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiWeaselCoreSettings implements PC_IGresBase {

	private PClo_WeaselPluginCore core;
	private PC_GresWindow w;

	private PC_GresWidget edNetwork, colorBulb, btnRename;
	private PC_GresWidget txError;
	private PC_GresColorPicker colorPicker;
	private PC_GresWidget edName;


	/**
	 * prog gate GUI
	 * 
	 * @param core gate TE
	 */
	public PClo_GuiWeaselCoreSettings(PClo_WeaselPluginCore core) {
		this.core = core;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		w = new PC_GresWindow(PC_Lang.tr("tile.PCloWeasel."+(core.isMaster()?"core":"slave")+".name"));
		w.setMinSize(380, 230);
		w.setAlignH(PC_GresAlign.STRETCH);
		w.setAlignV(PC_GresAlign.TOP);

		PC_GresWidget hg;

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton("+").setId(103).setMinWidth(0).enable(false).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.program")).setId(100).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.status")).setId(101).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.settings")).setId(102).enable(false).setWidgetMargin(2));
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.close")).setId(0).enable(true).setWidgetMargin(2));

		w.add(hg);

		String lNetwork = PC_Lang.tr("pc.gui.weasel.core.networkLabel");
		String lColor = PC_Lang.tr("pc.gui.weasel.core.colorLabel");

		int width = 0;
		width = Math.max(width, w.getStringWidth(lNetwork));
		width = Math.max(width, w.getStringWidth(lColor));
		width += 40;
		
		if(core.isMaster()) {
	
			hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
			hg.add(new PC_GresLabel(lNetwork).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));
			hg.add(edNetwork = new PC_GresTextEdit(core.getNetworkName(), 14, PC_GresInputType.TEXT));
			hg.add(btnRename = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.rename")).setId(1).setMinWidth(40));
			w.add(hg);
	
			hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
			hg.add(new PC_GresLabel(lColor).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));
	
			PC_GresFrame frame = new PC_GresFrame();
	
	
			frame.add(colorBulb = new PC_GresColor(core.getNetworkColor()));
			frame.add(colorPicker = new PC_GresColorPicker(core.getNetworkColor().getHex(), 100, 50));
			hg.add(frame);
			hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.colorChange")).setId(3).setMinWidth(40));
			w.add(hg);
		}else {
			int colorLabel = 0x000000;
			int colorValue = 0x000099;
			
			hg = new PC_GresLayoutH();
			hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.connectedToNetwork")).setColor(PC_GresWidget.textColorEnabled, colorLabel));
			hg.add(new PC_GresColor(core.getNetworkColor()));
			hg.add(new PC_GresLabel(core.getNetworkName()).setColor(PC_GresWidget.textColorEnabled, colorValue));
			w.add(hg);


			hg = new PC_GresLayoutH();
			hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.slave.slaveName")));
			hg.add(edName = new PC_GresTextEdit(core.getName(), 14, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
			hg.add(btnRename = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.rename")).setId(1).setMinWidth(40));
			w.add(hg);
		}

		w.add(txError = new PC_GresLabel("").setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		btnRename.enable(false);
		//updateColorLetters();

		gui.add(w);


	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget == edNetwork) {
			String name = edNetwork.text.trim();
			if (name.length() == 0) {
				txError.text = PC_Lang.tr("pc.gui.weasel.core.errNetworkNameTooShort");
				btnRename.enabled = false;
			} else if (mod_PClogic.NETWORK.getNetwork(name) != null) {
				txError.text = PC_Lang.tr("pc.gui.weasel.core.errNetworkNameAlreadyUsed");
				btnRename.enabled = false;
			} else {
				txError.text = "";
				btnRename.enabled = true;
			}

			w.calcSize();
			return;
		}	

		if (widget == edName) {
			String name = edName.text.trim();
			if (name.length() == 0) {
				txError.text = PC_Lang.tr("pc.gui.weasel.errDeviceNameTooShort");
				btnRename.enabled = false;
			} else if (core.getNetwork() != null && core.getNetwork().getMembers().get(name) != null) {
				txError.text = PC_Lang.tr("pc.gui.weasel.errDeviceNameAlreadyUsed", name);
				btnRename.enabled = false;
			} else {
				txError.text = "";
				btnRename.enabled = true;
			}
			w.calcSize();
			return;
		}

		if (widget.getId() == 100) {
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreProgram(core));
			return;
		}
		if (widget.getId() == 101) {
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreStatus(core));
			return;
		}

		if (widget.getId() == 0) {
			gui.close();
			return;
		}

		if (widget.getId() == 1) {
			if(core.isMaster()) {
				String name = edNetwork.text.trim();
	
				txError.text = PC_Lang.tr("pc.gui.weasel.core.msgNetworkRenamed", new String[] { name });
				core.renameNetwork(name);
	
				w.calcSize();
			}else {
				String name = edName.text.trim();
				core.setMemberName(name);
			}
			return;
		}

		if (widget == colorPicker) {
			((PC_GresColor) colorBulb).setColor(PC_Color.fromHex(colorPicker.getColor()));
			return;
		}

		if (widget.getId() == 3) {
			core.getNetwork().setColor(((PC_GresColor) colorBulb).getColor());

			txError.text = PC_Lang.tr("pc.gui.weasel.core.msgNetworkColorChanged");

			w.calcSize();
			return;
		}

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
		// TODO Auto-generated method stub
		return null;
	}

}
