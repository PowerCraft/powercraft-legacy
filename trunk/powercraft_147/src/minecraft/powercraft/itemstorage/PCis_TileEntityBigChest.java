package powercraft.itemstorage;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.management.PC_Color;
import powercraft.management.PC_Entry;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.inventory.PC_IInventoryWrapper;
import powercraft.management.recipes.PC_3DRecipe;
import powercraft.management.PC_VecF;
import powercraft.management.PC_VecI;

public class PCis_TileEntityBigChest extends PC_TileEntity implements PC_IInventoryWrapper {

	public static final int TOPBACKLEFT=0, TOPBACKRIGHT=1, TOPFRONTLEFT=2, TOPFRONTRIGHT=3, BOTTOMBACKLEFT=4, BOTTOMBACKRIGHT=5, BOTTOMFRONTLEFT=6, BOTTOMFRONTRIGHT=7;
	
	private static PC_3DRecipe recipe = new PC_3DRecipe(null, 
				new String[]{
				"g  g",
				"    ",
				"    ",
				"g  g"},
				new String[]{
				"f  f",
				"    ",
				"    ",
				"f  f"},
				new String[]{
				"f  f",
				"    ",
				"    ",
				"f  f"},
				new String[]{
				"g  g",
				"    ",
				"    ",
				"g  g"},
				'g', PCis_App.bigChest, 'f', Block.fence, ' ', null);
	
	private int pos;
	private PCis_BigChestInventory inv;
	
	
	@Override
	public PCis_BigChestInventory getInventory() {
		if(pos==BOTTOMBACKLEFT)
			return inv;
		else{
			PCis_TileEntityBigChest master = getMaster();
			if(master==null)
				return null;
			return master.getInventory();
		}
	}
	
	public PCis_TileEntityBigChest getMaster(){
		PC_VecI p = getCoord().copy();
		if(pos==TOPBACKLEFT || pos==TOPBACKRIGHT || pos==TOPFRONTLEFT || pos==TOPFRONTRIGHT){
			p.sub(0, 3, 0);
		}
		if(pos==TOPFRONTLEFT || pos==TOPFRONTRIGHT || pos==BOTTOMFRONTLEFT || pos==BOTTOMFRONTRIGHT){
			p.sub(0, 0, 3);
		}
		if(pos==TOPBACKRIGHT || pos==TOPFRONTRIGHT || pos==BOTTOMBACKRIGHT || pos==BOTTOMFRONTRIGHT){
			p.sub(3, 0, 0);
		}
		return GameInfo.getTE(worldObj, p);
	}
	
	public void setPos(int pos){
		this.pos = pos;
		if(pos==BOTTOMBACKLEFT){
			inv = new PCis_BigChestInventory(worldObj, getCoord().offset(2), this);
		}
	}
	
	@Override
	public void setWorldObj(World world) {
		super.setWorldObj(world);
		if(inv!=null){
			inv.setPos(worldObj, getCoord().offset(2));
		}
	}

	@Override
	public void updateEntity() {
		if(pos==BOTTOMBACKLEFT && !worldObj.isRemote){
			AxisAlignedBB bb=AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 4, yCoord + 4, zCoord + 4);
			List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, bb);
			for(EntityItem entity:list){
				inv.collectItem(entity);
			}
			if(recipe.getStructRotation(worldObj, getCoord())==-1){
				breakStruct();
			}
		}
		Random rand = new Random();
		PC_Color color = new PC_Color(0.7f + rand.nextFloat()*0.3f, rand.nextFloat()*0.3f, 0.2f+rand.nextFloat()*0.3f);
		ValueWriting.spawnParticle("PC_EntityFanFX", worldObj, new PC_VecF(getCoord()), new PC_VecF(rand.nextFloat()-0.5f, rand.nextFloat()-0.5f, rand.nextFloat()-0.5f),
				new PC_VecF(rand.nextFloat()-0.5f, rand.nextFloat()-0.5f, rand.nextFloat()-0.5f).div(10.0f), 0.05f + rand.nextFloat()*0.1f, color);
		
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		pos = nbtTagCompound.getInteger("pos");
		if(pos==BOTTOMBACKLEFT){
			inv = new PCis_BigChestInventory(this);
			PC_InvUtils.loadInventoryFromNBT(nbtTagCompound, "inv", inv);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("pos", pos);
		if(pos==BOTTOMBACKLEFT){
			PC_InvUtils.saveInventoryToNBT(nbtTagCompound, "inv", inv);
		}
	}

	public void breakStruct() {
		PCis_TileEntityBigChest master = getMaster();
		master.removeAllWithout();
	}
	
	private void removeAllWithout(){
		PC_VecI pos = getCoord();
		for(int x=0; x<=1; x++){
			for(int y=0; y<=1; y++){
				for(int z=0; z<=1; z++){
					if(GameInfo.getBID(worldObj, pos.offset(x*3, y*3, z*3))==PCis_App.bigChest.blockID){
						ValueWriting.setBID(worldObj, pos.offset(x*3, y*3, z*3), Block.glass.blockID, 0);
					}
				}
			}
		}

	}

	@Override
	public void setData(PC_Struct2<String, Object>[] data) {
		for(PC_Struct2<String, Object>d:data){
            if (d.a.equals("slotChange")){
            	PC_Struct2<Integer, byte[]>s = (PC_Struct2<Integer, byte[]>)d.b;
            	ItemStack is = null;
            	if(s.b!=null){
            		try {
						is = ItemStack.loadItemStackFromNBT(CompressedStreamTools.decompress(s.b));
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            	inv.setInventorySlotContents(s.a, is);
            }else if (d.a.equals("pos")){
            	setPos((Integer)d.b);
            }else if (d.a.equals("inv")){
            	byte b[] = (byte[])d.b;
            	try {
					NBTTagCompound nbtTag = CompressedStreamTools.decompress(b);
					PC_InvUtils.loadInventoryFromNBT(nbtTag, "inv", inv);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }else if (d.a.equals("interact")){
            	PC_Struct2<Integer, Integer>s = (PC_Struct2<Integer, Integer>)d.b;
            	EntityPlayer player = (EntityPlayer)worldObj.getEntityByID(s.a);
            	inv.interact(player, s.b);
            }
        }
	}

	@Override
	public PC_Struct2<String, Object>[] getData() {
		if(inv==null){
			return new PC_Struct2[]{
					new PC_Entry("pos", pos)
			};
		}
		NBTTagCompound nbtTag = new NBTTagCompound();
		PC_InvUtils.saveInventoryToNBT(nbtTag, "inv", inv);
		try {
			byte b[]= CompressedStreamTools.compress(nbtTag);
			return new PC_Struct2[]{
					new PC_Entry("pos", pos),
					new PC_Entry("inv", b)
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new PC_Struct2[]{
				new PC_Entry("pos", pos)
		};
	}
	
}
