package powercraft.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_MSGRegistry.MSGIterator;
import powercraft.api.registry.PC_RecipeRegistry;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PCco_ItemActivator extends PC_Item{
    
	public PCco_ItemActivator(int id){
		super(id, "activator");
        setMaxDamage(100);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabTools);
    }

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		names.add(new LangEntry(getUnlocalizedName(), "Activation Crystal"));
		return names;
	}
	
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int l, float par8, float par9, float par10)
    {
    	if(world.isRemote)
    		return false;
    	
    	Boolean ok = (Boolean)PC_MSGRegistry.callAllMSG(new MSGIterator() {
			@Override
			public Object onRet(Object o) {
				if(o instanceof Boolean && (Boolean)o){
	        		return true;
	        	}
				return null;
			}
		}, PC_MSGRegistry.MSG_ON_ACTIVATOR_USED_ON_BLOCK, itemstack, entityplayer, world, new PC_VecI(x, y, z));
    	
    	if(ok!=null && ok){
    		return true;
    	}
    	
        if(PC_RecipeRegistry.searchRecipe3DAndDo(entityplayer, world, new PC_VecI(x, y, z)))
        	return true;
        
    	int dir = ((PC_MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

		for (int i = 0; i < 3; i++) {

			PC_VecI pos = new PC_VecI(x-Direction.offsetX[dir], y+i, z-Direction.offsetZ[dir]);
			if (i == 2) {
				//try direct up.
				pos = new PC_VecI(x, y+1, z);
			}

			if (PC_Utils.getBID(world, pos) == Block.chest.blockID && PC_Utils.getBID(world, pos.copy().add(0, -1, 0)) == Block.blockSteel.blockID) {
				break;
			}

			/*ItemStack stackchest = PC_InventoryUtils.extractAndRemoveChest(world, pos);
			if (stackchest != null) {
				PC_Utils.dropItemStack(world, pos, stackchest);
				return true;
			}*/
		}

        return false;
    }
}