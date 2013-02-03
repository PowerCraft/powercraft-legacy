package powercraft.checkpoints;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCcp_ContainerCheckpoint extends PC_GresBaseWithInventory<PCcp_TileEntityCheckpoint> {

	protected List<Slot> lSlot;
	
	public PCcp_ContainerCheckpoint(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, GameInfo.<PCcp_TileEntityCheckpoint>getTE(player.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]), o);
	}

	@Override
	protected void init(Object[] o) {}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		for(int i=0; i<tileEntity.getSizeInventory(); i++){
			lSlot.add(new PC_Slot(tileEntity, i));
		}
		slots.addAll(lSlot);
		return slots;
	}

}
