package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_Entry;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Inventory;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.inventory.PC_ISpecialAccessInventory;

public class PCws_TileEntityWeasel extends PC_TileEntity implements PC_ITileEntityRenderer, IInventory, PC_ISpecialAccessInventory{

	private HashMap<String, Object>map = new HashMap<String, Object>();
	private int pluginID = -1;
	private ItemStack inv[];
	
	public void setData(String key, Object value){
		map.put(key, value);
		PC_PacketHandler.setTileEntity(this, new PC_Struct2<String, Object>(key, value));
	}
	
	public Object getData(String key){
		return map.get(key);
	}
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		int type = stack.getItemDamage();
		PCws_WeaselPluginInfo wpi = PCws_WeaselManager.getPluginInfo(type);
		inv = new ItemStack[wpi.inventorySize()];
		setData("type", type);
		if(wpi.hasSpecialRot()){
			setData("specialRot", PC_MathHelper.floor_double(((player.rotationYaw + 180F) * 16F) / 360F + 0.5D) & 0xf);
		}
		if(!world.isRemote){
			PCws_WeaselPlugin plugin = wpi.createPlugin();
			pluginID = plugin.getID();
			plugin.setPlace(world, x, y, z);
			plugin.sync(this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag) {
		super.readFromNBT(nbtTag);
		pluginID = nbtTag.getInteger("pluginID");
		if(nbtTag.hasKey("invSize")){
			inv = new ItemStack[nbtTag.getInteger("invSize")];
			Inventory.loadInventoryFromNBT(nbtTag, "inv", this);
		}
		NBTTagCompound nbtTagCompound = nbtTag.getCompoundTag("weaselMap");
		int size = nbtTagCompound.getInteger("count");
		for(int i=0; i<size; i++){
			String key = nbtTagCompound.getString("key["+i+"]");
			Object value = SaveHandler.loadFromNBT(nbtTagCompound, "value["+i+"]");
			map.put(key, value);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		nbtTag.setInteger("pluginID", pluginID);
		if(inv!=null && inv.length!=0){
			nbtTag.setInteger("invSize", inv.length);
			Inventory.saveInventoryToNBT(nbtTag, "inv", this);
		}
		NBTTagCompound nbtTagCompound = new NBTTagCompound("weaselMap");
		nbtTagCompound.setInteger("count", map.size());
		int i=0;
		for(Entry<String, Object>e:map.entrySet()){
			nbtTagCompound.setString("key["+i+"]", e.getKey());
			SaveHandler.saveToNBT(nbtTagCompound, "value["+i+"]", e.getValue());
			i++;
		}
		nbtTag.setCompoundTag("weaselMap", nbtTagCompound);
	}

	@Override
	public void setWorldObj(World world) {
		super.setWorldObj(world);
		if(!world.isRemote){
			PCws_WeaselPlugin pugin = getPlugin();
			if(pugin!=null)
				pugin.sync(this);
		}
	}

	public PCws_WeaselPlugin getPlugin(){
		if(worldObj == null || worldObj.isRemote)
			return null;
		return (PCws_WeaselPlugin)PCws_WeaselManager.getPlugin(pluginID);
	}

	public int getType(){
		if(getData("type")==null)
			return 0;
		return (Integer)getData("type");
	}
	
	public PCws_WeaselPluginInfo getPluginInfo(){
		return PCws_WeaselManager.getPluginInfo(getType());
	}
	
	public void setDataNoSend(String key, Object obj) {
		map.put(key, obj);
	}
	
	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		getPluginInfo().renderPluginAt(this, x, y, z, rot);
	}
	
	@Override
	protected void dataChange(String key, Object value) {
		PCws_WeaselPlugin plugin = getPlugin();
		if(key.equals("invSize")){
			inv = new ItemStack[(Integer)value];
		}else if(plugin!=null){
			plugin.reciveData(key, value);
		}
	}

	@Override
	public void call(String key, Object value){
    	PC_PacketHandler.setTileEntity(this, new PC_Entry("call", new PC_Entry(key, value)));
    }
	
	@Override
	protected void onCall(String key, Object value) {
		PCws_WeaselPlugin plugin = getPlugin();
		if(plugin!=null)
    		plugin.getClientMsg(key, value);
    	else if(worldObj.isRemote){
    		getPluginInfo().getServerMsg(this, key, value);
    	}
	}

	@Override
	public void setData(PC_Struct2<String, Object>[] data) {
		for(int i=0; i<data.length; i++){
    		if(data[i].a.equals("call")){
    			PC_Struct2<String, Object> s = (PC_Struct2<String, Object>)data[i].b;
    			onCall(s.a, s.b);
    		}else{
    			map.put(data[i].a, data[i].b);
    			dataChange(data[i].a, data[i].b);
    		}
    	}
    	dataRecieved();
	}
	
	@Override
	public PC_Struct2<String, Object>[] getData() {
		int i=0;
		List<List<PC_Struct2<String, Object>>> l = new ArrayList<List<PC_Struct2<String, Object>>>();
		List<PC_Struct2<String, Object>> l1 = new ArrayList<PC_Struct2<String, Object>>();
		l.add(l1);
		if(inv!=null){
			l1.add(new PC_Entry("invSize", inv.length));
		}
		for(Entry<String, Object> data:map.entrySet()){
			l1.add(new PC_Entry(data.getKey(), data.getValue()));
			i++;
			if(i>200){
				l1 = new ArrayList<PC_Struct2<String, Object>>();
				l.add(l1);
				i=0;
			}
		}
		for(i=1; i<l.size(); i++){
			PC_PacketHandler.setTileEntity(this, l.get(i).toArray(new PC_Struct2[0]));
		}
		return l.get(0).toArray(new PC_Struct2[0]);
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return Inventory.insetItemTo(stack, this, 0, getPluginInfo().inventorySize()-1)==0;
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		return stack.itemID==PCws_App.weaselDisk.itemID;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return canPlayerInsertStackTo(slot, stack);
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return true;
	}

	@Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}

	@Override
	public int getSizeInventory() {
		if(inv==null)
			return 0;
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack is = inv[i];
		if(is != null)
			if(getPlugin() instanceof PCws_IWeaselInventory)
				((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(i, null);
		inv[i] = null;
		return is;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		ItemStack itemstack = inv[var1];
		inv[var1] = null;
		if(getPlugin() instanceof PCws_IWeaselInventory)
			((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(var1, null);
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv[var1] = var2;
		if(getPlugin() instanceof PCws_IWeaselInventory)
			((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(var1, var2);
	}

	@Override
	public String getInvName() {
		return getClass().getSimpleName();
	}

	@Override
	public int getInventoryStackLimit() {
		return getPluginInfo().inventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return getInventoryStackLimit();
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}
	
	
	
}
