package powercraft.itemstorage;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Entry;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecF;
import powercraft.api.PC_VecI;

public class PCis_EntityItemInBigChest extends EntityItem {

	private PC_VecI mid;
	private int slot;
	private PC_VecF move = new PC_VecF();

	public PCis_EntityItemInBigChest(World world, PC_VecF pos, PC_VecI mid, PC_VecF move, int slot) {
		super(world, pos.x, pos.y, pos.z);
		this.slot = slot;
		this.mid = mid.copy();
		this.move = move.copy();
	}
	
	@Override
	public void onUpdate(){
		move.add(new PC_VecF(mid).sub((float)posX, (float)posY, (float)posZ));
		posX += move.x/100.0;
		posY += move.y/100.0;
		posZ += move.z/100.0;
		setVelocity(move.x/100.0, move.y/100.0, move.z/100.0);
		super.onUpdate();
		isDead = false;
	}
	
	@Override
	public float getCollisionBorderSize(){
		return 0.3f;
	}
	
	@Override
	public boolean combineItems(EntityItem entityItem){
		return false;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer){}
	
	@Override
	public boolean interact(EntityPlayer entityPlayer) {
		PC_PacketHandler.setTileEntity(getChest(), new PC_Entry("interact", new PC_Struct2<Integer, Integer>(entityPlayer.entityId, slot)));
		getInv().interact(entityPlayer, slot);
		return true;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public ItemStack func_92014_d() {
		if(getInv()==null){
			setDead();
			return new ItemStack(0, 0, 0);
		}
		ItemStack is = getInv().getStackInSlot(slot);
		if(is==null){
			setDead();
			return new ItemStack(0, 0, 0);
		}
		return is;
	}

	private PCis_BigChestInventory getInv(){
		PCis_TileEntityBigChest chest = getChest();
		if(chest==null)
			return null;
		return chest.getInventory();
	}
	
	private PCis_TileEntityBigChest getChest(){
		return GameInfo.getTE(worldObj, mid.offset(-2));
	}
	
}
