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
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;

public class PCws_TileEntityWeasel extends PC_TileEntity implements PC_ITileEntityRenderer, IInventory, PC_ISpecialAccessInventory{

	private int pluginID = -1;
	private HashMap<String, Object> datas = new HashMap<String, Object>();
	private ItemStack inv[];
	
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
		datas.put("type", nbtTag.getInteger("type"));
		if(nbtTag.hasKey("specialRot")){
			datas.put("specialRot", nbtTag.getInteger("specialRot"));
		}
		if(nbtTag.hasKey("invSize")){
			inv = new ItemStack[nbtTag.getInteger("invSize")];
			PC_InvUtils.loadInventoryFromNBT(nbtTag, "inv", this);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		nbtTag.setInteger("pluginID", pluginID);
		nbtTag.setInteger("type", getType());
		if(getData("specialRot")!=null){
			nbtTag.setInteger("specialRot", (Integer)getData("specialRot"));
		}
		if(inv!=null && inv.length!=0){
			nbtTag.setInteger("invSize", inv.length);
			PC_InvUtils.saveInventoryToNBT(nbtTag, "inv", this);
		}
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
		return PCws_WeaselManager.getPlugin(pluginID);
	}

	public int getType(){
		if(getData("type")==null)
			return 0;
		return (Integer)getData("type");
	}
	
	public PCws_WeaselPluginInfo getPluginInfo(){
		return PCws_WeaselManager.getPluginInfo(getType());
	}
	
	public Object getData(String key) {
		return datas.get(key);
	}
	
	public void setData(String key, Object obj) {
		datas.put(key, obj);
		PC_PacketHandler.setTileEntity(this, key, obj);
	}
	
	public void setDataNoSend(String key, Object obj) {
		datas.put(key, obj);
	}
	
	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {
		getPluginInfo().renderPluginAt(this, x, y, z, rot);
	}

	@Override
	public void setData(Object[] o) {
		int p = 0;
		PCws_WeaselPlugin plugin = getPlugin();
        while (p < o.length)
        {
            String var = (String)o[p++];
            if(var.equals("msg")){
            	String msg = (String)o[p++];
            	Object obj = o[p++];
            	if(plugin!=null)
            		plugin.getClientMsg(msg, obj);
            	else if(worldObj.isRemote){
            		getPluginInfo().getServerMsg(this, msg, obj);
            	}
            		
            }else if(var.equals("invSize")){
            	inv = new ItemStack[(Integer)o[p++]];
            }else{
            	Object obj = o[p++];
            	datas.put(var, obj);
            	if(plugin!=null)
            		plugin.reciveData(var, obj);
            }
        }
	}

	@Override
	public Object[] getData() {
		int i=0;
		List<List<Object>> l = new ArrayList<List<Object>>();
		List<Object> l1 = new ArrayList<Object>();
		l.add(l1);
		if(inv!=null){
			l1.add("invSize");
			l1.add(inv.length);
		}
		for(Entry<String, Object> data:datas.entrySet()){
			l1.add(data.getKey());
			l1.add(data.getValue());
			i++;
			if(i>200){
				l1 = new ArrayList<Object>();
				l.add(l1);
				i=0;
			}
		}
		for(i=1; i<l.size(); i++){
			PC_PacketHandler.setTileEntity(this, l.get(i).toArray());
		}
		return l.get(0).toArray();
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return PC_InvUtils.insetItemTo(stack, this, 0, getPluginInfo().inventorySize()-1)==0;
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		return stack.itemID==PCws_App.weaselDisk.shiftedIndex;
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
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inv[i] != null)
        {
            if (inv[i].stackSize <= j)
            {
                ItemStack itemstack = inv[i];
                inv[i] = null;
                if(getPlugin() instanceof PCws_IWeaselInventory)
                	((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(i, null);
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = inv[i].splitStack(j);

            if (inv[i].stackSize == 0)
            {
            	inv[i] = null;
            }

            if(getPlugin() instanceof PCws_IWeaselInventory)
            	((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(i, inv[i]);
            
            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (inv[var1] != null)
        {
            ItemStack itemstack = inv[var1];
            inv[var1] = null;
            if(getPlugin() instanceof PCws_IWeaselInventory)
            	((PCws_IWeaselInventory)getPlugin()).setInventorySlotContents(var1, null);
            return itemstack;
        }
        else
        {
            return null;
        }
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
	
	
	
}
