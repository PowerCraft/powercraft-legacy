package powercraft.api.blocks;


import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Module;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public interface PC_IBlock {

	public void onBlockMessage(World world, int x, int y, int z, EntityPlayer player, NBTTagCompound nbtTagCompound);


	public void loadFromNBT(World world, int x, int y, int z, NBTTagCompound nbtTagCompound);


	public void saveToNBT(World world, int x, int y, int z, NBTTagCompound nbtTagCompound);


	@SideOnly(Side.CLIENT)
	public void loadIcons();


	public PC_BlockInfo getBlockInfo();


	public PC_Module getModule();


	public void registerRecipes();


	@SideOnly(Side.CLIENT)
	public boolean renderInventoryBlock(int metadata, RenderBlocks renderer);


	@SideOnly(Side.CLIENT)
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, RenderBlocks renderer);

}
