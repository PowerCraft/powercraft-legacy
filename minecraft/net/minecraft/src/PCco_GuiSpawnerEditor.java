package net.minecraft.src;


import net.minecraft.src.PC_GresWidget.PC_GresAlign;


/**
 * GUI for editing spawned mob from spawner.<br>
 * Part of CORE.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_GuiSpawnerEditor implements PC_IGresBase {

	private TileEntityMobSpawner spawner;

	private PC_GresCheckBox checkDangerous;

	private PC_GresWidget[] buttons = new PC_GresWidget[25];

	private static String[] mapping = { "Creeper", "Skeleton", "Spider", "CaveSpider", "Zombie", "PigZombie", "Enderman", "Silverfish", "Slime", "LavaSlime", "Ghast", "Blaze", "SnowMan", "VillagerGolem", "Villager", "Pig", "Sheep", "Cow", "Chicken",
			"Squid", "Wolf", "MushroomCow", "Ozelot", "EnderDragon" };

	/**
	 * Spawner mob editor
	 * 
	 * @param tileEntityMobSpawner the spawner to edit
	 */
	public PCco_GuiSpawnerEditor(TileEntityMobSpawner tileEntityMobSpawner) {
		spawner = tileEntityMobSpawner;

		spawner.getMobID();
	}

	@Override
	public EntityPlayer getPlayer() {
		return PC_Utils.mc().thePlayer;
	}

	@Override
	public void initGui(PC_IGresGui gui) {

		// @formatter:off
		String[] names = new String[]{
			PC_Lang.tr("entity.Creeper.name"),
			PC_Lang.tr("entity.Skeleton.name"),
			PC_Lang.tr("entity.Spider.name"),
			PC_Lang.tr("entity.CaveSpider.name"),
			PC_Lang.tr("entity.Zombie.name"),
			PC_Lang.tr("entity.PigZombie.name"),
			PC_Lang.tr("entity.Enderman.name"),
			PC_Lang.tr("entity.Silverfish.name"),
			PC_Lang.tr("entity.Slime.name"),
			PC_Lang.tr("entity.LavaSlime.name"),
			PC_Lang.tr("entity.Ghast.name"),
			PC_Lang.tr("entity.Blaze.name"),
			PC_Lang.tr("entity.SnowMan.name"),
			PC_Lang.tr("entity.VillagerGolem.name"),
			PC_Lang.tr("entity.Villager.name"),
			PC_Lang.tr("entity.Pig.name"),
			PC_Lang.tr("entity.Sheep.name"),
			PC_Lang.tr("entity.Cow.name"),
			PC_Lang.tr("entity.Chicken.name"),
			PC_Lang.tr("entity.Squid.name"),
			PC_Lang.tr("entity.Wolf.name"),
			PC_Lang.tr("entity.MushroomCow.name"),
			PC_Lang.tr("entity.Ozelot.name"),
			PC_Lang.tr("entity.EnderDragon.name") };
		// @formatter:on

		PC_GresWidget w = new PC_GresWindow(230, 100, PC_Lang.tr("tile.mobSpawner.name")).setAlignH(PC_GresAlign.STRETCH);
		((PC_GresWindow) w).gapUnderTitle = 12;

		PC_GresLayoutV vg = new PC_GresLayoutV();

		PC_GresLayoutH hg = null;

		int maxw = 0;
		int hgTotalWidth = 0;

		for (int i = 0; i < mapping.length; i++) {
			if (i % 3 == 0) {
				hg = new PC_GresLayoutH();
				hg.setWidgetMargin(0);
			}

			buttons[i] = new PC_GresButton(names[i]).setId(i).setWidgetMargin(1);

			maxw = Math.max(buttons[i].getMinSize().x, maxw);

			hg.add(buttons[i]);
			if (i % 3 == 2) {
				vg.add(hg);
			}
		}
		w.add(vg);

		hgTotalWidth = maxw * 3 + 2;

		for (int i = 0; i < mapping.length; i++) {
			buttons[i].setMinWidth(maxw);
		}

		hg = new PC_GresLayoutH();

		hg.add(checkDangerous = new PC_GresCheckBox(PC_Lang.tr("pc.gui.spawnerEditor.enableDangerous")));
		hg.add(new PC_GresGap(hgTotalWidth - (maxw + checkDangerous.getMinSize().x + 8), 3));
		hg.add(buttons[24] = new PC_GresButton(PC_Lang.tr("pc.gui.cancel")).setId(24).setMinWidth(maxw));

		w.add(hg);

		gui.add(w);

		setDangerousEnabled(false);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

	private void setDangerousEnabled(boolean state) {
		buttons[10].enabled = buttons[11].enabled = buttons[23].enabled = state;
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {

		if (widget == checkDangerous) {
			setDangerousEnabled(checkDangerous.isChecked());
			return;
		}

		if (widget.getId() == 24) {
			gui.close();
			return;
		}

		if (widget.getId() < 24) {
			spawner.setMobID(mapping[widget.getId()]);
			gui.close();
			return;
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
	public void updateTick(PC_IGresGui gui) {}

}
