package powercraft.api.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.api.entity.PC_FakePlayer;
import powercraft.api.registry.PC_BuildingRegistry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PC_BuildingManager {
	
	private static Random rand = new Random();
	private List<PC_ISpecialHarvesting> specialHarvesting = new ArrayList<PC_ISpecialHarvesting>();
	
	public static List<PC_Struct2<PC_VecI, ItemStack>> harvest(World world, PC_VecI pos, int fortune){
		return harvest(world, pos.x, pos.y, pos.z, fortune);
	}
	
	public static List<PC_Struct2<PC_VecI, ItemStack>> harvest(World world, int x, int y, int z, int fortune){
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block==null)
			return null;
		int meta = PC_Utils.getMD(world, x, y, z);
		PC_ISpecialHarvesting specialHarvesting = PC_BuildingRegistry.getSpecialHarvestingFor(world, x, y, z, block, meta);
		if(specialHarvesting!=null){
			return specialHarvesting.harvest(world, x, y, z, block, meta, fortune);
		}else{
			List<ItemStack> drops = harvestEasy(world, x, y, z, fortune);
			List<PC_Struct2<PC_VecI, ItemStack>> dropsWithPos = new ArrayList<PC_Struct2<PC_VecI, ItemStack>>();
			for(ItemStack drop:drops){
				dropsWithPos.add(new PC_Struct2<PC_VecI, ItemStack>(new PC_VecI(x, y, z), drop));
			}
			return dropsWithPos;
		}
	}
	
	public static List<ItemStack> harvestEasy(World world, int x, int y, int z, int fortune){
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block==null)
			return null;
		int meta = PC_Utils.getMD(world, x, y, z);
		int dropCount = block.quantityDroppedWithBonus(fortune, rand);
		List<ItemStack> drops = new ArrayList<ItemStack>();
		for(int i=0; i<dropCount; i++){
			int blockDrop = block.idDropped(meta, rand, fortune);
			if(blockDrop>0){
				drops.add(new ItemStack(blockDrop, 1, block.damageDropped(meta)));
			}
		}
		PC_Utils.setBID(world, x, y, z, 0);
		if(drops.size()==0)
			return null;
		return drops;
	}
	
	public static boolean tryUseItem(World world, int x, int y, int z, PC_Direction dir, ItemStack itemStack){
		return itemStack.getItem().onItemUse(itemStack, new PC_FakePlayer(world), world, x, y, z, dir.getMCDir(), 0, 0, 0);
	}
	
}
