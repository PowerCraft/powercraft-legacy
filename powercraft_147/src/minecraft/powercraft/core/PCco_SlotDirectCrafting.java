package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_ISlotWithBackground;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Utils.GameInfo;


public class PCco_SlotDirectCrafting extends Slot implements PC_ISlotWithBackground {

	private static int MAX_RECURSION = 50; 
	private EntityPlayer thePlayer;
	private ItemStack product;
	private boolean available = false;
	
	public PCco_SlotDirectCrafting(EntityPlayer entityplayer, ItemStack product, int index, int x, int y) {
		 super(null, index, x, y);
		 thePlayer = entityplayer;
	     this.product = product;
	     updateAvailable();
	}

	@Override
	public ItemStack getBackgroundStack() {
		return product;
	}

	@Override
	public Slot setBackgroundStack(ItemStack stack) {
		product = stack.copy();
        return this;
	}

	@Override
	public boolean renderTooltipWhenEmpty() {
		 return true;
	}

	@Override
	public boolean renderGrayWhenEmpty() {
		return true;
	}

	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int i){

        if (product != null){
            ItemStack output = product.copy();

            if(GameInfo.isCreative(thePlayer) || PC_GlobalVariables.config.getBoolean("cheats.survivalCheating")){
            	if(GameInfo.isPlacingReversed(thePlayer)){
            		 output.stackSize = output.getMaxStackSize();
            	}
            	available = true;
            }else{
            	ItemStack[] is = getPlayerInventory();
            	int stackSize = craft(output, is, new ArrayList<PC_ItemStack>(), 0);
            	if(stackSize>0){
            		output.stackSize = stackSize;
            		setPlayerInventory(is);
            		updateAvailable();
            	}else{
            		available = false;
            		return null;
            	}
            }

            return output;
        }

        return null;
    }

	@Override
    public ItemStack getStack(){
        if (product != null && available){
            return product.copy();
        }

        return null;
    }

	@Override
    public void putStack(ItemStack itemstack) {}

    public void setProduct(ItemStack itemstack)
    {
        product = itemstack;
    }

    @Override
    public void onSlotChanged() {}

    @Override
    public int getSlotStackLimit()
    {
        if (product == null)
        {
            return 64;
        }

        return product.getMaxStackSize();
    }
	
    private ItemStack[] getPlayerInventory(){
    	ItemStack[] inv = new ItemStack[thePlayer.inventory.getSizeInventory()];
    	for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++){
    		inv[i] = thePlayer.inventory.getStackInSlot(i);
    		if(inv[i]!=null)
    			inv[i] = inv[i].copy();
        }
    	return inv;
    }
    
    private void setPlayerInventory(ItemStack[] inv){
    	for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++){
    		thePlayer.inventory.setInventorySlotContents(i, inv[i]);
        }
    }
    
    private ItemStack[] setTo(ItemStack[] inv, ItemStack[] inv1){
    	if(inv==null)
    		inv = new ItemStack[inv1.length];
    	for (int i = 0; i < inv.length; i++){
    		inv[i] = inv1[i];
        }
    	return inv;
    }
    
    private int testItem(PC_ItemStack get, ItemStack[] is){
    	get = get.copy();
    	for(int i=0; i<is.length; i++){
    		if(get.equals(is[i])){
    			if(get.getCount()>is[i].stackSize){
    				get.setCount(get.getCount()-is[i].stackSize);
    			}else{
    				return 0;
    			}
    		}
    	}
    	return get.getCount();
    }
    
    private int testItem(List<PC_ItemStack> get, ItemStack[] is, List<PC_ItemStack> not, int rec){
    	int i=0;
    	for(PC_ItemStack stack:get){
    		if(testItem(stack, is)==0)
    			return i;
    		i++;
    	}
    	for(PC_ItemStack stack:get){
    		int need = testItem(stack, is);
    		ItemStack[] isc = setTo(null, is);
    		int size = 0;
    		List<PC_ItemStack> notc = new ArrayList<PC_ItemStack>(not);
    		while(size<need){
				int nSize = craft(stack.toItemStack(), isc, notc, rec);
				if(nSize==0){
					size = 0;
					break;
				}
    			size += craft(stack.toItemStack(), isc, notc, rec);
    		}
    		if(size>0){
    			stack = stack.copy();
    			stack.setCount(size);
    			if(storeTo(stack, isc)){
    				setTo(is, isc);
    				not.clear();
    				not.addAll(notc);
    				return i;
    			}
    		}
    		i++;
    	}
    	return -1;
    }
    
    private boolean storeTo(PC_ItemStack get, ItemStack[] is){
    	for(int i=0; i<is.length; i++){
    		if(get.equals(is[i])){
    			int canPut = Math.min(thePlayer.inventory.getInventoryStackLimit(), is[i].getMaxStackSize()) - is[i].stackSize;
    			if(get.getCount()>canPut){
    				get.setCount(get.getCount()-canPut);
    				is[i].stackSize += canPut;
    			}else{
    				is[i].stackSize += get.getCount();
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private void takeOut(PC_ItemStack get, ItemStack[] is){
    	for(int i=0; i<is.length; i++){
    		if(get.equals(is[i])){
    			if(get.getCount()>is[i].stackSize){
    				get.setCount(get.getCount()-is[i].stackSize);
    				is[i] = null;
    			}else{
    				is[i].stackSize -= get.getCount();
    				if(is[i].stackSize==0)
    					is[i] = null;
    				return;
    			}
    		}
    	}
    }
    
    private int craft(ItemStack craft, ItemStack[] is, List<PC_ItemStack> not, int rec) {
		List<IRecipe> recipes = GameInfo.getRecipesForProduct(craft);
		if(rec>MAX_RECURSION)
			return 0;
		if(not.contains(new PC_ItemStack(craft)))
			return 0;
		not.add(new PC_ItemStack(craft));
		rec++;
		for(IRecipe recipe:recipes){
			ItemStack[] isc = setTo(null, is);
			List<PC_ItemStack>[][] inp = GameInfo.getExpectedInput(recipe, -1, -1);
			List<List<PC_ItemStack>> input = new ArrayList<List<PC_ItemStack>>();
			if(inp==null)
				continue;
			for(int x=0; x<inp.length; x++){
				for(int y=0; y<inp[x].length; y++){
					if(inp[x][y]!=null){
						input.add(inp[x][y]);
					}
				}
			}
			
			int ret;
			boolean con=false;
			for(List<PC_ItemStack> l:input){
				ret = testItem(l, isc, not, rec);
				if(ret<0){
					con = true;
					break;
				}
				takeOut(l.get(ret), isc);
			}
			if(con)
				continue;
			
			setTo(is, isc);
			return recipe.getRecipeOutput().stackSize;
		}
		return 0;
	}
    
    private boolean isAvailable() {
    	if(GameInfo.isCreative(thePlayer) || PC_GlobalVariables.config.getBoolean("cheats.survivalCheating"))
    		return true;
		return craft(product, getPlayerInventory(), new ArrayList<PC_ItemStack>(), 0)>0;
	}
    
    public void updateAvailable(){
    	available = isAvailable();
    }
    
}
