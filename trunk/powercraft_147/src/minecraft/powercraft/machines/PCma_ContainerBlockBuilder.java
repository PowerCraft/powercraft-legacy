package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_SlotSelective;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerBlockBuilder extends PC_GresBaseWithInventory {

	protected List<Slot> lSlot;
	protected PCma_TileEntityBlockBuilder te;

	public PCma_ContainerBlockBuilder(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	protected void init(Object[] o) {
		te = GameInfo.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (i + j * 3 < te.getSizeInventory()) {
					lSlot.add(new PC_SlotSelective(te, i + j * 3, 0, 0));
				}
			}
		}
		slots.addAll(lSlot);
		return slots;
	}

}
