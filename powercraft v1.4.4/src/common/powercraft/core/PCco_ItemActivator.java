package powercraft.core;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PCco_ItemActivator extends PC_Item
{
    public PCco_ItemActivator(int id)
    {
        super(id);
        setMaxDamage(100);
        setMaxStackSize(1);
        setIconIndex(2);
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int l, float par8, float par9, float par10)
    {
    	
    	int dir = ((PC_MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

//		if (PC_Utils.isPlacingReversed()) {
//			dir = PC_Utils.reverseSide(dir);
//		}

		for (int i = 0; i < 3; i++) {

			PC_CoordI pos = new PC_CoordI(x, y, z).offset(-Direction.offsetX[dir], i, -Direction.offsetZ[dir]);
			if (i == 2) {
				//try direct up.
				pos = new PC_CoordI(x, y+1, z);
			}

			if (pos.getId(world) == Block.chest.blockID && pos.offset(0, -1, 0).getId(world) == Block.blockSteel.blockID) {
				break;
			}

			ItemStack stackchest = PC_Utils.extractAndRemoveChest(world, pos);
			if (stackchest != null) {
				PC_Utils.dropItemStack(world, stackchest, pos);
				return true;
			}
		}
        
        List<PC_IActivatorListener>listeners = PC_ActivatorListener.getListeners();

        for (PC_IActivatorListener listener : listeners)
        {
            if (listener.onActivatorUsedOnBlock(itemstack, entityplayer, world, new PC_CoordI(x, y, z)))
            {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public String[] getDefaultNames()
    {
        return new String[] {getItemName(), "Activation Crystal"};
    }
}
