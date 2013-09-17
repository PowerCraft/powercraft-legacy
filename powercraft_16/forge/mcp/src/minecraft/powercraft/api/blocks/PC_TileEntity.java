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
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import powercraft.api.PC_Direction;
import powercraft.api.PC_PacketHandler;
import powercraft.api.PC_Utils;
import powercraft.api.energy.PC_EnergyGrid;
import powercraft.api.energy.PC_IEnergyConsumer;
import powercraft.api.energy.PC_IEnergyProvider;
import powercraft.api.energy.PC_IEnergyPuffer;
import powercraft.api.gres.PC_Gres;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresGuiOpenHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused") 
public abstract class PC_TileEntity extends TileEntity {

	protected boolean send = false;
	protected final List<PC_GresBaseWithInventory> containers = new ArrayList<PC_GresBaseWithInventory>();


	public boolean isClient() {

		if (worldObj == null) return true;
		return worldObj.isRemote;
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

		return worldObj.setBlockToAir(xCoord, yCoord, zCoord);
	}


	public ArrayList<ItemStack> getBlockDropped(int fortune) {

		Block block = getBlockType();
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		int count = block.quantityDropped(blockMetadata, fortune, worldObj.rand);
		for (int i = 0; i < count; i++) {
			int id = block.idDropped(blockMetadata, worldObj.rand, fortune);
			if (id > 0) {
				ret.add(new ItemStack(id, 1, block.damageDropped(blockMetadata)));
			}
		}
		return ret;
	}


	public boolean canConnectRedstone(PC_Direction side) {

		return getBlockType().canProvidePower() && side != PC_Direction.UNKNOWN;
	}


	public ItemStack getPickBlock(MovingObjectPosition target) {

		Block block = getBlockType();
		int id = block.idPicked(worldObj, xCoord, yCoord, zCoord);

		if (id == 0) {
			return null;
		}

		Item item = Item.itemsList[id];
		if (item == null) {
			return null;
		}

		return new ItemStack(id, 1, block.getDamageValue(worldObj, xCoord, yCoord, zCoord));
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


	public void loadFromNBT(NBTTagCompound nbtTagCompound) {

	}


	public void saveToNBT(NBTTagCompound nbtTagCompound) {

	}


	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {

		super.readFromNBT(nbtTagCompound);
		loadFromNBT(nbtTagCompound);
	}


	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {

		super.writeToNBT(nbtTagCompound);
		saveToNBT(nbtTagCompound);
	}


	@Override
	public Packet getDescriptionPacket() {

		return PC_PacketHandler.getBlockDataPacket(worldObj, xCoord, yCoord, zCoord);
	}


	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(RenderBlocks renderer) {

		return false;
	}


	public void notifyNeighbors() {

		PC_Utils.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
	}


	public void sendToClient() {

		if (isClient()) return;
		send = true;
	}


	@Override
	public void updateEntity() {

		if (!isClient()) {
			if (this instanceof PC_IEnergyConsumer) {
				List<PC_IEnergyProvider> providers = new ArrayList<PC_IEnergyProvider>();
				List<PC_IEnergyPuffer> puffers = new ArrayList<PC_IEnergyPuffer>();
				for (PC_Direction dir : PC_Direction.VALID_DIRECTIONS) {
					TileEntity te = PC_Utils.getTE(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
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
			if (send) {
				PC_PacketHandler.sendPacketToAllInDimension(PC_PacketHandler.getBlockDataPacket(worldObj, xCoord, yCoord, zCoord), worldObj
						.getWorldInfo().getVanillaDimension());
				send = false;
			}
		}
	}


	public void renderUpdate() {

		if (worldObj != null) worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}


	public void lightUpdate() {

		if (worldObj != null) worldObj.updateAllLightTypes(xCoord, yCoord, zCoord);
	}


	public void openContainer(PC_GresBaseWithInventory container) {

		if (!containers.contains(container)) {
			containers.add(container);
		}
	}


	public void closeContainer(PC_GresBaseWithInventory container) {

		containers.remove(container);
	}


	public void sendProgressBarUpdates() {

	}


	public void sendProgressBarUpdate(int key, int value) {

		for (PC_GresBaseWithInventory container : containers) {
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
	
}
