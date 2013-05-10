package mods.betterworld.CB.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.betterworld.CB.BWCB;
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

public class BW_BlockX extends BlockContainer {
	
	protected Icon[] iconBuffer = new Icon[32];
	private int damVal;
	
	public BW_BlockX (int id) {
		super(id, Material.ground);
		setUnlocalizedName("BlockX");
		setCreativeTab(CreativeTabs.tabBlock);
		setResistance(2000.0F);
		setHardness(4.0F);
	}

	public TileEntity createNewTileEntity(World world) {
		return new BW_TileEntityBlockX();
	}


    @Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
    	BW_TileEntityBlockX tes = (BW_TileEntityBlockX) par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
    	
		return iconBuffer[tes.getBlockDamageID()];
	}
	
	
	@Override
	public Icon getIcon(int side, int metadata) {
		//System.out.println("getIcon Metadata: "+ metadata);	
		return this.iconBuffer[metadata];
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister)
    {
		for (int i = 0; i < 32; i++) {
	
		iconBuffer[i] = iconRegister.registerIcon("betterworld/CB:bricks"+i+"_"+BWCB.textureRes);
		}
    }
	@Override
	public int damageDropped (int metadata) {
		return 0;
	}
/*	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < 32; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}
	*/
	public int getSubBlockCount() {
		return iconBuffer.length;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		// TODO Auto-generated method stub
		return 0;
	}
/*
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4,
			int par5, int par6) {
		int damage =((BW_TileEntityBlockX)par1World.getBlockTileEntity(par2, par3, par4)).getBlockDamageID();
		ItemStack dropItem = new ItemStack(this, 1, damage);
		dropBlockAsItem_do(par1World, par2, par3, par4, dropItem);
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y,
			int z, int metadata, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		int damage =((BW_TileEntityBlockX)world.getBlockTileEntity(x, y, z)).getBlockDamageID();
		ItemStack dropItem = new ItemStack(this, 1, damage);
		list.add(dropItem);
		return list;
	}


	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4,
			int par5, EntityPlayer par6EntityPlayer) {
		if(!par1World.isRemote && !((EntityPlayerMP)par6EntityPlayer).theItemInWorldManager.isCreative())
		{
		int damage =((BW_TileEntityBlockX)par1World.getBlockTileEntity(par2, par3, par4)).getBlockDamageID();
		ItemStack dropItem = new ItemStack(this, 1, damage);
		dropBlockAsItem_do(par1World, par2, par3, par4, dropItem);
		}
		super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
	}
*/
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x,
			int y, int z) {
		String userName =((BW_TileEntityBlockX)world.getBlockTileEntity(x, y, z)).getUserName();
		if(userName == null || !userName.equals(player.username))
		{
			return false;

		}
		if(!world.isRemote && !((EntityPlayerMP)player).theItemInWorldManager.isCreative())
		{
		int damage =((BW_TileEntityBlockX)world.getBlockTileEntity(x, y, z)).getBlockDamageID();
		ItemStack dropItem = new ItemStack(this, 1, damage);
		dropBlockAsItem_do(world, x, y, z, dropItem);
		}
		return super.removeBlockByPlayer(world, player, x, y, z);
	}
	public static boolean isPlayerOPOrOwner(EntityPlayer player) {
		  if (MinecraftServer.getServer().getConfigurationManager().getOps().contains(player.username.trim().toLowerCase()))
		   return true;
		  return MinecraftServer.getServer().getServerOwner() == player.getEntityName();
		 }
}
