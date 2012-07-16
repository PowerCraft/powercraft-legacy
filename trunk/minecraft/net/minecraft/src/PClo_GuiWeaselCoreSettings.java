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

	private PC_GresWidget edNetwork, colorBulb, btnRename;
	private PC_GresWidget txError, txr, txg, txb;


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

		PC_GresWidget hg, vg, hg1;

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresGap(4, 0));
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

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lNetwork).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));
		hg.add(edNetwork = new PC_GresTextEdit(core.getNetworkName(), 14, PC_GresInputType.TEXT));
		hg.add(btnRename = new PC_GresButton(PC_Lang.tr("pc.gui.weasel.rename")).setId(1).setMinWidth(40));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg.add(new PC_GresLabel(lColor).setMinWidth(width).setAlignH(PC_GresAlign.RIGHT));

		PC_GresFrame frame = new PC_GresFrame();

		frame.add(colorBulb = new PC_GresColor(core.getNetworkColor()));

		hg1 = new PC_GresLayoutH();

		hg1.add(txr = new PC_GresLabel(" R").setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(20).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(21).setWidgetMargin(0));
		hg1.add(vg);

		hg1.add(txg = new PC_GresLabel(" G").setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(22).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(23).setWidgetMargin(0));
		hg1.add(vg);

		hg1.add(txb = new PC_GresLabel(" B").setWidgetMargin(1));
		vg = new PC_GresLayoutV().setWidgetMargin(1);
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(44, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(24).setWidgetMargin(0));
		vg.add(new PC_GresButtonImage(mod_PCcore.getImgDir() + "gres/widgets.png", new PC_CoordI(50, 18), new PC_CoordI(6, 4)).setButtonPadding(3, 3).setId(25).setWidgetMargin(0));
		hg1.add(vg);

		frame.add(hg1);

		hg.add(frame);


		hg.add(new PC_GresGap(6, 0));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.weasel.core.colorChange")).setId(3).setMinWidth(40));
		w.add(hg);

		w.add(txError = new PC_GresLabel("").setWidgetMargin(2).setAlignH(PC_GresAlign.CENTER).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		btnRename.enable(false);
		updateColorLetters();

		gui.add(w);


	}

	private void updateColorLetters() {
		PC_Color color = ((PC_GresColor) colorBulb).getColor();

		txr.setColor(PC_GresWidget.textColorEnabled, new PC_Color(color.r, 0, 0).getHex());
		txg.setColor(PC_GresWidget.textColorEnabled, new PC_Color(0, color.g, 0).getHex());
		txb.setColor(PC_GresWidget.textColorEnabled, new PC_Color(0, 0, color.b).getHex());

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

		PC_Color color = ((PC_GresColor) colorBulb).getColor();

		float increment = 0.1F;

		if (widget.getId() == 20) {
			color.r += increment;
			if (color.r > 1) color.r = 1;
			updateColorLetters();
			return;
		}

		if (widget.getId() == 21) {
			color.r -= increment;
			if (color.r < 0) color.r = 0;
			updateColorLetters();
			return;
		}

		if (widget.getId() == 22) {
			color.g += increment;
			if (color.g > 1) color.g = 1;
			updateColorLetters();
			return;
		}

		if (widget.getId() == 23) {
			color.g -= increment;
			if (color.g < 0) color.g = 0;
			updateColorLetters();
			return;
		}

		if (widget.getId() == 24) {
			color.b += increment;
			if (color.b > 1) color.b = 1;
			updateColorLetters();
			return;
		}

		if (widget.getId() == 25) {
			color.b -= increment;
			if (color.b < 0) color.b = 0;
			updateColorLetters();
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
			String name = edNetwork.text.trim();

			txError.text = PC_Lang.tr("pc.gui.weasel.core.msgNetworkRenamed", new String[] { name });
			core.renameNetwork(name);

			w.calcSize();

			return;
		}

		if (widget.getId() == 2) {
			((PC_GresColor) colorBulb).setColor(PC_Color.pureRandomColor());
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

}
