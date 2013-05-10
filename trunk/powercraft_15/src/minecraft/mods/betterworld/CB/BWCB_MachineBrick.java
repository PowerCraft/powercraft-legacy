package mods.betterworld.CB;

import java.util.Random;

import com.google.common.base.CaseFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BWCB_MachineBrick extends Block implements ITileEntityProvider{

	private Icon[] iconBuffer = new Icon[3];
	private  boolean isActive = false;
	private boolean structureComplete = false;

	
	public BWCB_MachineBrick(int par1, Material material) {
		super(par1, material);
		this.isBlockContainer = true;
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);

	}

	public TileEntity createNewTileEntity(World world) {
		return new BWCB_TileEntityBlockMachineBrick();
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{

		if (side == 2 && isActive == false) return iconBuffer[0];
		if (side == 2 && isActive == true ) return iconBuffer[1];
		else{
		return Block.brick.getBlockTextureFromSide(side);
		}
	}
	
	@Override
	public void registerIcons(IconRegister iconReg)
	{
		iconBuffer[0]= iconReg.registerIcon("furnace_front");
		iconBuffer[1]=iconReg.registerIcon("furnace_front_lit");
		iconBuffer[2]=iconReg.registerIcon("brick");
	}
	/**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDefaultDirection(par1World, par2, par3, par4);
       
    }

    /**
     * set a blocks direction
     */
    private void setDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockId(par2, par3, par4 - 1);
            int i1 = par1World.getBlockId(par2, par3, par4 + 1);
            int j1 = par1World.getBlockId(par2 - 1, par3, par4);
            int k1 = par1World.getBlockId(par2 + 1, par3, par4);
            byte b0 = 3;

            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
            {
                b0 = 3;
            }

            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
            {
                b0 = 2;
            }

            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
            {
                b0 = 5;
            }

            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
            {
                b0 = 4;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
        }
    }
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2); //South
        }

        if (l == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2); //West
        }

        if (l == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2); //North
        }

        if (l == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2); //East
        }

    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                    EntityPlayer player, int idk, float what, float these, float are) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (tileEntity == null || player.isSneaking()) {
                    return false;
            }
    //code to open gui explained later
            if(!world.isRemote){
            		System.out.println("OpenGUi");
            		player.openGui(BWCB.instance, 150, world, x, y, z);
            }
            return true;
    }

	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4, int par5) {
		if (par5 == 1) {
			return this.iconBuffer[2];
		} else if (par5 == 0) {
			return this.iconBuffer[2];  // Furnace Side?
		}

		else {
			int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
			return par5 != var6 ? this.iconBuffer[2]
					: (this.isActive ? this.iconBuffer[1]
							: this.iconBuffer[0]);
		}

	}

	@Override
	public void onNeighborBlockChange(World world, int par2, int par3,
			int par4, int par5) {
		super.onNeighborBlockChange(world, par2, par3 +1, par4, par5);

		System.out.println("block change: is Active: " +isActive);
	}

	/**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
	@Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
        par1World.removeBlockTileEntity(par2, par3, par4);
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
     */
	@Override
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
    }
}
	
	

