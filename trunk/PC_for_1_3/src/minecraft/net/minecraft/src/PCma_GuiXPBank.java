package net.minecraft.src;


import java.util.Random;

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
	private PC_GresWidget txPlayerLevels;
	private PC_GresWindow w;


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
		w = new PC_GresWindow(PC_Lang.tr("tile.PCmaXPBank.name"));
		w.setAlignH(PC_GresAlign.CENTER);

		EntityPlayer player = getPlayer();

		if (player.experience < 0) player.experience = 0;
		if (player.experience >= 0.99 && player.experience <= 1.01) {
			player.experience = 0;
			player.experienceLevel++;
		}
		if (player.experienceTotal < 0) player.experienceTotal = 0;
		if (player.experienceLevel < 0) player.experienceLevel = 0;
		if (player.score < 0) player.score = 0;
		if (xpbank.xp < 0) xpbank.xp = 0;

		PC_GresWidget hg;

		int labelWidth = 0;
		labelWidth = Math.max(labelWidth, w.getStringWidth(PC_Lang.tr("pc.gui.xpbank.storagePoints")));
		labelWidth = Math.max(labelWidth, w.getStringWidth(PC_Lang.tr("pc.gui.xpbank.currentPlayerLevel")));
		labelWidth = Math.max(labelWidth, w.getStringWidth(PC_Lang.tr("pc.gui.xpbank.withdraw")));
		labelWidth = Math.max(labelWidth, w.getStringWidth(PC_Lang.tr("pc.gui.xpbank.deposit")));
		labelWidth = Math.max(labelWidth, 80);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setMinWidth(200);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.storagePoints")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg.add(txStoragePoints = new PC_GresLabel(xpbank.xp + "").setColor(PC_GresWidget.textColorEnabled, 0x009900));
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.xpUnit")));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setMinWidth(200);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.withdraw")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));

		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.oneLevel")).setId(10).setMinWidth(50).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.all")).setId(11).setMinWidth(50).setWidgetMargin(2));
		w.add(hg);

		w.add(new PC_GresSeparatorH(0, 5).setLineColor(0x999999));

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setMinWidth(200);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.currentPlayerLevel")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));
		hg.add(txPlayerLevels = new PC_GresLabel(xpbank.xp + "").setColor(PC_GresWidget.textColorEnabled, 0x990099));
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.xpLevels")));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.LEFT).setMinWidth(200);
		hg.add(new PC_GresLabel(PC_Lang.tr("pc.gui.xpbank.deposit")).setMinWidth(labelWidth).setAlignH(PC_GresAlign.RIGHT));

		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.oneLevel")).setId(20).setMinWidth(50).setWidgetMargin(2));
		hg.add(new PC_GresButton(PC_Lang.tr("pc.gui.xpbank.all")).setId(21).setMinWidth(50).setWidgetMargin(2));
		w.add(hg);

		hg = new PC_GresLayoutH().setAlignH(PC_GresAlign.CENTER);
		hg.add(buttonClose = new PC_GresButton(PC_Lang.tr("pc.gui.ok")).setId(0));
		w.add(hg);

		updateCounters();

		gui.add(w);

	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
		xpbank.worldObj.markBlockAsNeedsUpdate(xpbank.xCoord, xpbank.yCoord, xpbank.zCoord);
	}

	private void addToPlayer(EntityPlayer player, int points) {
		int xpsum = points;
		int cnt = 0;
		// add by parts to avoid bug in player.
		while (xpsum > 0) {
			int addedXP = 1;

			player.addExperience(addedXP);

			xpsum -= addedXP;
			if (xpsum <= 0) break;

			if (cnt++ > 10000000) {
				System.out.println("wtf!");
				return;
			}
		}
	}

	private void updateCounters() {
		txStoragePoints.setText(xpbank.xp + "").setMinWidth(0);
		txPlayerLevels.setText(getPlayer().experienceLevel + "").setMinWidth(0);
	}

	private int xpBarCap(int level) {
		return 7 + (level * 7 >> 1);
	}

	private void withdrawOneLevel(EntityPlayer player) {
		int level = player.experienceLevel;

		while ((player.experienceLevel == level) && (xpbank.xp > 0)) {
			int a = Math.min(xpbank.xp, 1);
			addToPlayer(player, a);
			xpbank.xp -= a;
		}
	}

	private void depositOneLevel(EntityPlayer player) {

		int playerPointsInLevelProgress = Math.round(player.experience * player.xpBarCap());

		if (playerPointsInLevelProgress > 0) {
			xpbank.xp += playerPointsInLevelProgress;
			player.experience = 0;
			player.experienceTotal -= playerPointsInLevelProgress;
			player.score -= playerPointsInLevelProgress;
			return;
		} else {

			player.experience = 0;

			player.experienceTotal -= playerPointsInLevelProgress;
			player.score -= playerPointsInLevelProgress;

			int pointsToDeposit;
			if (player.experienceLevel > 0) {
				player.experienceLevel--;
				pointsToDeposit = xpBarCap(player.experienceLevel);

				xpbank.xp += pointsToDeposit;

				player.experience -= pointsToDeposit;
				player.experienceTotal -= pointsToDeposit;
				player.score -= pointsToDeposit;

			}
		}
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		EntityPlayer player = getPlayer();
		Random rand = new Random();



		switch (widget.getId()) {
			case 0:
				gui.close();
				break;

			case 10: //withdraw one level

				withdrawOneLevel(player);

				PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				break;

			case 11: // withdraw all				

				while (xpbank.xp > 0) {
					if (player.experience < 0) player.experience = 0;
					if (player.experience >= 0.99 && player.experience <= 1.01) {
						player.experience = 0;
						player.experienceLevel++;
					}
					if (player.experienceTotal < 0) player.experienceTotal = 0;
					if (player.experienceLevel < 0) player.experienceLevel = 0;
					if (player.score < 0) player.score = 0;
					withdrawOneLevel(player);
				}
				PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				break;

			case 20: //deposit one level
				depositOneLevel(player);

				PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				break;

			case 21: //deposit all
				while (player.experienceLevel > 0 || player.experience > 0) {
					if (player.experience < 0) player.experience = 0;
					if (player.experience >= 0.99 && player.experience <= 1.01) {
						player.experience = 0;
						player.experienceLevel++;
					}
					if (player.experienceTotal < 0) player.experienceTotal = 0;
					if (player.experienceLevel < 0) player.experienceLevel = 0;
					if (player.score < 0) player.score = 0;
					depositOneLevel(player);
				}

				player.experienceLevel = 0;
				player.experience = 0;
				player.experienceTotal = 0;
				player.score = 0;

				PC_Utils.mc().theWorld.playSoundAtEntity(player, "random.orb", 0.3F, 0.5F * ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.8F));
				break;


		}

		if (player.experience < 0) player.experience = 0;
		if (player.experienceTotal < 0) player.experienceTotal = 0;
		if (player.experienceLevel < 0) player.experienceLevel = 0;
		if (player.score < 0) player.score = 0;
		updateCounters();
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
