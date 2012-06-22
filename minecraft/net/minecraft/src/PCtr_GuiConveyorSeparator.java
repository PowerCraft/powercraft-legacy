// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
// GuiContainer, ContainerDispenser, FontRenderer, RenderEngine,
// InventoryPlayer, TileEntityDispenser

public class PCtr_GuiConveyorSeparator implements PC_IGresBase {

	EntityPlayer player;
	PCtr_TileEntitySeparationBelt tileentityconveyorfilter;
	
	public PCtr_GuiConveyorSeparator(EntityPlayer player, PCtr_TileEntitySeparationBelt tileentityconveyorfilter) {
		this.player = player;
		this.tileentityconveyorfilter = tileentityconveyorfilter;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(50, 50, PC_Lang.tr("tile.PCconveyorFilter.name"));
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 56, 66, 8, 15));
		hg.add(new PC_GresInventory(tileentityconveyorfilter, 3, 3));
		hg.add(new PC_GresInventory(tileentityconveyorfilter, 3, 3, 9, 18));
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 64, 66, 8, 15));
		w.add(hg);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {
	}

	@Override
	public void actionPerformed(PC_GresWidget widget, PC_IGresGui gui) {
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
	public void onCraftMatrixChanged(IInventory iinventory) {
	}
}
