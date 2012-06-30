// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
// Container, Slot, TileEntityDispenser, IInventory,
// EntityPlayer

public class PCtr_ContainerConveyorFilter extends Container {

	@Override
	protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer) {

	}

	public PCtr_ContainerConveyorFilter(IInventory iinventory, PCtr_TileEntitySeparationBelt tileentityconveyorfilter) {
		tileEntityDispenser = tileentityconveyorfilter;
		int fieldCounter = -1;
		for (int i = 0; i < 3; i++) {
			for (int l = 0; l < 7; l++) {
				if (l != 3) {
					fieldCounter++;
					addSlot(new Slot(tileentityconveyorfilter, fieldCounter, 26 + l * 18, 17 + i * 18));
				}
			}
		}

		for (int j = 0; j < 3; j++) {
			for (int i1 = 0; i1 < 9; i1++) {
				addSlot(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
			}

		}

		for (int k = 0; k < 9; k++) {
			addSlot(new Slot(iinventory, k, 8 + k * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntityDispenser.canInteractWith(entityplayer);
	}

	private PCtr_TileEntitySeparationBelt tileEntityDispenser;
}
