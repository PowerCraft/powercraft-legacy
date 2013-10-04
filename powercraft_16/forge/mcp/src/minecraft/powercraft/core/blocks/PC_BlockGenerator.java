package powercraft.core.blocks;


import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.core.blocks.tileentities.PC_TileEntityGenerator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@PC_BlockInfo(name = "Generator", blockid = "generator", defaultid = 3001, tileEntity = PC_TileEntityGenerator.class, rotateable=true, pitchable=true)
public class PC_BlockGenerator extends PC_Block {

	private Icon frontLevel[] = new Icon[4];


	public PC_BlockGenerator(int id) {

		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabDecorations);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons() {

		blockIcon = PC_TextureRegistry.registerIcon("DefaultMachineTexture");
		frontLevel[0] = PC_TextureRegistry.registerIcon("Front_Lvl0");
		frontLevel[1] = PC_TextureRegistry.registerIcon("Front_Lvl1");
		frontLevel[2] = PC_TextureRegistry.registerIcon("Front_Lvl2");
		frontLevel[3] = PC_TextureRegistry.registerIcon("Front_Lvl3");
	}


	@Override
	public void registerRecipes() {

	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, PC_Direction side) {

		if (side == PC_Direction.NORTH) {
			PC_TileEntityGenerator generator = PC_Utils.getTE(world, x, y, z);
			if (generator != null && generator.getHeat() > 0) {
				return frontLevel[(generator.getHeat() - 1) * 3 / PC_TileEntityGenerator.maxHeat + 1];
			} 
			return frontLevel[0];
		} 
		return blockIcon;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(PC_Direction side, int metadata) {

		if (side == PC_Direction.EAST) return frontLevel[0];
		return blockIcon;
	}

}
