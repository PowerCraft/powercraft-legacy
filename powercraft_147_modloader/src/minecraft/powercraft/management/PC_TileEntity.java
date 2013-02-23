package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.annotation.PC_ClientServerSync;
import powercraft.management.reflect.PC_FieldWithAnnotation;
import powercraft.management.reflect.PC_IFieldAnnotationIterator;
import powercraft.management.reflect.PC_ReflectHelper;

public abstract class PC_TileEntity extends TileEntity
{

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
    
    public Field getSyncFieldWithName(final String name){
    	System.out.println(getClass() + ":" + this);
    	List<Field> l = PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_ClientServerSync.class, new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

			@Override
			public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
				String fieldName = fieldWithAnnotation.getAnnotation().name();
				if(fieldName.equals("")){
					fieldName = fieldWithAnnotation.getFieldName();
				}
				System.out.println("need: "+name+" get "+fieldName);
				if(fieldName.equals(name))
					return true;
				return false;
			}
		});
    	if(l.size()>0){
    		return l.get(0);
    	}
    	return null;
    }
    
    public void setData(PC_Struct2<String, Object>[] o){
    	for(int i=0; i<o.length; i++){
    		if(o[i].a.equals("call")){
    			PC_Struct2<String, Object> s = (PC_Struct2<String, Object>)o[i].b;
    			onCall(s.a, s.b);
    		}else{
    			dataChange(o[i].a, o[i].b);
    			Field f = getSyncFieldWithName(o[i].a);
    			f.setAccessible(true);
    			try {
					f.set(this, o[i].b);
				} catch (Exception e) {
					e.printStackTrace();
				} 
    		}
    	}
    	dataRecieved();
    }

    protected void notifyChanges(final String...fields){
    	final List<PC_Struct2<String, Object>> data = new ArrayList<PC_Struct2<String, Object>>();
    	PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_ClientServerSync.class, new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

			@Override
			public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
				String fieldName = fieldWithAnnotation.getAnnotation().name();
				if(fieldName.equals("")){
					fieldName = fieldWithAnnotation.getFieldName();
				}
				for(String name:fields){
					if(fieldName.equals(name)){
						data.add(new PC_Struct2<String, Object>(name, fieldWithAnnotation.getValue()));
						break;
					}
				}
				return false;
			}
		});
    	PC_PacketHandler.setTileEntity(this, data.toArray(new PC_Struct2[0]));
    }
    
    protected void dataRecieved(){}
    
    protected void dataChange(String key, Object value){}
    
    protected void onCall(String key, Object value){}
    
    public PC_Struct2<String, Object>[] getData(){
    	final List<PC_Struct2<String, Object>> data = new ArrayList<PC_Struct2<String, Object>>();
    	PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_ClientServerSync.class, new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

			@Override
			public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
				String fieldName = fieldWithAnnotation.getAnnotation().name();
				if(fieldName.equals("")){
					fieldName = fieldWithAnnotation.getFieldName();
				}
				data.add(new PC_Struct2<String, Object>(fieldName, fieldWithAnnotation.getValue()));
				return false;
			}
		});
    	return data.toArray(new PC_Struct2[0]);
    }

    public void call(String key, Object value){
    	onCall(key, value);
    	PC_PacketHandler.setTileEntity(this, new PC_Entry("call", new PC_Entry(key, value)));
    	dataRecieved();
    }
    
    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
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
		final NBTTagCompound nbtTag = nbtTagCompound.getCompoundTag("map");
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_ClientServerSync.class, new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

			@Override
			public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
				if(fieldWithAnnotation.getAnnotation().save()){
					String fieldName = fieldWithAnnotation.getAnnotation().name();
					if(fieldName.equals("")){
						fieldName = fieldWithAnnotation.getFieldName();
					}
					fieldWithAnnotation.setValue(SaveHandler.loadFromNBT(nbtTag, fieldName));
				}
				return false;
			}
		});
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		final NBTTagCompound nbtTag = new NBTTagCompound();
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_ClientServerSync.class, new PC_IFieldAnnotationIterator<PC_ClientServerSync>() {

			@Override
			public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_ClientServerSync> fieldWithAnnotation) {
				if(fieldWithAnnotation.getAnnotation().save()){
					String fieldName = fieldWithAnnotation.getAnnotation().name();
					if(fieldName.equals("")){
						fieldName = fieldWithAnnotation.getFieldName();
					}
					SaveHandler.saveToNBT(nbtTag, fieldName, fieldWithAnnotation.getValue());
				}
				return false;
			}
		});
		nbtTagCompound.setCompoundTag("map", nbtTag);
	}
	
}
