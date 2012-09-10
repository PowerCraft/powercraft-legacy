package net.minecraft.src;


public class PCnt_TeleporterData implements PC_INBTWD {

	public PC_CoordI pos = new PC_CoordI();
	public String defaultTarget = "";
	private String name = "";

	public boolean lastActiveState = false;

	public boolean items = true;
	public boolean animals = true;
	public boolean monsters = true;
	public boolean players = true;
	public boolean sneakTrigger = false;
	public String direction = "N";
	public boolean hideLabel = false;

	public int dimension;
	
	private boolean needSave=true;
	
	public PCnt_TeleporterData(){
		System.out.println("new PCtr_TeleporterData:" + this);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		PC_Utils.saveToNBT(tag, "pos", pos);
		tag.setString("name", getName());
		tag.setString("defaultTarget", defaultTarget);
		tag.setInteger("dimension", dimension);
		return tag;
	}

	@Override
	public PC_INBT readFromNBT(NBTTagCompound tag) {
		System.out.println("Load Data");
		PC_Utils.loadFromNBT(tag, "pos", pos);
		setName(tag.getString("name"));
		defaultTarget = tag.getString("defaultTarget");
		dimension = tag.getInteger("dimension");
		System.out.println("====>"+getName());
		PC_Utils.sendToPacketHandler(null, "TeleporterNetHandler", 0, pos.x, pos.y, pos.z, getName(), defaultTarget, dimension);
		return this;
	}

	@Override
	public boolean needsSave() {
		return needSave;
	}

	public String getName() {
		//System.out.println("MC:"+PC_Utils.mc()+" get "+name);
		return name;
	}

	public void setName(String name) {
		System.out.println("T:"+this+" set "+this.name+" to "+name);
		this.name = name;
	}
	
}
