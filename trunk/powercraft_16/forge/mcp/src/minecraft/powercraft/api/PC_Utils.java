package powercraft.api;

import java.io.File;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import powercraft.api.blocks.PC_TileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PC_Utils {

	protected static PC_Utils instance;
	
	public PC_Utils(){
		instance = this;
		TickRegistry.registerTickHandler(new PC_TickHandler(), Side.SERVER);
	}
	
	public static ItemStack getItemStack(Object obj){
		if(obj instanceof ItemStack){
			return (ItemStack)obj;
		}else if(obj instanceof Block){
			return new ItemStack((Block)obj);
		}else if(obj instanceof Item){
			return new ItemStack((Item)obj);
		}else{
			PC_Logger.severe("Can't make %s to ItemStack", obj.getClass());
		}
		return null;
	}

	public static <T extends TileEntity> T getTE(IBlockAccess world, int x, int y, int z) {
		return (T)world.getBlockTileEntity(x, y, z);
	}

	public static <T extends Block> T getBlock(IBlockAccess world, int x, int y, int z) {
		return (T)Block.blocksList[world.getBlockId(x, y, z)];
	}

	public static File getPowerCraftFile(String directory, String f) {
		File file = instance.iGetPowerCraftFile();
		if(!file.exists())
			file.mkdir();
		if(directory!=null){
			file = new File(file, directory);
			if(!file.exists())
				file.mkdir();
		}
		return new File(file, f);
	}
	
	public static MinecraftServer mcs(){
		return MinecraftServer.getServer();
	}
	
	public static ItemStack getSmeltingResult(ItemStack item) {
		return FurnaceRecipes.smelting().getSmeltingResult(item);
	}
	
	public static int getRedstoneValue(World world, int x, int y, int z) {
		return world.getStrongestIndirectPower(x, y, z);
	}
	
	protected File iGetPowerCraftFile(){
		return mcs().getFile("PowerCraft");
	}
	
}
