package powercraft.api.blocks;


import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercraft.apiOld.PC_Logger;
import powercraft.apiOld.PC_Module;
import powercraft.apiOld.PC_Renderer;
import powercraft.apiOld.PC_Utils;
import powercraft.apiOld.registries.PC_ModuleRegistry;
import powercraft.apiOld.registries.PC_TextureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public abstract class PC_Block extends BlockContainer implements PC_IBlock {

	public final PC_BlockInfo blockInfo;

	public final PC_Module module;


	public PC_Block(int id, Material material) {

		super(id, material);
		blockInfo = getClass().getAnnotation(PC_BlockInfo.class);
		module = PC_ModuleRegistry.getActiveModule();
	}


	@Override
	public TileEntity createNewTileEntity(World world) {

		try {
			return blockInfo.tileEntity().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to generate tileEntity %s", blockInfo.tileEntity());
		}
		return null;
	}


	@Override
	public PC_BlockInfo getBlockInfo() {

		return blockInfo;
	}


	@Override
	public PC_Module getModule() {

		return module;
	}


	@Override
	public void onBlockAdded(World world, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.onBlockAdded();
		super.onBlockAdded(world, x, y, z);
	}


	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.onBlockBreak();
		super.breakBlock(world, x, y, z, blockID, metadata);
	}


	@Override
	public float getBlockHardness(World world, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getBlockHardness();
	}


	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.onNeighborBlockChange(neighborID);
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.onBlockActivated(player, side, xHit, yHit, zHit);
	}


	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getRedstonePowerValue(side);
	}


	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getRedstonePowerValue(side);
	}


	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {

		Block block = PC_Utils.getBlock(world, x, y, z);
		if (block != null && block != this) {
			return block.getLightValue(world, x, y, z);
		}
		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity == null) return 0;
		return tileEntity.getLightValue();
	}


	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.canConnectRedstone(side);
	}


	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getPickBlock(target);
	}


	@Override
	public int getLightOpacity(World world, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getLightOpacity();
	}


	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.rotateBlock(axis);
	}


	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getValidRotations();
	}


	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.recolourBlock(side, colour);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.randomDisplayTick(random);
	}


	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.onEntityCollidedWithBlock(entity);
	}


	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.removeBlockByPlayer(player);
	}


	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity != null) {
			return tileEntity.getBlockDropped(fortune);
		}
		return new ArrayList<ItemStack>();
	}


	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer entityPlayer) {

		if (!PC_Utils.isCreativ(entityPlayer)) super.harvestBlock(world, entityPlayer, x, y, z, metadata);
	}


	@Override
	public void harvestBlock(World world, EntityPlayer entityPlayer, int x, int y, int z, int metadata) {

	}


	@Override
	public void onBlockMessage(World world, int x, int y, int z, EntityPlayer player, NBTTagCompound nbtTagCompound) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.onBlockMessage(player, nbtTagCompound);
	}


	@Override
	public void loadFromNBT(World world, int x, int y, int z, NBTTagCompound nbtTagCompound) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.loadFromNBT(nbtTagCompound);
	}


	@Override
	public void saveToNBT(World world, int x, int y, int z, NBTTagCompound nbtTagCompound) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		tileEntity.saveToNBT(nbtTagCompound);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {

		PC_TextureRegistry.registerIcons(this, iconRegistry);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderInventoryBlock(int metadata, RenderBlocks renderer) {

		return false;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {

		PC_TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.renderWorldBlock(renderer);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {

		return PC_Renderer.getRenderType();
	}
	
}
