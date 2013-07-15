package powercraft.api.energy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Vec3I;
import powercraft.api.grids.PC_IGridProvider;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_TileEntityMultiblock;
import powercraft.api.multiblocks.conduits.PC_ConduitTileEntity;

public class PC_ConduitEnergyTileEntity extends PC_ConduitTileEntity implements PC_IGridProvider<PC_EnergyGrid, PC_ConduitEnergyTileEntity> {

	private int type;
	private PC_EnergyGrid grid;
	
	public PC_ConduitEnergyTileEntity() {

	}
	
	public PC_ConduitEnergyTileEntity(int type) {
		this.type = type;
	}
	
	@Override
	public boolean canConnectToBlock(World world, int x, int y, int z, int side, Block block) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		return te instanceof PC_IEnergyConsumer || te instanceof PC_IEnergyProvider;
	}
	
	@Override
	public void checkConnections() {
		int oldConnections = connections;
		super.checkConnections();
		if(!isClient()){
			if(oldConnections != connections){
				getGridIfNull();
				int num = 0;
				int oldNum = 0;
				boolean isIO=false;
				boolean wasIO=false;
				for(PC_Direction dir:PC_Direction.VALID_DIRECTIONS){
					if(((connections>>(dir.ordinal()*2)) & 0x3) != 0){
						num++;
						if(((connections>>(dir.ordinal()*2)) & 0x3)==2){
							isIO = true;
						}
					}
					if(((oldConnections>>(dir.ordinal()*2)) & 0x3) != 0){
						oldNum++;
						if(((connections>>(dir.ordinal()*2)) & 0x3)==2){
							wasIO = true;
						}
					}
				}
				if(isIO){
					if(!wasIO){
						grid.addIO(this);
					}
				}else{
					if(num==2){
						if(oldNum!=2)
							grid.addNode(this);
					}else if(oldNum==2){
						grid.add(this);
					}
				}
			}
		}
	}

	@Override
	public void onAdded() {
		super.onAdded();
		getGridIfNull();
	}

	@Override
	public void onBreak() {
		super.onBreak();
		removeFormGrid();
	}

	@Override
	public void onChunkUnload() {
		System.out.println("onChunkUnload");
		super.onChunkUnload();
		removeFormGrid();
	}
	
	private void getGridIfNull(){
		if(grid==null && !isClient()){
			for(PC_Direction dir:PC_Direction.values()){
				PC_EnergyGrid g = getGrid(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord + dir.offsetZ);
				if(g!=null){
					if(grid==null){
						grid = g;
					}else if(grid!=g){
						grid.mixGrids(g);
					}
				}
			}
			if(grid==null){
				grid = new PC_EnergyGrid();
			}
			addToGrid();
		}
	}
	
	private void removeFormGrid(){
		if(grid!=null && !isClient()){
			grid.remove(this);
			grid = null;
			boolean isFirst = true;
			List<PC_EnergyGrid> doneGrids = new ArrayList<PC_EnergyGrid>();
			for(PC_Direction dir:PC_Direction.values()){
				PC_EnergyGrid g = getGrid(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord + dir.offsetZ);
				if(g!=null && !doneGrids.contains(g)){
					if(isFirst){
						isFirst = false;
					}else{
						PC_EnergyGrid ng = new PC_EnergyGrid();
						doneGrids.add(ng);
						setBlockAndNeighborGrid(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord + dir.offsetZ, ng);
					}
				}
			}
		}
	}
	
	private void setBlockAndNeighborGrid(World world, int x, int y, int z, PC_EnergyGrid grid) {
		List<PC_Vec3I> toLookAt = new ArrayList<PC_Vec3I>();
		toLookAt.add(new PC_Vec3I(x, y, z));
		while(!toLookAt.isEmpty()){
			PC_Vec3I pos = toLookAt.remove(0);
			PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, pos.x, pos.y, pos.z);
			PC_MultiblockTileEntity multiblockTileEntity = tileEntity.getCenter();
			if(multiblockTileEntity instanceof PC_ConduitEnergyTileEntity){
				PC_EnergyGrid og = ((PC_ConduitEnergyTileEntity)multiblockTileEntity).getGrid();
				og.remove((PC_ConduitEnergyTileEntity)multiblockTileEntity);
				((PC_ConduitEnergyTileEntity)multiblockTileEntity).setGrid(grid);
				((PC_ConduitEnergyTileEntity)multiblockTileEntity).addToGrid();
				for(PC_Direction dir:PC_Direction.values()){
					PC_EnergyGrid g = getGrid(world, pos.x + dir.offsetX, pos.y + dir.offsetY, pos.z + dir.offsetZ);
					if(g!=null && g!=grid){
						PC_Vec3I p = new PC_Vec3I(pos.x + dir.offsetX, pos.y + dir.offsetY, pos.z + dir.offsetZ);
						if(!toLookAt.contains(p)){
							toLookAt.add(p);
						}
					}
				}
			}
		}
	}
	
	private void addToGrid(){
		if(!isClient()){
			getGridIfNull();
			int num = 0;
			boolean isIO=false;
			for(PC_Direction dir:PC_Direction.VALID_DIRECTIONS){
				if(((connections>>(dir.ordinal()*2)) & 0x3) != 0){
					num++;
					if(((connections>>(dir.ordinal()*2)) & 0x3)==2){
						isIO = true;
					}
				}
			}
			if(isIO){
				grid.addIO(this);
			}else if(num==2){
				grid.addNode(this);
			}else{
				grid.add(this);
			}
		}
	}
	
	private static PC_EnergyGrid getGrid(World world, int x, int y, int z){
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if(te instanceof PC_TileEntityMultiblock){
			PC_TileEntityMultiblock tileEntity = (PC_TileEntityMultiblock)te;
			PC_MultiblockTileEntity multiblockTileEntity = tileEntity.getCenter();
			if(multiblockTileEntity instanceof PC_ConduitEnergyTileEntity){
				return ((PC_ConduitEnergyTileEntity)multiblockTileEntity).getGrid();
			}
		}
		return null;
	}

	@Override
	public PC_EnergyGrid getGrid() {
		return grid;
	}

	@Override
	public void setGrid(PC_EnergyGrid grid) {
		this.grid = grid;
	}
	
	@Override
	public void loadFromNBT(NBTTagCompound nbtCompoundTag) {
		super.loadFromNBT(nbtCompoundTag);
		type = nbtCompoundTag.getInteger("type");
	}

	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		super.saveToNBT(nbtCompoundTag);
		nbtCompoundTag.setInteger("type", type);
	}

	@Override
	public Icon getNormalConduitIcon() {
		return PC_ConduitEnergyItem.getData(type).iconNormal;
	}

	@Override
	public Icon getCornerConduitIcon() {
		return PC_ConduitEnergyItem.getData(type).iconCorner;
	}

	@Override
	public Icon getConnectionConduitIcon() {
		return PC_ConduitEnergyItem.getData(type).iconConnection;
	}

	public void addEnergyInterfaces(List<PC_IEnergyConsumer> consumers, List<PC_IEnergyProvider> providers, List<PC_IEnergyPuffer> puffers) {
		for(PC_Direction dir:PC_Direction.VALID_DIRECTIONS){
			if(((connections>>(dir.ordinal()*2)) & 0x3)==2){
				TileEntity te = PC_Utils.getTE(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord + dir.offsetZ);
				if(te instanceof PC_IEnergyPuffer){
					puffers.add((PC_IEnergyPuffer) te);
				}else if(te instanceof PC_IEnergyConsumer){
					consumers.add((PC_IEnergyConsumer) te);
				}else if(te instanceof PC_IEnergyProvider){
					providers.add((PC_IEnergyProvider) te);
				}
			}
		}
	}
	
	@Override
	public void onLoaded() {
		getGridIfNull();
	}
	
	@Override
	public void update() {
		if(!isClient()){
			getGridIfNull();
			grid.onUpdateTick(this);
		}
	}
	
}
