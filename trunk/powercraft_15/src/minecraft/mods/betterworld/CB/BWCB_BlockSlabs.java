package mods.betterworld.CB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.client.BWCB_Render;
import mods.betterworld.CB.core.BWCB_BlockList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStep;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BWCB_BlockSlabs extends BlockStep implements
		ITileEntityProvider {

	private int damVal;
	private Icon[] iconBuffer;
	public final int otherID;
	public final boolean isDoubleSlab;
	
	/** The block that is used as model for the stair. */
	

	// private final int modelBlockMetadata;

	public BWCB_BlockSlabs(int par1, boolean var2, int otherID) {
		super(par1, var2);
		this.otherID = otherID;
		this.isBlockContainer = true;
		isDoubleSlab = var2;
		this.setLightOpacity(255);
		setUnlocalizedName("BlockSlabs");
		setCreativeTab(BWCB.tab);
		setResistance(10.0F);
		setHardness(5.0F);
        if (var2)
        {
            opaqueCubeLookup[par1] = true;
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }

        this.setLightOpacity(255);
    }



	public TileEntity createNewTileEntity(World world) {
		return new BWCB_TileEntityBlockSlabs();
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if (block == null) {
			return 0;
		} else if (block != this) {
			return block.getLightValue(world, x, y, z);
		} else {
			if (world.getBlockTileEntity(x, y, z) == null) {
				return 0;
			}
			return ((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(x, y,
					z)).getLightValue();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4, int par5) {
		BWCB_TileEntityBlockSlabs tes = (BWCB_TileEntityBlockSlabs) par1iBlockAccess
				.getBlockTileEntity(par2, par3, par4);

		return iconBuffer[tes.getBlockDamageID()];
	}
/*
	@Override
	public Icon getIcon(int side, int metadata) {
		return this.iconBuffer[metadata];
	}
*/
	@Override
	public void registerIcons(IconRegister iconRegister) {
		iconBuffer = new Icon[BWCB_BlockList.blockStoneNormalTexture.size()];
		for (int i = 0; i < BWCB_BlockList.blockStoneNormalTexture.size(); i++) {
			iconBuffer[i] = iconRegister.registerIcon("betterworld/CB:"
					+ String.valueOf(BWCB_BlockList.blockStoneNormalTexture
							.get(i)) + "_" + BWCB.textureRes);
		}
	}
	
    public String getFullSlabName(int par1)
    {
        if (par1 < 0 || par1 >= BWCB_BlockList.blockGlassNormalName.size())
        {
            par1 = 0;
        }

        return super.getUnlocalizedName() + "." + BWCB_BlockList.blockGlassNormalName.get(par1);
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        int k = par2 & 7;

        if (this.isDoubleSlab && (par2 & 8) != 0)
        {
            par1 = 1;
        }

        return iconBuffer[par2];
    }
	@Override
	public int damageDropped(int metadata) {
		return 0;
	}

	public int getSubBlockCount() {
		return iconBuffer.length;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return 0;
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x,
			int y, int z) {
		String userName = ((BWCB_TileEntityBlockSlabs) world
				.getBlockTileEntity(x, y, z)).getUserName();
		boolean lock = ((BWCB_TileEntityBlockSlabs) world.getBlockTileEntity(
				x, y, z)).isBlockLocked();
		if ((userName == null || !userName.equals(player.username)) && lock) {
			return false;

		}
		if (!world.isRemote
				&& !((EntityPlayerMP) player).theItemInWorldManager
						.isCreative()) {
			int damage = ((BWCB_TileEntityBlockSlabs) world
					.getBlockTileEntity(x, y, z)).getBlockDamageID();
			ItemStack dropItem = new ItemStack(this, 1, damage);
			dropBlockAsItem_do(world, x, y, z, dropItem);
		}
		return super.removeBlockByPlayer(world, player, x, y, z);
	}

	public static boolean isPlayerOPOrOwner(EntityPlayer player) {
		if (MinecraftServer.getServer().getConfigurationManager().getOps()
				.contains(player.username.trim().toLowerCase()))
			return true;
		return MinecraftServer.getServer().getServerOwner() == player
				.getEntityName();
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an
	 * update, as appropriate
	 */
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeBlockTileEntity(par2, par3, par4);
	}

	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile entity at this location. Args: world,
	 * x, y, z, blockID, EventID, event parameter
	 */
	public boolean onBlockEventReceived(World par1World, int par2, int par3,
			int par4, int par5, int par6) {
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6)
				: false;
	}

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (par1 != BWCB.blockDoubleSlabs.blockID)
        {
            for (int j = 0; j <= getSubBlockCount(); ++j)
            {
                if (j != 2)
                {
                    par3List.add(new ItemStack(par1, 1, j));
                }
            }
        }
    }
}
