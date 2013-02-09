package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.gres.PC_GresCheckBox;
import powercraft.management.gres.PC_GresGap;
import powercraft.management.gres.PC_GresImage;
import powercraft.management.gres.PC_GresInventory;
import powercraft.management.gres.PC_GresInventoryPlayer;
import powercraft.management.gres.PC_GresLabel;
import powercraft.management.gres.PC_GresLayoutH;
import powercraft.management.gres.PC_GresLayoutV;
import powercraft.management.gres.PC_GresWidget;
import powercraft.management.gres.PC_GresWindow;
import powercraft.management.gres.PC_IGresClient;
import powercraft.management.gres.PC_IGresGui;
import powercraft.management.gres.PC_GresWidget.PC_GresAlign;

public class PCtr_GuiSeparationBelt extends PCtr_ContainerSeparationBelt implements PC_IGresClient {

	private PC_GresCheckBox checkLogs;
	private PC_GresCheckBox checkPlanks;
	private PC_GresCheckBox checkAll;

	public PCtr_GuiSeparationBelt(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(00, 00, Lang.tr("tile.PCtr_BlockBeltSeparator.name"));
		w.setWidthForInventory();
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.add(new PC_GresImage(ModuleInfo.getGresImgDir()+"widgets.png", 56, 66, 8, 15));

		PC_GresInventory left, right;

		hg.add(left = new PC_GresInventory(3, 3));

		hg.add(right = new PC_GresInventory(3, 3));

		for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
			if (i % 6 >= 3) {
				left.setSlot(i % 3, (int) Math.floor(i / 6), invSlots[i]);
			} else {
				right.setSlot(i % 3, (int) Math.floor(i / 6), invSlots[i]);
			}
		}
		hg.add(new PC_GresImage(ModuleInfo.getGresImgDir()+"widgets.png", 64, 66, 8, 15));
		w.add(hg);

		PC_GresLayoutV vg = new PC_GresLayoutV();
		vg.setAlignH(PC_GresAlign.LEFT);
		vg.setMinWidth(100);
		vg.add(new PC_GresLabel(Lang.tr("pc.gui.separationBelt.group")).setWidgetMargin(0));
		vg.setWidgetMargin(0);

		hg = new PC_GresLayoutH();
		hg.setAlignH(PC_GresAlign.LEFT);
		hg.setWidgetMargin(0);
		hg.add(checkLogs = new PC_GresCheckBox(Lang.tr("pc.gui.separationBelt.groupLogs")).check(tileEntity.isGroupLogs()));
		hg.add(checkPlanks = new PC_GresCheckBox(Lang.tr("pc.gui.separationBelt.groupPlanks")).check(tileEntity.isGroupPlanks()));
		hg.add(checkAll = new PC_GresCheckBox(Lang.tr("pc.gui.separationBelt.groupAll")).check(tileEntity.isGroupAll()));

		vg.add(hg);

		w.add(new PC_GresGap(0, 2));
		w.add(vg);
		w.add(new PC_GresGap(0, 2));

		w.add(new PC_GresInventoryPlayer(true));
		w.add(new PC_GresGap(0, 0));
		gui.add(w);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		tileEntity.setGroupLogs(checkLogs.isChecked());
		tileEntity.setGroupPlanks(checkPlanks.isChecked());
		tileEntity.setGroupAll(checkAll.isChecked());
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void updateTick(PC_IGresGui gui) {}

	@Override
	public void updateScreen(PC_IGresGui gui) {}

	@Override
	public boolean drawBackground(PC_IGresGui gui, int par1, int par2, float par3) {
		return false;
	}

	@Override
	public void keyChange(String key, Object value) {}

}
