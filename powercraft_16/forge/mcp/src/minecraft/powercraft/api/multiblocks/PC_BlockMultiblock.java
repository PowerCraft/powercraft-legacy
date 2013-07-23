package powercraft.api.multiblocks;


import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_MultiblockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@PC_BlockInfo(name = "Multiblock", blockid = "multiblock", defaultid = 3000, tileEntity = PC_TileEntityMultiblock.class)
public class PC_BlockMultiblock extends PC_Block {

	private static PC_MultiblockIndex selectionIndex;
	private static boolean rayTrace;
	public static PC_MultiblockTileEntity renderTileEntity;

	public static PC_BlockMultiblock block;
	@SideOnly(Side.CLIENT)
	private static Icon[] icons;
	@SideOnly(Side.CLIENT)
	public static int colorMultiplier = 0xFFFFFFFF;


	public PC_BlockMultiblock(int id) {

		super(id, Material.ground);
		block = this;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {

		PC_MultiblockRegistry.loadIcons();
	}


	@Override
	public void registerRecipes() {

	}


	@Override
	public boolean isOpaqueCube() {

		return false;
	}


	@Override
	public boolean renderAsNormalBlock() {

		return false;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {

		return getIcon(side, 0) != null;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {

		if (icons == null) return null;
		if (side >= icons.length) side = icons.length - 1;
		return icons[side];
	}


	@SideOnly(Side.CLIENT)
	public static void setIcons(Icon... newIcons) {

		icons = newIcons;
	}


	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 pos, Vec3 startPos) {

		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity == null) {
			return null;
		}
		MovingObjectPosition bestMovingObjectPosition = null;
		PC_MultiblockIndex bestIndex = null;
		for (PC_MultiblockIndex index : PC_MultiblockIndex.values()) {
			List<AxisAlignedBB> collisionBoxes = tileEntity.getCollisionBoxes(index);
			if (collisionBoxes != null) {
				for (AxisAlignedBB collisionBox : collisionBoxes) {
					setBlockBounds((float) collisionBox.minX, (float) collisionBox.minY, (float) collisionBox.minZ, (float) collisionBox.maxX,
							(float) collisionBox.maxY, (float) collisionBox.maxZ);
					rayTrace = true;
					MovingObjectPosition movingObjectPosition = super.collisionRayTrace(world, x, y, z, pos, startPos);
					rayTrace = false;
					if (movingObjectPosition != null
							&& (bestMovingObjectPosition == null || movingObjectPosition.hitVec.distanceTo(pos) < bestMovingObjectPosition.hitVec
									.distanceTo(pos))) {
						bestMovingObjectPosition = movingObjectPosition;
						bestIndex = index;
					}
				}
			}
		}
		if (bestMovingObjectPosition != null && world.isRemote) {
			if (selectionIndex != bestIndex) {
				PC_ClientUtils.mc().playerController.resetBlockRemoving();
				selectionIndex = bestIndex;
			}
			setBlockBoundsBasedOnState(world, x, y, z);
		}
		return bestMovingObjectPosition;
	}


	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

		if (rayTrace) return;
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {

		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity != null && selectionIndex != null) {
			return tileEntity.getSelectionBox(selectionIndex);
		}
		return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {

		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity != null) tileEntity.addCollisionBoxesToList(aabb, list, entity);
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {

		return null;
	}


	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {

		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (selectionIndex == null || world.isRemote) return false;
		List<ItemStack> drops = tileEntity.removeMultiblockTileEntity(selectionIndex);
		if (drops != null && !PC_Utils.isCreativ(player)) {
			for (ItemStack drop : drops)
				dropBlockAsItem_do(world, x, y, z, drop);
		}
		selectionIndex = null;
		if (tileEntity.noBlocks()) return super.removeBlockByPlayer(world, player, x, y, z);
		return false;
	}


	@Override
	public int idDropped(int metadata, Random rand, int fortune) {

		return 0;
	}


	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {

		return colorMultiplier;
	}

}
