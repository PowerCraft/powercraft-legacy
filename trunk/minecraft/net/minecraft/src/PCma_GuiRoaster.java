package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Roaster's GUI screen
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_GuiRoaster extends GuiContainer {
	private IInventory roasterinv;
	private int inventoryRows;

	/**
	 * Roaster's GUI screen
	 * 
	 * @param playerinv
	 * @param roasterinv
	 */
	public PCma_GuiRoaster(IInventory playerinv, IInventory roasterinv) {
		super(new PCma_ContainerRoaster(playerinv, roasterinv));
		inventoryRows = 0;
		this.roasterinv = roasterinv;
		allowUserInput = false;
		char c = '\336';
		int i = c - 108;
		inventoryRows = roasterinv.getSizeInventory() / 9;
		ySize = i + inventoryRows * 18;
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString(roasterinv.getInvName(), 8, 6, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/PowerCraft/core/normal-container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(k);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, inventoryRows * 18 + 17);
		drawTexturedModalRect(l, i1 + inventoryRows * 18 + 17, 0, 144, xSize, 96);
	}
}
