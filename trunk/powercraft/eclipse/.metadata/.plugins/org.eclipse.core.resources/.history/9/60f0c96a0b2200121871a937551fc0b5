package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class PCco_ContainerCraftingTool extends PC_GresBaseWithInventory{

	protected Slot trash;
	protected List<PCco_SlotDirectCrafting> allMcSlots;
	protected HashMap<String, List<PCco_SlotDirectCrafting>> moduleList;
	
	public PCco_ContainerCraftingTool(EntityPlayer player, Object[]o){
		super(player);
	}
	
	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		allMcSlots = new ArrayList<PCco_SlotDirectCrafting>();
		moduleList = new HashMap<String, List<PCco_SlotDirectCrafting>>();
		slots.add(trash = new PC_SlotTrash());
		for(Item i:Item.itemsList){
			if(i!=null){
				List<ItemStack> a = new ArrayList<ItemStack>();
				if(i instanceof PC_ICraftingToolDisplayer){
					String module = ((PC_ICraftingToolDisplayer)i).getCraftingToolModule();
					List<ItemStack> l = ((PC_ICraftingToolDisplayer)i).getItemStacks(new ArrayList<ItemStack>());
					List<PCco_SlotDirectCrafting> ls;
					if(moduleList.containsKey(module)){
						ls = moduleList.get(module);
					}else{
						moduleList.put(module, ls = new ArrayList<PCco_SlotDirectCrafting>());
					}
					for(ItemStack is:l){
						ls.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
					}
					continue;
				}
				if(i instanceof ItemBlock){
					int id = ((ItemBlock)i).getBlockID();
					Block b = Block.blocksList[id];
					if(b!=null && b instanceof PC_ICraftingToolDisplayer){
						String module = ((PC_ICraftingToolDisplayer)b).getCraftingToolModule();
						List<ItemStack> l = ((PC_ICraftingToolDisplayer)b).getItemStacks(new ArrayList<ItemStack>());
						List<PCco_SlotDirectCrafting> ls;
						if(moduleList.containsKey(module)){
							ls = moduleList.get(module);
						}else{
							moduleList.put(module, ls = new ArrayList<PCco_SlotDirectCrafting>());
						}
						for(ItemStack is:l){
							ls.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
						}
						continue;
					}
				}
				if(i.getHasSubtypes()){
					for(int j=0; j<16; j++){
						ItemStack is = new ItemStack(i, 1, j);
						if(PC_Utils.getRecipesForProduct(is).size()>0){
							allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
						}
					}
					continue;
				}
				
				ItemStack is = new ItemStack(i);
				if(PC_Utils.getRecipesForProduct(is).size()>0){
					allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
				}
			}
		}
		Collection<List<PCco_SlotDirectCrafting>> cls = moduleList.values();
		for(List<PCco_SlotDirectCrafting> ls: cls)
			slots.addAll(ls);
		slots.addAll(allMcSlots);
		return slots;
	}
	
	@Override
	public void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer entityPlayer) {
		if (PC_Utils.isCreative(entityPlayer) || PCco_SlotDirectCrafting.survivalCheating)
			return;
		super.retrySlotClick(par1, par2, par3, entityPlayer);
	}
	
}
