package powercraft.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecF;
import powercraft.management.PC_VecI;

public class PCnt_TileEntityRadio extends PC_TileEntity implements PC_ITileEntityRenderer {
	/** Device channel */
	private String channel = PCnt_RadioManager.default_radio_channel;
	/** Device type, 0=TX, 1=RX */
	public int type = 0; // 0=tx, 1=rx
	/** Device active flag */
	public boolean active = false;
	/** Hide the label */
	public boolean hideLabel = false;


	/** Render a smaller model */
	public boolean renderMicro = false;

	private static PCnt_ModelRadio model = new PCnt_ModelRadio();
	
	@Override
	public void create(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		type = stack.getItemDamage();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setString("channel", channel);
		nbttagcompound.setInteger("type", type);
		nbttagcompound.setBoolean("active", active);
		nbttagcompound.setBoolean("NoLabel", hideLabel);
		nbttagcompound.setBoolean("Micro", renderMicro);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		channel = nbttagcompound.getString("channel");
		type = nbttagcompound.getInteger("type");
		active = nbttagcompound.getBoolean("active");
		hideLabel = nbttagcompound.getBoolean("NoLabel");
		renderMicro = nbttagcompound.getBoolean("Micro");
	}

	@Override
	public void updateEntity() {
		if(isReceiver()){
			boolean newstate = PCnt_RadioManager.getChannelState(channel);
			if (active != newstate) {
				active = newstate;
				PC_PacketHandler.setTileEntity(this, "active", active);
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType().blockID, 1);
				updateBlock();
			}
		}
	}

	/**
	 * Notify block change.
	 */
	public void updateBlock() {
		ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return true
	 */
	@Override
	public boolean canUpdate() {
		return true;
	}

	/**
	 * Set device type
	 * 
	 * @param typeindex 0=gold TX, 1=iron RX
	 */
	public void setType(int typeindex) {
		if(type==0 && type!=typeindex && !worldObj.isRemote){
			PCnt_RadioManager.transmitterOff(channel);
		}
		type = typeindex;
	}

	/**
	 * @return is this device transmitter
	 */
	public boolean isTransmitter() {
		return type == 0;
	}

	/**
	 * @return is this device receiver
	 */
	public boolean isReceiver() {
		return type == 1;
	}

	/**
	 * @return is the radio device active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Set "active" flag and send update to radio manager
	 * 
	 * @param act is active
	 */
	public void setTransmitterState(boolean act) {
		if(active != act){
			active = act;
			if(act && type==0 && !worldObj.isRemote){
				PCnt_RadioManager.transmitterOn(channel);
			}else if(type==0 && !worldObj.isRemote){
				PCnt_RadioManager.transmitterOff(channel);
			}
			if(type==1)
				ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
		}
	}

	/**
	 * @return radio channel assigned to this entity
	 */
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		if(!this.channel.equals(channel)){
			if(this.isActive()&&this.isTransmitter()&&!worldObj.isRemote)
				PCnt_RadioManager.transmitterOff(this.channel);
			this.channel = channel;
			if(this.isActive()&&this.isTransmitter()&&!worldObj.isRemote)
				PCnt_RadioManager.transmitterOn(this.channel);
		}
	}
	
	@Override
	public void setData(Object[] o) {
		int p = 0;
		while(p<o.length){
			String var = (String)o[p++];
			if(var.equals("type")){
				this.setType((Integer)o[p++]);
			}else if(var.equals("channel")){
				setChannel((String)o[p++]);
			}else if(var.equals("active")){
				active = !(Boolean)o[p++];
				setTransmitterState(!active);
			}else if(var.equals("hideLabel")){
				this.hideLabel = (Boolean)o[p++];
			}else if(var.equals("renderMicro")){
				this.renderMicro = (Boolean)o[p++];
			}
		}
	}

	@Override
	public Object[] getData() {
		return new Object[]{
				"type", type,
				"channel", channel,
				"active", active,
				"hideLabel", hideLabel,
				"renderMicro", renderMicro
		};
	}

	@Override
	public void renderTileEntityAt(double x, double y, double z, float rot) {

		PC_Renderer.glPushMatrix();
		float f = 1.0F;

		PC_Renderer.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		
		PC_Renderer.bindTexture(ModuleInfo.getTextureDirectory(ModuleInfo.getModule("Net")) + "block_radio.png");

		PC_Renderer.glPushMatrix();
		PC_Renderer.glScalef(f, -f, -f);
		model.setType(isTransmitter(), worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1); // ter.active);

		model.tiny = renderMicro;

		model.render();
		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();

		if (!hideLabel) {
			String foo = getChannel();
			PC_Renderer.renderEntityLabelAt(foo, new PC_VecF(xCoord, yCoord, zCoord), 8, renderMicro ? 0.5F : 1.3F, x, y, z);
		}
	}
}
