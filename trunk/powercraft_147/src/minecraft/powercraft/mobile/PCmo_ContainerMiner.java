package powercraft.mobile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_TileEntity;

public class PCmo_ContainerMiner extends PC_GresBaseWithInventory<PC_TileEntity> {

	protected PCmo_EntityMiner miner;
	protected List<Slot> lSlot;
	
	
	public PCmo_ContainerMiner(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}
	
	@Override
	protected void init(Object[] o) {
		miner = (PCmo_EntityMiner)thePlayer.worldObj.getEntityByID((Integer)o[0]);
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		for(int i=0; i<miner.xtals.getSizeInventory(); i++){
			lSlot.add(new PC_Slot(miner.xtals, i));
		}
		for(int i=0; i<miner.cargo.getSizeInventory(); i++){
			lSlot.add(new PC_Slot(miner.cargo, i));
		}
		slots.addAll(lSlot);
		return slots;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		miner.xtals.closeChest();
		miner.cargo.closeChest();
	}
	
}
