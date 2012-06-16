package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * GUI for editing spawned mob from spawner.<br>
 * Part of CORE.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCco_GuiSpawnerEditor extends GuiScreen {

	private TileEntityMobSpawner spawner;

	private PC_GuiCheckBox checkDangerous;

	private GuiButton[] buttons = new GuiButton[24];

	private static String[] mapping = { "Creeper", "Skeleton", "Spider", "CaveSpider", "Zombie", "PigZombie", "Enderman", "Silverfish", "Slime", "LavaSlime", "Ghast", "Blaze", "SnowMan", "VillagerGolem", "Villager", "Pig", "Sheep", "Cow", "Chicken", "Squid", "Wolf", "MushroomCow", "Ozelot", "EnderDragon" };

	private static String[] names;

	/**
	 * Spawner mob editor
	 * 
	 * @param tileEntityMobSpawner the spawner to edit
	 */
	public PCco_GuiSpawnerEditor(TileEntityMobSpawner tileEntityMobSpawner) {
		spawner = tileEntityMobSpawner;

		spawner.getMobID();
	}

	private void loadNames() {
		// @formatter:off
		names = new String[]{
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
	}

	@Override
	public void updateScreen() {

		buttons[10].enabled = buttons[11].enabled = buttons[23].enabled = checkDangerous.isChecked();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		loadNames();
		controlList.clear();

		int row = 3;
		int bwidth = 77;
		int bheight = 20;
		int hGap = 1;
		int vGap = 2;

		for (int i = 0; i < mapping.length; i++) {
			GuiButton but = new GuiButton(i, (width / 2) + 1 - ((row * (bwidth + hGap)) / 2) + (i % row) * (bwidth + hGap), (height / 2)
					- 90 + (int) Math.floor(i / row) * (bheight + vGap), names[i]);
			but.width = bwidth;

			controlList.add(but);

			buttons[i] = but;
		}

		PC_GuiButtonAligner.alignSingleToRight(controlList, 24, "pc.gui.cancel", bwidth, height / 2 + 88, width / 2 + 120 - 10);

		checkDangerous = new PC_GuiCheckBox(this, fontRenderer, width / 2 - 100, height / 2 + 94, false,
				PC_Lang.tr("pc.gui.spawnerEditor.enableDangerous"));

		buttons[10].enabled = buttons[11].enabled = buttons[23].enabled = false;

	}

	@Override
	public void onGuiClosed() {}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) { return; }

		if (guibutton.id < 24) {
			spawner.setMobID(mapping[guibutton.id]);
		}

		mc.displayGuiScreen(null);
		mc.setIngameFocus();

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char c, int i) {}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		checkDangerous.mouseClicked(i, j, k);
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();

		drawGuiBackgroundLayer(f);

		GL11.glPushMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		String title = PC_Lang.tr("tile.mobSpawner.name");

		fontRenderer.drawString(title, width / 2 - (fontRenderer.getStringWidth(title) / 2), (height / 2) - 105, 0x000000);

		GL11.glPopMatrix();

		super.drawScreen(i, j, f);
		checkDangerous.drawCheckBox();

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private void drawGuiBackgroundLayer(float f) {
		int i = mc.renderEngine.getTexture(mod_PCcore.getImgDir() + "dialog-large.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width) / 2;
		int k = (height) / 2;
		drawTexturedModalRect(j - 115 - 5, k - 115, 0, 0, 240, 230);
	}

}
