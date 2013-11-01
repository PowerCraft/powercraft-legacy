package powercraft.api.multiblocks;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_Direction;
import powercraft.api.PC_IPacketHandler;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_Reflection;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_MultiblockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@PC_BlockInfo(name = "Multiblock", blockid = "multiblock", defaultid = 3000, tileEntity = PC_TileEntityMultiblock.class)
public class PC_BlockMultiblock extends PC_Block implements PC_IPacketHandler{

	private static PC_MultiblockIndex selectionIndex;
	private static boolean rayTrace;
	public static PC_MultiblockTileEntity renderTileEntity;
	protected static HashMap<Integer, PC_MultiblockIndex> playerSelection = new HashMap<Integer, PC_MultiblockIndex>();
	
	public static PC_BlockMultiblock block;
	@SideOnly(Side.CLIENT)
	private static Icon[] icons;
	@SideOnly(Side.CLIENT)
	public static int colorMultiplier;
	
	public PC_BlockMultiblock(int id) {

		super(id, Material.ground);
		block = this;
		
		if(PC_Utils.isClient())
			colorMultiplier = 0xFFFFFFFF;
		
		PC_PacketHandler.registerPacketHandler("MultiblockPacketHandler", this);
		
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
	public Icon getIcon(PC_Direction side, int metadata) {

		if (icons == null) return null;
		int sideID = side.ordinal();
		if (sideID >= icons.length) sideID = icons.length - 1;
		return icons[sideID];
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
		if(bestMovingObjectPosition!=null && bestIndex!=null){
			bestMovingObjectPosition.subHit = bestIndex.ordinal();
			if (world.isRemote) {
				if (selectionIndex != bestIndex) {
					selectionIndex = bestIndex;
					int playerID = PC_ClientUtils.mc().thePlayer.entityId;
					playerSelection.put(playerID, selectionIndex);
					NBTTagCompound tagCompound = new NBTTagCompound();
					tagCompound.setInteger("playerID", playerID);
					tagCompound.setInteger("x", x);
					tagCompound.setInteger("y", y);
					tagCompound.setInteger("z", z);
					tagCompound.setInteger("side", selectionIndex.ordinal());
					PC_PacketHandler.sendPacketToServer(PC_PacketHandler.getPacketHandlerPacket("MultiblockPacketHandler", tagCompound));
					resetClientDigging(x, y, z, bestMovingObjectPosition.sideHit);
				}
				setBlockBoundsBasedOnState(world, x, y, z);
			}
		}
		return bestMovingObjectPosition;
	}
	
	@SideOnly(Side.CLIENT)
	private static void resetClientDigging(int x, int y, int z, int side){
		if(PC_Reflection.getValue(PlayerControllerMP.class, PC_ClientUtils.mc().playerController, 9, boolean.class)){
			PC_ClientUtils.mc().playerController.resetBlockRemoving();
			if(!PC_Utils.isCreativ(PC_ClientUtils.mc().thePlayer)){
				PC_ClientUtils.mc().playerController.clickBlock(x, y, z, side);
			}
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {

		if (rayTrace) return;
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World world, int x, int y, int z) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity != null && selectionIndex!=null) {
			AxisAlignedBB aabb = tileEntity.getSelectionBox(selectionIndex);
			if(aabb!=null)
				return aabb;
		}
		return AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	}


	@Override
	public List<AxisAlignedBB> getCollisonBoxesList(World world, int x, int y, int z, Entity entity) {
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if (tileEntity != null) tileEntity.addCollisionBoxesToList(list, entity);
		return list;
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, int x, int y, int z) {
		return null;
	}


	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		
		if (world.isRemote) return false;
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		PC_MultiblockIndex selectionIndex = playerSelection.get(player.entityId);
		if (selectionIndex==null) return false;
		List<ItemStack> drops = tileEntity.removeMultiblockTileEntity(selectionIndex);
		if (drops != null && !PC_Utils.isCreativ(player)) {
			for (ItemStack drop : drops)
				dropBlockAsItem_do(world, x, y, z, drop);
		}
		playerSelection.remove(player.entityId);
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
	
	@Override
	public float getBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		PC_MultiblockIndex index;
		if(world.isRemote){
			if(PC_ClientUtils.mc().thePlayer != player)
				return -1;
			index = selectionIndex;
		}else{
			index = playerSelection.get(player.entityId);
		}
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		return tileEntity.getTileHardness(index, player);
	}
	
	@Override
	public void handlePacket(World world, EntityPlayer entityPlayer, NBTTagCompound packet) {
		int playerID = packet.getInteger("playerID");
		int x = packet.getInteger("x");
		int y = packet.getInteger("y");
		int z = packet.getInteger("z");
		PC_MultiblockIndex oldIndex = playerSelection.get(playerID);
		PC_MultiblockIndex newIndex = PC_MultiblockIndex.values()[packet.getInteger("side")];
		if(oldIndex!=newIndex){
			if(world.isRemote){
				if(playerID!=entityPlayer.entityId){
					double difX = entityPlayer.posX-x+0.5;
					double difY = entityPlayer.posY-y+0.5;
					double difZ = entityPlayer.posZ-z+0.5;
					if(difX*difX+difY*difY+difZ*difZ>1024){
						playerSelection.remove(playerID);
					}else{
						playerSelection.put(playerID, newIndex);
					}
				}
			}else{
				playerSelection.put(playerID, newIndex);
				((EntityPlayerMP)entityPlayer).theItemInWorldManager.cancelDestroyingBlock(x, y, z);
				PC_PacketHandler.sendPacketToAllInDimension(PC_PacketHandler.getPacketHandlerPacket("MultiblockPacketHandler", packet), entityPlayer.dimension);
			}
		}
	}
	
}
