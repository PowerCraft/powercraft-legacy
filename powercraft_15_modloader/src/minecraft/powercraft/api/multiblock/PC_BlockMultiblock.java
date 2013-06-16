package powercraft.api.multiblock;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Multiblock", tileEntity=PC_TileEntityMultiblock.class)
public class PC_BlockMultiblock extends PC_Block {

	public static PC_BlockMultiblock instance;
	public static PC_MultiblockSelection selection;
	public static boolean rayTrace;
	
	private static String[] getIconList(){
		String[] list = new String[18];
		list[0] = "Cable_Isolation";
		list[1] = "Cable_Solo";
		for(int i=0; i<16; i++){
			list[i+2] = "Cable"+i;
		}
		return list;
	}
	
	public PC_BlockMultiblock(int id) {
		super(id, Material.rock, getIconList());
		instance = this;
		setHardness(2);
	}

	public static Icon getIcon(){
		return instance.sideIcons[0];
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int dir, float xHit, float yHit, float zHit) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity==null)
			return false;
		return tileEntity.onBlockActivated(entityPlayer, dir, xHit, yHit, zHit);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity==null)
			return true;
		if(((RenderBlocks)renderer).overrideBlockTexture!=null){
			if(selection!=null && selection.selectPos!=null) {
				if(selection.selectPos.x == x && selection.selectPos.y == y && selection.selectPos.z == z){ 
					AxisAlignedBB selectAABB = tileEntity.getCollisionBox(selection.side);
					if(selectAABB!=null){
						setBlockBounds((float)selectAABB.minX-x, (float)selectAABB.minY-y, (float)selectAABB.minZ-z, 
								(float)selectAABB.maxX-x, (float)selectAABB.maxY-y, (float)selectAABB.maxZ-z);
						PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
					}
				}
			}
			return true;
		}
		return tileEntity.renderBlock(renderer);
	}

	@Override
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		return true;
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity!=null)
			tileEntity.addCollisionBoxesToList(aabb, list, entity);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z){
		return null;
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 pos, Vec3 startPos) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity==null){
			return null;
		}
		MovingObjectPosition bestMovingObjectPosition=null;
		PC_FractionSide bestSide = null;
		for(int i=0; i<27; i++){
			PC_FractionSide side = PC_FractionSide.fromIndex(i);
			AxisAlignedBB collisionBox = tileEntity.getCollisionBox(side);
			if(collisionBox!=null){
				setBlockBounds((float)collisionBox.minX-x, (float)collisionBox.minY-y, (float)collisionBox.minZ-z, 
						(float)collisionBox.maxX-x, (float)collisionBox.maxY-y, (float)collisionBox.maxZ-z);
				rayTrace = true;
				MovingObjectPosition movingObjectPosition = super.collisionRayTrace(world, x, y, z, pos, startPos);
				rayTrace = false;
				if(movingObjectPosition != null && (bestMovingObjectPosition==null || movingObjectPosition.hitVec.distanceTo(pos)<bestMovingObjectPosition.hitVec.distanceTo(pos))){
					bestMovingObjectPosition = movingObjectPosition;
					bestSide = side;
				}
			}
		}
		if(bestMovingObjectPosition!=null){
			PC_VecI blockPos = new PC_VecI(x, y, z);
			if(selection==null || !(selection.selectPos.equals(blockPos) && selection.side == bestSide)){
				PC_ClientUtils.mc().playerController.resetBlockRemoving();
				selection = new PC_MultiblockSelection(blockPos, bestSide);
			}
			setBlockBoundsBasedOnState(world, x, y, z);
		}
		return bestMovingObjectPosition; 
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		if(rayTrace)
			return;
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity==null){
			return;
		}
		if(selection!=null && selection.selectPos!=null) {
			if(selection.selectPos.x == x && selection.selectPos.y == y && selection.selectPos.z == z){ 
				AxisAlignedBB selectAABB = tileEntity.getCollisionBox(selection.side);
				if(selectAABB!=null){
					setBlockBounds((float)selectAABB.minX-x, (float)selectAABB.minY-y, (float)selectAABB.minZ-z, 
							(float)selectAABB.maxX-x, (float)selectAABB.maxY-y, (float)selectAABB.maxZ-z);
				}
				return;
			}
		}
		setBlockBounds(0, 0, 0, 1, 1, 1);
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
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity==null){
			return true;
		}
		if(selection!=null && selection.selectPos!=null) {
			if(selection.selectPos.x == x && selection.selectPos.y == y && selection.selectPos.z == z){
				return tileEntity.removeFraction(selection.side);
			}
		}
		return true;
	}

	public Icon getCableIcon(int i) {
		return sideIcons[i];
	}
	
}
