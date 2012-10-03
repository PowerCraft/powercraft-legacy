package net.minecraft.src;

public class PCnt_TileEntityWeasel extends PC_TileEntity {

	private int id = -1;
	private int type = -1;
	private PCnt_WeaselPlugin plugin=null;
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public void invalidate() {
		plugin.setTileEntity(null);
		super.invalidate();
	}

	@Override
	public void readFromNBT(NBTTagCompound maintag) {
		super.readFromNBT(maintag);
		id =  maintag.getInteger("myid");
		type =  maintag.getInteger("type");
		plugin = getPlugin();
	}

	@Override
	public void writeToNBT(NBTTagCompound maintag) {
		super.writeToNBT(maintag);
		maintag.setInteger("myid", id);
		maintag.setInteger("type", type);
	}
	
	public void releasePlugin(){
		plugin = null;
	}
	
	public PCnt_WeaselPlugin getPlugin(){
		if(plugin==null){
			plugin = PCnt_WeaselManager.getPlugin(id);
			if(plugin==null){
				plugin = PCnt_WeaselPlugin.getPluginForType(type, id);
				if(plugin!=null)
					plugin.setTileEntity(this);
			}
		}
		return plugin;
	}
	
	public PCnt_TileEntityWeasel setType(int type){
		if(this.type == type)
			return this;
		if(!worldObj.isRemote)
			PC_Utils.setTileEntity(null, this, "type", type);
		this.type = type;
		if(plugin!=null){
			plugin.onDeviceDestroyed();
			PCnt_WeaselManager.removePlugin(plugin);
		}
		if(id!=-1)
			plugin = PCnt_WeaselPlugin.getPluginForType(type, id);
		else{
			plugin = PCnt_WeaselPlugin.getPluginForType(type);
			id = plugin.getID();
		}
		plugin.setTileEntity(this);
		return this;
	}
	
	public int getType() {
		return type;
	}
	
	/**
	 * Call weasel's function update
	 */
	public void onDirectPinChanged() {
		PCnt_WeaselPlugin plugin = (PCnt_WeaselPlugin)PCnt_WeaselManager.getPlugin(id);
		if (plugin != null) plugin.onNeighborBlockChanged();
	}
	
	@Override
	public void onBlockPickup() {
		if (plugin != null) {
			plugin.onDeviceDestroyed();
			PCnt_WeaselManager.removePlugin(plugin);
		}
	}
	
	@Override
	public void set(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("id")){
				id = (Integer)o[p++];
			}else if(var.equals("type")){
				setType((Integer)o[p++]);
			}else if(var.equals("plugin")){
				plugin.set((Object[])o[p++]);
			}
		}
	}

	@Override
	public Object[] get() {
		return new Object[]{
				"id", id,
				"type", type,
				"plugin", plugin.get()
		};
	}

}
