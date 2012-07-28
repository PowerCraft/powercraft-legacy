package net.minecraft.src;


import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * drive
 * 
 * @author MightyPork
 */
public class PClo_GuiWeaselDiskDrive implements PC_IGresBase {

	private PC_GresWindow w;
	private PClo_WeaselPluginDiskDrive drive;
	private PC_GresWidget edName;
	private PC_GresWidget txError;
	private PC_GresWidget btnOk;

	/**
	 * gui for drive
	 * 
	 * @param drive drive plugin
	 */
	public PClo_GuiWeaselDiskDrive(PClo_WeaselPluginDiskDrive drive) {
		this.drive = drive;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}


	@Override
	public void initGui(PC_IGresGui gui) {

		w = new PC_GresWindow(PC_Lang.tr("pc.gui.weasel.diskDrive.title"));
		w.setAlignH(PC_GresAlign.CENTER);
		w.setWidthForInventory();
		w.setMinWidth(260);

		PC_GresWidget hg;

		int colorLabel = 0x000000;
		int colorValue = 0x000099;

		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.connectedToNetwork")).setColor(PC_GresWidget.textColorEnabled, colorLabel));
		hg.add(new PC_GresColor(drive.getNetworkColor()));
		hg.add(new PC_GresLabel(drive.getNetworkName()).setColor(PC_GresWidget.textColorEnabled, colorValue));
		w.add(hg);


		hg = new PC_GresLayoutH();
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.weasel.drive.driveName")));
		hg.add(edName = new PC_GresTextEdit(drive.getName(), 14, PC_GresInputType.IDENTIFIER).setWidgetMargin(2));
		hg.add(btnOk = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setMinWidth(0));
		w.add(hg);

		w.add(txError = new PC_GresLabel("").setWidgetMargin(2).setColor(PC_GresWidget.textColorEnabled, 0x000000));

		w.add(new PC_GresInventory(drive.getInventory(), 4, 2));

		w.add(new PC_GresInventoryPlayer(true));


		w.add(new PC_GresGap(0, 0));

		gui.add(w);

		gui.setCanShiftTransfer(true);
		btnOk.enable(false);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		drive.isChanged = true;
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		if (widget == edName) {
			String name = edName.text.trim();
			if (name.length() == 0) {
				txError.text = PC_Lang.tr("pc.gui.weasel.errDeviceNameTooShort");
				btnOk.enabled = false;
			} else if (drive.getNetwork() != null && drive.getNetwork().getMembers().get(name) != null) {
				txError.text = PC_Lang.tr("pc.gui.weasel.errDeviceNameAlreadyUsed", name);
				btnOk.enabled = false;
			} else {
				txError.text = "";
				btnOk.enabled = true;
			}
			w.calcSize();
			return;
		}

		if (widget == btnOk) {
			String name = edName.text.trim();
			drive.setMemberName(name);
			txError.text = PC_Lang.tr("pc.gui.weasel.deviceRenamed", name);
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
