package net.minecraft.src;



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
	
	private PC_GresWidget edNetwork, colorBulb, btnRename, btnChangeColor, btnSaveColor;
	private PC_GresWidget txError;


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
		w = new PC_GresWindow(PC_Lang.tr("tile.PCloWeasel.core.name"));
		w.setMinSize(380, 230);
		w.setAlignH(PC_GresAlign.STRETCH);
		w.setAlignV(PC_GresAlign.TOP);
		
		PC_GresWidget hg;
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresGap(4, 0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.program")).setId(100).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.status")).setId(101).enable(true).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.settings")).setId(102).enable(false).setWidgetMargin(2));
		
		w.add(hg);
		
		String lNetwork=PC_Lang.tr("pc.gui.weasel.core.networkLabel");
		String lColor=PC_Lang.tr("pc.gui.weasel.core.colorLabel");
		
		int width = 0;
		width = Math.max(width, w.getStringWidth(lNetwork));
		width = Math.max(width, w.getStringWidth(lColor));
		width += 40;
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lNetwork).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));
		hg.add(edNetwork = new PC_GresTextEdit(core.networkName,14, PC_GresInputType.TEXT));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.rename")).setId(1));
		w.add(hg);
		
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lColor).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));
		hg.add(colorBulb = new PC_GresColor(core.getNetworkColor()));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.colorChange")).setId(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.colorSave")).setId(3));
		w.add(hg);
		
		w.add(txError = new PC_GresLabel("").setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		
		
		gui.add(w);


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
			String name = edNetwork.text.trim();
			txError.text = "";
			if(name.length() == 0) {
				txError.text = PC_Lang.tr("pc.gui.weasel.core.errNetworkNameTooShort");
			}else if(mod_PClogic.NETWORK.getNetwork(name) != null) {
				txError.text = PC_Lang.tr("pc.gui.weasel.core.errNetworkNameAlreadyUsed");
			}else {
				txError.text = PC_Lang.tr("pc.gui.weasel.core.networkRenamed",new String[] {name});
				core.renameNetwork(name);
			}
			return;
		}
		
		if (widget.getId() == 2) {
			((PC_GresColor)colorBulb).setColor(PC_Color.randomColor());
		}
		
		if (widget.getId() == 3) {
			core.getNetwork().setColor(((PC_GresColor)colorBulb).getColor());

			txError.text = PC_Lang.tr("pc.gui.weasel.core.networkColorChanged");
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
