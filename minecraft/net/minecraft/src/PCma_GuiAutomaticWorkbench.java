package net.minecraft.src;

import org.lwjgl.opengl.GL11;


/**
 * Automatic Workbench's GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_GuiAutomaticWorkbench extends GuiContainer {
	/**
	 * @param inventoryplayer player inventory
	 * @param tileentity tile entity of the Automatic Workbench
	 */
	public PCma_GuiAutomaticWorkbench(InventoryPlayer inventoryplayer, PCma_TileEntityAutomaticWorkbench tileentity) {
		super(new PCma_ContainerAutomaticWorkbench(inventoryplayer, tileentity));
		ySize = 186;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString(PC_Lang.tr("tile.PCmaAutoWorkbench.name"), 37, 8, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int o, int p) {
		int i = mc.renderEngine.getTexture(mod_PCmachines.getImgDir() + "gui_act.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}
}
