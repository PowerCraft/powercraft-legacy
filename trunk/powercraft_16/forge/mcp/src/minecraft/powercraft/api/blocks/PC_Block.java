package powercraft.api.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Module;
import powercraft.api.PC_Renderer;
import powercraft.api.PC_Utils;
import powercraft.api.registries.PC_ModuleRegistry;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.api.security.PC_Permission;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_Block extends BlockContainer {

	public final PC_BlockInfo blockInfo;

	public final PC_Module module;
	
	protected PC_Block(int id, Material material) {
		super(id, material);
		blockInfo = getClass().getAnnotation(PC_BlockInfo.class);
		if(blockInfo==null)
			PC_Logger.severe("No BlockInfo for block %s", getClass().getName());
		module = PC_ModuleRegistry.getActiveModule();
		if(module==null)
			PC_Logger.severe("No Module for block %s", getClass().getName());
	}

	@SuppressWarnings("unused")
	public PC_TileEntity createTileEntity(World world){
		return null;
	}
	
	@Override
	public final TileEntity createNewTileEntity(World world) {
		PC_TileEntity tileEntity = createTileEntity(world);
		if(tileEntity!=null)
			return tileEntity;
		Class<? extends PC_TileEntity> tileEntityClass = getTileEntityClass(world);
		if(tileEntityClass!=null && tileEntityClass!=PC_TileEntity.class){
			try {
				return tileEntityClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				PC_Logger.severe("Failed to generate tileEntity %s", tileEntityClass);
			}
		}
		return null;
	}

	public final PC_BlockInfo getBlockInfo() {
		return blockInfo;
	}

	public final PC_Module getModule() {
		return module;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			((PC_TileEntity)tileEntity).onBlockAdded();
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			((PC_TileEntity)tileEntity).onBlockBreak();
		super.breakBlock(world, x, y, z, blockID, metadata);
	}

	@Override
	protected final void initializeBlock() {}

	@Override
	public final Block setStepSound(StepSound stepSound) {
		return super.setStepSound(stepSound);
	}

	@Override
	public final Block setLightOpacity(int opacity) {
		return super.setLightOpacity(opacity);
	}

	@Override
	public final Block setLightValue(float lightValue) {
		return super.setLightValue(lightValue);
	}

	@Override
	public final Block setResistance(float resistance) {
		return super.setResistance(resistance);
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			return ((PC_TileEntity)tileEntity).getBlocksMovement();
		return super.getBlocksMovement(world, x, y, z);
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.getRenderType();
	}

	@Override
	public final Block setHardness(float hardness) {
		return super.setHardness(hardness);
	}

	@Override
	public final Block setBlockUnbreakable() {
		return super.setBlockUnbreakable();
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			return ((PC_TileEntity)tileEntity).getBlockHardness();
		return super.getBlockHardness(world, x, y, z);
	}

	@Override
	public final Block setTickRandomly(boolean state) {
		return super.setTickRandomly(state);
	}

	@Override
	public boolean getTickRandomly() {
		return super.getTickRandomly();
	}

	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isBlockSolid(side);
		return super.isBlockSolid(world, x, y, z, side.ordinal());
	}
	
	@Override
	public final boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
		return isBlockSolid(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}

	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		return super.getBlockTexture(world, x, y, z, side.ordinal());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		return getBlockTexture(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(PC_Direction side, int metadata) {
		return super.getIcon( side.ordinal(), metadata);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final Icon getIcon(int side, int metadata) {
		return getIcon(PC_Direction.getOrientation(side), metadata);
	}
	
	@SuppressWarnings("unused")
	public List<AxisAlignedBB> getCollisonBoxesList(World world, int x, int y, int z, Entity entity){
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		list.add(getCollisionBoundingBox(world, x, y, z));
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public final void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		List<AxisAlignedBB> l = getCollisonBoxesList(world, x, y, z, entity);
		if(l!=null){
			for(AxisAlignedBB aabb:l){
				aabb = makeAABBOffset(rotateAABB(world, x, y, z, aabb), x, y, z);
				if (aabb != null && axisAlignedBB.intersectsWith(aabb)){
			       	list.add(aabb);
			    }
			}
		}
	}

	private static AxisAlignedBB makeAABBOffset(AxisAlignedBB axisAlignedBB, int x, int y, int z){
		if(axisAlignedBB==null)
			return null;
		return AxisAlignedBB.getAABBPool().getAABB(axisAlignedBB.minX+x, axisAlignedBB.minY+y, axisAlignedBB.minZ+z, axisAlignedBB.maxX+x, axisAlignedBB.maxY+y, axisAlignedBB.maxZ+z);
	}
	
	@SuppressWarnings("unused")
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public final AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		return makeAABBOffset(rotateAABB(world, x, y, z, getSelectedBoundingBox(world, x, y, z)), x, y, z);
	}

	@SuppressWarnings("unused")
	public AxisAlignedBB getCollisionBoundingBox(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public final AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return makeAABBOffset(rotateAABB(world, x, y, z, getCollisionBoundingBox(world, x, y, z)), x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			((PC_TileEntity)tileEntity).onNeighborBlockChange(neighborID);
		super.onNeighborBlockChange(world, x, y, z, neighborID);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			return ((PC_TileEntity)tileEntity).getPlayerRelativeBlockHardness(player);
		return super.getPlayerRelativeBlockHardness(player, world, x, y, z);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, PC_Direction side, float xHit, float yHit, float zHit) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity) 
			return ((PC_TileEntity)tileEntity).onBlockActivated(player, side, xHit, yHit, zHit);
		return super.onBlockActivated(world, x, y, z, player, side.ordinal(), xHit, yHit, zHit);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
		return onBlockActivated(world, x, y, z, player, getBlockRotation(world, x, y, z, side), xHit, yHit, zHit);
	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onEntityWalking(entity);
		super.onEntityWalking(world, x, y, z, entity);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onBlockClicked(player);
		super.onBlockClicked(world, x, y, z, player);
	}

	@Override
	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec3) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).velocityToAddToEntity(entity, vec3);
		super.velocityToAddToEntity(world, x, y, z, entity, vec3);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		super.setBlockBoundsBasedOnState(world, x, y, z);
	}

	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isProvidingWeakPower(side);
		return super.isProvidingWeakPower(world, x, y, z, side.ordinal());
	}
	
	@Override
	public final int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return isProvidingWeakPower(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).colorMultiplier();
		return super.colorMultiplier(world, x, y, z);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onEntityCollidedWithBlock(entity);
		super.onEntityCollidedWithBlock(world, x, y, z, entity);
	}

	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isProvidingStrongPower(side);
		return super.isProvidingStrongPower(world, x, y, z, side.ordinal());
	}
	
	@Override
	public final int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return isProvidingStrongPower(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}

	@Override
	public final void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack itemStack) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onBlockPlacedBy(living, itemStack);
		super.onBlockPlacedBy(world, x, y, z, living, itemStack);
	}

	@Override
	public final void onPostBlockPlaced(World world, int x, int y, int z, int metadata) {
		super.onPostBlockPlaced(world, x, y, z, metadata);
	}

	@Override
	public final Block setUnlocalizedName(String name) {
		return super.setUnlocalizedName(name);
	}

	@Override
	protected final Block disableStats() {
		return super.disableStats();
	}

	@Override
	public void onFallenUpon(World world, int x, int y, int z, Entity entity, float fallDistance) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onFallenUpon(entity, fallDistance);
		super.onFallenUpon(world, x, y, z, entity, fallDistance);
	}

	@Override
	public final Block setCreativeTab(CreativeTabs creativeTab) {
		return super.setCreativeTab(creativeTab);
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
		if(hasPermissionToHarvest(world, x, y, z, player) && !PC_Utils.isCreativ(player)){
			super.harvestBlock(world, player, x, y, z, metadata);
		}
	}

	@Override
	public void fillWithRain(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).fillWithRain();
		super.fillWithRain(world, x, y, z);
	}

	public int getComparatorInputOverride(World world, int x, int y, int z, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getComparatorInputOverride(side);
		return super.getComparatorInputOverride(world, x, y, z, side.ordinal());
	}
	
	@Override
	public final int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return getComparatorInputOverride(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}

	@Override
	public final void registerIcons(IconRegister iconRegister) {
		PC_TextureRegistry.registerIcons(this, iconRegister);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getLightValue();
		return super.getLightValue(world, x, y, z);
	}

	@Override
	public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entity) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isLadder(entity);
		return super.isLadder(world, x, y, z, entity);
	}

	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isBlockNormalCube();
		return super.isBlockNormalCube(world, x, y, z);
	}

	@Override
	public final boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return isBlockSolid(world, x, y, z, getBlockRotation(world, x, y, z, side.ordinal()));
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isBlockReplaceable();
		return super.isBlockReplaceable(world, x, y, z);
	}

	@Override
	public boolean isBlockBurning(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isBlockBurning();
		return super.isBlockBurning(world, x, y, z);
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if(hasPermissionToHarvest(world, x, y, z, player)){
			return super.removeBlockByPlayer(world, player, x, y, z);
		}
		return false;
	}

	@SuppressWarnings("unused")
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getFlammability(side);
		return 0;
	}
	
	@Override
	public final int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
		return getFlammability(world, x, y, z, metadata, getBlockRotation(world, x, y, z, face.ordinal()));
	}

	@SuppressWarnings("unused")
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isFlammable(side);
		return false;
	}
	
	@Override
	public final boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face) {
		return isFlammable(world, x, y, z, metadata, getBlockRotation(world, x, y, z, face.ordinal()));
	}

	@SuppressWarnings("unused")
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getFireSpreadSpeed(side);
		return 0;
	}
	
	@Override
	public final int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face) {
		return getFireSpreadSpeed(world, x, y, z, metadata, getBlockRotation(world, x, y, z, face.ordinal()));
	}

	@SuppressWarnings("unused")
	public boolean isFireSource(World world, int x, int y, int z, int metadata, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isFireSource(side);
		return false;
	}
	
	@Override
	public final boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side) {
		return isFireSource(world, x, y, z, metadata, getBlockRotation(world, x, y, z, side.ordinal()));
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getBlockDropped(fortune);
		return super.getBlockDropped(world, x, y, z, metadata, fortune);
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canSilkHarvest(player);
		return super.canSilkHarvest(world, player, x, y, z, metadata);
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canCreatureSpawn(type);
		return super.canCreatureSpawn(type, world, x, y, z);
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getExplosionResistance(entity, explosionX, explosionY, explosionZ);
		return super.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	public boolean canExplode(World world, int x, int y, int z, Explosion explosion){
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canExplode(explosion);
		return true;
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		if(canExplode(world, x, y, z, explosion))
			super.onBlockExploded(world, x, y, z, explosion);
	}

	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, PC_Direction side) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canConnectRedstone(side);
		return super.canConnectRedstone(world, x, y, z, side.ordinal());
	}
	
	@Override
	public final boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
		return canConnectRedstone(world, x, y, z, getBlockRotation(world, x, y, z, side));
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canPlaceTorchOnTop();
		return super.canPlaceTorchOnTop(world, x, y, z);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getPickBlock(target);
		return super.getPickBlock(target, world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		TileEntity tileEntity = PC_Utils.getTE(world, target.blockX, target.blockY, target.blockZ);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).addBlockHitEffects(target, effectRenderer);
		return super.addBlockHitEffects(world, target, effectRenderer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).addBlockDestroyEffects(effectRenderer);
		return super.addBlockDestroyEffects(world, x, y, z, meta, effectRenderer);
	}

	public boolean canSustainPlant(World world, int x, int y, int z, PC_Direction side, IPlantable plant) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canSustainPlant(side, plant);
		return false;
	}
	
	@Override
	public final boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
		return canSustainPlant(world, x, y, z, getBlockRotation(world, x, y, z, direction.ordinal()), plant);
	}

	@Override
	public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).onPlantGrow(sourceX, sourceY, sourceZ);
		super.onPlantGrow(world, x, y, z, sourceX, sourceY, sourceZ);
	}

	@Override
	public boolean isFertile(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isFertile();
		return super.isFertile(world, x, y, z);
	}

	@Override
	public int getLightOpacity(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getLightOpacity();
		return super.getLightOpacity(world, x, y, z);
	}

	@Override
	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).canEntityDestroy(entity);
		return super.canEntityDestroy(world, x, y, z, entity);
	}

	@Override
	public boolean isBeaconBase(World world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).isBeaconBase(beaconX, beaconY, beaconZ);
		return super.isBeaconBase(world, x, y, z, beaconX, beaconY, beaconZ);
	}
	
	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis) {
		// TODO Auto-generated method stub
		return super.rotateBlock(world, x, y, z, axis);
	}
	
	@Override
	public final ForgeDirection[] getValidRotations(World world, int x, int y, int z) {
		boolean canRotate = canRotate(world, x, y, z);
		boolean canPitch = canPitch(world, x, y, z);
		if(canPitch && canRotate){
			return ForgeDirection.VALID_DIRECTIONS;
		}else if(canPitch){
			ForgeDirection pitch = getBlockPitch(world, x, y, z).getForgeDirection();
			int i=0;
			ForgeDirection dirs[] = new ForgeDirection[4];
			for(ForgeDirection d:ForgeDirection.VALID_DIRECTIONS){
				if(!(d==pitch || d==pitch.getOpposite())){
					dirs[i] = d;
					i++;
				}
			}
			return dirs;
		}else if(canRotate){
			ForgeDirection pitch = getBlockPitch(world, x, y, z).getForgeDirection();
			return new ForgeDirection[]{pitch, pitch.getOpposite()};
		}
		return new ForgeDirection[0];
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).getEnchantPowerBonus();
		return super.getEnchantPowerBonus(world, x, y, z);
	}

	public boolean recolourBlock(World world, int x, int y, int z, PC_Direction side, int colour) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).recolourBlock(side, colour);
		return false;
	}
	
	@Override
	public final boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
		return recolourBlock(world, x, y, z, getBlockRotation(world, x, y, z, side.ordinal()), colour);
	}
	
	public int getBlockRotation(IBlockAccess world, int x, int y, int z){
		if(canRotate(world, x, y, z)){
			return (PC_Utils.getMD(world, x, y, z) & 3);
		}
		return 0;
	}
	
	public PC_Direction getBlockPitch(IBlockAccess world, int x, int y, int z){
		if(canPitch(world, x, y, z)){
			int side = PC_Utils.getMD(world, x, y, z);
			if(canRotate(world, x, y, z)){
				side >>= 2;
			}
			return PC_Direction.getOrientation(side&7);
		}
		return PC_Direction.DOWN;
	}
	
	public PC_Direction getBlockRotation(IBlockAccess world, int x, int y, int z, int side){
		PC_Direction pitch = getBlockPitch(world, x, y, z);
		PC_Direction dir = PC_Direction.getOrientation(side);
		if(pitch!=PC_Direction.DOWN){
			if(pitch==PC_Direction.UP){
				dir = dir.getRotation(PC_Direction.NORTH).getRotation(PC_Direction.NORTH);
			}else{
				dir = dir.getRotation(pitch.getRotation(PC_Direction.UP));
			}
		}
		int rot = getBlockRotation(world, x, y, z);
		if(rot==1){
			dir = dir.getRotation(pitch);
		}else if(rot==2){
			dir = dir.getRotation(pitch).getRotation(pitch);
		}else if(rot==3){
			dir = dir.getRotation(pitch.getOpposite());
		}
		return dir;
	}
	
	public AxisAlignedBB rotateAABB(IBlockAccess world, int x, int y, int z, AxisAlignedBB aabb){
		if(aabb==null)
			return null;
		PC_Direction pitch = getBlockPitch(world, x, y, z);
		if(pitch!=PC_Direction.DOWN){
			if(pitch==PC_Direction.UP){
				aabb = rotateAABBAround(rotateAABBAround(aabb, PC_Direction.NORTH), PC_Direction.NORTH);
			}else{
				aabb = rotateAABBAround(aabb, pitch.getRotation(PC_Direction.UP));
			}
		}
		int rot = getBlockRotation(world, x, y, z);
		if(rot==1){
			aabb = rotateAABBAround(aabb, pitch);
		}else if(rot==2){
			aabb = rotateAABBAround(rotateAABBAround(aabb, pitch), pitch);
		}else if(rot==3){
			aabb = rotateAABBAround(aabb, pitch.getOpposite());
		}
		return aabb;
	}
	
	private static AxisAlignedBB rotateAABBAround(AxisAlignedBB aabb, PC_Direction dir){
		switch(dir){
		case DOWN:
			return AxisAlignedBB.getBoundingBox(aabb.minZ, aabb.minY, 1-aabb.maxX, aabb.maxZ, aabb.maxY, 1-aabb.minX);
		case EAST:
			return AxisAlignedBB.getBoundingBox(aabb.minX, aabb.minZ, 1-aabb.maxY, aabb.maxX, aabb.maxZ, 1-aabb.minY);
		case NORTH:
			return AxisAlignedBB.getBoundingBox(1-aabb.maxY, aabb.minX, aabb.minZ, 1-aabb.minY, aabb.maxX, aabb.maxZ);
		case SOUTH:
			return AxisAlignedBB.getBoundingBox(aabb.minY, 1-aabb.maxX, aabb.minZ, aabb.maxY, 1-aabb.minX, aabb.maxZ);
		case UP:
			return AxisAlignedBB.getBoundingBox(1-aabb.maxZ, aabb.minY, aabb.minX, 1-aabb.minZ, aabb.maxY, aabb.maxX);
		case WEST:
			return AxisAlignedBB.getBoundingBox(aabb.minX, 1-aabb.maxZ, aabb.minY, aabb.maxX, 1-aabb.minZ, aabb.maxY);
		default:
			return aabb;
		}
	}
	
	public boolean hasPermissionToHarvest(IBlockAccess world, int x, int y, int z, EntityPlayer player){
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			return ((PC_TileEntity)tileEntity).hasPermission(player, PC_Permission.BLOCKHARVEST);
		return true;
	}
	
	@SuppressWarnings("unused")
	@SideOnly(Side.CLIENT)
	public boolean renderInventoryBlock(int metadata, RenderBlocks renderer) {
		return false;
	}


	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity)
			((PC_TileEntity)tileEntity).renderWorldBlock(renderer);
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void setupRotation(IBlockAccess world, int x, int y, int z, RenderBlocks renderer) {
		
	}
	
	@SuppressWarnings("unused")
	public int modifyMetadataPostPlace(World world, int x, int y, int z,
			PC_Direction side, float hitX, float hitY, float hitZ, int metadata,
			ItemStack stack, EntityPlayer player) {
		boolean canRotate = canRotate(world, x, y, z);
		boolean canPitch = canPitch(world, x, y, z);
		if(canPitch){
			metadata = side.ordinal() & 7;
		}
		if(canRotate){
			metadata <<= 2;
			int dir = 0;
			
			metadata |= dir&2;
		}
		return metadata;
	}

	@SuppressWarnings("unused")
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
	}
	
	@SuppressWarnings("unused")
	public Class<? extends PC_TileEntity> getTileEntityClass(IBlockAccess world){
		return blockInfo.tileEntity();
	}
	
	public boolean canPitch(IBlockAccess world, int x, int y, int z){
		if(canRotate(world, x, y, z)){
			return blockInfo.tileEntity()!=PC_TileEntity.class;
		}
		return blockInfo.pitchable();
	}
	
	@SuppressWarnings("unused")
	public boolean canRotate(IBlockAccess world, int x, int y, int z){
		return blockInfo.rotateable();
	}

	public void saveToNBTPacket(World world, int x, int y, int z, NBTTagCompound nbtTagCompound) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity){
			((PC_TileEntity)tileEntity).saveToNBTPacket(nbtTagCompound);
		}
	}

	public void onBlockMessage(World world, int x, int y, int z, EntityPlayer player, NBTTagCompound nbtTagCompound) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity){
			((PC_TileEntity)tileEntity).onBlockMessage(player, nbtTagCompound);
		}
	}

	public void loadFromNBTPacket(World world, int x, int y, int z, NBTTagCompound nbtTagCompound) {
		TileEntity tileEntity = PC_Utils.getTE(world, x, y, z);
		if(tileEntity instanceof PC_TileEntity){
			((PC_TileEntity)tileEntity).loadFromNBTPacket(nbtTagCompound);
		}
	}

	@SideOnly(Side.CLIENT)
	public abstract void loadIcons();
	
	public abstract void registerRecipes();
	
}
