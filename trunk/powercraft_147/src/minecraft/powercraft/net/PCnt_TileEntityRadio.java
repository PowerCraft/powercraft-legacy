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
	
	public static final String TYPE = "type", ACTIVE = "active", HIDELABLE = "hideLabel", RENDERMICRO = "renderMicro", CHANNEL = "channel";
	
	/** Device channel */
	//private String channel = PCnt_RadioManager.default_radio_channel;
	/** Device type, 0=TX, 1=RX */
	//public int type = 0; // 0=tx, 1=rx
	/** Device active flag */
	//public boolean active = false;
	/** Hide the label */
	//public boolean hideLabel = false;


	/** Render a smaller model */
	//public boolean renderMicro = false;

	private static PCnt_ModelRadio model = new PCnt_ModelRadio();
	
	public PCnt_TileEntityRadio(){
		setData(TYPE, 0);
		setData(ACTIVE, false);
		setData(HIDELABLE, false);
		setData(RENDERMICRO, false);
		setData(CHANNEL, PCnt_RadioManager.default_radio_channel);
	}

	@Override
	public void create(ItemStack stack, EntityPlayer player, World world,
			int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		setData(TYPE, stack.getItemDamage());
	}
	
	public boolean isHideLabel() {
		return (Boolean)getData(HIDELABLE);
	}

	public void setHideLabel(boolean hideLabel) {
		setData(HIDELABLE, hideLabel);
	}

	public boolean isRenderMicro() {
		return (Boolean)getData(RENDERMICRO);
	}

	public void setRenderMicro(boolean renderMicro) {
		setData(RENDERMICRO, renderMicro);
	}

	public int getType() {
		return (Integer)getData(TYPE);
	}

	public void setActive(boolean active) {
		setData(ACTIVE, active);
	}

	@Override
	public void updateEntity() {
		if(isReceiver() && !worldObj.isRemote){
			boolean newstate = PCnt_RadioManager.getChannelState(getChannel());
			if (isActive() != newstate) {
				setActive(newstate);
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
		if(getType()==0 && getType()!=typeindex && !worldObj.isRemote){
			PCnt_RadioManager.transmitterOff(getChannel());
		}
		setType(typeindex);
	}

	/**
	 * @return is this device transmitter
	 */
	public boolean isTransmitter() {
		return getType() == 0;
	}

	/**
	 * @return is this device receiver
	 */
	public boolean isReceiver() {
		return getType() == 1;
	}

	/**
	 * @return is the radio device active
	 */
	public boolean isActive() {
		return (Boolean)getData(ACTIVE);
	}

	/**
	 * Set "active" flag and send update to radio manager
	 * 
	 * @param act is active
	 */
	public void setTransmitterState(boolean act) {
		if(isActive() != act){
			setActive(act);
			if(act && getType()==0 && !worldObj.isRemote){
				PCnt_RadioManager.transmitterOn(getChannel());
			}else if(getType()==0 && !worldObj.isRemote){
				PCnt_RadioManager.transmitterOff(getChannel());
			}
			if(getType()==1)
				ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
		}
	}

	/**
	 * @return radio channel assigned to this entity
	 */
	public String getChannel() {
		return (String)getData(CHANNEL);
	}

	public void setChannel(String channel) {
		if(!getChannel().equals(channel)){
			if(this.isActive()&&this.isTransmitter()&&!worldObj.isRemote)
				PCnt_RadioManager.transmitterOff(getChannel());
			setData(CHANNEL, channel);
			if(this.isActive()&&this.isTransmitter()&&!worldObj.isRemote)
				PCnt_RadioManager.transmitterOn(getChannel());
		}
	}

	@Override
	protected void dataChange(String key, Object value) {
		if(key.equals(CHANNEL)){
			setChannel((String)value);
		}else if(key.equals(ACTIVE)){
			setTransmitterState((Boolean)value);
		}
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

		model.tiny = isRenderMicro();

		model.render();
		PC_Renderer.glPopMatrix();

		PC_Renderer.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		PC_Renderer.glPopMatrix();

		if (!isHideLabel()) {
			String foo = getChannel();
			PC_Renderer.renderEntityLabelAt(foo, new PC_VecF(xCoord, yCoord, zCoord), 8, isRenderMicro() ? 0.5F : 1.3F, x, y, z);
		}
	}
}
