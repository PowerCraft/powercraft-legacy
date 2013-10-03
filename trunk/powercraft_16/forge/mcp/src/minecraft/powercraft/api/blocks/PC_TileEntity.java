package powercraft.api.blocks;


import java.lang.reflect.Field;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.IPlantable;
import powercraft.api.PC_Direction;
import powercraft.api.PC_FieldDescription;
import powercraft.api.PC_Logger;
import powercraft.api.PC_NBTTagHandler;
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

	@PC_FieldDescription
	private PC_Permissions permissions;
	
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
		if(worldObj!=null)
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


	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		try{
			super.readFromNBT(nbtTagCompound);
			loadFieldsFromNBT(nbtTagCompound);
			onLoadedFromNBT();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}


	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {

		super.writeToNBT(nbtTagCompound);
		saveFieldsToNBT(nbtTagCompound, 0);
		
	}
	
	public void onLoadedFromNBT(){
		
	}
	
	private void saveFieldsToNBT(NBTTagCompound nbtTagCompound, int type){
		Class<?> c = getClass();
		saveFieldsToNBT(nbtTagCompound, c, type);
	}
	
	private void saveFieldsToNBT(NBTTagCompound nbtTagCompound, Class<?> c, int type){
		Field[] fields = c.getDeclaredFields();
		String s = c.getSimpleName();
		for(int i=0; i<fields.length; i++){
			PC_FieldDescription fieldDescription = fields[i].getAnnotation(PC_FieldDescription.class);
			if(fieldDescription!=null){
				if((type==0 && fieldDescription.save())||(type==1 && fieldDescription.sync())||(type==2 && fieldDescription.guiSync())){
					String name = fieldDescription.name();
					if(name.isEmpty()){
						name = fields[i].getName();
					}
					fields[i].setAccessible(true);
					try {
						PC_NBTTagHandler.saveToNBT(nbtTagCompound, name, fields[i].get(this));
					} catch (Exception e) {
						e.printStackTrace();
						PC_Logger.severe("Error while saving %s of %s", name, c);
					} 
				}
			}
		}
		c = c.getSuperclass();
		if(PC_TileEntity.class.isAssignableFrom(c)){
			saveFieldsToNBT(nbtTagCompound, c, type);
		}
	}
	
	private void loadFieldsFromNBT(NBTTagCompound nbtTagCompound){
		Class<?> c = getClass();
		loadFieldsFromNBT(nbtTagCompound, c);
	}
	
	private void loadFieldsFromNBT(NBTTagCompound nbtTagCompound, Class<?> c){
		Field[] fields = c.getDeclaredFields();
		String s = c.getSimpleName();
		for(int i=0; i<fields.length; i++){
			PC_FieldDescription fieldDescription = fields[i].getAnnotation(PC_FieldDescription.class);
			if(fieldDescription!=null){
				String name = fieldDescription.name();
				if(name.isEmpty()){
					name = fields[i].getName();
				}
				if(nbtTagCompound.hasKey(name)){
					fields[i].setAccessible(true);
					Object obj = PC_NBTTagHandler.loadFromNBT(nbtTagCompound, name, fields[i].getType());
					try {
						fields[i].set(this, obj);
					} catch (Exception e) {
						e.printStackTrace();
						PC_Logger.severe("Error while loading %s of %s", name, c);
					}
				}else{
					String otherNames[] = fieldDescription.otherNames();
					for(int j=0; j<otherNames.length; j++){
						if(nbtTagCompound.hasKey(otherNames[j])){
							fields[i].setAccessible(true);
							Object obj = PC_NBTTagHandler.loadFromNBT(nbtTagCompound, otherNames[j], fields[i].getType());
							try {
								fields[i].set(this, obj);
							} catch (Exception e) {
								e.printStackTrace();
								PC_Logger.severe("Error while loading %s of %s", name, c);
							}
						}
					}
				}
			}
		}
		c = c.getSuperclass();
		if(PC_TileEntity.class.isAssignableFrom(c)){
			loadFieldsFromNBT(nbtTagCompound, c);
		}
	}

	public void loadFromNBTPacket(NBTTagCompound nbtTagCompound) {
		loadFieldsFromNBT(nbtTagCompound);
		onLoadedFromNBT();
		
	}

	public void saveToNBTPacket(NBTTagCompound nbtTagCompound) {
		saveFieldsToNBT(nbtTagCompound, 1);
	}
	
	public void saveToGuiNBTPacket(NBTTagCompound nbtTagCompound){
		saveFieldsToNBT(nbtTagCompound, 2);
	}

	public boolean getBlocksMovement() {
		return !getBlockType().blockMaterial.blocksMovement();
	}

	public boolean isBlockSolid(PC_Direction side) {
		return getBlockType().blockMaterial.isSolid();
	}

	public float getPlayerRelativeBlockHardness(EntityPlayer player) {
		return hasPermission(player, PC_Permission.BLOCKHARVEST)?getBlockType().getBlockHardness(worldObj, xCoord, yCoord, zCoord):-1;
	}

	public void onEntityWalking(Entity entity) {}

	public void onBlockClicked(EntityPlayer player) {}

	public void velocityToAddToEntity(Entity entity, Vec3 vec3) {}

	public int isProvidingWeakPower(PC_Direction side) {
		return 0;
	}

	public int colorMultiplier() {
		return 0xffffffff;
	}

	public int isProvidingStrongPower(PC_Direction side) {
		return 0;
	}

	public void onBlockPlacedBy(EntityLivingBase living, ItemStack itemStack) {
		if(living instanceof EntityPlayer){
			if(this.permissions==null && !isClient()){
				this.permissions = new PC_Permissions(((EntityPlayer)living).username);
			}
		}
	}

	public void onFallenUpon(Entity entity, float fallDistance) {}

	public void fillWithRain() {}

	public int getComparatorInputOverride(PC_Direction side) {
		return 0;
	}

	public boolean isLadder(EntityLivingBase entity) {
		return false;
	}

	public boolean isBlockNormalCube() {
		Block block = getBlockType();
		return block.blockMaterial.isOpaque() && block.renderAsNormalBlock() && !block.canProvidePower();
	}

	public boolean isBlockReplaceable() {
		return false;
	}

	public boolean isBlockBurning() {
		return false;
	}

	public int getFlammability(PC_Direction side) {
		return 0;
	}

	public boolean isFlammable(PC_Direction side) {
		return false;
	}

	public int getFireSpreadSpeed(PC_Direction side) {
		return 0;
	}

	public boolean isFireSource(PC_Direction side) {
		return false;
	}

	public boolean canSilkHarvest(EntityPlayer player) {
		return false;
	}

	public boolean canCreatureSpawn(EnumCreatureType type) {
		return false;
	}

	public float getExplosionResistance(Entity entity, double explosionX, double explosionY, double explosionZ) {
		return getBlockType().getExplosionResistance(entity);
	}

	public boolean canPlaceTorchOnTop() {
		return worldObj.doesBlockHaveSolidTopSurface(xCoord, yCoord, zCoord);
	}

	public boolean addBlockHitEffects(MovingObjectPosition target, EffectRenderer effectRenderer) {
		return false;
	}

	public boolean addBlockDestroyEffects(EffectRenderer effectRenderer) {
		return false;
	}

	public boolean canSustainPlant(PC_Direction side, IPlantable plant) {
		return false;
	}

	public boolean canEntityDestroy(Entity entity) {
		if(entity instanceof EntityPlayer){
			return hasPermission((EntityPlayer)entity, PC_Permission.BLOCKHARVEST);
		}
		return true;
	}

	public boolean isBeaconBase(int beaconX, int beaconY, int beaconZ) {
		return false;
	}

	public float getEnchantPowerBonus() {
		return 0;
	}

	public boolean canExplode(Explosion explosion) {
		return true;
	}

	public void onPlantGrow(int sourceX, int sourceY, int sourceZ) {}


	public boolean isFertile() {
		return false;
	}
	
}
