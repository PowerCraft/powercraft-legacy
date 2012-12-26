package powercraft.net;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_GresButton;
import powercraft.management.PC_GresCheckBox;
import powercraft.management.PC_GresLabel;
import powercraft.management.PC_GresLayoutH;
import powercraft.management.PC_GresLayoutV;
import powercraft.management.PC_GresTextEdit;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_GresTextEdit.PC_GresInputType;
import powercraft.management.PC_GresWidget;
import powercraft.management.PC_GresWidget.PC_GresAlign;
import powercraft.management.PC_GresWindow;
import powercraft.management.PC_IGresClient;
import powercraft.management.PC_IGresGui;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_VecI;

public class PCnt_GuiRadio implements PC_IGresClient {

	private String errMsg = "";

	private PC_GresWidget buttonOK, buttonCancel;
	private PC_GresWidget edit;
	private PC_GresWidget txError;
	private PCnt_TileEntityRadio ter;

	private PC_GresCheckBox checkLabel;

	private PC_GresCheckBox checkMicro;
	
	public PCnt_GuiRadio(EntityPlayer player, Object[] o) {
		ter = GameInfo.getTE(player.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}
	
	@Override
	public void initGui(PC_IGresGui gui) {
		String title = "";
		if (ter.isTransmitter()) {
			title = Lang.tr("tile.PCnt_BlockRadio.tx.name");
		} else {
			title = Lang.tr("tile.PCnt_BlockRadio.rx.name");
		}

		// window
		PC_GresWindow w = new PC_GresWindow(title);
		w.setAlignH(PC_GresAlign.CENTER);
		PC_GresWidget hg;

		// layout with the input
		PC_GresWidget vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.LEFT);
		vg.add(new PC_GresLabel(Lang.tr("pc.gui.radio.channel")));
		vg.add(edit = new PC_GresTextEdit(ter.getChannel(), 20, PC_GresInputType.TEXT).setMinWidth(140));
		w.add(vg);

		// eror
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(txError = new PC_GresLabel("").setColor(PC_GresWidget.textColorEnabled, 0x990000));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(checkLabel = new PC_GresCheckBox(Lang.tr("pc.gui.radio.showLabel")));
		checkLabel.check(!ter.hideLabel);

		hg.add(checkMicro = new PC_GresCheckBox(Lang.tr("pc.gui.radio.renderSmall")));
		checkMicro.check(ter.renderMicro);
		w.add(hg);

		// buttons
		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonCancel = new PC_GresButton(Lang.tr("pc.gui.cancel")).setId(1));
		hg.add(buttonOK = new PC_GresButton(Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		gui.add(w);


		//gui.setPausesGame(true);

		// refresh labels.
		actionPerformed(edit, gui);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget.getId() == 0) {

			String newChannel = edit.getText().trim();

			PC_PacketHandler.setTileEntity(ter, "channel", newChannel,
					"renderMicro", checkMicro.isChecked(),
					"hideLabel", !checkLabel.isChecked());

			gui.close();

		} else if (widget.getId() == 1) {
			gui.close();
		}

		if (widget == edit) {

			if (edit.getText().trim().length() == 0) {
				errMsg = "pc.gui.radio.errChannel";
				txError.setText(Lang.tr(errMsg));
			} else {
				txError.setText("");
			}

		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		actionPerformed(buttonOK, gui);
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2,
			float par3) {
		return false;
	}

}
