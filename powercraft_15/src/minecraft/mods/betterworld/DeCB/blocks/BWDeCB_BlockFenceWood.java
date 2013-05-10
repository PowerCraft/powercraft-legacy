package mods.betterworld.DeCB.blocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.DeCB.BWDeCB;
import mods.betterworld.DeCB.client.BWDeCB_Render;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockFenceWood;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockStairs;
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

public class BWDeCB_BlockFenceWood extends BlockFence implements ITileEntityProvider {

	private int damVal;
	private Icon[] iconBuffer;


	public BWDeCB_BlockFenceWood(int par1, String par2Str, Material par3Material) {
		super(par1, par2Str, Material.rock);
		
		this.isBlockContainer = true;
		setUnlocalizedName("BlockFenceWood");
        this.setCreativeTab(BWDeCB.tabDeco);
        this.setStepSound(soundWoodFootstep);
		setResistance(10.0F);
		setHardness(5.0F);

	}

	public TileEntity createNewTileEntity(World world) {
		return new BWDeCB_TileEntityBlockFenceWood();
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
			return ((BWDeCB_TileEntityBlockFenceWood) world.getBlockTileEntity(x, y,
					z)).getLightValue();
		}
	}
	@Override
    public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockId(par2, par3, par4);

        if (l != this.blockID && l != Block.fenceGate.blockID &&l != Block.fence.blockID && l != BWDeCB.blockFenceWoodGate.blockID)
        {
            Block block = Block.blocksList[l];
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() ? block.blockMaterial != Material.pumpkin : false;
        }
        else
        {
            return true;
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4, int par5) {
		BWDeCB_TileEntityBlockFenceWood tes = (BWDeCB_TileEntityBlockFenceWood) par1iBlockAccess
				.getBlockTileEntity(par2, par3, par4);

		return iconBuffer[tes.getBlockDamageID()];
	}

	@Override
	public Icon getIcon(int side, int metadata) {
		return this.iconBuffer[metadata];
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		iconBuffer = new Icon[BWCB_BlockList.blockPlanksNormalTexture.size()];
		for (int i = 0; i < BWCB_BlockList.blockPlanksNormalTexture.size(); i++) {
			iconBuffer[i] = iconRegister.registerIcon("betterworld/CB:"
					+ String.valueOf(BWCB_BlockList.blockPlanksNormalTexture
							.get(i)) + "_" + BWCB.textureRes);
		}
	}
	@Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        if (world.doesBlockHaveSolidTopSurface(x, y, z))
        {
            return true;
        }
        else
        {
            int id = world.getBlockId(x, y, z);
            return id == this.blockID;
        }
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
	public int getRenderType() {
		return BWDeCB_Render.getRender().getRenderId();
  	//return 11;
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x,
			int y, int z) {
		String userName = ((BWDeCB_TileEntityBlockFenceWood) world
				.getBlockTileEntity(x, y, z)).getUserName();
		boolean lock = ((BWDeCB_TileEntityBlockFenceWood) world.getBlockTileEntity(
				x, y, z)).isBlockLocked();
		if ((userName == null || !userName.equals(player.username)) && lock) {
			return false;

		}
		if (!world.isRemote
				&& !((EntityPlayerMP) player).theItemInWorldManager
						.isCreative()) {
			int damage = ((BWDeCB_TileEntityBlockFenceWood) world
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

}
