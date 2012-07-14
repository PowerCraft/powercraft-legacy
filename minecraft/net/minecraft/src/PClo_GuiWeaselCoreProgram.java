package net.minecraft.src;



import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiWeaselCoreProgram implements PC_IGresBase {

	private PClo_WeaselPluginCore core;
	private PC_GresWidget edit;
	private PC_GresWidget txError;
	private PC_GresWindow w;


	/**
	 * prog gate GUI
	 * 
	 * @param core gate TE
	 */
	public PClo_GuiWeaselCoreProgram(PClo_WeaselPluginCore core) {
		this.core = core;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.core.title"));
		w.setMinSize(380, 230);
		w.setAlignH(PC_GresAlign.STRETCH);
		w.setAlignV(PC_GresAlign.TOP);
		
		PC_GresWidget hg;
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.program")).setId(100).enable(false).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.status")).setId(101).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.settings")).setId(102).enable(true).setWidgetMargin(2));
		
		w.add(hg);
		
		PC_GresWidget mainHg = new PC_GresLayoutH().setAlignV(PC_GresAlign.TOP);
		
		PC_GresWidget leftCol = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH).setMinWidth(294).setWidgetMargin(1);
		
		

		leftCol.add(edit = new PC_GresTextEditMultiline(core.program, 290, 154, PC_GresHighlight.weasel(core)).setWidgetMargin(2));
		leftCol.add(txError = new PC_GresLabelMultiline("Weasel status: " + (core.getError() == null ? "OK" : core.getError()), 270).setMinRows(2).setMaxRows(2).setWidgetMargin(2).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		mainHg.add(leftCol);
		
		PC_GresWidget rightCol = new PC_GresLayoutV().setAlignV(PC_GresAlign.TOP).setAlignH(PC_GresAlign.CENTER).setMinWidth(60).setWidgetMargin(0);
		
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.save")).setId(1).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.check")).setId(2).setMinWidth(55).setWidgetMargin(2));
		rightCol.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.launch")).setId(3).setMinWidth(55).setWidgetMargin(2));
		mainHg.add(rightCol);
		
		w.add(mainHg);
		
		gui.add(w);

		actionPerformed(edit, gui);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget.getId() == 100) {
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreProgram(core));		
			return;
		}
		if (widget.getId() == 101) {
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreStatus(core));		
			return;
		}
		if (widget.getId() == 102) {
			PC_Utils.openGres(getPlayer(), new PClo_GuiWeaselCoreSettings(core));		
			return;
		}
		
		if (widget.getId() == 1) {
			core.setProgram(edit.getText());
			gui.close();

		} else if (widget.getId() == 2) {
			txError.setText("");
			try {
				core.checkProgramForErrors(edit.getText());
				txError.setText("Program has no errors.");
			} catch (Exception e) {
				txError.setText(e.getMessage());
			}

		} else if (widget.getId() == 3) {
			txError.setText("");
			try {
				core.setAndStartNewProgram(edit.getText());
				core.world().notifyBlockChange(core.coord().x, core.coord().y, core.coord().z, mod_PClogic.weaselDevice.blockID);
				gui.close();
			} catch (Exception e) {
				txError.setText(e.getMessage());
			}
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

}
