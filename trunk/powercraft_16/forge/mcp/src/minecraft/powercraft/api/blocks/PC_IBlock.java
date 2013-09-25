package powercraft.api.blocks;


import java.util.Random;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import powercraft.api.PC_Module;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public interface PC_IBlock {

	public void onBlockMessage(World world, int x, int y, int z, EntityPlayer player, NBTTagCompound nbtTagCompound);


	@SideOnly(Side.CLIENT)
	public void loadIcons();


	public PC_BlockInfo getBlockInfo();


	public PC_Module getModule();


	public void registerRecipes();


	@SideOnly(Side.CLIENT)
	public boolean renderInventoryBlock(int metadata, RenderBlocks renderer);


	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, RenderBlocks renderer);

	
	public abstract void loadFromNBT(World world, int x, int y, int z,
			NBTTagCompound nbtTagCompound);

	
	public abstract void saveToNBT(World world, int x, int y, int z,
			NBTTagCompound nbtTagCompound);
	
	public int modifyMetadataPostPlace(World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ, int metadata,
			ItemStack stack, EntityPlayer player);


	/**
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @param chunkGenerator
	 * @param chunkProvider
	 */
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider);
	
}
