package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_SlotSelective;
import powercraft.management.PC_Utils.GameInfo;

public class PCws_ContainerWeaselDiskDrive extends PC_GresBaseWithInventory {

	protected PCws_TileEntityWeasel te;
	protected List<Slot> lSlot;
	
	public PCws_ContainerWeaselDiskDrive(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	protected void init(Object[] o) {
		te = GameInfo.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		for(int i=0; i<te.getSizeInventory(); i++)
			lSlot.add(new PC_SlotSelective(te, i, 0, 0));
		slots.addAll(lSlot);
		return slots;
	}

}
