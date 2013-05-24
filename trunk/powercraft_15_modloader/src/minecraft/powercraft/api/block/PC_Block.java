package powercraft.api.block;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.api.PC_BeamTracer.BeamHitResult;
import powercraft.api.PC_BeamTracer.BeamSettings;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.annotation.PC_Config;
import powercraft.api.annotation.PC_OreInfo;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.interfaces.PC_IIDChangeAble;
import powercraft.api.interfaces.PC_IWorldGenerator;
import powercraft.api.reflect.PC_FieldWithAnnotation;
import powercraft.api.reflect.PC_IFieldAnnotationIterator;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_KeyRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.api.utils.PC_MathHelper;
import powercraft.api.utils.PC_Struct3;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_ModuleObject;

public abstract class PC_Block extends BlockContainer implements PC_IIDChangeAble, PC_IWorldGenerator {
	
	private PC_BlockInfo blockInfo;
	private PC_ModuleObject module;
	private BlockInfo replaced = new BlockInfo();
	private BlockInfo thisBlock;
	private String[] sideTextures;
	protected Icon[] sideIcons;
	private int[] oreGens;
	
	public PC_Block(int id, Material material) {
		super(id, material);
		thisBlock = new BlockInfo(id);
		blockInfo = getClass().getAnnotation(PC_BlockInfo.class);
	}
	
	public PC_Block(int id, Material material, String texture) {
		this(id, material);
		sideTextures = new String[] { texture };
	}
	
	public PC_Block(int id, Material material, String... textures) {
		this(id, material);
		sideTextures = textures;
	}
	
	public boolean showInCraftingTool() {
		return true;
	}
	
	public String getName() {
		return blockInfo == null ? null : blockInfo.name();
	}
	
	public boolean canPlacedRotated(){
		return blockInfo.canPlacedRotated();
	}
	
	public void initConfig(PC_Property config) {
		PC_OreInfo oreInfo = getClass().getAnnotation(PC_OreInfo.class);
		if (oreInfo != null) {
			oreGens = new int[4];
			oreGens[0] = config.getInt("spawn.in_chunk", oreInfo.genOresInChunk(), "Number of deposits in each 16x16 chunk.");
			oreGens[1] = config.getInt("spawn.deposit_max_size", oreInfo.genOresDepositMaxCount(), "Highest Ore count in one deposit");
			oreGens[2] = config.getInt("spawn.max_y", oreInfo.genOresMaxY(), "Max Y coordinate of ore deposits.");
			oreGens[3] = config.getInt("spawn.min_y", oreInfo.genOresMinY(), "Min Y coordinate of ore deposits.");
		}
		PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), this, PC_Config.class, new InitConfigFieldAnnotationIterator(config));
	}
	
	public TileEntity newTileEntity(World world) {
		if (blockInfo == null || blockInfo.tileEntity() == null || blockInfo.tileEntity() == PC_BlockInfo.PC_FakeTileEntity.class) {
			return null;
		} else {
			return PC_ReflectHelper.create(blockInfo.tileEntity());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		if (PC_GlobalVariables.tileEntity.size() > 0 && !world.isRemote) {
			TileEntity tileEntity = PC_GlobalVariables.tileEntity.get(0);
			if (tileEntity.isInvalid())
				tileEntity.validate();
			return tileEntity;
		}
		return newTileEntity(world);
	}
	
	public void setModule(PC_ModuleObject module) {
		this.module = module;
	}
	
	public PC_ModuleObject getModule() {
		return module;
	}
	
	public void setItemBlock(ItemBlock itemBlock) {
		thisBlock.itemBlock = itemBlock;
	}
	
	public ItemBlock getItemBlock() {
		return thisBlock.itemBlock;
	}
	
	@Override
	public void setID(int id) {
		int oldID = blockID;
		if (oldID == id)
			return;
		if (PC_ReflectHelper.setValue(Block.class, this, PC_GlobalVariables.indexBlockID, id, int.class)) {
			if (PC_ReflectHelper.setValue(Item.class, thisBlock.itemBlock, PC_GlobalVariables.indexItemSthiftedIndex, id, int.class)) {
				if (PC_ReflectHelper.setValue(ItemBlock.class, thisBlock.itemBlock, 0, id, int.class)) {
					if (oldID != -1) {
						replaced.storeToID(oldID);
					}
					if (id != -1) {
						replaced = new BlockInfo(id);
						thisBlock.storeToID(id);
					} else {
						new BlockInfo().storeToID(oldID);
						replaced = null;
					}
				} else {
					PC_ReflectHelper.setValue(Item.class, thisBlock.itemBlock, PC_GlobalVariables.indexItemSthiftedIndex, oldID, int.class);
					PC_ReflectHelper.setValue(Block.class, this, PC_GlobalVariables.indexBlockID, oldID, int.class);
				}
			} else {
				PC_ReflectHelper.setValue(Block.class, this, PC_GlobalVariables.indexBlockID, oldID, int.class);
			}
		}
	}
	
	@Override
	public Block setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(PC_Utils.getCreativeTab(_default));
	}
	
	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int metadata) {
		if (PC_GlobalVariables.tileEntity.size()==0 || world.isRemote) {
			super.breakBlock(world, x, y, z, id, metadata);
		}
	}
	
	public int makeBlockMetadata(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit, float yHit,
			float zHit, int metadata) {
		if (blockInfo.canPlacedRotated()) {
			int rotation = PC_MathHelper.floor_double(((entityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			if (PC_KeyRegistry.isPlacingReversed(entityPlayer)) {
				rotation = (rotation + 2) % 4;
			}
			metadata &= ~3;
			metadata |= rotation;
		}
		return metadata;
	}
	
	public PC_Direction getRotation(int metadata) {
		if (blockInfo.canPlacedRotated()) {
			metadata &= 3;
			if (metadata == 0) {
				return PC_Direction.FRONT;
			} else if (metadata == 1) {
				return PC_Direction.RIGHT;
			} else if (metadata == 2) {
				return PC_Direction.BACK;
			} else if (metadata == 3) {
				return PC_Direction.LEFT;
			}
		}
		return PC_Direction.FRONT;
	}
	
	public PC_Direction getRotation2(int metadata) {
		if (blockInfo.canPlacedRotated()) {
			metadata &= 3;
			if (metadata == 0) {
				return PC_Direction.FRONT;
			} else if (metadata == 1) {
				return PC_Direction.LEFT;
			} else if (metadata == 2) {
				return PC_Direction.BACK;
			} else if (metadata == 3) {
				return PC_Direction.RIGHT;
			}
		}
		return PC_Direction.FRONT;
	}
	
	@Override
	public final Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int dir) {
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		pcDir = pcDir.rotate(getRotation(PC_Utils.getMD(world, x, y, z)));
		return getBlockTexture(world, x, y, z, pcDir);
	}
	
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		return getIcon(dir, PC_Utils.getMD(world, x, y, z));
	}
	
	@Override
	public final Icon getIcon(int dir, int metadata) {
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		pcDir = pcDir.rotate(getRotation(metadata));
		return getIcon(pcDir, metadata);
	}
	
	public Icon getIcon(PC_Direction dir, int metadata) {
		if (sideIcons != null) {
			int index = dir.getMCDir();
			if (index >= sideIcons.length)
				index = sideIcons.length-1;
			return sideIcons[index];
		}
		return null;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntity) {
			((PC_TileEntity) te).onNeighborBlockChange(id);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int dir, float xHit, float yHit, float zHit) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntity) {
			return ((PC_TileEntity) te).openGui(entityPlayer);
		}
		return false;
	}
	
	@Override
	public final int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int dir) {
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		pcDir = pcDir.rotate(getRotation(PC_Utils.getMD(world, x, y, z))).mirror();
		return getProvidingWeakRedstonePowerValue(world, x, y, z, pcDir);
	}
	
	public int getProvidingWeakRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntity) {
			return ((PC_TileEntity) te).getProvidingWeakRedstonePowerValue(dir);
		}
		return getProvidingStrongRedstonePowerValue(world, x, y, z, dir);
	}
	
	@Override
	public final int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int dir) {
		PC_Direction pcDir = PC_Direction.getFromMCDir(dir);
		pcDir = pcDir.rotate(getRotation(PC_Utils.getMD(world, x, y, z))).mirror();
		return getProvidingStrongRedstonePowerValue(world, x, y, z, pcDir);
	}
	
	public int getProvidingStrongRedstonePowerValue(IBlockAccess world, int x, int y, int z, PC_Direction dir) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntity) {
			return ((PC_TileEntity) te).getProvidingStrongRedstonePowerValue(dir);
		}
		return 0;
	}
	
	public int getRedstonePowerValueFromInput(World world, int x, int y, int z, PC_Direction dir) {
		dir = dir.rotateRev(getRotation(PC_Utils.getMD(world, x, y, z)));
		PC_VecI offset = dir.getOffset();
		int value = world.getIndirectPowerLevelTo(x + offset.x, y + offset.y, z + offset.z, dir.getMCDir());
		if(canProvidePower() && value==0 && PC_Utils.getBID(world, x + offset.x, y + offset.y, z + offset.z) == Block.redstoneWire.blockID){
			return PC_Utils.getMD(world, x + offset.x, y + offset.y, z + offset.z);
		}
		return value;
	}
	
	public int getRedstonePowereValue(World world, int x, int y, int z) {
		return world.getStrongestIndirectPower(x, y, z);
	}
	
	public int getRedstonePowerValueFromInputEx(World world, int x, int y, int z, PC_Direction dir) {
		dir = dir.rotateRev(getRotation(PC_Utils.getMD(world, x, y, z)));
		PC_VecI offset = dir.getOffset();
		int powerLevel = world.getIndirectPowerLevelTo(x + offset.x, y + offset.y, z + offset.z, dir.getMCDir());
		if (powerLevel == 0 && PC_Utils.getBID(world, x + offset.x, y + offset.y, z + offset.z) == Block.redstoneWire.blockID) {
			powerLevel = PC_Utils.getMD(world, x + offset.x, y + offset.y, z + offset.z);
		}
		return powerLevel;
	}
	
	public int getRedstonePowereValueEx(World world, int x, int y, int z) {
		int max = 0;
		for(int i=0; i<6; i++){
			int value = getRedstonePowerValueFromInputEx(world, x, y, z, PC_Direction.getFromMCDir(i));
			if(value>max){
				max = value;
			}
		}
		return max;
	}
	
	@Override
	public Block setLightOpacity(int lightOpacity) {
		thisBlock.lightOpacity = lightOpacity;
		return super.setLightOpacity(lightOpacity);
	}
	
	@Override
	public Block setLightValue(float lightValue) {
		thisBlock.lightValue = (int) (15.0F * lightValue);
		return super.setLightValue(lightValue);
	}
	
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int md) {
		return getFlammability(world, x, y, z, md)>0;
	}
	
	public int getFlammability(IBlockAccess world, int x, int y, int z, int md) {
		return 0;
	}
	
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public final void registerIcons(IconRegister iconRegister) {
		if (sideTextures != null) {
			sideIcons = new Icon[sideTextures.length];
			for (int i = 0; i < sideTextures.length; i++) {
				if (sideTextures[i] != null) {
					sideIcons[i] = iconRegister.registerIcon(PC_TextureRegistry.getTextureName(module, sideTextures[i]));
				}
			}
		}
		
		PC_TextureRegistry.onIconLoading(this, iconRegister);
		
	}
	
	public void onIconLoading() {
		
	}
	
	public boolean isOre() {
		return oreGens != null;
	}
	
	public int getGenOresSpawnsInChunk(Random random, World world, int chunkX, int chunkZ) {
		return oreGens[0];
	}
	
	public int getGenOreblocksOnSpawnPoint(Random random, World world, int chunkX, int chunkZ) {
		return random.nextInt(oreGens[1] + 1);
	}
	
	public PC_VecI getGenOresSpawnPoint(Random random, World world, int chunkX, int chunkZ) {
		return new PC_VecI(random.nextInt(16), oreGens[3] + random.nextInt(oreGens[2] - oreGens[3] + 1), random.nextInt(16));
	}
	
	public int getGenOresSpawnMetadata(Random random, World world, int chunkX, int chunkZ) {
		return 0;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (isOre()) {
			int num = getGenOresSpawnsInChunk(random, world, chunkX, chunkZ);
			for (int i = 0; i < num; i++) {
				int blocksCount = getGenOreblocksOnSpawnPoint(random, world, chunkX, chunkZ);
				PC_VecI pos = getGenOresSpawnPoint(random, world, chunkX, chunkZ);
				pos.y = PC_MathHelper.clamp_int(pos.y, 1, world.getHeight());
				int metadata = getGenOresSpawnMetadata(random, world, chunkX, chunkZ);
				pos.add(chunkX * 16, 0, chunkZ * 16);
				new PC_WorldGenMinableMetadata(blockID, metadata, blocksCount).generate(world, random, pos.x, pos.y, pos.z);
			}
		}
	}
	
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return false;
	}
	
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		return false;
	}
	
	public PC_VecI moveBlockTryToPlaceOnSide(World world, int x, int y, int z, PC_Direction side, float xHit, float yHit, float zHit, Block block, ItemStack itemStack, EntityPlayer entityPlayer){
		return null;
	}
	
	public PC_VecI moveBlockTryToPlaceAt(World world, int x, int y, int z, PC_Direction dir, float xHit, float yHit, float zHit, ItemStack itemStack, EntityPlayer entityPlayer){
		return null;
	}
	
	public BeamHitResult onBlockHitByBeam(World world, int x, int y, int z, BeamSettings settings) {
		return BeamHitResult.FALLBACK;
	}
	
	public boolean canTubeConnectTo(IBlockAccess world, int x, int y, int z, ItemStack tube, PC_Direction dir){
		return false;
	}
	
	private class InitConfigFieldAnnotationIterator implements PC_IFieldAnnotationIterator<PC_Config> {
		
		private PC_Property config;
		
		public InitConfigFieldAnnotationIterator(PC_Property config) {
			this.config = config;
		}
		
		@Override
		public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_Config> fieldWithAnnotation) {
			Class<?> c = fieldWithAnnotation.getFieldClass();
			String name = fieldWithAnnotation.getAnnotation().name();
			if (name.equals("")) {
				name = fieldWithAnnotation.getFieldName();
			}
			String[] comment = fieldWithAnnotation.getAnnotation().comment();
			if (c == String.class) {
				String data = (String) fieldWithAnnotation.getValue();
				data = config.getString(name, data, comment);
				fieldWithAnnotation.setValue(data);
			} else if (c == Integer.class || c == int.class) {
				int data = (Integer) fieldWithAnnotation.getValue();
				data = config.getInt(name, data, comment);
				fieldWithAnnotation.setValue(data);
			} else if (c == Float.class || c == float.class) {
				float data = (Float) fieldWithAnnotation.getValue();
				data = config.getFloat(name, data, comment);
				fieldWithAnnotation.setValue(data);
			} else if (c == Boolean.class || c == boolean.class) {
				boolean data = (Boolean) fieldWithAnnotation.getValue();
				data = config.getBoolean(name, data, comment);
				fieldWithAnnotation.setValue(data);
			}
			return false;
		}
		
	}
	
    @Override
	public void onBlockHarvested(World world, int x, int y, int z, int side, EntityPlayer player) {
    	removeBlockByPlayer(world, player, x, y, z);
	}

	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z){
		return true;
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntity) {
			return ((PC_TileEntity) te).getPickMetadata();
		}
		return super.getDamageValue(world, x, y, z);
	}
	
	@Override
	public int idPicked(World world, int x, int y, int z) {
		List<Field> l = PC_ReflectHelper.getAllFieldsWithAnnotation(getClass(), PC_Shining.ON.class);
		if(l==null || l.size()==0){
			return super.idPicked(world, x, y, z);
		}
		try {
			return ((Block)l.get(0).get(null)).blockID;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return super.idPicked(world, x, y, z);
	}
	
	private static class BlockInfo {
		public Block block = null;
		public boolean opaqueCubeLookup = false;
		public int lightOpacity = 0;
		public boolean canBlockGrass = false;
		public int lightValue = 0;
		public boolean useNeighborBrightness = false;
		public int blockFireSpreadSpeed = 0;
		public int blockFlammability = 0;
		public List<PC_Struct3<Integer, ItemStack, Float>> furnaceRecipes;
		public ItemBlock itemBlock = null;
		
		public BlockInfo() {
		}
		
		public BlockInfo(int id) {
			block = Block.blocksList[id];
			opaqueCubeLookup = Block.opaqueCubeLookup[id];
			lightOpacity = Block.lightOpacity[id];
			canBlockGrass = Block.canBlockGrass[id];
			lightValue = Block.lightValue[id];
			useNeighborBrightness = Block.useNeighborBrightness[id];
			itemBlock = (ItemBlock) Item.itemsList[id];
		}
		
		public void storeToID(int id) {
			Block.blocksList[id] = block;
			Block.opaqueCubeLookup[id] = opaqueCubeLookup;
			Block.lightOpacity[id] = lightOpacity;
			Block.canBlockGrass[id] = canBlockGrass;
			Block.lightValue[id] = lightValue;
			Block.useNeighborBrightness[id] = useNeighborBrightness;
			Item.itemsList[id] = itemBlock;
		}
		
	}
	
}
