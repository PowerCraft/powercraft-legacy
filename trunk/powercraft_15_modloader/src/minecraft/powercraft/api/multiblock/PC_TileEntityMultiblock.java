package powercraft.api.multiblock;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;

public class PC_TileEntityMultiblock extends PC_TileEntity implements PC_IMultiblock {

	@PC_ClientServerSync(clientChangeAble=false)
	private PC_FractionBlock fractions[] = new PC_FractionBlock[27];
	
	public boolean renderBlock(Object renderer) {
		for(int i=0; i<fractions.length; i++){
			if(fractions[i]!=null)
				fractions[i].renderFraction(this, PC_FractionSide.fromIndex(i), renderer);
		}
		return true;
	}

	public boolean onBlockActivated(EntityPlayer entityPlayer, int dir, float xHit, float yHit, float zHit) {
		
		return false;
	}

	public AxisAlignedBB getCollisionBox(PC_FractionSide side) {
		if(fractions[side.index]!=null){
			AxisAlignedBB collisionBox = fractions[side.index].getCollisionBox(this, side, null);
			if(collisionBox!=null){
				collisionBox.offset(xCoord, yCoord, zCoord);
			}
			return collisionBox;
		}
		return null;
	}
	
	public void addCollisionBoxesToList(AxisAlignedBB aabb, List list, Entity entity) {
		for(int i=0; i<fractions.length; i++){
			if(fractions[i]!=null){
				AxisAlignedBB collisionBox = fractions[i].getCollisionBox(this, PC_FractionSide.fromIndex(i), entity);
				if(collisionBox!=null){
					collisionBox.offset(xCoord, yCoord, zCoord);
					if(aabb==null||aabb.intersectsWith(collisionBox)){
						list.add(collisionBox);
					}
				}
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(int id) {
		for(int i=0; i<fractions.length; i++){
			if(fractions[i]!=null){
				fractions[i].onNeighborBlockChange(this, PC_FractionSide.fromIndex(i), id);
			}
		}
		notifyChanges("fractions");
	}

	@Override
	public boolean addFraction(ItemStack itemStack, PC_Direction dir, float xHit, float yHit, float zHit, PC_FractionBlock fractionBlock) {
		int index = PC_FractionSide.getFractionSide(dir.mirror()).index;
		if(fractions[index]==null){
			fractions[index] = fractionBlock;
			fractionBlock.onNeighborBlockChange(this, PC_FractionSide.fromIndex(index), 0);
		}else if(fractions[index].canMixWithFraction(fractionBlock)){
			fractions[index] = fractions[index].mixWithFraction(fractionBlock);
			onNeighborBlockChange(0);
		}
		PC_Utils.hugeUpdate(worldObj, getCoord());
		notifyChanges("fractions");
		return true;
	}

	@Override
	public PC_FractionBlock getFractionOnSide(PC_FractionSide side) {
		return fractions[side.index];
	}

	@Override
	protected void dataRecieved() {
		PC_Utils.markBlockForUpdate(worldObj, getCoord());
	}

	public boolean removeFraction(PC_FractionSide side) {
		fractions[side.index] = null;
		for(int i=0; i<fractions.length; i++){
			if(fractions[i]!=null)
				return false;
		}
		return true;
	}
	
	
	
}
