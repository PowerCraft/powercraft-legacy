package powercraft.api.block;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_IIDChangeAble;
import powercraft.api.PC_IMSG;
import powercraft.api.PC_Struct3;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_VecI;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.launcher.loader.PC_ModuleObject;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PC_Block extends BlockContainer implements PC_IMSG, PC_IIDChangeAble {
	private PC_ModuleObject module;
	protected Icon[] icons;
	private String[] textureNames;
	private BlockInfo replaced = new BlockInfo();
	private BlockInfo thisBlock;
	
	protected PC_Block(int id, Material material, String textureName, String... textureNames) {
		super(id, material);
		thisBlock = new BlockInfo(id);
		this.textureNames = new String[1 + textureNames.length];
		icons = new Icon[1 + textureNames.length];
		this.textureNames[0] = textureName;
		for (int i = 0; i < textureNames.length; i++) {
			this.textureNames[i + 1] = textureNames[i];
		}
	}

	public Object msg(int msg, Object... obj) {
		IBlockAccess world = null;
		PC_VecI pos = null;
		int i = 0;
		if (obj != null) {
			if (obj.length >= 1) {
				if (obj[0] instanceof IBlockAccess) {
					world = (IBlockAccess) obj[0];
					i = 1;
					if (obj.length >= 2) {
						if (obj[1] instanceof PC_VecI) {
							pos = (PC_VecI) obj[1];
							i = 2;
						}
					}
				}
			}
		}
		Object o[] = new Object[obj.length - i];
		for (int j = 0; j < o.length; j++) {
			o[j] = obj[j + i];
		}
		return msg(world, pos, msg, o);
	}

	public abstract Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj);

	public TileEntity newTileEntity(World world, int metadata) {
		PC_BlockInfo blockInfo = getClass().getAnnotation(PC_BlockInfo.class);
		if(blockInfo==null || blockInfo.tileEntity()==null || blockInfo.tileEntity()==PC_BlockInfo.PC_FakeTileEntity.class){
			return null;
		}else{
			return PC_ReflectHelper.create(blockInfo.tileEntity());
		}
	}

	public final TileEntity createNewTileEntity(World world) {
		return createNewTileEntity(world, 0);
	}

	public final TileEntity createNewTileEntity(World world, int metadata) {
		if (PC_GlobalVariables.tileEntity != null && !world.isRemote) {
			PC_GlobalVariables.tileEntity.validate();
			return PC_GlobalVariables.tileEntity;
		}
		return newTileEntity(world, metadata);
	}

	public void setModule(PC_ModuleObject module) {
		this.module = module;
	}

	public PC_ModuleObject getModule() {
		return module;
	}

	public boolean showInCraftingTool() {
		return true;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if (side == 1 && getRenderType() == PC_Renderer.getRendererID(true) && msg(PC_MSGRegistry.MSG_ROTATION, GameInfo.getMD(world, x, y, z)) != null) {
			return false;
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	public static boolean canSilkHarvest(Block block) {
		return block.renderAsNormalBlock() && !block.hasTileEntity();
	}

	public static ItemStack createStackedBlock(Block block, int meta) {
		int var2 = 0;

		if (block.blockID >= 0 && block.blockID < Item.itemsList.length && Item.itemsList[block.blockID].getHasSubtypes()) {
			var2 = meta;
		}

		return new ItemStack(block.blockID, 1, var2);
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

	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		PC_TileEntity te = GameInfo.getTE(world, x, y, z);
		if (PC_GlobalVariables.tileEntity == null) {
			if(te!=null)
				te.onBreakBlock();
			super.breakBlock(world, x, y, z, par5, par6);
		}
	}

	@Override
	public Block setLightOpacity(int par1) {
		thisBlock.lightOpacity = par1;
		return super.setLightOpacity(par1);
	}

	@Override
	public Block setLightValue(float par1) {
		thisBlock.lightValue = (int) (15.0F * par1);
		return super.setLightValue(par1);
	}

	public static class BlockInfo {
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
		public ItemData itemData = null;

		public BlockInfo() {
		}

		public BlockInfo(int id) {
			block = Block.blocksList[id];
			opaqueCubeLookup = Block.opaqueCubeLookup[id];
			lightOpacity = Block.lightOpacity[id];
			canBlockGrass = Block.canBlockGrass[id];
			lightValue = Block.lightValue[id];
			useNeighborBrightness = Block.useNeighborBrightness[id];
			blockFireSpreadSpeed = Block.blockFireSpreadSpeed[id];
			blockFlammability = Block.blockFlammability[id];

			itemBlock = (ItemBlock) Item.itemsList[id];
			Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
			itemData = map.get(id);
		}

		public void storeToID(int id) {
			Block.blocksList[id] = block;
			Block.opaqueCubeLookup[id] = opaqueCubeLookup;
			Block.lightOpacity[id] = lightOpacity;
			Block.canBlockGrass[id] = canBlockGrass;
			Block.lightValue[id] = lightValue;
			Block.useNeighborBrightness[id] = useNeighborBrightness;
			Block.blockFireSpreadSpeed[id] = blockFireSpreadSpeed;
			Block.blockFlammability[id] = blockFlammability;

			Item.itemsList[id] = itemBlock;
			Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
			if (itemData == null) {
				map.remove(id);
			} else {
				PC_ReflectHelper.setValue(ItemData.class, itemData, 3, id, int.class);
				map.put(id, itemData);
			}
		}

	}

	@Override
	public Block setCreativeTab(CreativeTabs _default) {
		return super.setCreativeTab(GameInfo.getCreativeTab(_default));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void func_94332_a(IconRegister par1IconRegister) {
		for (int i = 0; i < textureNames.length; i++) {
			if(textureNames[i]!=null){
				icons[i] = par1IconRegister.func_94245_a(PC_TextureRegistry.getItemAndBlockTextureDirectory(module) + ":" + textureNames[i]);
			}
		}
	}

	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		if (par2 >= icons.length) {
			par2 = icons.length - 1;
		}
		return icons[par2];
	}

}
