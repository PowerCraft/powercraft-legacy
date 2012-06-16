package net.minecraft.src;

import org.lwjgl.opengl.GL11;



/**
 * Block Builder GUI
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_GuiBlockBuilder extends GuiContainer {

	/**
	 * @param inventoryplayer player inventory
	 * @param tilee device tile entity
	 */
	public PCma_GuiBlockBuilder(InventoryPlayer inventoryplayer, PCma_TileEntityBlockBuilder tilee) {
		super(new PCma_ContainerBlockBuilder(inventoryplayer, tilee));
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		String title = PC_Lang.tr("tile.PCmaBlockBuilder.name");
		fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 6, 0x404040);
		fontRenderer.drawString(PC_Lang.tr("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/gui/trap.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
}
