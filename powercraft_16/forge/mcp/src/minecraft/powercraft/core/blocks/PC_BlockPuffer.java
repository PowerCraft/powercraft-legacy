package powercraft.core.blocks;


import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.core.blocks.itemblocks.PC_ItemBlockPuffer;
import powercraft.core.blocks.tileentities.PC_TileEntityPuffer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@PC_BlockInfo(name = "Puffer", blockid = "puffer", defaultid = 3002, tileEntity = PC_TileEntityPuffer.class, itemBlock = PC_ItemBlockPuffer.class)
public class PC_BlockPuffer extends PC_Block {

	private Icon sideLevel[] = new Icon[4];


	public PC_BlockPuffer(int id) {

		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabDecorations);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {

		blockIcon = PC_TextureRegistry.registerIcon("DefaultMachineTexture");
		sideLevel[0] = PC_TextureRegistry.registerIcon("Side_Lvl0");
		sideLevel[1] = PC_TextureRegistry.registerIcon("Side_Lvl1");
		sideLevel[2] = PC_TextureRegistry.registerIcon("Side_Lvl2");
		sideLevel[3] = PC_TextureRegistry.registerIcon("Side_Lvl3");
	}


	@Override
	public void registerRecipes() {

		// TODO Auto-generated method stub

	}


	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {

		PC_TileEntityPuffer puffer = PC_Utils.getTE(world, x, y, z);
		if (puffer != null) {
			puffer.setEnergy(itemStack.getItemDamage());
		}
	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {

		if (side < 2) {
			return blockIcon;
		} 
		PC_TileEntityPuffer puffer = PC_Utils.getTE(world, x, y, z);
		if (puffer != null && ((int) puffer.getEnergyLevel(null)) > 0) {
			return sideLevel[((int) puffer.getEnergyLevel(null) - 1) * 3 / PC_TileEntityPuffer.maxEnergy + 1];
		} 
		return sideLevel[0];
	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {

		if (side < 2) return blockIcon;
		if (metadata > 0) {
			return sideLevel[(metadata - 1) * 3 / PC_TileEntityPuffer.maxEnergy + 1];
		}
		return sideLevel[0];
	}

}
