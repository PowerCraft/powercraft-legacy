package powercraft.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import powercraft.management.PC_Item;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_MSGRegistry;
import powercraft.management.registry.PC_MSGRegistry.MSGIterator;
import powercraft.management.registry.PC_RecipeRegistry;

public class PCco_ItemActivator extends PC_Item{
    
	public PCco_ItemActivator(int id){
		super(id);
        setMaxDamage(100);
        setMaxStackSize(1);
        setIconIndex(2);
        setCreativeTab(CreativeTabs.tabTools);
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

//		if (PC_Utils.isPlacingReversed()) {
//			dir = PC_Utils.reverseSide(dir);
//		}

		for (int i = 0; i < 3; i++) {

			PC_VecI pos = new PC_VecI(x-Direction.offsetX[dir], y+i, z-Direction.offsetZ[dir]);
			if (i == 2) {
				//try direct up.
				pos = new PC_VecI(x, y+1, z);
			}

			if (GameInfo.getBID(world, pos) == Block.chest.blockID && GameInfo.getBID(world, pos.copy().add(0, -1, 0)) == Block.blockSteel.blockID) {
				break;
			}

			ItemStack stackchest = ValueWriting.extractAndRemoveChest(world, pos);
			if (stackchest != null) {
				ValueWriting.dropItemStack(world, stackchest, pos);
				return true;
			}
		}

        return false;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Activation Crystal"));
            return names;
		}
		return null;
	}
}