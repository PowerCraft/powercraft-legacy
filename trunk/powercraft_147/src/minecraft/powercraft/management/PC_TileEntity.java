package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import powercraft.management.PC_Utils.SaveHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class PC_TileEntity extends TileEntity
{

	protected HashMap<String, Object> map = new HashMap<String, Object>();
	private List<PC_ITileEntityWatcher> watcher = new ArrayList<PC_ITileEntityWatcher>();
	
    @Override
    public Packet getDescriptionPacket()
    {
    	PC_Struct2<String, Object>[] o = getData();
        
        if (o == null)
        {
            return null;
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
            sendData.writeInt(xCoord);
            sendData.writeInt(yCoord);
            sendData.writeInt(zCoord);
           	sendData.writeObject(o);
            sendData.writeInt(PC_PacketHandler.PACKETTILEENTITY);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new Packet250CustomPayload("PowerCraft", data.toByteArray());
    }

    public PC_VecI getCoord()
    {
        return new PC_VecI(xCoord, yCoord, zCoord);
    }
    
    public void setData(PC_Struct2<String, Object>[] o){
    	for(int i=0; i<o.length; i++){
    		if(o[i].a.equals("call")){
    			PC_Struct2<String, Object> s = (PC_Struct2<String, Object>)o[i].b;
    			onCall(s.a, s.b);
    		}else{
    			dataChange(o[i].a, o[i].b);
    			map.put(o[i].a, o[i].b);
    		}
    	}
    	dataRecived();
    }

    protected void dataRecived(){}
    
    protected void dataChange(String key, Object value){}
    
    protected void onCall(String key, Object value){}
    
    public PC_Struct2<String, Object>[] getData(){
    	PC_Struct2<String, Object>[] data = new PC_Struct2[map.size()];
    	int i=0;
    	for(Entry<String, Object>e:map.entrySet()){
    		data[i] = new PC_Struct2<String, Object>(e.getKey(), e.getValue());
    		i++;
    	}
    	return data;
    }

    public void call(String key, Object value){
    	onCall(key, value);
    	PC_PacketHandler.setTileEntity(this, new PC_Entry("call", new PC_Entry(key, value)));
    	dataRecived();
    }
    
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
    }

    public void setData(String key, Object value){
    	map.put(key, value);
    	PC_PacketHandler.setTileEntity(this, new PC_Struct2<String, Object>(key, value));
    	dataRecived();
    }
    
    public Object getData(String key){
    	return map.get(key);
    }
    
    protected void notifyWatcher(String key, Object value){
    	for(PC_ITileEntityWatcher w:watcher){
    		w.keyChange(key, value);
    	}
    }
    
    public void addTileEntityWatcher(PC_ITileEntityWatcher w){
    	if(!watcher.contains(w)){
    		watcher.add(w);
    	}
    }
    
    public void removeTileEntityWatcher(PC_ITileEntityWatcher w){
    	if(watcher.contains(w)){
    		watcher.remove(w);
    	}
    }

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagCompound nbtTag = nbtTagCompound.getCompoundTag("map");
		int size = nbtTag.getInteger("count");
		for(int i=0; i<size; i++){
			map.put(nbtTag.getString("key["+i+"]"), SaveHandler.loadFromNBT(nbtTag, "value["+i+"]"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("count", map.size());
		int i=0;
		for(Entry<String, Object> e:map.entrySet()){
			nbtTag.setString("key["+i+"]", e.getKey());
			SaveHandler.saveToNBT(nbtTag, "value["+i+"]", e.getValue());
		}
		nbtTagCompound.setCompoundTag("map", nbtTag);
	}
	
}
