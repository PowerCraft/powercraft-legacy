package mods.betterworld.CB.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.betterworld.CB.BWCB;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.CB.tileEntity.BWCB_TileEntityBlockWood;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BWCB_BlockWood extends BlockContainer {

	private int damVal;
	private Icon[] iconBuffer;

	public BWCB_BlockWood(int id) {

		super(id, Material.wood);
		setStepSound(soundWoodFootstep);
		setUnlocalizedName("BlockWood");
		setCreativeTab(BWCB.tab);
		setResistance(10.0F);
		setHardness(5.0F);

	}

	public TileEntity createNewTileEntity(World world) {
		return new BWCB_TileEntityBlockWood();
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
			return ((BWCB_TileEntityBlockWood) world
					.getBlockTileEntity(x, y, z)).getLightValue();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2,
			int par3, int par4, int par5) {
		BWCB_TileEntityBlockWood tes = (BWCB_TileEntityBlockWood) par1iBlockAccess
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
		String userName = ((BWCB_TileEntityBlockWood) world.getBlockTileEntity(
				x, y, z)).getUserName();
		boolean lock = ((BWCB_TileEntityBlockWood) world.getBlockTileEntity(x,
				y, z)).isBlockLocked();
		if ((userName == null || !userName.equals(player.username)) && lock) {
			return false;

		}
		if (!world.isRemote
				&& !((EntityPlayerMP) player).theItemInWorldManager
						.isCreative()) {
			int damage = ((BWCB_TileEntityBlockWood) world.getBlockTileEntity(
					x, y, z)).getBlockDamageID();
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
}
