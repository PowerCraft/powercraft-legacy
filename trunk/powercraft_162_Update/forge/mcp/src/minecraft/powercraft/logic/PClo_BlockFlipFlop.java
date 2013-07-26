package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="FlipFlop", itemBlock=PClo_ItemBlockFlipFlop.class, tileEntity=PClo_TileEntityFlipFlop.class, canPlacedRotated=true)
public class PClo_BlockFlipFlop extends PC_Block
{
    private static Random rand = new Random();

    @ON
    public static PClo_BlockFlipFlop on;
    @OFF
    public static PClo_BlockFlipFlop off;

    public PClo_BlockFlipFlop(int id, boolean on)
    {
        super(id, Material.ground, PClo_FlipFlopType.getTextures());
        setHardness(0.35F);
        setStepSound(Block.soundWoodFootstep);
        disableStats();
        setResistance(30.0F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);

        if (on)
        {
            setCreativeTab(CreativeTabs.tabRedstone);
        }
    }

    @Override
	public void initConfig(PC_Property config) {
		super.initConfig(config);
		on.setLightValue(config.getInt("brightness", 7) * 0.0625F);
	}

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        PClo_TileEntityFlipFlop te = getTE(world, x, y, z);
        boolean state = isActive(world, x, y, z);
        boolean i1 = getRedstonePowerValueFromInput(world, x, y, z, PC_Direction.RIGHT)>0;
        boolean i2 = getRedstonePowerValueFromInput(world, x, y, z, PC_Direction.BACK)>0;
        boolean i3 = getRedstonePowerValueFromInput(world, x, y, z, PC_Direction.LEFT)>0;
        boolean shouldState = state;

        switch (te.getType())
        {
            case PClo_FlipFlopType.D:
            	
            	if (i3){
            		shouldState = false;
            	}
            	
                if (i1)
                {
                    shouldState = i2;
                }
                	

                break;

            case PClo_FlipFlopType.RS:

                if (i1)
                {
                    shouldState = false;
                }

                if (i3)
                {
                    shouldState = true;
                }

                break;

            case PClo_FlipFlopType.T:

                if (i2)
                {
                    if (!te.getClock())
                    {
                        te.setClock(true);
                        shouldState = !state;
                    }
                }
                else
                {
                    if (te.getClock())
                    {
                        te.setClock(false);
                    }
                }

                if (i1 || i3)
                {
                    shouldState = false;
                }

                break;

            case PClo_FlipFlopType.RANDOM:

                if (i2)
                {
                    if (!te.getClock())
                    {
                        te.setClock(true);
                        shouldState = rand.nextBoolean();
                    }
                }
                else
                {
                    if (te.getClock())
                    {
                        te.setClock(false);
                    }
                }
        }

        if (state != shouldState)
        {
            PC_Utils.setBlockState(world, x, y, z, shouldState);
        }
    }

    @Override
    public int tickRate(World world)
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int side)
    {
        world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
    }
    
    @Override
	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
	}
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itmeStack){
        world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
        PC_Utils.hugeUpdate(world, x, y, z);
    }
    
    @Override
	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
    	if (!isActive(world, x, y, z))
        {
            return 0;
        }

        if (PC_Direction.FRONT == dir)
        {
            return 15;
        }

        if (getType(world, x, y, z) == PClo_FlipFlopType.RS)
        {
            if (PC_Direction.BACK == dir)
            {
                return 15;
            }
        }

        return 0;
	}

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return PC_Renderer.getRendererID(true);
    }

    public static PClo_TileEntityFlipFlop getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);;

        if (te instanceof PClo_TileEntityFlipFlop)
        {
            return (PClo_TileEntityFlipFlop)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityFlipFlop te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getType();
        }

        return 0;
    }

    public static boolean isActive(IBlockAccess world, int x, int y, int z)
    {
        return PC_Utils.getBID(world, x, y, z) == on.blockID;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, PC_Direction side)
    {
        if (side == PC_Direction.TOP)
        {
            return sideIcons[getType(iblockaccess, x, y, z)+2+(isActive(iblockaccess, x, y, z) ? 0 : PClo_FlipFlopType.TOTAL_FLIPFLOP_COUNT)];
        }

        if (side == PC_Direction.BOTTOM)
        {
            return sideIcons[0];
        }

        return sideIcons[1];
    }

    @Override
    public Icon getIcon(PC_Direction side, int meta)
    {
        if (side == PC_Direction.BOTTOM)
        {
            return sideIcons[0];
        }

        if (side == PC_Direction.TOP)
        {
            return sideIcons[meta+2];
        }
        else
        {
            return sideIcons[1];
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (!isActive(world, x, y, z))
        {
            return;
        }

        if (random.nextInt(3) != 0)
        {
            return;
        }

        double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        double d1 = (y + 0.2F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
        world.spawnParticle("reddust", d, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return -1;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        int type = getType(world, x, y, z);
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !PC_Utils.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.flipFlop, 1, type));
        }

        return remove;
    }
   	
}
