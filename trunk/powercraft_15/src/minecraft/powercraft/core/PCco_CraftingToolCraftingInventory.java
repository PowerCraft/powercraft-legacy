package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import powercraft.api.inventory.PC_IInventory;
import powercraft.api.item.PC_ItemStack;
import powercraft.api.registry.PC_RecipeRegistry;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Struct3;

public class PCco_CraftingToolCraftingInventory implements PC_IInventory {

	private ItemStack product;
	private List<PC_Struct3<Integer, IRecipe, List<ItemStack>[]>> craftings = new ArrayList<PC_Struct3<Integer, IRecipe, List<ItemStack>[]>>();
	private int scroll;
	private int tick = 0;
	private int tick2 = 0;
	private RecipeSearchThread recipeSearch;
	private PCco_GuiCraftingTool gui;
	
	public PCco_CraftingToolCraftingInventory(PCco_GuiCraftingTool gui) {
		this.gui = gui;
	}

	public void setProduct(ItemStack product){
		this.product = product;
		craftings.clear();
		if(recipeSearch!=null){
			recipeSearch.stopSearch();
			recipeSearch=null;
		}
		recipeSearch = new RecipeSearchThread();
	}
	
	public void setScroll(int scroll){
		this.scroll = scroll;
		if(craftings.size()>0){
			if(tick>=craftings.get(scroll).a){
				tick=0;
			}
		}
	}
	
	public int getNumRecipes(){
		return craftings.size();
	}
	
	public void nextTick(){
		tick2++;
		if(tick2>20){
			tick2=0;
			if(craftings.size()>0){
				tick++;
				if(tick>=craftings.get(scroll).a){
					tick=0;
				}
			}
		}
	}
	
	private ItemStack getItemStackInSlot(int i){
		int page = i/10;
		i = i%10;
		if(i==0){
			if(craftings.size()>0){
				InventoryCrafting inventorycrafting = new InventoryCrafting(gui, 3, 3);
				for(int j=0; j<9; j++){
					List<ItemStack> list = craftings.get(scroll+page).c[j];
					if(list!=null)
						inventorycrafting.setInventorySlotContents(j, list.get(tick));
				}
				return craftings.get(scroll+page).b.getCraftingResult(inventorycrafting);
			}
			return product;
		}
		if(craftings.size()>0){
			List<ItemStack> list = craftings.get(scroll+page).c[i-1];
			if(list!=null){
				ItemStack is = list.get(tick);
				is.stackSize=1;
				return is;
			}
		}
		return null;
	}
	
	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getItemStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {}

	@Override
	public String getInvName() {
		return "CraftingToolCraftingInventory";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int[] getSizeInventorySide(int var1) {
		return null;
	}

	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canPlayerTakeStack(int i, EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int i) {
		return false;
	}

	@Override
	public boolean canDropStackFrom(int i) {
		return false;
	}

	@Override
	public int getSlotStackLimit(int i) {
		ItemStack is = getItemStackInSlot(i);
		if(is!=null)
			return is.getMaxStackSize();
		return 0;
	}

	private class RecipeSearchThread extends Thread{
		
		private boolean stop;
		
		public RecipeSearchThread(){
			setDaemon(true);
			start();
		}
		
		public void stopSearch(){
			stop=true;
		}
		
		@Override
		public void run(){
			List<IRecipe> recipes = PC_RecipeRegistry.getRecipesForProduct(product);
			for(IRecipe recipe : recipes){
				
				List<PC_ItemStack>[][] expectedInputs2d = PC_RecipeRegistry.getExpectedInput(recipe, 3, 3);
				if(stop)
					return;
				List<ItemStack>[] expectedInputs = new List[9];
				int nums=1;
				for(int y=0; y<3; y++){
					for(int x=0; x<3; x++){
						List<PC_ItemStack> list2 = expectedInputs2d[x][y];
						if(list2!=null && list2.size()>0){
							List<ItemStack> list = expectedInputs[y*3+x] = new ArrayList<ItemStack>();
							for(PC_ItemStack is:list2){
								ItemStack isd = is.toItemStack();
								if(is.getMeta()==-1){
									Item i = isd.getItem();
									i.getSubItems(i.itemID, i.getCreativeTab(), list);
								}else{
									list.add(isd);
								}
							}
							nums *= list.size();
						}
					}
				}
				if(stop)
					return;
				List<ItemStack>[] crafting = new List[9];
				for(int i=0; i<crafting.length; i++){
					if(expectedInputs[i]!=null){
						crafting[i] = new ArrayList<ItemStack>();
					}
				}
				if(stop)
					return;
				for(int n=0; n<nums; n++){
					int n1 = n;
					for(int i=0; i<crafting.length; i++){
						List<ItemStack> list = expectedInputs[i];
						if(list!=null){
							int n2 = n1 % list.size();
							n1 = n1 / list.size();
							ItemStack is = list.get(n2);
							crafting[i].add(is);
						}
					}
				}
				if(stop)
					return;
				craftings.add(new PC_Struct3<Integer, IRecipe, List<ItemStack>[]>(nums, recipe, crafting));
				gui.updateCraftings();
			}
		}
		
	}
	
}
