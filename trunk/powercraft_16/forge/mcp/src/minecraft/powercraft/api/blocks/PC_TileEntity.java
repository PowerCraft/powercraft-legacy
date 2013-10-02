package powercraft.api.blocks;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import powercraft.api.PC_Direction;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_PacketHandlerClient;
import powercraft.api.PC_Utils;
import powercraft.api.energy.PC_EnergyGrid;
import powercraft.api.energy.PC_IEnergyConsumer;
import powercraft.api.energy.PC_IEnergyProvider;
import powercraft.api.energy.PC_IEnergyPuffer;
import powercraft.api.gres.PC_Gres;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import powercraft.api.security.PC_IPermissionHandler;
import powercraft.api.security.PC_Permission;
import powercraft.api.security.PC_Permissions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused") 
public abstract class PC_TileEntity extends TileEntity implements PC_IPermissionHandler {

	protected boolean send = false;
	protected final List<PC_GresBaseWithInventory> containers = new ArrayList<PC_GresBaseWithInventory>();

	private PC_Permissions permissions;
	
	public void setOwner(String name){
		if(this.permissions==null && !isClient()){
			this.permissions = new PC_Permissions(name);
		}
	}
	
	public boolean isClient() {

		if (this.worldObj == null) return true;
		return this.worldObj.isRemote;
	}


	public void onBlockAdded() {

	}


	public void onBlockBreak() {

	}


	public float getBlockHardness() {

		return getBlockType().blockHardness;
	}


	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random random) {

	}


	public void onNeighborBlockChange(int neighborID) {

	}


	public boolean onBlockActivated(EntityPlayer player, PC_Direction side, float xHit, float yHit, float zHit) {

		if (this instanceof PC_IGresGuiOpenHandler) {
			PC_Gres.openGui(player, this);
			return true;
		}
		return false;
	}


	public void onEntityCollidedWithBlock(Entity entity) {

	}


	public int getRedstonePowerValue(PC_Direction side) {

		return 0;
	}


	public int getLightValue() {

		return Block.lightValue[getBlockType().blockID];
	}


	public boolean removeBlockByPlayer(EntityPlayer player) {

		return this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
	}


	public ArrayList<ItemStack> getBlockDropped(int fortune) {

		Block block = getBlockType();
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = block.quantityDropped(this.blockMetadata, fortune, this.worldObj.rand);
		for (int i = 0; i < count; i++) {
			int id = block.idDropped(this.blockMetadata, this.worldObj.rand, fortune);
			if (id > 0) {
				ret.add(new ItemStack(id, 1, block.damageDropped(this.blockMetadata)));
			}
		}
		return ret;
	}


	public boolean canConnectRedstone(PC_Direction side) {

		return getBlockType().canProvidePower() && side != PC_Direction.UNKNOWN;
	}


	public ItemStack getPickBlock(MovingObjectPosition target) {

		Block block = getBlockType();
		int id = block.idPicked(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

		if (id == 0) {
			return null;
		}

		Item item = Item.itemsList[id];
		if (item == null) {
			return null;
		}

		return new ItemStack(id, 1, block.getDamageValue(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
	}


	public int getLightOpacity() {

		return Block.lightOpacity[getBlockType().blockID];
	}


	public boolean rotateBlock(PC_Direction side) {

		return false;
	}


	public PC_Direction[] getValidRotations() {

		return null;
	}


	public boolean recolourBlock(PC_Direction side, int colour) {

		return false;
	}


	public void onBlockMessage(EntityPlayer player, NBTTagCompound nbtTagCompound) {

	}

	@Override
	public Packet getDescriptionPacket() {

		return PC_PacketHandler.getBlockDataPacket(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}


	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(RenderBlocks renderer) {

		return false;
	}


	public void notifyNeighbors() {

		PC_Utils.hugeUpdate(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}


	public void sendToClient() {

		if (isClient()) return;
		this.send = true;
	}


	@Override
	public void updateEntity() {

		if (!isClient()) {
			if (this instanceof PC_IEnergyConsumer) {
				List<PC_IEnergyProvider> providers = new ArrayList<PC_IEnergyProvider>();
				List<PC_IEnergyPuffer> puffers = new ArrayList<PC_IEnergyPuffer>();
				for (PC_Direction dir : PC_Direction.VALID_DIRECTIONS) {
					TileEntity te = PC_Utils.getTE(this.worldObj, this.xCoord + dir.offsetX, this.yCoord + dir.offsetY, this.zCoord + dir.offsetZ);
					if (te instanceof PC_IEnergyPuffer) {
						puffers.add((PC_IEnergyPuffer) te);
					} else if (te instanceof PC_IEnergyProvider) {
						providers.add((PC_IEnergyProvider) te);
					}
				}
				List<PC_IEnergyConsumer> consumers = new ArrayList<PC_IEnergyConsumer>();
				if (this instanceof PC_IEnergyPuffer) {
					puffers.add((PC_IEnergyPuffer) this);
				} else {
					consumers.add((PC_IEnergyConsumer) this);
				}
				PC_EnergyGrid.calc(consumers, providers, puffers);
			}
			if (this.send) {
				PC_PacketHandler.sendPacketToAllInDimension(PC_PacketHandler.getBlockDataPacket(this.worldObj, this.xCoord, this.yCoord, this.zCoord), this.worldObj
						.getWorldInfo().getVanillaDimension());
				this.send = false;
			}
		}
	}


	public void renderUpdate() {

		if (this.worldObj != null) this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
	}


	public void lightUpdate() {

		if (this.worldObj != null) this.worldObj.updateAllLightTypes(this.xCoord, this.yCoord, this.zCoord);
	}


	public void openContainer(PC_GresBaseWithInventory container) {

		if (!this.containers.contains(container)) {
			this.containers.add(container);
		}
	}


	public void closeContainer(PC_GresBaseWithInventory container) {

		this.containers.remove(container);
	}


	public void sendProgressBarUpdates() {

	}


	public void sendProgressBarUpdate(int key, int value) {

		for (PC_GresBaseWithInventory container : this.containers) {
			container.sendProgressBarUpdate(key, value);
		}
	}

	public boolean isUpgradeable()
	{
		return false;
	}
	
	public boolean isUpgradeableByUpgrade(int upgrade)
	{
		return false;
	}
	
	@Override
	public boolean checkPermission(EntityPlayer player, PC_Permission permission, String password) {
		if(this.permissions==null)
			return true;
		return this.permissions.checkPermission(player, permission, password);
	}

	@Override
	public boolean hasPermission(EntityPlayer player, PC_Permission permission) {
		if(this.permissions==null)
			return true;
		return this.permissions.hasPermission(player, permission);
	}

	@Override
	public boolean tryPermission(EntityPlayer player, PC_Permission permission) {
		if(this.permissions==null)
			return true;
		return this.permissions.hasPermission(player, permission);
	}
	
	@Override
	public boolean needPassword(EntityPlayer player) {
		if(this.permissions==null)
			return false;
		return this.permissions.needPassword(player);
	}
	
	public void sendMessage(NBTTagCompound nbtTagCompound){
		if(isClient()){
			PC_PacketHandler.sendPacketToServer(PC_PacketHandler.getBlockMessagePacket(worldObj, xCoord, yCoord, zCoord, nbtTagCompound));
		}else{
			PC_PacketHandler.sendPacketToAllInDimension(PC_PacketHandler.getBlockMessagePacket(worldObj, xCoord, yCoord, zCoord, nbtTagCompound), worldObj
					.getWorldInfo().getVanillaDimension());
		}
	}
	
	public abstract void loadFromNBT(NBTTagCompound nbtTagCompound);


	public abstract void saveToNBT(NBTTagCompound nbtTagCompound);


	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {

		super.readFromNBT(nbtTagCompound);
		if(nbtTagCompound.hasKey("permissions")) //$NON-NLS-1$
			this.permissions = new PC_Permissions(nbtTagCompound.getCompoundTag("permissions")); //$NON-NLS-1$
		loadFromNBT(nbtTagCompound);
	}


	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {

		super.writeToNBT(nbtTagCompound);
		if(this.permissions!=null){
			NBTTagCompound permissionsCompound = new NBTTagCompound();
			this.permissions.saveToNBT(permissionsCompound);
			nbtTagCompound.setCompoundTag("permissions", permissionsCompound); //$NON-NLS-1$
		}
		saveToNBT(nbtTagCompound);
	}
	
}
