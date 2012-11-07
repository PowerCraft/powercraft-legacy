package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCraftResult;
import net.minecraft.src.Slot;
import powercraft.core.PC_GresBaseWithInventory;
import powercraft.core.PC_Utils;

public class PCma_ContainerAutomaticWorkbench extends PC_GresBaseWithInventory {

	private IInventory craftResult;
	protected PCma_TileEntityAutomaticWorkbench teaw;
	protected List<Slot> lSlot;
	
	public PCma_ContainerAutomaticWorkbench(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	protected void init(Object[] o) {
		craftResult = new InventoryCraftResult();
		teaw = (PCma_TileEntityAutomaticWorkbench)PC_Utils.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {
		craftResult.setInventorySlotContents(0, teaw.getRecipeProduct());
	}

	@Override
	public List<Slot> getAllSlots(List<Slot> list) {
		lSlot = new ArrayList<Slot>();
		int cnt = 0;
		lSlot.add(new PCma_SlotAutomaticWorkbenchResult(teaw, craftResult, this, 0, 0, 0));
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(teaw, this, false, cnt++, 0, 0));
			}
		}
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(teaw, this, true, cnt++, 0, 0));
			}
		}
		list.addAll(lSlot);
		return list;
	}

}
