package powercraft.logic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="Repeater", itemBlock=PClo_ItemBlockRepeater.class, tileEntity=PClo_TileEntityRepeater.class, canPlacedRotated=true)
public class PClo_BlockRepeater extends PC_Block
{
    @ON
    public static PClo_BlockRepeater on;
    @OFF
    public static PClo_BlockRepeater off;

    public PClo_BlockRepeater(int id, boolean on)
    {
    	super(id, Material.ground, PClo_RepeaterType.getTextures());
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving, ItemStack itmeStack){
        world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
        PC_Utils.hugeUpdate(world, x, y, z);
    }

    @Override
	public void initConfig(PC_Property config) {
		super.initConfig(config);
		on.setLightValue(config.getInt("brightness", 7) * 0.0625F);
	}
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te.getType() == PClo_RepeaterType.CROSSING)
        {
            int[] inp = {getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.LEFT)>0 ? 1 : 0, 
            			getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.BACK)>0 ? 1 : 0,
            			getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.RIGHT)>0 ? 1 : 0, 
            			getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.FRONT)>0 ? 1 : 0};
            int variant = te.getInp();
            int shouldState = 0;

            switch (variant)
            {
                case 0:
                    shouldState = inp[0] | inp[1] << 1;
                    break;

                case 1:
                    shouldState = inp[1] | inp[2] << 1;
                    break;

                case 2:
                    shouldState = inp[0] | inp[3] << 1;
                    break;

                case 3:
                    shouldState = inp[2] | inp[3] << 1;
                    break;
            }

            if (te.getState() != shouldState)
            {
                te.setState(shouldState);
                PC_Utils.setBlockState(world, x, y, z, shouldState != 0);
            }
        }
        else
        {
            boolean shouldState = getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.BACK)>0;

            if (isActive(world, x, y, z) != shouldState)
            {
                PC_Utils.setBlockState(world, x, y, z, shouldState);
            }
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
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te.getType() == PClo_RepeaterType.REPEATER_STRAIGHT_I || te.getType() == PClo_RepeaterType.REPEATER_CORNER_I)
        {
            updateTick(world, x, y, z, new Random());
        }else{
        	boolean shouldState = getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.BACK)>0;
        	if (isActive(world, x, y, z) != shouldState || te.getType() == PClo_RepeaterType.CROSSING){
        		world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
        	}
        }
    }

    @Override
   	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
   		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
   	}
    
    @Override
   	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);
        int type = te.getType();
        boolean L = false, R = false, F = false, B = false;
        int variant = te.getInp();
        
        if (type == PClo_RepeaterType.CROSSING)
        {
            int state = te.getState();
            boolean power1 = (state & 1) != 0 ;
            boolean power2 = (state & 2) != 0 ;

            switch (variant)
            {
                case 0:
                    if (dir == PC_Direction.RIGHT)
                    {
                        return power1?15:0;
                    }

                    if (dir == PC_Direction.FRONT)
                    {
                        return power2?15:0;
                    }

                    break;

                case 1:
                    if (dir == PC_Direction.FRONT)
                    {
                        return power1?15:0;
                    }

                    if (dir == PC_Direction.LEFT)
                    {
                        return power2?15:0;
                    }

                    break;

                case 2:
                    if (dir == PC_Direction.RIGHT)
                    {
                        return power1?15:0;
                    }

                    if (dir == PC_Direction.BACK)
                    {
                        return power2?15:0;
                    }

                    break;

                case 3:
                    if (dir == PC_Direction.LEFT)
                    {
                        return power1?15:0;
                    }

                    if (dir == PC_Direction.BACK)
                    {
                        return power2?15:0;
                    }

                    break;
            }

            return 0;
        }

        boolean power = isActive(world, x, y, z);

        if (!power)
        {
            return 0;
        }

        if (type == PClo_RepeaterType.SPLITTER_I)
        {
            L = variant != 3;
            R = variant != 1;
            F = variant != 2;
            B = false;
        }
        else if (type == PClo_RepeaterType.REPEATER_STRAIGHT_I || type == PClo_RepeaterType.REPEATER_STRAIGHT)
        {
            F = true;
        }
        else if (type == PClo_RepeaterType.REPEATER_CORNER_I || type == PClo_RepeaterType.REPEATER_CORNER)
        {
            L = variant == 0;
            R = variant == 1;
        }

        if(dir==PC_Direction.LEFT)
        	return L?15:0;
        if(dir==PC_Direction.RIGHT)
        	return R?15:0;
        if(dir==PC_Direction.FRONT)
        	return F?15:0;
        if(dir==PC_Direction.BOTTOM)
        	return B?15:0;

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

    public static PClo_TileEntityRepeater getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);

        if (te instanceof PClo_TileEntityRepeater)
        {
            return (PClo_TileEntityRepeater)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getType();
        }

        return 0;
    }

    public static int getInp(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntityRepeater te = getTE(world, x, y, z);

        if (te != null)
        {
            return te.getInp();
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
            return sideIcons[PClo_RepeaterType.getTextureIndex(getType(iblockaccess, x, y, z), isActive(iblockaccess, x, y, z))+getInp(iblockaccess, x, y, z)];
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
            return sideIcons[PClo_RepeaterType.getTextureIndex(meta, true)];
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack ihold = player.getCurrentEquippedItem();

        if (ihold != null)
        {
            if (ihold.getItem().itemID == Item.stick.itemID)
            {
                if (!world.isRemote)
                {
                    getTE(world, x, y, z).change();
                }

                return true;
            }
        }

        return false;
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
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.repeater, 1, type));
        }

        return remove;
    }

}
