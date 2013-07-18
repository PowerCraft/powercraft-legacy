package powercraft.api.multiblocks.cable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.PC_MultiblockIndex;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_TileEntityMultiblock;
import powercraft.api.multiblocks.cable.redstone.PC_IRedstoneCable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_CableTileEntity extends PC_MultiblockTileEntity {
	
	protected int width;
	private int centerThickness;
	private int connections[][] = new int[4][];
	
	public PC_CableTileEntity(int thickness, int width) {
		super(thickness);
		this.width = width;
	}
	
	protected abstract PC_IRedstoneCable getCableType(int cableID);

	protected abstract Icon getCableIcon();
	
	protected abstract Icon getCableLineIcon(int index);
	
	private float min(float w, int offset, float l){
		if(offset<0){
			return 0.5f-l;
		}else if(offset>0){
			return 0.5f+w;
		}
		return 0.5f-w;
	}
	
	private float max(float w, int offset, float l){
		if(offset<0){
			return 0.5f-w;
		}else if(offset>0){
			return 0.5f+l;
		}
		return 0.5f+w;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderWorldBlock(RenderBlocks renderer) {
		float s = thickness/16.0f;
		float w = width/32.0f;
		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		PC_BlockMultiblock.setIcons(getCableIcon());
		float min1 = 1-s;
		float min2 = 0;
		float max1 = 1;
		float max2 = s;
		if(centerThickness>0){
			float t = (centerThickness+2)/32.0f;
			min1 = 0.5f+t;
			min2 = 0.5f-t-s;
			max1 = 0.5f+t+s;
			max2 = 0.5f-t;
		}
		if(dir==PC_Direction.UP || dir==PC_Direction.DOWN){
			float minY = dir==PC_Direction.UP?min1:min2;
			float maxY = dir==PC_Direction.UP?max1:max2;
			renderer.setRenderBounds(0.5-w, minY, 0.5-w, 0.5+w, maxY, 0.5+w);
			renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
			int i=0;
			for(PC_Direction dir2:PC_Direction.VALID_DIRECTIONS){
				if(dir2==dir||dir2.getOpposite()==dir)
					continue;
				int connection[] = connections[i++];
				if(connection!=null){
					int c1 = connection[0];
					float c1e = (c1>>16)/32.0f;
					if(c1==0){
						int c3 = connection[2];
						if(c3==0){
							int c4 = connection[3];
							c1e = 1-((c4>>16)-2)/32.0f;
						}else{
							c1e = 0.5f;
						}
					}
					renderer.setRenderBounds(min(w, dir2.offsetX, c1e), minY, min(w, dir2.offsetZ, c1e), max(w, dir2.offsetX, c1e), maxY, max(w, dir2.offsetZ, c1e));
					renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
				}
			}
		}else if(dir==PC_Direction.NORTH || dir==PC_Direction.SOUTH){
			float minZ = dir==PC_Direction.SOUTH?min1:min2;
			float maxZ = dir==PC_Direction.SOUTH?max1:max2;
			renderer.setRenderBounds(0.5-w, 0.5-w, minZ, 0.5+w, 0.5+w, maxZ);
			renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
			int i=0;
			for(PC_Direction dir2:PC_Direction.VALID_DIRECTIONS){
				if(dir2==dir||dir2.getOpposite()==dir)
					continue;
				int connection[] = connections[i++];
				if(connection!=null){
					int c1 = connection[0];
					float c1e = ((c1>>16)-2)/32.0f;
					if(c1==0){
						int c3 = connection[2];
						if(c3==0){
							int c4 = connection[3];
							c1e = 1-(c4>>16)/32.0f;
						}else{
							c1e = 0.5f;
						}
					}
					renderer.setRenderBounds(min(w, dir2.offsetX, c1e), min(w, dir2.offsetY, c1e), minZ, max(w, dir2.offsetX, c1e), max(w, dir2.offsetY, c1e), maxZ);
					renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
				}
			}
		}else if(dir==PC_Direction.EAST || dir==PC_Direction.WEST){
			float minX = dir==PC_Direction.EAST?min1:min2;
			float maxX = dir==PC_Direction.EAST?max1:max2;
			renderer.setRenderBounds(minX, 0.5-w, 0.5-w, maxX, 0.5+w, 0.5+w);
			renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
			int i=0;
			for(PC_Direction dir2:PC_Direction.VALID_DIRECTIONS){
				if(dir2==dir||dir2.getOpposite()==dir)
					continue;
				int connection[] = connections[i++];
				if(connection!=null){
					int c1 = connection[0];
					float c1e = ((c1>>16)-(dir2==PC_Direction.UP || dir2==PC_Direction.DOWN?2:0))/32.0f;
					if(c1==0){
						int c3 = connection[2];
						if(c3==0){
							int c4 = connection[3];
							c1e = 1-((c4>>16)-(dir2==PC_Direction.UP || dir2==PC_Direction.DOWN?0:2))/32.0f;
						}else{
							c1e = 0.5f;
						}
					}
					renderer.setRenderBounds(minX, min(w, dir2.offsetY, c1e), min(w, dir2.offsetZ, c1e), maxX, max(w, dir2.offsetY, c1e), max(w, dir2.offsetZ, c1e));
					renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
				}
			}
		}
	}
	
	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock){
		if(multiblock.getClass()!=getClass())
			return 0;
		int connection = 0xFFFF;
		int length = 16;
		if(((PC_CableTileEntity)multiblock).centerThickness>0)
			length = ((PC_CableTileEntity)multiblock).centerThickness+2+((PC_CableTileEntity)multiblock).getThickness()*2;
		return connection | (length<<16);
	}
	
	protected int canConnectToBlock(World world, int x, int y, int z, Block block){
		return 0;
	}
	
	private int canConnectToBlock(World world, int x, int y, int z, PC_Direction dir, PC_Direction dir2){
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block instanceof PC_BlockMultiblock){
			PC_TileEntityMultiblock multiblock = PC_Utils.getTE(world, x, y, z);
			PC_MultiblockTileEntity mbte = multiblock.getMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[dir.ordinal()]);
			if(mbte!=null)
				return canConnectToMultiblock(mbte);
		}
		if(block!=null){
			return canConnectToBlock(world, x, y, z, block);
		}
		return 0;
	}
	
	private int[] canConnectTo(PC_Direction dir, PC_Direction dir2, int[] oldConnection) {
		World world = multiblock.worldObj;
		int x = multiblock.xCoord;
		int y = multiblock.yCoord;
		int z = multiblock.zCoord;
		int c1 = canConnectToBlock(world, x, y, z, dir2, dir);
		int c2 = canConnectToBlock(world, x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir2, dir.getOpposite());
		if(c1==0){
			int c3 = canConnectToBlock(world, x+dir2.offsetX, y+dir2.offsetY, z+dir2.offsetZ, dir, dir2.getOpposite());
			c3 |= canConnectToBlock(world, x+dir2.offsetX, y+dir2.offsetY, z+dir2.offsetZ, dir2.getOpposite(), dir);
			if(c3==0){
				int c4 = canConnectToBlock(world, x+dir2.offsetX+dir.offsetX, y+dir2.offsetY+dir.offsetY, z+dir2.offsetZ+dir.offsetZ, dir2.getOpposite(), dir.getOpposite());
				if((c1|c2|c3|c4)==0)
					return null;
				return new int[]{c1, c2, c3, c4};
			}
			return new int[]{c1, c2, c3};
		}
		return new int[]{c1, c2};
	}
	
	public List<ItemStack> checkConnections(){
		int i=0;
		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		centerThickness = 0;
		Block block = PC_Utils.getBlock(multiblock.worldObj, multiblock.xCoord+dir.offsetX, multiblock.yCoord+dir.offsetY, multiblock.zCoord+dir.offsetZ);
		if(block==null || !block.isBlockSolidOnSide(multiblock.worldObj, multiblock.xCoord+dir.offsetX, multiblock.yCoord+dir.offsetY, multiblock.zCoord+dir.offsetZ, ForgeDirection.values()[dir.getOpposite().ordinal()])){
			if(multiblock.getMultiblockTileEntity(PC_MultiblockIndex.CENTER)!=null)
				centerThickness = multiblock.getMultiblockTileEntity(PC_MultiblockIndex.CENTER).getThickness();
			else{
				return multiblock.removeMultiblockTileEntity(index);
			}
		}
		
		if(isClient()){
			return null;
		}
		
		for(PC_Direction dir2:PC_Direction.VALID_DIRECTIONS){
			if(dir2==dir||dir2.getOpposite()==dir)
				continue;
			connections[i] = canConnectTo(dir, dir2, connections[i]);
			i++;
		}
		multiblock.sendToClient();
		return null;
	}

	@Override
	public void onNeighborBlockChange(int neighborID) {
		List<ItemStack> drops = checkConnections();
		if(drops!=null)
			multiblock.drop(drops);
	}
	
	private void getGridIfNull(){
		if(!isClient()){
			
		}
	}
	
	private void removeFormGrid(){
		if(!isClient()){
			
		}
	}
	
	@Override
	public boolean onAdded() {
		if(checkConnections()!=null){
			return false;
		}
		getGridIfNull();
		return true;
	}

	@Override
	public void onBreak() {
		super.onBreak();
		removeFormGrid();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		removeFormGrid();
	}
	
	@Override
	public void loadFromNBT(NBTTagCompound nbtCompoundTag) {
		for(int i=0; i<connections.length; i++){
			if(nbtCompoundTag.hasKey("connections"+i))
				connections[i] = nbtCompoundTag.getIntArray("connections"+i);
			else
				connections[i] = null;
		}	
		centerThickness = nbtCompoundTag.getInteger("centerThickness");
		multiblock.renderUpdate();
	}

	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		for(int i=0; i<connections.length; i++)
			if(connections[i]!=null)
				nbtCompoundTag.setIntArray("connections"+i, connections[i]);
		nbtCompoundTag.setInteger("centerThickness", centerThickness);
	}
	
	@Override
	public void onLoaded() {
		getGridIfNull();
	}
	
	@Override
	public AxisAlignedBB getSelectionBox() {
		float s = thickness/16.0f;
		float w = width/32.0f;
		float min[] = {0, 0.5f-w, 0.5f+w};
		float max[] = {0.5f-w, 0.5f+w, 1};
		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		float min1 = 1-s;
		float min2 = 0;
		float max1 = 1;
		float max2 = s;
		if(centerThickness>0){
			float t = (centerThickness+2)/32.0f;
			min1 = 0.5f+t;
			min2 = 0.5f-t-s;
			max1 = 0.5f+t+s;
			max2 = 0.5f-t;
		}
		if(dir==PC_Direction.UP || dir==PC_Direction.DOWN){
			float minY = dir==PC_Direction.UP?min1:min2;
			float maxY = dir==PC_Direction.UP?max1:max2;
			return AxisAlignedBB.getBoundingBox(0, minY, 0, 1, maxY, 1);
		}else if(dir==PC_Direction.NORTH || dir==PC_Direction.SOUTH){
			float minZ = dir==PC_Direction.SOUTH?min1:min2;
			float maxZ = dir==PC_Direction.SOUTH?max1:max2;
			return AxisAlignedBB.getBoundingBox(0, 0, minZ, 1, 1, maxZ);
		}else if(dir==PC_Direction.EAST || dir==PC_Direction.WEST){
			float minX = dir==PC_Direction.EAST?min1:min2;
			float maxX = dir==PC_Direction.EAST?max1:max2;
			return AxisAlignedBB.getBoundingBox(minX, 0, 0, maxX, 1, 1);
		}
		return null;
	}
	
}
