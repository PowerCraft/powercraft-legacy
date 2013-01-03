package powercraft.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Item;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

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
    	
    	List<PC_IMSG> objs = ModuleInfo.getMSGObjects();

        for (PC_IMSG obj : objs){
        	Object o = obj.msg(PC_Utils.MSG_ON_ACTIVATOR_USED_ON_BLOCK, itemstack, entityplayer, world, new PC_VecI(x, y, z));
        	if(o instanceof Boolean && (Boolean)o){
        		return true;
        	}
        }
    	
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
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName(), "Activation Crystal", null));
            return names;
		}
		return null;
	}
}