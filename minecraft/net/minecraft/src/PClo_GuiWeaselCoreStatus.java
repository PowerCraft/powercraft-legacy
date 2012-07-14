package net.minecraft.src;


import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * gui for editing programmable gate's program
 * 
 * @author MightyPork
 */
public class PClo_GuiWeaselCoreStatus implements PC_IGresBase {

	private PClo_WeaselPluginCore core;
	private PC_GresWindow w;
	private PC_GresWidget txRunning, txStack, txMemory, txPeripherals, txStatus, txLength;


	/**
	 * prog gate GUI
	 * 
	 * @param core gate TE
	 */
	public PClo_GuiWeaselCoreStatus(PClo_WeaselPluginCore core) {
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
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.program")).setId(100).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.status")).setId(101).enable(false).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.settings")).setId(102).enable(true).setWidgetMargin(2));		
		w.add(hg);
		
		String lRunning=PC_Lang.tr("pc.gui.weasel.core.runningLabel");
		String lStack=PC_Lang.tr("pc.gui.weasel.core.stackLabel");
		String lMemory=PC_Lang.tr("pc.gui.weasel.core.memoryLabel");
		String lPerip=PC_Lang.tr("pc.gui.weasel.core.peripheralsLabel");
		String lLength=PC_Lang.tr("pc.gui.weasel.core.programLength");
		String lStatus=PC_Lang.tr("pc.gui.weasel.core.statusLabel"); 
		
		int width = 0;
		width = Math.max(width, w.getStringWidth(lRunning));
		width = Math.max(width, w.getStringWidth(lStack));
		width = Math.max(width, w.getStringWidth(lMemory));
		width = Math.max(width, w.getStringWidth(lPerip));
		width = Math.max(width, w.getStringWidth(lStatus));
		width = Math.max(width, w.getStringWidth(lLength));
		width += 40;
		
		int colorLabel = 0x000000;
		int colorValue = 0x000099;
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lRunning).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txRunning = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lLength).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txLength = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lStack).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txStack = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lMemory).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txMemory = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lPerip).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txPeripherals = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lStatus).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(txStatus = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);
		
		updateCounters();
		gui.add(w);

	}
	
	private void updateCounters() {
		txRunning.text = core.weasel.isProgramFinished?PC_Lang.tr("pc.gui.weasel.core.no"):PC_Lang.tr("pc.gui.weasel.core.yes");
		txStack.text = (core.weasel.dataStack.get().size() + core.weasel.systemStack.get().size())+" "+PC_Lang.tr("pc.gui.weasel.core.unitObjects");
		txMemory.text = (core.weasel.variables.get().size())+" "+PC_Lang.tr("pc.gui.weasel.core.unitObjects");
		txPeripherals.text = (core.getNetwork().members.size()-1)+"";
		txStatus.text = core.getError()==null?"OK":core.getError();
		txLength.text = core.weasel.instructionList.list.size()+" "+PC_Lang.tr("pc.gui.weasel.core.unitInstructions");
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
		
		if (widget.getId() == 0) {
			gui.close();

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
	public void updateTick(PC_IGresGui gui) {
		updateCounters();
	}

}
