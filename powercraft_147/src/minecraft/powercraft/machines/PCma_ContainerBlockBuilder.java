package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerBlockBuilder extends PC_GresBaseWithInventory<PCma_TileEntityBlockBuilder> {

	protected List<Slot> lSlot;
	
	public PCma_ContainerBlockBuilder(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCma_TileEntityBlockBuilder)te, o);
	}

	@Override
	protected void init(Object[] o) {}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (i + j * 3 < tileEntity.getSizeInventory()) {
					lSlot.add(new PC_Slot(tileEntity, i + j * 3));
				}
			}
		}
		slots.addAll(lSlot);
		return slots;
	}

}
