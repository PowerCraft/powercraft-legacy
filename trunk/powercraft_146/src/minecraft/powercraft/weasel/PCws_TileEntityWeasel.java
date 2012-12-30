package powercraft.weasel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_MathHelper;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;

public class PCws_TileEntityWeasel extends PC_TileEntity implements PC_ITileEntityRenderer{

	private int pluginID = -1;
	private HashMap<String, Object> datas = new HashMap<String, Object>();
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		int type = stack.getItemDamage();
		PCws_WeaselPluginInfo wpi = PCws_WeaselManager.getPluginInfo(type);
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
			setData("specialRot", nbtTag.getDouble("specialRot"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		nbtTag.setInteger("pluginID", pluginID);
		nbtTag.setInteger("type", getType());
		if(getData("specialRot")!=null){
			nbtTag.setDouble("specialRot", (Double)getData("specialRot"));
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
		if(worldObj.isRemote)
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
		List<Object> l = new ArrayList<Object>();
		for(Entry<String, Object> data:datas.entrySet()){
			l.add(data.getKey());
			l.add(data.getValue());
		}
		return l.toArray();
	}
	
	
	
}
