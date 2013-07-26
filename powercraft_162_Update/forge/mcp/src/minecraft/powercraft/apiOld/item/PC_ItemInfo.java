package powercraft.api.item;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.api.block.PC_Block;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_Struct3;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.ItemData;

public class PC_ItemInfo {

	public Item item = null;
	public Block block = null;
	public boolean opaqueCubeLookup = false;
	public int lightOpacity = 0;
	public boolean canBlockGrass = false;
	public int lightValue = 0;
	public boolean useNeighborBrightness = false;
	public int blockFireSpreadSpeed = 0;
	public int blockFlammability = 0;
	public List<PC_Struct3<Integer, ItemStack, Float>> furnaceRecipes;
	public ItemData itemData = null;
	
	public PC_ItemInfo(int id) {
		if(id>0){
			item = Item.itemsList[id];
			if(id<Block.blocksList.length){
				block = Block.blocksList[id];
				opaqueCubeLookup = Block.opaqueCubeLookup[id];
				lightOpacity = Block.lightOpacity[id];
				canBlockGrass = Block.canBlockGrass[id];
				lightValue = Block.lightValue[id];
				useNeighborBrightness = Block.useNeighborBrightness[id];
				blockFireSpreadSpeed = PC_Block.getBlockFireSpreadSpeed(id);
				blockFlammability = PC_Block.getBlockFlammability(id);
			}
			Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
			itemData = map.get(id);
		}
	}
	
	public PC_ItemInfo storeToID(int id) {
		PC_ItemInfo old = new PC_ItemInfo(id);
		if(id>0){
			Item.itemsList[id] = item;
			if(id<Block.blocksList.length){
				Block.blocksList[id] = block;
				Block.opaqueCubeLookup[id] = opaqueCubeLookup;
				Block.lightOpacity[id] = lightOpacity;
				Block.canBlockGrass[id] = canBlockGrass;
				Block.lightValue[id] = lightValue;
				Block.useNeighborBrightness[id] = useNeighborBrightness;
				PC_Block.setBlockFireSpreadSpeed(id, blockFireSpreadSpeed);
				PC_Block.setBlockFlammability(id, blockFlammability);
			}
			Map<Integer, ItemData> map = PC_ReflectHelper.getValue(GameData.class, GameData.class, 0, Map.class);
			if (itemData == null) {
				map.remove(id);
			} else {
				PC_ReflectHelper.setValue(ItemData.class, itemData, 3, id, int.class);
				map.put(id, itemData);
			}
		}
		return old;
	}
	
}
