package powercraft.api.multiblocks.cable.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.cable.PC_CableTileEntity;

public class PC_RedstoneIsolatedTileEntity extends PC_CableTileEntity {

	private PC_RedstoneCable cable[] = new PC_RedstoneCable[16];
	
	public PC_RedstoneIsolatedTileEntity() {
		super(2, 4);
	}
	
	public PC_RedstoneIsolatedTileEntity(int cableType) {
		super(2, 4);
		cable[cableType] = new PC_RedstoneCable(1<<cableType);
	}

	@Override
	protected PC_IRedstoneCable getCableType(int cableID){
		return cable[cableID];
	}

	@Override
	public boolean canMixWith(PC_MultiblockTileEntity tileEntity) {
		if(tileEntity instanceof PC_RedstoneIsolatedTileEntity){
			PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity)tileEntity;
			for(int i=0; i<cable.length; i++){
				if(cable[i]!=null && redstoneIsolated.cable[i]!=null)
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public PC_MultiblockTileEntity mixWith(PC_MultiblockTileEntity tileEntity) {
		PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity)tileEntity;
		for(int i=0; i<cable.length; i++){
			if(cable[i]==null && redstoneIsolated.cable[i]!=null){
				cable[i] = redstoneIsolated.cable[i];
				cable[i].setTileEntity(this);
			}
		}
		calculateThickness();
		return this;
	}

	@Override
	protected Icon getCableIcon() {
		return PC_RedstoneIsolatedItem.getCableIcon();
	}

	@Override
	protected Icon getCableLineIcon(int index) {
		return null;
	}
	
	@Override
	public List<ItemStack> getDrop() {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(PC_RedstoneIsolatedItem.item, 1, 0));
		return drops;
	}

	@Override
	public void loadFromNBT(NBTTagCompound nbtCompoundTag) {
		int mask = nbtCompoundTag.getInteger("mask");
		for(int i=0; i<cable.length; i++){
			if((mask & (1<<i))==0){
				cable[i] = null;
			}else{
				cable[i] = new PC_RedstoneCable(1<<i);
			}
		}
		calculateThickness();
		super.loadFromNBT(nbtCompoundTag);
	}

	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		nbtCompoundTag.setInteger("mask", getMask());
		super.saveToNBT(nbtCompoundTag);
	}
	
	private void calculateThickness(){
		thickness = 2+getCableCount()/8;
		width = thickness*2;
	}

	public int getCableCount() {
		int num=0;
		for(int i=0; i<cable.length; i++){
			if(cable[i]!=null){
				num++;
			}
		}
		return num;
	}
	
	public int getMask() {
		int mask=0;
		for(int i=0; i<cable.length; i++){
			if(cable[i]!=null){
				mask|=1<<i;
			}
		}
		return mask;
	}
	
	@Override
	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock) {
		if(multiblock instanceof PC_RedstoneIsolatedTileEntity){
			PC_RedstoneIsolatedTileEntity isolated = (PC_RedstoneIsolatedTileEntity)multiblock;
			int connection = getMask()&isolated.getMask();
			if(connection!=0){
				int length = 16;
				if(isolated.getCenterThickness()>0)
					length = isolated.getCenterThickness()+2+isolated.getThickness()*2;
				return connection | (length<<16);
			}
		}else if(multiblock instanceof PC_RedstoneUnisolatedTileEntity){
			PC_RedstoneUnisolatedTileEntity unisolated = (PC_RedstoneUnisolatedTileEntity)multiblock;
			if(getCableCount()==1){
				int connection = getMask();
				int length = 16;
				if(unisolated.getCenterThickness()>0)
					length = unisolated.getCenterThickness()+2+unisolated.getThickness()*2;
				return connection | (length<<16);
			}
		}
		return 0;
	}
	
}
