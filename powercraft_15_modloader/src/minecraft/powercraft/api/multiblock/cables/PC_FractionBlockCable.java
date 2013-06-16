package powercraft.api.multiblock.cables;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.api.multiblock.PC_BlockMultiblock;
import powercraft.api.multiblock.PC_FractionBlock;
import powercraft.api.multiblock.PC_FractionSide;
import powercraft.api.multiblock.PC_FractionType;
import powercraft.api.multiblock.PC_IMultiblock;
import powercraft.api.renderer.PC_ISpecialRenderer;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.renderer.PC_SpecialRenderer;
import powercraft.api.renderer.PC_SpecialRenderer.CableNode;
import powercraft.api.utils.PC_Color;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_RectF;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public abstract class PC_FractionBlockCable extends PC_FractionBlock {
	
	protected int[] connectionMasks = new int[4];
	protected float width = 1.0f/16.0f;
	
	public PC_FractionBlockCable(){
		thick = 1.0f/16.0f;
	}
	
	public abstract PC_CableTypes getCableType();
	
	public int canConnectToFraction(PC_FractionBlock fractionBlock){
		if(fractionBlock instanceof PC_FractionBlockCable){
			if(((PC_FractionBlockCable) fractionBlock).getCableType() == getCableType()){
				return (((PC_FractionBlockCable) fractionBlock).getCableMask() & getCableMask());
			}
		}
		return 0;
	}
	
	public abstract int getConnectionMaskToBlockAndTileEntity(PC_IMultiblock multiblock, PC_FractionSide fromIndex, Block block, TileEntity tileEntity);
	
	public abstract int getCableMask();
	
	public int canConnectTo(PC_IMultiblock thisMultiblock, PC_FractionSide fromIndex, World world, PC_VecI pos, PC_Direction side, PC_Direction side2){
		int mask=0;
		PC_VecI pos2 = pos.offset(side.getOffset());
		TileEntity te = PC_Utils.getTE(world, pos);
		if(te instanceof PC_IMultiblock){
			PC_IMultiblock multiblock = (PC_IMultiblock)te;
			PC_FractionSide fraction = PC_FractionSide.getFractionSide(side);
			PC_FractionBlock fractionBlock = multiblock.getFractionOnSide(fraction);
			mask |= canConnectToFraction(fractionBlock);
		}
		side = side.mirror();
		te = PC_Utils.getTE(world, pos2);
		Block block = PC_Utils.getBlock(world, pos2);
		if(te instanceof PC_IMultiblock){
			PC_IMultiblock multiblock = (PC_IMultiblock)te;
			PC_FractionSide fraction = PC_FractionSide.getFractionSide(side2);
			PC_FractionBlock fractionBlock = multiblock.getFractionOnSide(fraction);
			mask |= canConnectToFraction(fractionBlock);
		}else if(block!=null){
			mask |= getConnectionMaskToBlockAndTileEntity(thisMultiblock, fromIndex, block, te);
		}
		te = PC_Utils.getTE(world, pos2.offset(side2.getOffset()));
		if(block==null){
			block = PC_Utils.getBlock(world, pos2.offset(side2.getOffset()));
			if(te instanceof PC_IMultiblock){
				PC_IMultiblock multiblock = (PC_IMultiblock)te;
				PC_FractionSide fraction = PC_FractionSide.getFractionSide(side);
				PC_FractionBlock fractionBlock = multiblock.getFractionOnSide(fraction);
				mask |= canConnectToFraction(fractionBlock);
			}else if(block!=null){
				mask |= getConnectionMaskToBlockAndTileEntity(thisMultiblock, fromIndex, block, te);
			}
		}
		te = PC_Utils.getTE(world, pos.offset(side2.getOffset()));
		block = PC_Utils.getBlock(world, pos.offset(side2.getOffset()));
		if(te instanceof PC_IMultiblock){
			PC_IMultiblock multiblock = (PC_IMultiblock)te;
			PC_FractionSide fraction = PC_FractionSide.getFractionSide(side.mirror());
			PC_FractionBlock fractionBlock = multiblock.getFractionOnSide(fraction);
			mask |= canConnectToFraction(fractionBlock);
		}else if(block!=null){
			mask |= getConnectionMaskToBlockAndTileEntity(thisMultiblock, fromIndex, block, te);
		}
		te = PC_Utils.getTE(world, pos2);
		block = PC_Utils.getBlock(world, pos2);
		if(te instanceof PC_IMultiblock){
			PC_IMultiblock multiblock = (PC_IMultiblock)te;
			PC_FractionSide fraction = PC_FractionSide.getFractionSide(side);
			PC_FractionBlock fractionBlock = multiblock.getFractionOnSide(fraction);
			mask |= canConnectToFraction(fractionBlock);
		}else if(block!=null){
			mask |= getConnectionMaskToBlockAndTileEntity(thisMultiblock, fromIndex, block, te);
		}
		return mask;
	}
	
	public boolean canPlaceOnPosition(PC_IMultiblock multiblock, PC_FractionSide side){
		return side.type == PC_FractionType.PLAIN;
	}
	
	public void onNeighborBlockChange(PC_IMultiblock multiblock, PC_FractionSide fromIndex, int id) {
		checkConnections(multiblock, fromIndex);
	}
	
	public abstract void updatePowerValue(PC_IMultiblock multiblock, PC_FractionSide fromIndex);
	
	private void checkConnections(PC_IMultiblock multiblock, PC_FractionSide fromIndex){
		World world = multiblock.getWorld();
		PC_VecI pos = multiblock.getCoord();
		PC_Direction dir = PC_FractionSide.getDir(fromIndex);
		int j=0;
		if(dir!=null){
			for(int i=0; i<6; i++){
				PC_Direction dir2 = PC_Direction.getFromMCSide(i);
				if(dir!=dir2 && dir!=dir2.mirror()){
					connectionMasks[j++]=canConnectTo(multiblock, fromIndex, world, pos, dir2, dir);
				}
			}
		}
	}
	
	@Override
	public void renderFraction(PC_IMultiblock multiblock, PC_FractionSide side, Object renderer) {
		int connections[] = new int[4];
		connections[0] = connectionMasks[3];
		connections[1] = connectionMasks[2];
		connections[2] = connectionMasks[1];
		connections[3] = connectionMasks[0];
		AxisAlignedBB aabb;
		PC_VecI pos = multiblock.getCoord();
		PC_ISpecialRenderer blockRenderer = new PC_SpecialRenderer();
		Icon[] icons = new Icon[18];
		for(int i=0; i<18; i++){
			icons[i] = PC_BlockMultiblock.instance.getCableIcon(i);
		}
		blockRenderer.drawBlock(pos, PC_FractionSide.getDir(side), icons);
		CableNode nodes[] = new CableNode[4];
		int mask = getCableMask();
		for(int i=0; i<4; i++){
			if(connections[i]>0){
				nodes[i] = new CableNode(connections[i], 0.5f, false);
			}
		}
		switch(side){
		case BACK:
		case LEFT:
			blockRenderer.drawCables(mask, nodes[2], nodes[0], nodes[1], nodes[3]);
			break;
		case BOTTOM:
			blockRenderer.drawCables(mask, nodes[0], nodes[1], nodes[2], nodes[3]);
			break;
		case FRONT:
		case RIGHT:
			blockRenderer.drawCables(mask, nodes[2], nodes[1], nodes[0], nodes[3]);
			break;
		case TOP:
			blockRenderer.drawCables(mask, nodes[0], nodes[2], nodes[1], nodes[3]);
			break;
		default:
			return;
		}
		PC_Utils.setBlockBounds(PC_BlockMultiblock.instance, 0, 0, 0, 1, 1, 1);
		return;
	}
	
	@Override
	public PC_FractionBlock readFromNBT(NBTTagCompound nbttag) {
		connectionMasks = nbttag.getIntArray("connectionMasks");
		width = nbttag.getFloat("width");
		return super.readFromNBT(nbttag);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttag) {
		nbttag.setIntArray("connectionMasks", connectionMasks);
		nbttag.setFloat("width", width);
		return super.writeToNBT(nbttag);
	}
	
}
