package powercraft.api.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.api.PC_Entry;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_Struct2;
import powercraft.api.PC_Utils.SaveHandler;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.reflect.PC_FieldWithAnnotation;
import powercraft.api.reflect.PC_IFieldAnnotationIterator;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.launcher.PC_Logger;

public abstract class PC_TileEntity extends TileEntity {

	private List<PC_ITileEntityWatcher> watcher = new ArrayList<PC_ITileEntityWatcher>();
	protected String inventoryTitle;
	protected int slotsCount;
	protected ItemStack inventoryContents[];
	
	protected PC_TileEntity() {
		inventoryTitle = "";
		slotsCount = 0;
		inventoryContents = null;
	}
	
	protected PC_TileEntity(String title, int size) {
		inventoryTitle = title;
		slotsCount = size;
		inventoryContents = new ItemStack[slotsCount];
	}
	
	@Override
	public Packet getDescriptionPacket() {
		PC_Struct2<String, Object>[] o = getData();

		if (o == null) {
			return null;
		}

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream sendData;

		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
			sendData.writeInt(xCoord);
			sendData.writeInt(yCoord);
			sendData.writeInt(zCoord);
			sendData.writeObject(o);
			sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Packet250CustomPayload("PowerCraft", data.toByteArray());
	}

	public PC_VecI getCoord() {
		return new PC_VecI(xCoord, yCoord, zCoord);
	}

	public Field getSyncFieldWithName(final String name) {
		List<Field> l = PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(),
				this, PC_ClientServerSync.class,
				new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

					@Override
					public boolean onFieldWithAnnotation(
							PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
						if (!worldObj.isRemote
								&& !fieldWithAnnotation.getAnnotation()
										.clientChangeAble())
							return false;
						String fieldName = fieldWithAnnotation.getAnnotation()
								.name();
						if (fieldName.equals("")) {
							fieldName = fieldWithAnnotation.getFieldName();
						}
						if (fieldName.equals(name))
							return true;
						return false;
					}
				});
		if (l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	public void setData(PC_Struct2<String, Object>[] o) {
		for (int i = 0; i < o.length; i++) {
			if (o[i].a.equals("call")) {
				PC_Struct2<String, Object> s = (PC_Struct2<String, Object>) o[i].b;
				onCall(s.a, s.b);
			} else {
				dataChange(o[i].a, o[i].b);
				Field f = getSyncFieldWithName(o[i].a);
				if (f != null) {
					f.setAccessible(true);
					try {
						f.set(this, o[i].b);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					PC_Logger.warning(this
							+ ":Try to write non existing Field " + o[i].a);
				}
			}
		}
		dataRecieved();
	}

	protected void notifyChanges(final String... fields) {
		final List<PC_Struct2<String, Object>> data = new ArrayList<PC_Struct2<String, Object>>();
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this,
				PC_ClientServerSync.class,
				new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

					@Override
					public boolean onFieldWithAnnotation(
							PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
						if (worldObj.isRemote
								&& !fieldWithAnnotation.getAnnotation()
										.clientChangeAble())
							return false;
						String fieldName = fieldWithAnnotation.getAnnotation()
								.name();
						if (fieldName.equals("")) {
							fieldName = fieldWithAnnotation.getFieldName();
						}
						for (String name : fields) {
							if (fieldName.equals(name)) {
								data.add(new PC_Struct2<String, Object>(name,
										fieldWithAnnotation.getValue()));
								break;
							}
						}
						return false;
					}
				});
		PC_PacketHandler.setTileEntity(this, data.toArray(new PC_Struct2[0]));
	}

	protected void dataRecieved() {
	}

	protected void dataChange(String key, Object value) {
	}

	protected void onCall(String key, Object value) {
	}

	public PC_Struct2<String, Object>[] getData() {
		final List<PC_Struct2<String, Object>> data = new ArrayList<PC_Struct2<String, Object>>();
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this,
				PC_ClientServerSync.class,
				new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

					@Override
					public boolean onFieldWithAnnotation(
							PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
						String fieldName = fieldWithAnnotation.getAnnotation()
								.name();
						if (fieldName.equals("")) {
							fieldName = fieldWithAnnotation.getFieldName();
						}
						data.add(new PC_Struct2<String, Object>(fieldName,
								fieldWithAnnotation.getValue()));
						return false;
					}
				});
		return data.toArray(new PC_Struct2[0]);
	}

	public void call(String key, Object value) {
		onCall(key, value);
		PC_PacketHandler.setTileEntity(this, new PC_Entry("call", new PC_Entry(
				key, value)));
		dataRecieved();
	}

	public void create(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
	}

	protected void notifyWatcher(String key, Object value) {
		for (PC_ITileEntityWatcher w : watcher) {
			w.keyChange(key, value);
		}
	}

	public void addTileEntityWatcher(PC_ITileEntityWatcher w) {
		if (!watcher.contains(w)) {
			watcher.add(w);
		}
	}

	public void removeTileEntityWatcher(PC_ITileEntityWatcher w) {
		if (watcher.contains(w)) {
			watcher.remove(w);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		if(this instanceof IInventory){
			PC_InventoryUtils.saveInventoryToNBT(nbtTagCompound, "inventory", (IInventory)this);
		}
		final NBTTagCompound nbtTag = nbtTagCompound.getCompoundTag("map");
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this,
				PC_ClientServerSync.class,
				new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

					@Override
					public boolean onFieldWithAnnotation(
							PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
						if (fieldWithAnnotation.getAnnotation().save()) {
							String fieldName = fieldWithAnnotation
									.getAnnotation().name();
							if (fieldName.equals("")) {
								fieldName = fieldWithAnnotation.getFieldName();
							}
							Object o = SaveHandler.loadFromNBT(nbtTag,
									fieldName);
							fieldWithAnnotation.setValue(o);
						}
						return false;
					}
				});
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		if(this instanceof IInventory){
			PC_InventoryUtils.loadInventoryFromNBT(nbtTagCompound, "inventory", (IInventory)this);
		}
		final NBTTagCompound nbtTag = new NBTTagCompound();
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this,
				PC_ClientServerSync.class,
				new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

					@Override
					public boolean onFieldWithAnnotation(
							PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
						if (fieldWithAnnotation.getAnnotation().save()) {
							String fieldName = fieldWithAnnotation
									.getAnnotation().name();
							if (fieldName.equals("")) {
								fieldName = fieldWithAnnotation.getFieldName();
							}
							SaveHandler.saveToNBT(nbtTag, fieldName,
									fieldWithAnnotation.getValue());
						}
						return false;
					}
				});
		nbtTagCompound.setCompoundTag("map", nbtTag);
	}

	public World getWorldObj() {
		return this.worldObj;
	}


	
	//////////////////////////////////////////////////////////////
	////// INVENTORY
	//////////////////////////////////////////////////////////////
	

	public int getSizeInventory() {
		return slotsCount;
	}

	public ItemStack getStackInSlot(int i) {
		return inventoryContents[i];
	}

	public ItemStack decrStackSize(int i, int j) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack;

			if (this.inventoryContents[i].stackSize <= j) {
				itemstack = this.inventoryContents[i];
				this.inventoryContents[i] = null;
				this.onInventoryChanged();
				return itemstack;
			} else {
				itemstack = this.inventoryContents[i].splitStack(j);

				if (this.inventoryContents[i].stackSize == 0) {
					this.inventoryContents[i] = null;
				}

				this.onInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack = this.inventoryContents[i];
			this.inventoryContents[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.inventoryContents[i] = itemstack;

		if (itemstack != null
				&& itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	public String getInvName() {
		return inventoryTitle;
	}

	public boolean func_94042_c() {
		return false;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	public void openChest() {
	}

	public void closeChest() {
	}
	
	public boolean func_94041_b(int i, ItemStack itemstack) {
		return true;
	}

	public int func_94127_c(int side) {
		return 0;
	}

	public int func_94128_d(int side) {
		return getSizeInventory();
	}

	public boolean canPlayerTakeStack(int i, EntityPlayer entityPlayer) {
		return true;
	}

	public boolean canMachineInsertStackTo(int i, ItemStack itemstack) {
		return true;
	}

	public boolean canDispenseStackFrom(int i) {
		return true;
	}

	public boolean canDropStackFrom(int i) {
		return true;
	}

	public int getSlotStackLimit(int i) {
		return getInventoryStackLimit();
	}

	
	
	//////////////////////////////////////////////////////////////
	////// BLOCK METHODS
	//////////////////////////////////////////////////////////////
	
	
	public void onBreakBlock() {
		if(this instanceof IInventory)
			PC_InventoryUtils.dropInventoryContents((IInventory)this, worldObj, getCoord());
	}
	
	
	
	
}
