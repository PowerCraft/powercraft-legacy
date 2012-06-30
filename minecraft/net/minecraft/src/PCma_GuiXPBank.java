package net.minecraft.src;


import java.util.Random;

import net.minecraft.src.PC_GresTextEdit.PC_GresInputType;
import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_GuiXPBank implements PC_IGresBase {

	private PCma_TileEntityXPBank xpbank;
	@SuppressWarnings("unused")
	private PC_GresWidget buttonClose;
	private PC_GresWidget txStoragePoints;
	private PC_GresWidget txPlayerPoints;
	private PC_GresWidget txPlayerLevel;
	private PC_GresWidget editWithdraw;
	private PC_GresWidget editDeposit;
	private PC_GresWindow window;



	/**
	 * XP bank gui
	 * 
	 * @param tex
	 */
	public PCma_GuiXPBank(PCma_TileEntityXPBank tex) {
		xpbank = tex;
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		window = new PC_GresWindow(PC_Lang.tr("tile.PCmaXPBank.name"));
		window.setAlignH(PC_GresAlign.CENTER);

		PC_GresWidget vg, hg, hg1;

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.STRETCH);

		vg = new PC_GresLayoutV().setAlignH(PC_GresAlign.STRETCH);

		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);

		int labelWidth = 0;
		labelWidth = Math.max(labelWidth, window.getStringWidth(PC_Lang.tr("pc.gui.xpbank.totalXp")));
		labelWidth = Math.max(labelWidth, window.getStringWidth(PC_Lang.tr("pc.gui.xpbank.currentPlayerPoints")));
		labelWidth = Math.max(labelWidth, window.getStringWidth(PC_Lang.tr("pc.gui.xpbank.currentPlayerLevel")));
		labelWidth = Math.max(labelWidth, window.getStringWidth(PC_Lang.tr("pc.gui.xpbank.pointsToWithdraw")));
		labelWidth = Math.max(labelWidth, window.getStringWidth(PC_Lang.tr("pc.gui.xpbank.pointsToDeposit")));
		labelWidth += 5;

		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.storagePoints")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg1.add(txStoragePoints = new PC_GresLabel(xpbank.xp + "").setColor(PC_GresWidget.textColorEnabled, 0x009900));
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.xpUnit")));
		vg.add(hg1);

		vg.add(new PC_GresSeparatorH(0, 5).setLineColor(0xbbbbbb));

		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.currentPlayerPoints")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg1.add(txPlayerPoints = new PC_GresLabel(getPlayer().experienceTotal + "").setColor(PC_GresWidget.textColorEnabled, 0x000099));
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.xpUnit")));
		vg.add(hg1);

		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.currentPlayerLevel")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg1.add(txPlayerLevel = new PC_GresLabel(getPlayer().experienceLevel + "").setColor(PC_GresWidget.textColorEnabled, 0x990099));
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.xpLevels")));
		vg.add(hg1);

		vg.add(new PC_GresSeparatorH(0, 5).setLineColor(0xbbbbbb));

		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.pointsToWithdraw")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg1.add(editWithdraw = new PC_GresTextEdit(xpbank.xp + "", 8, PC_GresInputType.UNSIGNED_INT).setId(10));
		hg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.withdrawButton")).setId(11));
		vg.add(hg1);

		hg1 = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT);
		hg1.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.pointsToDeposit")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg1.add(editDeposit = new PC_GresTextEdit(getPlayer().experienceTotal + "", 8, PC_GresInputType.UNSIGNED_INT).setId(20));
		hg1.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.depositButton")).setId(21));
		vg.add(hg1);

		hg.add(vg);
		window.add(hg);

		// window.add(new PC_GresGap(0, 6));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonClose = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		window.add(hg);

		gui.add(window);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private void addToPlayer(EntityPlayer player, int points) {
		int xpsum = points;
		// add by parts to avoid bug in player.
		while (xpsum > 0) {
			int addedXP = Math.min(xpsum, player.xpBarCap());
			player.addExperience(addedXP);
			xpsum -= addedXP;
		}
	}

	private void updateCounters() {
		txPlayerLevel.setText(getPlayer().experienceLevel + "").setMinWidth(0);
		txPlayerPoints.setText(getPlayer().experienceTotal + "").setMinWidth(0);
		txStoragePoints.setText(xpbank.xp + "").setMinWidth(0);
		window.calcSize();
	}

	private void updateFields() {
		editDeposit.setText(getPlayer().experienceTotal + "");
		editWithdraw.setText(xpbank.xp + "");
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
		int points = 0;
		EntityPlayer player = getPlayer();
		switch (widget.getId()) {
			case 0:
				gui.close();
				break;

			case 11:
				points = 0;
				try {
					points = Integer.parseInt(editWithdraw.getText());

					points = Math.min(points, xpbank.xp);

					Random rand = new Random();
					PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));

					addToPlayer(player, points);

					xpbank.xp -= points;

					updateCounters();
					updateFields();


				} catch (NumberFormatException e) {}

				break;
			case 21:
				points = 0;
				try {
					points = Integer.parseInt(editDeposit.getText());

					points = Math.min(points, player.experienceTotal);

					Random rand = new Random();
					PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));

					int totalPoints = player.experienceTotal;
					player.experienceLevel = 0;
					player.experienceTotal = 0;
					player.score = 0;
					player.experience = 0;

					xpbank.xp += points;
					totalPoints -= points;

					addToPlayer(player, totalPoints);

					updateCounters();
					updateFields();

				} catch (NumberFormatException e) {}

				break;
		}
	}

	@Override
	public void onEscapePressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onReturnPressed(PC_IGresGui gui) {
		gui.close();
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {
		updateCounters();
	}

}
