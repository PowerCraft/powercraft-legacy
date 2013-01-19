package powercraft.backpacks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_SlotSelective;

public class PCbp_ContainerBackpackEnder extends PC_GresBaseWithInventory {

	protected List<Slot> lSlot;
	protected InventoryEnderChest inv;
	
	public PCbp_ContainerBackpackEnder(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	protected void init(Object[] o) {
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		inv = thePlayer.getInventoryEnderChest();
		for(int i=0; i<inv.getSizeInventory(); i++){
			lSlot.add(new PC_SlotSelective(inv, i, 0, 0));
		}
		slots.addAll(lSlot);
		return slots;
	}

}
