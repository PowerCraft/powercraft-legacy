// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
// GuiContainer, ContainerDispenser, FontRenderer, RenderEngine,
// InventoryPlayer, TileEntityDispenser

public class PCtr_GuiConveyorSeparator extends GuiContainer {

	public PCtr_GuiConveyorSeparator(InventoryPlayer inventoryplayer, PCtr_TileEntitySeparationBelt tileentityconveyorfilter) {
		super(new PCtr_ContainerConveyorFilter(inventoryplayer, tileentityconveyorfilter));
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString(PC_Lang.tr("tile.PCconveyorFilter.name"), 39, 6, 0x404040);
		fontRenderer.drawString(PC_Lang.tr("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int o, int p) {
		int i = mc.renderEngine.getTexture("/PowerCraft/transport/gui_separator.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}
}
