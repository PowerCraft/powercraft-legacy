package powercraft.transport;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_MathHelper;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_MSGRegistry;

@PC_BlockInfo(itemBlock=PCtr_ItemBlockElevator.class)
public class PCtr_BlockElevator extends PC_Block
{
    private static final double BORDERS = 0.25D;
    private static final double BORDER_BOOST = 0.062D;

    public PCtr_BlockElevator(int id)
    {
        super(id, PCtr_MaterialElevator.getMaterial(), "elevator_up", "elevator_down");
        setHardness(0.5F);
        setResistance(8.0F);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public int damageDropped(int i)
    {
        return i;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return true;
    }
    
    @Override
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta) {
		return meta==0?icons[0]:icons[1];
	}

	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        PC_VecI pos = new PC_VecI(i, j, k);

        if (PCtr_BeltHelper.isEntityIgnored(entity))
        {
            return;
        }

        if (entity instanceof EntityItem)
        {
            PCtr_BeltHelper.packItems(world, pos);

            if (PCtr_BeltHelper.storeItemIntoMinecart(world, pos, (EntityItem) entity) || PCtr_BeltHelper.storeAllSides(world, pos, (EntityItem) entity))
            {
                entity.setDead();
                return;
            }
        }

        boolean down = (GameInfo.getMD(world, pos) == 1);
        PCtr_BeltHelper.entityPreventDespawning(world, pos, true, entity);
        boolean halted = world.isBlockIndirectlyGettingPowered(i, j, k);
        double BBOOST = (entity instanceof EntityPlayer) ? BORDER_BOOST / 4.0D : BORDER_BOOST;
        int id = world.getBlockId(i, j + (down ? -1 : 1), k);
        
        if (Math.abs(entity.motionY) > 0.4D)
        {
            entity.motionY *= 0.3D;
        }

        entity.fallDistance = 0;

        if (id != blockID || halted)
        {
            if (entity instanceof EntityLiving)
            {
                int side = -1;

                if ((PCtr_BeltHelper.isConveyorAt(world, pos.offset(1, 0, 0)) && world.isAirBlock(i + 1, j + 1, k)))
                {
                    side = 1;
                }
                else if ((PCtr_BeltHelper.isConveyorAt(world, pos.offset(-1, 0, 0)) && world.isAirBlock(i - 1, j + 1, k)))
                {
                    side = 3;
                }
                else if ((PCtr_BeltHelper.isConveyorAt(world, pos.offset(0, 0, 1)) && world.isAirBlock(i, j + 1, k + 1)))
                {
                    side = 2;
                }
                else if ((PCtr_BeltHelper.isConveyorAt(world, pos.offset(0, 0, -1)) && world.isAirBlock(i, j + 1, k - 1)))
                {
                    side = 0;
                }
                else if ((world.isAirBlock(i + 1, j, k) && !world.isAirBlock(i + 1, j - 1, k)))
                {
                    side = 1;
                }
                else if ((world.isAirBlock(i - 1, j, k) && !world.isAirBlock(i - 1, j - 1, k)))
                {
                    side = 3;
                }
                else if ((world.isAirBlock(i, j, k + 1) && !world.isAirBlock(i, j - 1, k + 1)))
                {
                    side = 2;
                }
                else if ((world.isAirBlock(i, j, k - 1) && !world.isAirBlock(i, j - 1, k - 1)))
                {
                    side = 0;
                }

                if (side != -1)
                {
                    PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, true, side, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                            PCtr_BeltHelper.HORIZONTAL_BOOST);
                }
            }
            else
            {
                if ((down && entity.posY < j + 0.6D) || (!down && entity.posY > j + 0.1D))
                {
                    if (PCtr_BeltHelper.isConveyorAt(world, pos.offset(1, 0, 0)))
                    {
                        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, true, 1, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                                PCtr_BeltHelper.HORIZONTAL_BOOST * (down ? 1.2D : 1));
                    }
                    else if (PCtr_BeltHelper.isConveyorAt(world, pos.offset(-1, 0, 0)))
                    {
                        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, true, 3, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                                PCtr_BeltHelper.HORIZONTAL_BOOST * (down ? 1.2D : 1));
                    }
                    else if (PCtr_BeltHelper.isConveyorAt(world, pos.offset(0, 0, 1)))
                    {
                        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, true, 2, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                                PCtr_BeltHelper.HORIZONTAL_BOOST * (down ? 1.2D : 1));
                    }
                    else if (PCtr_BeltHelper.isConveyorAt(world, pos.offset(0, 0, -1)))
                    {
                        PCtr_BeltHelper.moveEntityOnBelt(world, pos, entity, true, true, 0, PCtr_BeltHelper.MAX_HORIZONTAL_SPEED,
                                PCtr_BeltHelper.HORIZONTAL_BOOST * (down ? 1.2D : 1));
                    }
                }
            }
        }
        else
        {
            if (!down)
            {
                if (entity.motionY < ((id != blockID || halted) ? 0.2D : 0.3D))
                {
                    entity.motionY = ((id != blockID || halted) ? 0.2D : 0.3D);

                    if (entity.onGround)
                    {
                        entity.moveEntity(0, 0.01D, 0);
                    }
                }
            }

            if (entity.posX > pos.x + (1D - BORDERS))
            {
                entity.motionX -= BBOOST;
            }

            if (entity.posX < pos.x + BORDERS)
            {
                entity.motionX += BBOOST;
            }

            if (entity.posZ > pos.z + BORDERS)
            {
                entity.motionZ -= BBOOST;
            }

            if (entity.posZ < pos.z + (1D - BORDERS))
            {
                entity.motionZ += BBOOST;
            }

            if (!(id != blockID || halted))
            {
                entity.motionZ = PC_MathHelper.clamp_float((float) entity.motionZ, (float) - (BORDER_BOOST * 1.5D), (float)(BORDER_BOOST * 1.5D));
                entity.motionX = PC_MathHelper.clamp_float((float) entity.motionX, (float) - (BORDER_BOOST * 1.5D), (float)(BORDER_BOOST * 1.5D));
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        boolean down = world.getBlockMetadata(i, j, k) == 1;
        boolean bottom = world.getBlockId(i, j - 1, k) != blockID;

        if (down && bottom)
        {
            return PCtr_App.conveyorBelt.getCollisionBoundingBoxFromPool(world, i, j, k);
        }

        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int i, int j, int k)
    {
        return getRenderColor(world.getBlockMetadata(i, j, k));
    }

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_MSGRegistry.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_MSGRegistry.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}
		}
		return null;
	}
    
}
