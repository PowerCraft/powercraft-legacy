package powercraft.logic;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.annotation.PC_Shining.OFF;
import powercraft.api.annotation.PC_Shining.ON;
import powercraft.api.block.PC_Block;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.PC_Property;

@PC_Shining
@PC_BlockInfo(name="Special", itemBlock=PClo_ItemBlockSpecial.class, tileEntity=PClo_TileEntitySpecial.class, canPlacedRotated=true)
public class PClo_BlockSpecial extends PC_Block
{
    @ON
    public static PClo_BlockSpecial on;
    @OFF
    public static PClo_BlockSpecial off;

    public PClo_BlockSpecial(int id, boolean on)
    {
    	super(id, Material.ground, PClo_SpecialType.getTextures());
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
        PClo_TileEntitySpecial te = getTE(world, x, y, z);
        boolean shouldState = false;
        boolean state = isActive(world, x, y, z);
        PC_Direction rot = getRotation(PC_Utils.getMD(world, x, y, z));
        int xAdd = rot.getOffset().x, zAdd = rot.getOffset().y;

        switch (te.getType())
        {
            case PClo_SpecialType.DAY:
                shouldState = world.isDaytime();
                break;

            case PClo_SpecialType.NIGHT:
                shouldState = !world.isDaytime();
                break;

            case PClo_SpecialType.RAIN:
                shouldState = world.isRaining();
                break;

            case PClo_SpecialType.CHEST_EMPTY:{
            	IInventory inv = PC_InventoryUtils.getInventoryAt(world, x + xAdd, y, z + zAdd);
            	if(inv!=null){
            		shouldState = PC_InventoryUtils.getInventoryCountOf(inv, te.getStackInSlot(0))==0;
            	}
            	break;

            }case PClo_SpecialType.CHEST_FULL:{
            	IInventory inv = PC_InventoryUtils.getInventoryAt(world, x + xAdd, y, z + zAdd);
            	if(inv!=null){
            		shouldState = PC_InventoryUtils.getInventorySpaceFor(inv, te.getStackInSlot(0))==0;
            	}
            	break;

            }case PClo_SpecialType.SPECIAL:
                shouldState = getRedstonePowereValueFromInput(world, x, y, z, PC_Direction.BACK)>0;
                TileEntity tes = PC_Utils.getTE(world, x - xAdd, y, z - zAdd);

                if (tes instanceof PClo_TileEntityPulsar)
                {
                    ((PClo_TileEntityPulsar) tes).setPaused(shouldState);
                }

                if (shouldState == true && shouldState != state)
                {
                    spawnMobsFromSpawners(world, x, y, z);
                }

                break;
        }

        if (state != shouldState)
        {
            PC_Utils.setBlockState(world, x, y, z, shouldState);
        }
    }

    private void spawnMobsFromSpawners(World world, int x, int y, int z)
    {
        spawnMobFromSpawner(world, x + 1, y, z);
        spawnMobFromSpawner(world, x - 1, y, z);
        spawnMobFromSpawner(world, x, y + 1, z);
        spawnMobFromSpawner(world, x, y, z + 1);
        spawnMobFromSpawner(world, x, y, z - 1);
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

    private boolean isOutputActive(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
	}
    
	@Override
   	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
    	if (!isActive(world, x, y, z))
        {
            return 0;
        }

        switch (getType(world, x, y, z))
        {
            case PClo_SpecialType.DAY:
            case PClo_SpecialType.NIGHT:
            case PClo_SpecialType.RAIN:
                return 15;

            case PClo_SpecialType.CHEST_EMPTY:
            case PClo_SpecialType.CHEST_FULL:
                if (dir == PC_Direction.FRONT)
                {
                    return 15;
                }

                break;
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

    public static PClo_TileEntitySpecial getTE(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = PC_Utils.getTE(world, x, y, z);

        if (te instanceof PClo_TileEntitySpecial)
        {
            return (PClo_TileEntitySpecial)te;
        }

        return null;
    }

    public static int getType(IBlockAccess world, int x, int y, int z)
    {
        PClo_TileEntitySpecial te = getTE(world, x, y, z);

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
            return sideIcons[getType(iblockaccess, x, y, z)+2+(isActive(iblockaccess, x, y, z) ? 0 : PClo_SpecialType.TOTAL_SPECIAL_COUNT)];
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
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PClo_App.special, 1, type));
        }

        return remove;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
    {
        int type = getType(world, i, j, k);

        if (type != PClo_SpecialType.CHEST_EMPTY && type != PClo_SpecialType.CHEST_FULL)
        {
            return false;
        }

        PC_GresRegistry.openGres("Special", entityplayer, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
        return true;
    }

    public static void spawnMobFromSpawner(World world, int x, int y, int z) {
    	TileEntity te = PC_Utils.getTE(world, x, y, z);
    	if(te instanceof TileEntityMobSpawner){
	        TileEntityMobSpawner tems = (TileEntityMobSpawner)te;
	
	        if (te != null) {
	        	PC_Utils.spawnMobs(world, x, y, z, tems.func_98049_a().getEntityNameToSpawn());
	        }
    	}
    }

    public static void preventSpawnerSpawning(World world, int x, int y, int z) {
    	TileEntity te = PC_Utils.getTE(world, x, y, z);
    	if(te instanceof TileEntityMobSpawner){
	        TileEntityMobSpawner tems = (TileEntityMobSpawner)te;
	        tems.func_98049_a().spawnDelay = 20;
        }
}

	
}
