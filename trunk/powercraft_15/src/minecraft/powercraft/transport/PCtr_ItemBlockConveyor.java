package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import powercraft.api.PC_MathHelper;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.block.PC_Block;
import powercraft.api.block.PC_ItemBlock;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.registry.PC_MSGRegistry;

public class PCtr_ItemBlockConveyor extends PC_ItemBlock
{
    public PCtr_ItemBlockConveyor(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(false);
    }

    @Override
    public int getBlockID()
    {
        return itemID;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack,
            EntityPlayer entityplayer, World world, int i, int j,
            int k, int l, float par8, float par9, float par10)
    {
        int id = world.getBlockId(i, j, k);

        if (id == Block.snow.blockID)
        {
            l = 1;
        }
        else if (id != Block.vine.blockID && id != Block.tallGrass.blockID && id != Block.deadBush.blockID)
        {
            if (l == 0)
            {
                j--;
            }

            if (l == 1)
            {
                j++;
            }

            if (l == 2)
            {
                k--;
            }

            if (l == 3)
            {
                k++;
            }

            if (l == 4)
            {
                i--;
            }

            if (l == 5)
            {
                i++;
            }
        }

        if (itemstack.stackSize == 0)
        {
            return false;
        }
        else if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }

        if (PCtr_BeltHelper.isConveyorAt(world, new PC_VecI(i, j - 1, k)))
        {
            int dir = ((PC_MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

            if (itemstack.getItemDamage() == 0 && PC_KeyRegistry.isPlacingReversed(entityplayer))
            {
                dir = ValueWriting.reverseSide(dir);
            }

            j--;
            int m = 0;

            while (PCtr_BeltHelper.isConveyorAt(world, new PC_VecI(i, j, k)) && m <= 128)
            {
                i -= Direction.offsetX[dir];
                k -= Direction.offsetZ[dir];
                m++;
            }
        }

        if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid())
        {
            return false;
        }

        if (world.canPlaceEntityOnSide(getBlockID(), i, j, k, false, l, entityplayer))
        {
            Block block = Block.blocksList[getBlockID()];

            if (world.setBlock(i, j, k, block.blockID))
            {
                block.onBlockPlacedBy(world, i, j, k, entityplayer);
                world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
                world.markBlockForUpdate(i, j, k);
                world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
                        block.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
            }
        }

        return true;
    }

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_MSGRegistry.MSG_DEFAULT_NAME:
			PC_Block b = (PC_Block)Block.blocksList[getBlockID()];
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(b.getBlockName(), (String)b.msg(PC_MSGRegistry.MSG_DEFAULT_NAME)));
			return names;
		}
		return null;
	}
}
