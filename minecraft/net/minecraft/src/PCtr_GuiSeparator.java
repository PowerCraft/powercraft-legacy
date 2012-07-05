// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
// GuiContainer, ContainerDispenser, FontRenderer, RenderEngine,
// InventoryPlayer, TileEntityDispenser

public class PCtr_GuiSeparator implements PC_IGresBase {

	EntityPlayer player;
	PCtr_TileEntitySeparationBelt tileentityconveyorfilter;

	public PCtr_GuiSeparator(EntityPlayer player, PCtr_TileEntitySeparationBelt tileentityconveyorfilter) {
		this.player = player;
		this.tileentityconveyorfilter = tileentityconveyorfilter;
	}

	@Override
	public EntityPlayer getPlayer() {
		return player;
	}

	@Override
	public void initGui(PC_IGresGui gui) {
		PC_GresWindow w = new PC_GresWindow(00, 00, PC_Lang.tr("tile.PCconveyorFilter.name"));
		w.setWidthForInventory();
		PC_GresLayoutH hg = new PC_GresLayoutH();
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 56, 66, 8, 15));
		
		PC_GresInventory left, right;
		
		hg.add(left = new PC_GresInventory(3, 3));
		
		hg.add(right = new PC_GresInventory(3, 3));
		
		for(int i=0; i<tileentityconveyorfilter.getSizeInventory(); i++) {
			if(i%6 >= 3) {
				left.setSlot(new Slot(tileentityconveyorfilter,i,0,0), i%3, (int) Math.floor(i/6));
			}else {
				right.setSlot(new Slot(tileentityconveyorfilter,i,0,0), i%3, (int) Math.floor(i/6));
			}
		}
		
		hg.add(new PC_GresImage(mod_PCcore.getImgDir() + "gres/widgets.png", 64, 66, 8, 15));
		w.add(hg);
		w.add(new PC_GresInventoryPlayer(true));
		gui.add(w);
	}

	@Override
	public void onGuiClosed(PC_IGresGui gui) {}

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
	public void onCraftMatrixChanged(IInventory iinventory) {}

	@Override
	public void updateTick(PC_IGresGui gui) {}
}
